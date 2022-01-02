package com.paymybuddy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.dto.AppAccountDto;
import com.paymybuddy.dto.FriendDto;
import com.paymybuddy.dto.UserDto;
import com.paymybuddy.model.Friend;
import com.paymybuddy.model.Response;
import com.paymybuddy.model.User;
import com.paymybuddy.service.IAccountService;
import com.paymybuddy.service.IPayMyBuddyService;
import com.paymybuddy.service.IUserService;

@Controller
@Transactional
public class UserController {
	
	static final Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private IPayMyBuddyService payMyBuddyService;
	
	@RequestMapping("/users")
	@ResponseBody
	public List<UserDto> list() {
		logger.info("list()");
				
		return  userService.findAllUsers();
		
	}
	
	@RequestMapping(value="/user")
	@ResponseBody
	public ResponseEntity<Response> createUser(@NotNull @RequestBody final UserDto user) {
		logger.info("createUser()");
	
		return userService.save(user);
	}
	
	@PostMapping(value="/userLogin")
	@ResponseBody
	public ResponseEntity<String> authenticateUser(@RequestBody @Valid AppAccountDto account) throws JsonProcessingException {
		logger.info("GET /loginUser called");
		// Enter email and password not null;
		
		if (account.getEmail() == null) {
			return new ResponseEntity<>("Authentication failed, your email is required", HttpStatus.BAD_REQUEST);
		}
		
		if (account.getPassword() == null) {
			return new ResponseEntity<>("Authentication failed, your password is required", HttpStatus.BAD_REQUEST);
		}
		
		boolean response = false;
		try {
			response = userService.logUser(account.getEmail(), account.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> resp = new HashMap<>();
		resp.put("Authentication succeded", "true");
		String respons = mapper.writeValueAsString(resp);
		return response ? 
				new ResponseEntity<>(respons, HttpStatus.OK) : 
					new ResponseEntity<>("Authentication failed", HttpStatus.FORBIDDEN);
	} 
	
	@RequestMapping(value="/addFriend")
	@ResponseBody
	public ResponseEntity<String> addFriend(@NotNull @RequestBody @Valid FriendDto friendDto) {
		
		Friend response = null;
		try {
			response = userService.addFriend(friendDto.getUserEmail(), friendDto.getFriendEmail());
		} catch (Exception e) {
			return new ResponseEntity<>(
					"Adding friend failed \n" + e.getMessage() , 
					HttpStatus.FORBIDDEN)	;
			
		} 
		
		return response != null ? 
				new ResponseEntity<>(
				"Friend saved with id : " + response.getFriendId().getFriendId() , 
				HttpStatus.OK) :
				new ResponseEntity<>(
						"Adding friend failed" , 
						HttpStatus.FORBIDDEN)	;
	}
	
	@PostMapping("/depositAmount")
	@ResponseBody
	public ResponseEntity<Response> addMoneyOnPayMyBuddyAccount(@RequestBody String body) throws JsonMappingException, JsonProcessingException {
// 	public ResponseEntity<Response> addMoneyOnPayMyBuddyAccount(String email, Double depositMoney) {
		logger.info("addMoney()"); 
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readTree(body);
		String email = jsonNode.get("email").asText();
		Double depositMoney = jsonNode.get("depositMoney").asDouble();
		logger.info(email);
		logger.info(depositMoney);
		Response response = new Response();
		
		// extraire l'email et depositMoney par Json parser
		try {
			User user = accountService.addMoneyOnPayMyBuddyAccount(email, depositMoney);
			
			if (user != null && user.getAccountPayMyBuddy() != null) {
				response.setData(user.getAccountPayMyBuddy().getAmountBalance());
				response.setStatus(HttpStatus.OK);
				response.setErrorCode(null);
			} 
			
		} catch (Exception e) {
			response.setData(null);
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setErrorCode("Operation add Money Failed : " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(value="/transferMoney")
	@ResponseBody
	public ResponseEntity<Response> transferMoneyToBuddy(@RequestBody String body) {
		logger.info("transferMoneyToBuddy()"); 
		return payMyBuddyService.transferMoneyToBuddy(body);
	}
	
	@RequestMapping(value="/sendMoney")
	@ResponseBody
	public ResponseEntity<Response> sendMoneyToMyBankAccount(@NotNull @RequestParam String email, Double moneyToSend) {
		logger.info("sendMoney()");
		
		Response response = new Response();
		
		try {
			User user = accountService.sendMoneyToMyBankAccount(email, moneyToSend);
			
			if (user !=null && user.getAccountPayMyBuddy() != null) {
				response.setData(user.getAccountPayMyBuddy().getAmountBalance());
				response.setStatus(HttpStatus.OK);
				response.setErrorCode(null);
			}
		} catch (Exception e) {
			response.setData(null);
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setErrorCode("Operation send Money Failed : " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/listFriend")
	@ResponseBody
	public ResponseEntity<Response> getUserFriends(@NotNull final String email) {
		logger.info("listFriend()");
	
		return userService.getUserFriends(email);
	}

}
