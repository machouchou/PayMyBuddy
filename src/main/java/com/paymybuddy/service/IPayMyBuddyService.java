package com.paymybuddy.service;

import org.springframework.http.ResponseEntity;

import com.paymybuddy.model.Response;

public interface IPayMyBuddyService {
	
	// PayMyBoddyDto transferMoneyToBuddy(String emailSender, String emailReceiver, String description,
	//		Double transactionAmount) throws Exception;

	ResponseEntity<Response> transferMoneyToBuddy(String body);
}
