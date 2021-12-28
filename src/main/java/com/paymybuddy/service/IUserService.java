package com.paymybuddy.service;


import java.util.List;

import com.paymybuddy.dto.UserDto;
import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.Friend;
import com.paymybuddy.model.User;

public interface IUserService {
	
	boolean save(UserDto user) throws Exception;
	
	User save(User userToBeSaved) throws Exception;
	
	AppAccount findByEmail(String email);
	
	User getUserFromAppAccount(String email);
	
	Friend addFriend(String userEmail, String friendEmail) throws Exception;
	
	List<UserDto> findAllUsers();
	
	User update(User user);
	
	Boolean logUser(String email, String password) throws Exception; 
	
}
