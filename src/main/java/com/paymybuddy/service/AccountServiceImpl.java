package com.paymybuddy.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.Account;
import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AccountRepository;

@Service
public class AccountServiceImpl implements IAccountService {
final Logger logger = LogManager.getLogger(AccountServiceImpl.class);
	

	@Autowired
	private IUserService userService;
	
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Optional<Account> getAccountNumber(String accountNumber) {
		return accountRepository.findByAccountNumber(accountNumber);
	}
	
	// creation et Mise à jour du compte bancaire
	/*@Override
	public boolean saveAccountNumber(String userEmail, Account userBankAccount) {
		
		Account userBankAccountToBeSaved = new Account();
		userBankAccountToBeSaved.setAccountNumber(userBankAccount.getAccountNumber());
		userBankAccountToBeSaved.setAmountBalance(userBankAccount.getAmountBalance()); 
		userBankAccountToBeSaved.setCurrency(userBankAccount.getCurrency()); 
		
		userBankAccountToBeSaved.setUser(userService.getUserFromAppAccount(userEmail));
		
		accountRepository.save(userBankAccountToBeSaved);
		
		return true;
	}*/

	@Override
	public boolean updateAccountNumber(Account accountNumber) {
		
		accountRepository.save(accountNumber);
		
		return true;
	}

	@Override
	public User addMoneyOnPayMyBuddyAccount(String email, Double depositAmount) throws Exception {
		
		AppAccount userAccount = userService.findByEmail(email);
				
		if (userAccount == null || userAccount.getUser() == null)
			throw new Exception("The email provided is not recognized. Please, check it !");
		
		User user =  userAccount.getUser();
		
		if (user.getAccountPayMyBuddy() == null)
			throw new Exception("The user bank account does not exist. Please create a new one before !");
				
		user.getAccountPayMyBuddy().setAmountBalance(user.getAccountPayMyBuddy().getAmountBalance() + depositAmount); 
		
		return userService.save(user);
		
	}
}
