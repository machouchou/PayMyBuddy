package com.paymybuddy.service;

//import com.paymybuddy.model.Account;
import com.paymybuddy.model.AppAccount;
//import com.paymybuddy.model.User;

public interface IPayMyBuddyService {
	
	//void addAccountNumber(AppAccount appAccount, String accountNumber); rajouter la création du accountNumber en même temps que la création du user
	
	boolean transferMoneyToBuddy(String emailSender, String emailReceiver, String description,
			Double transactionAmount) throws Exception;
	
	//void addMoneyFromAccountToPayMyBuddy(User user, Account account, Double amountadded);

	//void sendMoneyFromPayMyBuddyToUserAccount(User user, Account account, Double amountSended);
}
