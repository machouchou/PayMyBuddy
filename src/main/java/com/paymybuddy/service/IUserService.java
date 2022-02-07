package com.paymybuddy.service;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.paymybuddy.dto.ProfileDto;
import com.paymybuddy.dto.UserDto;
import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.Response;
import com.paymybuddy.model.User;

public interface IUserService {
	
	ResponseEntity<Response> save(UserDto user);
	
	User save(User userToBeSaved) throws Exception;
	
	AppAccount findByEmail(String email);
	
	User getUserFromAppAccount(String email);
	
	ResponseEntity<Response> addFriend(String body);
	
	List<UserDto> findAllUsers();
	
	ResponseEntity<Response> getUser(String email);
	
	User update(User user);
	
	ResponseEntity<Response> logUser(String body); 
	
	ResponseEntity<Response> getUserFriends(String email);
	
	ResponseEntity<Response> getUserTransactions(String email, Pageable pageable);
	
}
