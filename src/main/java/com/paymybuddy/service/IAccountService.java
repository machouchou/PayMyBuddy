package com.paymybuddy.service;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.paymybuddy.model.Account;
import com.paymybuddy.model.Response;
import com.paymybuddy.model.User;

public interface IAccountService {
	
	Optional<Account> getAccountNumber(String accountNumber);
	
	boolean updateAccountNumber(Account accountNumber);
	
	ResponseEntity<Response> addMoneyOnPayMyBuddyAccount(String body) throws Exception;
	
	ResponseEntity<Response> sendMoneyToMyBankAccount(String body) throws Exception;
}
