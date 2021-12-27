package com.paymybuddy.service;

import java.util.Optional;

import com.paymybuddy.model.Account;

public interface IAccountService {
	
	Optional<Account> getAccountNumber(String accountNumber);
	
	boolean saveAccountNumber(Account accountNumber);
	
	boolean updateAccountNumber(Account accountNumber);
	
}
