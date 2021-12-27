package com.paymybuddy.service;

import com.paymybuddy.model.Account;
import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.User;

public interface IPayMyBuddyService {
	
	//void addAccountNumber(AppAccount appAccount, String accountNumber); rajouter la création du accountNumber en même temps que la création du user
	
	void createTransaction(AppAccount emailSender, AppAccount emailReceiver, String description,
			Double amountOfTransaction);
	
	void addFriend(Integer userId, String emailFriend);
	
	void addMoneyFromAccountToPayMyBuddy(User user, Account account, Double amountadded);

	void sendMoneyFromPayMyBuddyToUserAccount(User user, Account account, Double amountSended);
}
