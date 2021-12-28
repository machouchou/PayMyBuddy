package com.paymybuddy.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paymybuddy.dto.AccountDto;
import com.paymybuddy.dto.AppAccountDto;
import com.paymybuddy.dto.FriendDto;
import com.paymybuddy.dto.UserDto;
import com.paymybuddy.model.Friend;
import com.paymybuddy.model.Response;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AccountRepository;
import com.paymybuddy.service.IAppAccountService;
import com.paymybuddy.service.IUserService;
import com.paymybuddy.service.IAccountService;

@Controller
public class UserController {
	
	static final Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IAccountService accountService;
	
	@RequestMapping("/users")
	@ResponseBody
	public List<UserDto> list() {
		logger.info("list()");
				
		return  userService.findAllUsers();
		
	}
	
	@RequestMapping(value="/user")
	@ResponseBody
	public ResponseEntity<Response> createUser(@NotNull @RequestBody final UserDto user) {
		logger.info("list()");
		
		Response response = new Response();
		
		try {
			userService.save(user);
			response.setData(user);
			response.setStatus(HttpStatus.OK);
			response.setError(null);
		} catch (Exception e) {
			response.setData(null);
			response.setStatus(HttpStatus.FORBIDDEN);
			response.setError(String.format("Creating user failed : %s", e.getMessage()));
			return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(value="/userLogin")
	@ResponseBody
	public ResponseEntity<String> authenticateUser(@RequestBody @Valid AppAccountDto account) {
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
		
		return response ? 
				new ResponseEntity<>("Authentication succeeded", HttpStatus.OK) : 
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
	
	@GetMapping("/depositAmount/{email}/{depositMoney}")
//	@RequestMapping(value="/depositAmount")
	@ResponseBody
	public ResponseEntity<Response> addMoneyOnPayMyBuddyAccount(@PathVariable String email, @PathVariable Double depositMoney) {
// 	public ResponseEntity<Response> addMoneyOnPayMyBuddyAccount(String email, Double depositMoney) {
		logger.info("addMoney()"); 
		
		Response response = new Response();
		
		try {
			User user = accountService.addMoneyOnPayMyBuddyAccount(email, depositMoney);
			
			if (user != null && user.getAccountPayMyBuddy() != null) {
				response.setData(user.getAccountPayMyBuddy().getAmountBalance());
				response.setStatus(HttpStatus.OK);
				response.setError(null);
			} 
			
		} catch (Exception e) {
			response.setData(null);
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setError("Operation add Money Failed : " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
			
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
