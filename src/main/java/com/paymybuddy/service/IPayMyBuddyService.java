package com.paymybuddy.service;

import org.springframework.http.ResponseEntity;

import com.paymybuddy.model.Response;

public interface IPayMyBuddyService {
	
	ResponseEntity<Response> transferMoneyToBuddy(String body);
}
