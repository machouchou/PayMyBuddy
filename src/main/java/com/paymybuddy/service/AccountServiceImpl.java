package com.paymybuddy.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.Account;
import com.paymybuddy.repository.AccountRepository;

@Service
public class AccountServiceImpl implements IAccountService {
final Logger logger = LogManager.getLogger(AccountServiceImpl.class);
	
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Optional<Account> getAccountNumber(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber);
	}

	@Override
	public boolean saveAccountNumber(Account accountNumber) {
		
		Account accountToBeSaved = new Account();
		accountToBeSaved.setAccountId(accountNumber.getAccountId());
		accountToBeSaved.setAccountNumber(accountNumber.getAccountNumber());
		accountToBeSaved.setAmountBalance(accountNumber.getAmountBalance()); 
		accountToBeSaved.setCurrency(accountNumber.getCurrency()); 
		accountRepository.save(accountNumber);
		
		return true;
	}

	@Override
	public boolean updateAccountNumber(Account accountNumber) {
		
		accountRepository.save(accountNumber);
		
		return true;
	}
}
