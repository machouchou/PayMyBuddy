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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paymybuddy.dto.AppAccountDto;
import com.paymybuddy.dto.FriendDto;
import com.paymybuddy.dto.UserDto;
import com.paymybuddy.model.Friend;
import com.paymybuddy.service.IUserService;

@Controller
public class UserController {
	
	static final Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private IUserService userService;
	
	@RequestMapping("/users")
	@ResponseBody
	public List<UserDto> list() {
		logger.info("list()");
				
		return  userService.findAllUsers();
		
	}
	
	@RequestMapping(value="/user")
	@ResponseBody
	public ResponseEntity<UserDto> createUser(@NotNull @RequestBody final UserDto user) {
		logger.info("list()");
		
		userService.save(user);
		
		return new ResponseEntity<>(user, HttpStatus.CREATED);
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
}
