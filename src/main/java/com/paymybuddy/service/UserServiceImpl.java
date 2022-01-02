package com.paymybuddy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.paymybuddy.dto.AppAccountDto;
import com.paymybuddy.dto.UserDto;
import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.Friend;
import com.paymybuddy.model.FriendRelationship;
import com.paymybuddy.model.Response;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.FriendsRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.utility.Constant;
import com.paymybuddy.utility.Utility;

@Service
public class UserServiceImpl implements IUserService {
	
	final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FriendsRepository friendRepository;
	
	@Autowired
	private AppAccountRepository appAccountRepository;
	
	private Utility utility;
	
	public UserServiceImpl() {
		utility = new Utility();
	}

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
	public ResponseEntity<Response> save(UserDto userDto) {
		Response response = new Response();
		String errorDescription = "";
		
		if (userDto == null) {
			errorDescription = "A valid user is required !";
			return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_USER_EXISTED, errorDescription);
		}
		
		if (userDto.getAppAccountDto() == null) {
			errorDescription = "A valid appAccount is required !";
			return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_APP_ACCOUNT_REQUIRED, errorDescription);
		}
		
		if (StringUtils.isEmpty(userDto.getAppAccountDto().getEmail())) {
			errorDescription = "A valid email is required !";
			return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, errorDescription);
		}
		
		if (StringUtils.isEmpty(userDto.getAppAccountDto().getPassword())) {
			errorDescription = "A valid password is required !";
			return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_PASSWORD_REQUIRED, errorDescription);
		}
		
		if (appAccountRepository.findByEmail(userDto.getAppAccountDto()
				.getEmail()) != null) {
			errorDescription = "A user with this email already exists !";
			return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_USER_EXISTED, errorDescription);
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
		User user = userRepository.save(userToBeSaved);
		
		// Preparing Response to be send to the client
		UserDto userSaved = new UserDto(user.getFirstName(),
				user.getLastName(),
				user.getBirthDate(),
				user.getAddress(),
				user.getCountry());
		
		AppAccountDto accountSaved = new AppAccountDto();
		accountSaved.setEmail(user.getAppAccount().getEmail());
		userSaved.setAppAccountDto(accountSaved);
		
		new Utility().createResponseWithSuccess(response, userSaved);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
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


	@Override
	public ResponseEntity<Response> getUserFriends(String email) {
		
		AppAccount userAccount = findByEmail(email);
		Response response = new Response();
		
		String errorType = "Getting friends";
		String errorMessage = "";
		
		if (userAccount == null) {
			errorMessage = String.format("There is no user registered with this email (%s).", email);
			return utility.createResponseWithErrors(errorType, errorMessage);
		}
		
		User user = userAccount.getUser();
		
		List<FriendRelationship> friendRelationshipList = user.getFriends()
			.stream().map(Friend::getFriendId).collect(Collectors.toList());
		 
		List<Integer> friendIds = friendRelationshipList.stream()
			.map(FriendRelationship::getFriendId).collect(Collectors.toList());
		
		List<UserDto> listUserFriend = new ArrayList<>();
		
		for (Integer id : friendIds) {
			Optional<User> friend = userRepository.findById(id);
			
			if (!friend.isPresent()) {
				errorMessage = String.format("There is no friend attached to this user (%s %s).",
						user.getFirstName(), user.getLastName().toUpperCase());
				return utility.createResponseWithErrors(errorType, errorMessage);
			}
			
			UserDto friendDto = new UserDto();
			friendDto.setFirstName(friend.get().getFirstName());
			friendDto.setLastName(friend.get().getLastName());
			friendDto.setBirthDate(friend.get().getBirthDate());
			friendDto.setAddress(friend.get().getAddress());
			friendDto.setCountry(friend.get().getCountry());
			
			listUserFriend.add(friendDto);
		}
		
		response.setData(listUserFriend);
		response.setStatus(HttpStatus.OK);
		response.setErrorCode(null);
		response.setErrorDescription(null);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
