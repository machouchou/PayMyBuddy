package com.paymybuddy.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.dto.AppAccountDto;
import com.paymybuddy.dto.UserDto;
import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.Friend;
import com.paymybuddy.model.FriendRelationship;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.FriendsRepository;
import com.paymybuddy.repository.UserRepository;

@Service
public class UserServiceImpl implements IUserService {
	
	final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FriendsRepository friendRepository;
	
	@Override
	public List<UserDto> findAllUsers() {
		logger.debug("list()");
		
		List<UserDto> usersDto = new ArrayList<>();
		
		List <User> users =  userRepository.findAll();
		
		for (User user : users) {
			UserDto userDto = new UserDto();
			AppAccountDto appAccountDto = new AppAccountDto();
			
			userDto.setFirstName(user.getFirstName());
			userDto.setLastName(user.getLastName());
			userDto.setAddress(user.getAddress());
			userDto.setBirthDate(user.getBirthDate());
			userDto.setCountry(user.getCountry());
			
			if (user.getAppAccount() != null)
			{
				appAccountDto.setEmail(user.getAppAccount().getEmail());
				appAccountDto.setPassword(user.getAppAccount().getPassword());
			}
			
			userDto.setAppAccountDto(appAccountDto);
			
			usersDto.add(userDto);
			
		}
		return usersDto;
	}
	
	@Override
	@Transactional
	public boolean save(UserDto userDto) throws Exception {
		if (userDto == null) {
			throw new Exception("A valid user is required");
		}
		
		if (userDto.getAppAccountDto() == null) {
			throw new Exception("A valid appAccount is required");
		}
		
		if (StringUtils.isEmpty(userDto.getAppAccountDto().getEmail())) {
			throw new Exception("A valid email is required");
		}
		
		if (appAccountRepository.findByEmail(userDto.getAppAccountDto()
				.getEmail()) != null) {
			throw new Exception("An user with this email exists already");
		}
		
		User userToBeSaved = new User();
		userToBeSaved.setFirstName(userDto.getFirstName());
		userToBeSaved.setLastName(userDto.getLastName());
		userToBeSaved.setAddress(userDto.getAddress());
		userToBeSaved.setBirthDate(userDto.getBirthDate());
		userToBeSaved.setCountry(userDto.getCountry());
		
		AppAccount appAccountToBeSaved = new AppAccount();
		appAccountToBeSaved.setEmail(userDto.getAppAccountDto().getEmail());
		appAccountToBeSaved.setPassword(userDto.getAppAccountDto().getPassword());
		
		// set child reference
		appAccountToBeSaved.setUser(userToBeSaved);

		// set Parent reference
		userToBeSaved.setAppAccount(appAccountToBeSaved);
		
		// save the parent which will save the child (appAccount) as well
		userRepository.save(userToBeSaved);
		return true;
	}
	
	@Override
	@Transactional
	public User save(User userToBeSaved) throws Exception {
		if (userToBeSaved == null) {
			throw new Exception("A valid user is required");
		}
		
		return userRepository.save(userToBeSaved);
	}

	@Override
	public User update(User user) {
		return userRepository.save(user) ;
	}
	
	@Autowired
	private AppAccountRepository appAccountRepository;
	
	@Override
	public Boolean logUser(String email, String password) throws Exception {
	    	
        if (email == null) {
            throw new Exception("Authentication failed, your email is required");
        }
        
        if (password == null) {
        	throw new Exception("Authentication failed, your email is required");
        }
        
        AppAccount appAccount = appAccountRepository.findByEmail(email);
        
        if (appAccount == null) {
        	throw new RuntimeException("Authentication failed !");
        }
 
        appAccount.setAuthenticated(appAccount.getPassword().equals(password));
        
        return appAccount.isAuthenticated();
	}

	@Override
	public AppAccount findByEmail(String email) {
		return appAccountRepository.findByEmail(email);
		
	}

	@Override
	public User getUserFromAppAccount(String email) {
		return findByEmail(email).getUser();
	}

	@Override
	public Friend addFriend(String userEmail, String friendEmail) throws Exception {
		
		if (StringUtils.isEmpty(userEmail)) {
			throw new Exception("A valid user email is required");
		}
		
		if (StringUtils.isEmpty(friendEmail)) {
			throw new Exception("A valid friend email is required");
		}
		
		User userFriend = getUserFromAppAccount(friendEmail);
		User user = getUserFromAppAccount(userEmail);
		
		if (userFriend == null) {
			throw new Exception("A user friend with this email :" + friendEmail +  "is not found.");
		}
		
		if (user == null) {
			throw new Exception("A user with this email :" + userEmail +  "is not found.");
		}
		
		if (user.getFriends()
				.stream()
				.anyMatch(friend -> friend.getFriendId()
						.getFriendId() == userFriend.getUserId())) {
			throw new Exception("The current user (" + 
						user.getFirstName() + " " + 
						user.getLastName().toUpperCase() + 
						") is already friend with user (" + 
						userFriend.getFirstName() + " " + 
						userFriend.getLastName().toUpperCase() + ").");
		}
		
		Friend friend = new Friend();
		friend.setUser(user);
		FriendRelationship friendId = new FriendRelationship();
		friendId.setUserId(user.getUserId());
		friendId.setFriendId(userFriend.getUserId());
		
		friend.setFriendId(friendId);
		
		return friendRepository.save(friend);
	}
}
