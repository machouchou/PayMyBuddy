package com.paymybuddy.service;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.dto.AccountDto;
import com.paymybuddy.model.Account;
import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.Response;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AccountRepository;
import com.paymybuddy.utility.Constant;
import com.paymybuddy.utility.Utility;

@Service
public class AccountServiceImpl implements IAccountService {
	
	final Logger logger = LogManager.getLogger(AccountServiceImpl.class);

	@Autowired
	private IUserService userService;
	
	@Autowired
	private AccountRepository accountRepository;
	
	private Utility utility;
	private Response response;
	
	public AccountServiceImpl() {
		super();
		utility = new Utility();
		response = new Response();
	}

	@Override
	public Optional<Account> getAccountNumber(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber);
	}

	@Override
	public boolean updateAccountNumber(Account accountNumber) {
		
		accountRepository.save(accountNumber);
		
		return true;
	}

	@Override
	@Transactional
	public ResponseEntity<Response> addMoneyOnPayMyBuddyAccount(String body) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(body);
		String errorDescription ="";
		
		try {
			JsonNode emailNode = jsonNode.get("email");
			if (emailNode == null) {
				errorDescription = "Sender email is required !";
				return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, errorDescription);
			}
			
			String email = jsonNode.get("email").asText();
			
			JsonNode depositMoneyNode = jsonNode.get("depositMoney");
			if (depositMoneyNode == null) {
				errorDescription = "Deposit money is required !";
				return utility.createResponseWithErrors(Constant.ERROR_TRANSACTION_AMOUNT_INVALID, errorDescription);
			}
			Double depositMoney = jsonNode.get("depositMoney").asDouble();
			
			JsonNode descriptionNode = jsonNode.get("description");
			if (descriptionNode == null) {
				errorDescription = "Description is required !";
				return utility.createResponseWithErrors(Constant.ERROR_TRANSFER_DESCRIPTION_REQUIRED, errorDescription);
			}
			String description = jsonNode.get("description").asText();
			
			logger.info(email);
			logger.info(depositMoney);
			logger.info(description);
			
			AppAccount userAccount = userService.findByEmail(email);
			User adminUser = userService.getUserFromAppAccount(Constant.ADMIN_EMAIL);
			
			if (userAccount == null || userAccount.getUser() == null)
				throw new Exception("The email provided is not recognized. Please, check it !");
			
			if(depositMoney <= 0)
				throw new Exception("The deposited Amount is incorrect.");
			
			User user =  userAccount.getUser();
			
			if (user.getAccountPayMyBuddy() == null)
				throw new Exception("The user bank account does not exist. Please create a new one before !");
			Double fees = Constant.FEES_RATE * depositMoney;	
			user.getAccountPayMyBuddy().setAmountBalance(user.getAccountPayMyBuddy().getAmountBalance() + depositMoney - fees);
			adminUser.getAccountPayMyBuddy().setAmountBalance(adminUser.getAccountPayMyBuddy().getAmountBalance() + fees);
			Hibernate.initialize(user.getTransactions());
	
			Transaction transaction = new Transaction();
			transaction.setEmailSender(email);
			transaction.setEmailReceiver(email);
			transaction.setDescription(description);
			transaction.setTransactionDate(new Date());
			transaction.setAmount(depositMoney);
			
			transaction.setFees(fees);
			
			transaction.setUser(user);
			
			user.getTransactions().add(transaction);
			
			userService.save(user);
			userService.save(adminUser);
			
			AccountDto myAccount = new AccountDto();
			myAccount.setUserAccountBalance(user.getAccountPayMyBuddy().getAmountBalance());
			myAccount.setDescription(description);
			myAccount.setFees(fees);
			myAccount.setEmail(user.getAppAccount().getEmail());
			
			utility.createResponseWithSuccess(response, myAccount);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (JsonProcessingException e) {
		
			return utility.createResponseWithErrors(Constant.JSON_PARSING_IMPOSSIBLE, e.getMessage());
		}
	}

	@Override
	@Transactional
	public ResponseEntity<Response> sendMoneyToMyBankAccount(String body) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(body);
		String errorDescription ="";
		
		try {
			JsonNode emailNode = jsonNode.get("email");
			if (emailNode == null) {
				errorDescription = "Sender email is required !";
				return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, errorDescription);
		}
		String email = jsonNode.get("email").asText();
		
		JsonNode depositMoneyNode = jsonNode.get("depositMoney");
		if (depositMoneyNode == null) {
			errorDescription = "Money to send is required !";
			return utility.createResponseWithErrors(Constant.ERROR_TRANSACTION_AMOUNT_INVALID, errorDescription);
		}
		Double depositMoney = jsonNode.get("depositMoney").asDouble();
		
		JsonNode descriptionNode = jsonNode.get("description");
		if (descriptionNode == null) {
			errorDescription = "Description is required !";
			return utility.createResponseWithErrors(Constant.ERROR_TRANSFER_DESCRIPTION_REQUIRED, errorDescription);
		}
		String description = jsonNode.get("description").asText();
		
		logger.info(email);
		logger.info(depositMoney);
		logger.info(description);
		
		AppAccount appAccount = userService.findByEmail(email);
		User adminUser = userService.getUserFromAppAccount(Constant.ADMIN_EMAIL);
		
		if (appAccount == null || appAccount.getUser() == null)
			throw new Exception("The email provided is not recognized. Please, check it !");
		
		if (depositMoney <= 0) {
			errorDescription ="Money to send is incorrect.";
			return utility.createResponseWithErrors(Constant.ERROR_TRANSACTION_AMOUNT_INVALID, errorDescription);
		}
		
		User user = appAccount.getUser();
		
		if (user.getAccountPayMyBuddy() == null)
			throw new Exception("The user bank account does not exist. Please create a new one before !");
		
		Double fees = Constant.FEES_RATE * depositMoney;
		if (user.getAccountPayMyBuddy().getAmountBalance() < (depositMoney + fees))
			throw new Exception("Your balance is not enough for the sending. Check and correct");
		
		user.getAccountPayMyBuddy().setAmountBalance(user.getAccountPayMyBuddy().getAmountBalance() - (depositMoney + fees));
		adminUser.getAccountPayMyBuddy().setAmountBalance(adminUser.getAccountPayMyBuddy().getAmountBalance() + fees);
		
		Transaction transaction = new Transaction();
		transaction.setEmailSender(email);
		transaction.setEmailReceiver(email);
		transaction.setDescription(description);
		transaction.setTransactionDate(new Date());
		transaction.setAmount(depositMoney);
		
		transaction.setFees(fees);
		
		transaction.setUser(user);
		
		user.getTransactions().add(transaction);
		
		userService.save(user);
		userService.save(adminUser);
		
		AccountDto myAccount = new AccountDto();
		myAccount.setUserAccountBalance(user.getAccountPayMyBuddy().getAmountBalance());
		myAccount.setDescription(description);
		myAccount.setFees(fees);
		myAccount.setEmail(user.getAppAccount().getEmail());
		
		utility.createResponseWithSuccess(response, myAccount);
		return new ResponseEntity<>(response, HttpStatus.OK);
		
		 
		} catch (JsonProcessingException e) {
		return utility.createResponseWithErrors(Constant.JSON_PARSING_IMPOSSIBLE, e.getMessage());
			
		}
	}
}
