package com.paymybuddy.service;

import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.dto.PayMyBoddyDto;
import com.paymybuddy.model.Response;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.utility.Constant;
import com.paymybuddy.utility.Utility;

@Service
public class PayMyBuddyServiceImpl implements IPayMyBuddyService{
	
	final Logger logger = LogManager.getLogger(PayMyBuddyServiceImpl.class);

	@Autowired
	private IUserService userService;
	
	private Utility utility;
	
	private Response response;
	
	public PayMyBuddyServiceImpl() {
		super();
		utility = new Utility();
		response = new Response();
	}

	private ResponseEntity<Response> transferMoneyToBuddy(String senderEmail,
			String receiverEmail, 
			String description,
			Double transactionAmount) {
		String errorDescription = "";
		
		if (StringUtils.isEmpty(senderEmail)) {
			errorDescription = "Sender email is required !";
			return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, errorDescription);
		}
		
		if (StringUtils.isEmpty(receiverEmail)) {
			errorDescription = "Receiver email is required !";
			return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, errorDescription);
		}
		
		if (transactionAmount == null || transactionAmount <= 0) {
			errorDescription = "Check your transfered amount.";
			return utility.createResponseWithErrors(Constant.ERROR_TRANSACTION_AMOUNT_INVALID, errorDescription);
		}
		
		logger.info("Sender email : {}, Receiver email : {}, Description : {}, TransactionAmount : {}", 
				senderEmail, receiverEmail, description, transactionAmount);
		
		User senderUser = userService.getUserFromAppAccount(senderEmail);
		User receiverUser = userService.getUserFromAppAccount(receiverEmail);
		User adminUser = userService.getUserFromAppAccount(Constant.ADMIN_EMAIL);
		
		if (senderUser == null) {
			errorDescription = String.format("There is no user attached to the email(%s)", senderEmail);
			return utility.createResponseWithErrors(Constant.ERROR_NO_USER_FOUND, errorDescription);
		}
		
		if (receiverUser == null) {
			errorDescription = String.format("There is no user attached to the email(%s)", receiverEmail);
			return utility.createResponseWithErrors(Constant.ERROR_NO_USER_FOUND, errorDescription);
		}
		
		if (senderUser.getAccountPayMyBuddy() != null &&
				(senderUser.getAccountPayMyBuddy().getAmountBalance() <= 0 
				|| senderUser.getAccountPayMyBuddy().getAmountBalance() < transactionAmount)) {
			errorDescription = "Transfer failed, amount available is not enough for this transaction";
			return utility.createResponseWithErrors(Constant.ERROR_AMOUNT_NOT_ENOUGH, errorDescription);
		}
		
		Double fees = new Utility().roundAmount(transactionAmount * Constant.FEES_RATE);
		senderUser.getAccountPayMyBuddy().setAmountBalance(senderUser.getAccountPayMyBuddy().getAmountBalance() - transactionAmount - fees);
		receiverUser.getAccountPayMyBuddy().setAmountBalance(receiverUser.getAccountPayMyBuddy().getAmountBalance() + transactionAmount);
		adminUser.getAccountPayMyBuddy().setAmountBalance(adminUser.getAccountPayMyBuddy()
				.getAmountBalance() + fees);
		
		Transaction transaction = new Transaction();
		transaction.setEmailSender(senderEmail);
		transaction.setEmailReceiver(receiverEmail);
		transaction.setDescription(description);
		transaction.setTransactionDate(new Date());
		transaction.setAmount(transactionAmount);
		
		transaction.setFees(fees);
		
		transaction.setUser(senderUser);
		
		senderUser.getTransactions().add(transaction);
		PayMyBoddyDto accountBalance = new PayMyBoddyDto();
		
		try {
			User userSenderSaved = userService.save(senderUser);
			User userReceiverSaved = userService.save(receiverUser);
			userService.save(adminUser);
			accountBalance.setSenderAccountBalance(userSenderSaved.getAccountPayMyBuddy().getAmountBalance());
			accountBalance.setReceiverAccountBalance(userReceiverSaved.getAccountPayMyBuddy().getAmountBalance());
			
			Transaction lastTransaction = null;
			
			Optional<Transaction> optionalTransaction = userSenderSaved.getTransactions().stream()
					.sorted(Comparator.comparing(Transaction::getTransactionId).reversed())
					.findFirst();
			
			if (optionalTransaction.isPresent()) {
				lastTransaction = optionalTransaction.get();
				accountBalance.setFees(lastTransaction.getFees());
			}
			
			utility.createResponseWithSuccess(response, accountBalance);
			
		} catch (Exception e) {
			return utility.createResponseWithErrors(Constant.INTERNAL_ERROR, e.getMessage());
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
		 
	}

	@Override
	@Transactional
	public ResponseEntity<Response> transferMoneyToBuddy(String body) {
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode;
		String errorDescription = "";
		try {
			jsonNode = mapper.readTree(body);
			
			JsonNode senderEmailNode = jsonNode.get("senderEmail");
			if (senderEmailNode == null) {
				errorDescription = "Sender email is required !";
				return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, errorDescription);
			}
			 
			String senderEmail = senderEmailNode.asText();
			
			JsonNode receiverEmailNode = jsonNode.get("receiverEmail");
			if (receiverEmailNode == null ) {
				errorDescription = "Receiver email is required !";
				return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, errorDescription);
			}
			String receiverEmail = receiverEmailNode.asText();
			
			JsonNode descriptionNode = jsonNode.get("description");
			if (descriptionNode == null) {
				errorDescription = "Description is required !";
				return utility.createResponseWithErrors(Constant.ERROR_TRANSFER_DESCRIPTION_REQUIRED, errorDescription);
			}
			String description = descriptionNode.asText();
			
			JsonNode transactionNode = jsonNode.get("transactionAmount");
			if (transactionNode == null) {
				errorDescription = "Transfer amount is required.";
				return utility.createResponseWithErrors(Constant.ERROR_TRANSACTION_AMOUNT_INVALID, errorDescription);
			}
			Double transactionAmount = transactionNode.asDouble();
			
			return transferMoneyToBuddy(senderEmail, receiverEmail, description, transactionAmount);
			
		} catch (JsonProcessingException e) {
			return utility.createResponseWithErrors(Constant.JSON_PARSING_IMPOSSIBLE, e.getMessage());
		}
	}
}
