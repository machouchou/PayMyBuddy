package com.paymybuddy.controller;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paymybuddy.model.Response;
import com.paymybuddy.service.IAccountService;
import com.paymybuddy.service.IPayMyBuddyService;

@Controller
@Transactional
@CrossOrigin(origins = "*")
public class BankController {
	static final Logger logger = LogManager.getLogger(UserController.class);
	
	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private IPayMyBuddyService payMyBuddyService;
	
	@PostMapping("/depositAmount")
	@ResponseBody
	public ResponseEntity<Response> addMoneyOnPayMyBuddyAccount(@RequestBody String body) throws Exception {
// 	public ResponseEntity<Response> addMoneyOnPayMyBuddyAccount(String email, Double depositMoney) {
		logger.info("addMoney()"); 
		return accountService.addMoneyOnPayMyBuddyAccount(body);
	}
	
	@PostMapping(value="/transferMoney")
	@ResponseBody
	public ResponseEntity<Response> transferMoneyToBuddy(@RequestBody String body) {
		logger.info("transferMoneyToBuddy()"); 
		return payMyBuddyService.transferMoneyToBuddy(body);
	}
	
	@RequestMapping(value="/sendMoney")
	@ResponseBody
	// public ResponseEntity<Response> sendMoneyToMyBankAccount(@NotNull @RequestParam String email, Double moneyToSend)
	public ResponseEntity<Response> sendMoneyToMyBankAccount(@NotNull @RequestBody String body) throws Exception {
		logger.info("sendMoney()");
		return accountService.sendMoneyToMyBankAccount(body);
	}

}
