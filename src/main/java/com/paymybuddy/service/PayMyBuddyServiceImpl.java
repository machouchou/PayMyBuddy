package com.paymybuddy.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.User;

@Service
public class PayMyBuddyServiceImpl implements IPayMyBuddyService{
	
	final Logger logger = LogManager.getLogger(PayMyBuddyServiceImpl.class);

	@Autowired
	private IUserService userService;
	@Override
	public boolean transferMoneyToBuddy(String emailSender, String emailReceiver, String description,
			Double transactionAmount) throws Exception {
		
		if (StringUtils.isEmpty(emailSender)) {
			throw new Exception("emailSender is required");
		}
		
		if (StringUtils.isEmpty(emailSender)) {
			throw new Exception("emailReceiver is required");
		}
		
		User userSender = userService.getUserFromAppAccount(emailSender);
		User userReceiver = userService.getUserFromAppAccount(emailReceiver);
		
		if (userSender.getAccountPayMyBuddy().getAmountBalance() == 0) {
			throw new Exception("Transfer failed , amount available is not enough for transaction");
		}
		
		//userSender.getAccountPayMyBuddy().getAmountBalance() - transactionAmount
		userReceiver.getAccountPayMyBuddy().setAmountBalance(transactionAmount);
		return false;
	}

}
