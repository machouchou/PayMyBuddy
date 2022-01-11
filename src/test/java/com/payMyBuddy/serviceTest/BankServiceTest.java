package com.payMyBuddy.serviceTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;
import org.junit.rules.ExpectedException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.paymybuddy.dto.AccountDto;
import com.paymybuddy.dto.PayMyBoddyDto;
import com.paymybuddy.dto.UserDto;
import com.paymybuddy.model.Account;
import com.paymybuddy.model.Response;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AccountRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.IAccountService;
import com.paymybuddy.service.IPayMyBuddyService;
import com.paymybuddy.service.IUserService;
import com.paymybuddy.utility.Constant;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@EnableRuleMigrationSupport
public class BankServiceTest {
	@Autowired
	IUserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	IPayMyBuddyService payService;
	
	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	IAccountService accountService;
	
	@Rule
	public ExpectedException exceptionThrown = ExpectedException.none();
	List<UserDto> lUser;
	
	@Test
	public void transferMoney_SenderEmailNull_ReturnsEmailRequiredError() {
		// Arrange
		String body = "{\"receiverEmail\":\"jb@gmail.com\", \"description\":\"Premier transfert\", \"transactionAmount\":30.0}";
		
		// Act
		ResponseEntity<Response> transfertResult = payService.transferMoneyToBuddy(body);
		
		// Assert
		assertNull(transfertResult.getBody().getData());
		assertNotNull(transfertResult.getBody().getErrorCode());
		assertEquals(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, transfertResult.getBody().getErrorCode());
		assertEquals("Sender email is required !", transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, transfertResult.getStatusCode());
	}
	
	@Test
	public void transferMoney_EmptySenderEmail_ReturnsEmailRequiredError() {
		// Arrange
		String body = "{\"senderEmail\":\"\",\"receiverEmail\":\"jb@gmail.com\", \"description\":\"Premier transfert\", \"transactionAmount\":30.0}";
		
		// Act
		ResponseEntity<Response> transfertResult = payService.transferMoneyToBuddy(body);
		
		// Assert
		assertNull(transfertResult.getBody().getData());
		assertNotNull(transfertResult.getBody().getErrorCode());
		assertEquals(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, transfertResult.getBody().getErrorCode());
		assertEquals("Sender email is required !", transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, transfertResult.getStatusCode());
	}
	
	@Test
	public void transferMoney_ReceiverEmailNull_ReturnsEmailRequiredError() {
		// Arrange
		String body = "{\"senderEmail\":\"at@live.fr\", \"description\":\"Premier transfert\", \"transactionAmount\":30.0}";
		
		// Act
		ResponseEntity<Response> transfertResult = payService.transferMoneyToBuddy(body);
		
		// Assert
		assertNull(transfertResult.getBody().getData());
		assertNotNull(transfertResult.getBody().getErrorCode());
		assertEquals(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, transfertResult.getBody().getErrorCode());
		assertEquals("Receiver email is required !", transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, transfertResult.getStatusCode());
	}
	
	@Test
	public void transferMoney_EmptyReceiverEmail_ReturnsEmailRequiredError() {
		// Arrange
		String body = "{\"senderEmail\":\"at@live.fr\",\"receiverEmail\":\"\", \"description\":\"Premier transfert\", \"transactionAmount\":30.0}";
		
		// Act
		ResponseEntity<Response> transfertResult = payService.transferMoneyToBuddy(body);
		
		// Assert
		assertNull(transfertResult.getBody().getData());
		assertNotNull(transfertResult.getBody().getErrorCode());
		assertEquals(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, transfertResult.getBody().getErrorCode());
		assertEquals("Receiver email is required !", transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, transfertResult.getStatusCode());
	}
	
	@Test
	public void transferMoney_DescriptionnodeNull_ReturnsDescriptionRequiredError() {
		// Arrange
		String body = "{\"senderEmail\":\"at@live.fr\",\"receiverEmail\":\"al@gmail.com\", \"transactionAmount\":30.0}";
		
		// Act
		ResponseEntity<Response> transfertResult = payService.transferMoneyToBuddy(body);
		
		// Assert
		assertNull(transfertResult.getBody().getData());
		assertNotNull(transfertResult.getBody().getErrorCode());
		assertEquals(Constant.ERROR_TRANSFER_DESCRIPTION_REQUIRED, transfertResult.getBody().getErrorCode());
		assertEquals("Description is required !", transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, transfertResult.getStatusCode());
	}
	@Test
	public void transferMoney_TransactionAmountNull_ReturnsTransactionAmountRequiredError() {
		// Arrange
		String body = "{\"senderEmail\":\"at@live.fr\", \"receiverEmail\":\"jb@gmail.com\", \"description\":\"Premier transfert\", \"transactionAmount\":0.0}";
		
		// Act
		ResponseEntity<Response> transfertResult = payService.transferMoneyToBuddy(body);
		
		// Assert
		assertNull(transfertResult.getBody().getData());
		assertNotNull(transfertResult.getBody().getErrorCode());
		assertEquals(Constant.ERROR_TRANSACTION_AMOUNT_INVALID, transfertResult.getBody().getErrorCode());
		assertEquals("Check your transfered amount.", transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, transfertResult.getStatusCode());
	}
	
	@Test
	public void transferMoney_TransactionAmountEmpty_ReturnsTransactionAmountRequiredError() {
		// Arrange
		String body = "{\"senderEmail\":\"at@live.fr\", \"receiverEmail\":\"jb@gmail.com\", \"description\":\"Premier transfert\"}";
		
		// Act
		ResponseEntity<Response> transfertResult = payService.transferMoneyToBuddy(body);
		
		// Assert
		assertNull(transfertResult.getBody().getData());
		assertNotNull(transfertResult.getBody().getErrorCode());
		assertEquals(Constant.ERROR_TRANSACTION_AMOUNT_INVALID, transfertResult.getBody().getErrorCode());
		assertEquals("Transfer amount is required.", transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, transfertResult.getStatusCode());
	}
	@Test
	public void transferMoney_SenderUserNull_ReturnsSenderUserRequiredError() {
		// Arrange
		String body = "{\"senderEmail\":\"toto\", \"receiverEmail\":\"jb@gmail.com\", \"description\":\"Premier transfert\", \"transactionAmount\":30.0}";
		
		// Act
		ResponseEntity<Response> transfertResult = payService.transferMoneyToBuddy(body);
		
		// Assert
		assertNull(transfertResult.getBody().getData());
		assertNotNull(transfertResult.getBody().getErrorCode());
		assertEquals(Constant.ERROR_NO_USER_FOUND, transfertResult.getBody().getErrorCode());
		assertEquals("There is no user attached to the email(toto)", transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, transfertResult.getStatusCode());
	}
	
	@Test
	public void transferMoney_ReceiverUserNull_ReturnsReceiverUserRequiredError() {
		// Arrange
		String body = "{\"senderEmail\":\"rg@gmail.com\", \"receiverEmail\":\"tata\", \"description\":\"Premier transfert\", \"transactionAmount\":30.0}";
		
		// Act
		ResponseEntity<Response> transfertResult = payService.transferMoneyToBuddy(body);
		
		// Assert
		assertNull(transfertResult.getBody().getData());
		assertNotNull(transfertResult.getBody().getErrorCode());
		assertEquals(Constant.ERROR_NO_USER_FOUND, transfertResult.getBody().getErrorCode());
		assertEquals("There is no user attached to the email(tata)", transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, transfertResult.getStatusCode());
	}
	@Test
	public void transferMoney_WithValidBody_TransferSuccessful() throws Exception {
		// Arrange
		String body = "{\"senderEmail\":\"at@live.fr\",\"receiverEmail\":\"jb@gmail.com\", \"description\":\"Premier transfert\", \"transactionAmount\":30.0}";
		accountService.addMoneyOnPayMyBuddyAccount("{\"email\":\"at@live.fr\", \"description\":\"Alimentation compte avant transfert\", \"depositMoney\": 30.15}");
		User senderUser = userService.getUserFromAppAccount("at@live.fr");
		User receiverUser = userService.getUserFromAppAccount("jb@gmail.com");
		
		double initialSenderBalance = senderUser.getAccountPayMyBuddy().getAmountBalance();
		double initialReceiverBalance = receiverUser.getAccountPayMyBuddy().getAmountBalance();
		double fees = Constant.FEES_RATE * 30.0;
		
		// Act
		ResponseEntity<Response> transfertResult = payService.transferMoneyToBuddy(body);
		PayMyBoddyDto transferInfo = (PayMyBoddyDto)(transfertResult.getBody().getData());
		
		// Assert
		assertNotNull(transfertResult.getBody().getData());
		assertNull(transfertResult.getBody().getErrorCode());
		assertEquals(initialSenderBalance - fees - 30.0 , transferInfo.getSenderAccountBalance());
		assertEquals(initialReceiverBalance + 30.0 , transferInfo.getReceiverAccountBalance());
		assertEquals(fees , transferInfo.getFees());
		assertNull(transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.OK, transfertResult.getStatusCode());
	}
	
	@Test
	public void getAccountNumber() throws Exception {
		// Arrange
		String accountNumber = "711728315";
		List<Account> lAccountNumber = new ArrayList<>();
		
		// Act
		lAccountNumber.addAll(accountRepository.findAll());
		
		Assertions.assertNotEquals(Collections.EMPTY_LIST, lAccountNumber.size());
		
		assertTrue(lAccountNumber.stream().anyMatch(user ->accountNumber.equalsIgnoreCase(accountNumber)));
	}
	
	@Test
	public void addMoneyOnPayMyBuddyAccount_emailNodeNull_ReturnsEmailRequiredError() throws Exception {
		// Arrange
		String body = "{\"description\":\"Alimentation compte\", \"depositMoney\":20.0}";
		
		// Act
		ResponseEntity<Response> transfertResult = accountService.addMoneyOnPayMyBuddyAccount(body);
		
		// Assert
		assertNull(transfertResult.getBody().getData());
		assertNotNull(transfertResult.getBody().getErrorCode());
		assertEquals(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, transfertResult.getBody().getErrorCode());
		assertEquals("Sender email is required !", transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, transfertResult.getStatusCode());
	}
	
	@Test
	public void addMoneyOnPayMyBuddyAccount_WithValidBody_ReturnsDepositMoneySuccessful() throws Exception {
		// Arrange
		String body = "{\"email\":\"rg@gmail.com\", \"description\":\"Alimentation compte\", \"depositMoney\": 20.0}";
		User user = userService.getUserFromAppAccount("rg@gmail.com");
		
		double initialUserBalance = user.getAccountPayMyBuddy().getAmountBalance();
		double fees = Constant.FEES_RATE * 20.0;
		
		// Act
		ResponseEntity<Response> transfertResult = accountService.addMoneyOnPayMyBuddyAccount(body);
		AccountDto transferInfo = (AccountDto)(transfertResult.getBody().getData());
		// Assert
		assertNotNull(transferInfo);
		assertNull(transfertResult.getBody().getErrorCode());
		assertEquals(initialUserBalance - fees + 20.0 , transferInfo.getUserAccountBalance());
		assertEquals(fees , transferInfo.getFees());
		assertNull(transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.OK, transfertResult.getStatusCode());
	}
	
	@Test
	public void sendMoneyToMyBankAccount_emailNodeNull_ReturnsEmailRequiredError() throws Exception {
		// Arrange
		String body = "{\"description\":\"Premier transfert\", \"moneyToSend\":30.0}";
		
		// Act
		ResponseEntity<Response> transfertResult = accountService.sendMoneyToMyBankAccount(body);
		
		// Assert
		assertNull(transfertResult.getBody().getData());
		assertNotNull(transfertResult.getBody().getErrorCode());
		assertEquals(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, transfertResult.getBody().getErrorCode());
		assertEquals("Sender email is required !", transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, transfertResult.getStatusCode());
	}
	
	@Test
	public void sendMoneyToMyBankAccount_depositMoneyNodeNull_ReturnsSendedMoneyRequiredError() throws Exception {
		// Arrange
		String body = "{\"email\":\"jb@gmail.com\", \"description\":\"Premier transfert\"}";
		
		// Act
		ResponseEntity<Response> transfertResult = accountService.sendMoneyToMyBankAccount(body);
		
		// Assert
		assertNull(transfertResult.getBody().getData());
		assertNotNull(transfertResult.getBody().getErrorCode());
		assertEquals(Constant.ERROR_TRANSACTION_AMOUNT_INVALID, transfertResult.getBody().getErrorCode());
		assertEquals("Money to send is required !", transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, transfertResult.getStatusCode());
	}
	
	@Test
	public void sendMoneyToMyBankAccount_depositMoneyNull_ReturnsSendedMoneyRequiredError() throws Exception {
		// Arrange
		String body = "{\"email\":\"jb@gmail.com\", \"description\":\"Premier transfert\", \"depositMoney\":0.0}";
		
		// Act
		ResponseEntity<Response> transfertResult = accountService.sendMoneyToMyBankAccount(body);
		
		// Assert
		assertNull(transfertResult.getBody().getData());
		assertNotNull(transfertResult.getBody().getErrorCode());
		assertEquals(Constant.ERROR_TRANSACTION_AMOUNT_INVALID, transfertResult.getBody().getErrorCode());
		assertEquals("Money to send is incorrect.", transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, transfertResult.getStatusCode());
	}
	@Test
	public void sendMoneyToMyBankAccount_descriptionNodeNull_ReturnsDescriptionRequiredError() throws Exception {
		// Arrange
		String body = "{\"email\":\"jb@gmail.com\", \"depositMoney\":30.0}";
		
		// Act
		ResponseEntity<Response> transfertResult = accountService.sendMoneyToMyBankAccount(body);
		
		// Assert
		assertNull(transfertResult.getBody().getData());
		assertNotNull(transfertResult.getBody().getErrorCode());
		assertEquals(Constant.ERROR_TRANSFER_DESCRIPTION_REQUIRED, transfertResult.getBody().getErrorCode());
		assertEquals("Description is required !", transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, transfertResult.getStatusCode());
	}
	 
	@Test
	public void sendMoneyToMyBankAccount_WithValidBody_ReturnsSendedMoneySuccessful() throws Exception {
		// Arrange
		String body = "{\"email\":\"rg@gmail.com\", \"description\":\"Alimentation compte\", \"depositMoney\": 20.0}";
		User user = userService.getUserFromAppAccount("rg@gmail.com");
		
		double initialUserBalance = user.getAccountPayMyBuddy().getAmountBalance();
		double fees = Constant.FEES_RATE * 20.0;
		
		// Act
		ResponseEntity<Response> transfertResult = accountService.sendMoneyToMyBankAccount(body);
		AccountDto transferInfo = (AccountDto)(transfertResult.getBody().getData());
		// Assert
		assertNotNull(transferInfo);
		assertNull(transfertResult.getBody().getErrorCode());
		assertEquals(initialUserBalance - fees - 20.0 , transferInfo.getUserAccountBalance());
		assertEquals(fees , transferInfo.getFees());
		assertNull(transfertResult.getBody().getErrorDescription());
		assertEquals(HttpStatus.OK, transfertResult.getStatusCode());
	}
	
	/*@Test
	public void sendMoneyToMyBankAccount_WithInValidBody_ReturnsSendedMoneyFailed() throws Exception {
		// Arrange
		String body = "tata";
				
		// Act
		ResponseEntity<Response> transfertResult = accountService.sendMoneyToMyBankAccount(body);
		// Assert
		assertNull(transfertResult.getBody().getData());
		assertNotNull(transfertResult.getBody().getErrorCode());
		assertEquals(Constant.JSON_PARSING_IMPOSSIBLE, transfertResult.getBody().getErrorCode());
		assertEquals(HttpStatus.FORBIDDEN, transfertResult.getStatusCode());
	}*/
}
