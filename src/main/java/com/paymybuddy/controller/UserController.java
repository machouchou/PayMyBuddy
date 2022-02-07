package com.paymybuddy.controller;

import java.util.List;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.paymybuddy.dto.ProfileDto;
import com.paymybuddy.dto.UserDto;
import com.paymybuddy.model.Response;
import com.paymybuddy.model.User;
import com.paymybuddy.service.IUserService;

@Controller
@Transactional
@CrossOrigin(origins = "*", exposedHeaders = "*")
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
	
	@RequestMapping("/profile")
	@ResponseBody
	public ResponseEntity<Response> getUser(@RequestParam final String email) {
		logger.info("getUser()");
		return userService.getUser(email);
		
	}
	
	@RequestMapping(value="/user")
	@ResponseBody
	public ResponseEntity<Response> createUser(@NotNull @RequestBody final UserDto user) {
		logger.info("createUser()");
		return userService.save(user);
	}
	
	@PostMapping(value="/userLogin")
	@ResponseBody
	//public ResponseEntity<String> authenticateUser(@RequestBody @Valid AppAccountDto account) throws JsonProcessingException
	public ResponseEntity<Response> authenticateUser(@NotNull @RequestBody String body) {
		logger.info("Post /loginUser called");
		return userService.logUser(body);
	} 
	
	@RequestMapping(value="/addFriend")
	@ResponseBody
	public ResponseEntity<Response> addFriend(@NotNull @RequestBody String body) {
		return userService.addFriend(body);
	}
	
	@RequestMapping(value="/listFriend")
	@ResponseBody
	public ResponseEntity<Response> getUserFriends(@NotNull final String email) {
		logger.info("listFriend()");
		return userService.getUserFriends(email);
	}
	
	@RequestMapping(value="/transaction")
	@ResponseBody
	public ResponseEntity<Response> getUserTransactions(@RequestParam @NotNull final String email, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
) {
		logger.info("listUserTransaction()");
		Pageable paging = PageRequest.of(page, size);

		return userService.getUserTransactions(email, paging);
	}

}
