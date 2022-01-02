package com.paymybuddy.service;

import java.util.Optional;

import com.paymybuddy.model.Account;
import com.paymybuddy.model.User;

public interface IAccountService {
	
	Optional<Account> getAccountNumber(String accountNumber);
	
	boolean updateAccountNumber(Account accountNumber);
	
	User addMoneyOnPayMyBuddyAccount(String email, Double depositAmount) throws Exception;
	
	User sendMoneyToMyBankAccount(String email, Double moneyToSend) throws Exception;
}
