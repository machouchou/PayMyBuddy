package com.paymybuddy.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.dto.AppAccountDto;
import com.paymybuddy.dto.FriendDto;
import com.paymybuddy.dto.ProfileDto;
import com.paymybuddy.dto.TransactionDto;
import com.paymybuddy.dto.UserDto;
import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.Friend;
import com.paymybuddy.model.FriendRelationship;
import com.paymybuddy.model.Response;
import com.paymybuddy.model.Transaction;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.FriendsRepository;
import com.paymybuddy.repository.TransactionRepository;
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
	private TransactionRepository transactionRepository;
	
	@Autowired
	private AppAccountRepository appAccountRepository;
	
	private Utility utility;
	
	private Response response;
	
	public UserServiceImpl() {
		utility = new Utility();
		response = new Response();
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
	public ResponseEntity<Response> saveUser(UserDto userDto) {
		String errorDescription = "";
		if (userDto == null) {
			errorDescription = "A valid user is required !";
			return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_USER_EXISTED, errorDescription);
		}
		
		if (userDto.getAppAccountDto() == null) {
			errorDescription = "A valid appAccount is required !";
			return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_APP_ACCOUNT_REQUIRED, errorDescription);
		}
		
		String emailRegex = "^(.+)@(.+)$";
		
		if (StringUtils.isEmpty(userDto.getAppAccountDto().getEmail()) ||
				!userDto.getAppAccountDto().getEmail().matches(emailRegex)) {
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
		try {
			appAccountToBeSaved.setPassword(new String(Base64.encodeBase64(userDto.getAppAccountDto().getPassword().getBytes()), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
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
		if (user == null) {
			return null;
		}
		return userRepository.save(user) ;
	}
	
	@Override
	public ResponseEntity<Response> logUser(String body) {
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode;
		String errorDescription = "";
		 AppAccount appAccount = null;
		try {
			jsonNode = mapper.readTree(body);
			
			JsonNode emailNode = jsonNode.get("email");
			if (emailNode == null) {
				errorDescription = "Authentication failed, your email is required";
				return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, errorDescription);
	        }
			
			String email = emailNode.asText();
			
			JsonNode passwordNode = jsonNode.get("password");
			if (passwordNode == null) {
				errorDescription = "Authentication failed, your password is required";
				return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_PASSWORD_REQUIRED, errorDescription);
	        }
			String password = passwordNode.asText();
			
			if (StringUtils.isEmpty(email)) {
				errorDescription = "email is required !";
				return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, errorDescription);
			}
			
			if (StringUtils.isEmpty(password)) {
				errorDescription = "password is required !";
				return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_PASSWORD_REQUIRED, errorDescription);
			}
		
         appAccount = appAccountRepository.findByEmail(email);
        
        if (appAccount == null) {
        	errorDescription = "Authentication : false !";
        	return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_USER_NOT_FOUND, errorDescription);
        }
 
        appAccount.setAuthenticated(appAccount.getPassword().equals(new String(Base64.encodeBase64(password.getBytes()), "UTF-8")));
        utility.createResponseWithSuccess(response, appAccount.isAuthenticated());
       
		return new ResponseEntity<>(response, HttpStatus.OK);
        
		} catch (Exception e) {
		return utility.createResponseWithErrors(Constant.INTERNAL_ERROR, e.getMessage());
		}
		
	}

	@Override
	public AppAccount findByEmail(String email) {
		return appAccountRepository.findByEmail(email);
		
	}

	@Override
	public User getUserFromAppAccount(String email) {
		if (findByEmail(email) == null) {
			return null;
		}
		return findByEmail(email).getUser();
	}

	@Override
	public ResponseEntity<Response> addFriend(String body) {
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode;
		String errorDescription = "";
		try {
			jsonNode = mapper.readTree(body);
			
			JsonNode userEmailNode = jsonNode.get("userEmail");
			if (userEmailNode == null) {
				errorDescription = "userEmail is required !";
				return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, errorDescription);
			}
			
			String userEmail = userEmailNode.asText();
			
			if (StringUtils.isEmpty(userEmail)) {
				errorDescription = "A valid user email is required";
				return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, errorDescription);
			}
			
			JsonNode friendEmailNode = jsonNode.get("friendEmail");
			if (friendEmailNode == null) {
				errorDescription = "friendEmail is required !";
				return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, errorDescription);
			}
			
			String friendEmail = friendEmailNode.asText();
			
			if (StringUtils.isEmpty(friendEmail)) {
				errorDescription = "A valid friend email is required";
				return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, errorDescription);
			}
			
			User userFriend = getUserFromAppAccount(friendEmail);
			User user = getUserFromAppAccount(userEmail);
			
			if (userFriend == null) {
				errorDescription = "A user friend with this email :" + friendEmail +  " is not found.";
				return utility.createResponseWithErrors(Constant.ERROR_NO_USER_FOUND, errorDescription);
			}
			
			if (user == null) {
				errorDescription = "A user with this email :" + userEmail +  " is not found.";
				return utility.createResponseWithErrors(Constant.ERROR_NO_USER_FOUND, errorDescription);
			}
			
			if (checkFriendExistence(userFriend, user)) {
				errorDescription = "The current user (" + 
						user.getFirstName() + " " + 
						user.getLastName().toUpperCase() + 
						") is already friend with user (" + 
						userFriend.getFirstName() + " " + 
						userFriend.getLastName().toUpperCase() + ").";
				
				return utility.createResponseWithErrors(Constant.ERROR_MESSAGE_FRIEND_ALREADY_EXITS, errorDescription);
			}
			
			Friend friend = new Friend();
			friend.setUser(user);
			FriendRelationship friendRelationship = new FriendRelationship();
			friendRelationship.setUserId(user.getUserId());
			friendRelationship.setFriendId(userFriend.getUserId());
			
			friend.setFriendRelationship(friendRelationship);
			
			Friend friendSaved = friendRepository.save(friend);
			
			FriendDto friendDto = new FriendDto();
			friendDto.setFriendEmail(friendEmail);
			friendDto.setUserEmail(userEmail);
			friendDto.setFriendId(friendSaved.getFriendRelationship().getFriendId());
			friendDto.setUserId(friendSaved.getUser().getUserId());
			
			utility.createResponseWithSuccess(response, friendDto);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			return utility.createResponseWithErrors(Constant.INTERNAL_ERROR, e.getMessage());
		}
	}
	
	public boolean checkFriendExistence(User userFriend, User user) {
		
		return user.getFriends()
				.stream()
				.anyMatch(friend -> friend.getFriendRelationship()
						.getFriendId() == userFriend.getUserId());
	}

	
	@Override
	public ResponseEntity<Response> getUserFriends(String email) {
		
		AppAccount userAccount = findByEmail(email);
		
		String errorType = "Getting friends";
		String errorMessage = "";
		
		if (userAccount == null) {
			errorMessage = String.format("There is no user registered with this email (%s).", email);
			return utility.createResponseWithErrors(errorType, errorMessage);
		}
		
		User user = userAccount.getUser();
		
		List<FriendRelationship> friendRelationshipList = user.getFriends()
			.stream().map(Friend::getFriendRelationship).collect(Collectors.toList());
		 
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
			AppAccountDto friendAppAccount = new AppAccountDto();
			friendAppAccount.setEmail(friend.get().getAppAccount().getEmail());
			
			friendDto.setFirstName(friend.get().getFirstName());
			friendDto.setLastName(friend.get().getLastName());
			friendDto.setBirthDate(friend.get().getBirthDate());
			friendDto.setAddress(friend.get().getAddress());
			friendDto.setCountry(friend.get().getCountry());
			friendDto.setAppAccountDto(friendAppAccount);
			
			listUserFriend.add(friendDto);
		}
		utility.createResponseWithSuccess(response, listUserFriend);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Response> getUserTransactions(String email, Pageable pageable) {
		AppAccount userAccount = findByEmail(email);
		
		String errorType = "Getting userTransactions";
		String errorMessage = "";
		
		if (userAccount == null) {
			errorMessage = String.format("There is no user registered with this email (%s).", email);
			return utility.createResponseWithErrors(errorType, errorMessage);
		}
		
		User user = userAccount.getUser();
		
		Page<Transaction> userTransactionList = transactionRepository.paginate(email, pageable);
		
		List<TransactionDto> userTransactions = new ArrayList<>();
		
		
		for (Transaction transaction : userTransactionList.getContent()) {
			
			TransactionDto transactionDto = new TransactionDto();
			AppAccount appAccountReceiver = findByEmail(transaction.getEmailReceiver());
			User receiver = appAccountReceiver.getUser();
			
			transactionDto.setFirstName(receiver.getFirstName());
			transactionDto.setEmail(receiver.getAppAccount().getEmail());
			transactionDto.setDescription(transaction.getDescription());
			transactionDto.setAmount(transaction.getAmount());
			
			userTransactions.add(transactionDto);
		}
		utility.createResponseWithSuccess(response, userTransactions);
		HttpHeaders headers = new HttpHeaders();
		headers.add("x-total-count", Long.toString(userTransactionList.getTotalElements()));
		
		return ResponseEntity.ok().headers(headers).body(response);
	}
	
	@Override
	public ResponseEntity<Response> getUser(String email) {
					
			User user = appAccountRepository.findByEmail(email).getUser();
			
			ProfileDto profileDto = new ProfileDto();
			AppAccountDto appAccountDto = new AppAccountDto();
			
			profileDto.setFirstName(user.getFirstName());
			profileDto.setLastName(user.getLastName());
			profileDto.setAddress(user.getAddress());
			profileDto.setBirthDate(user.getBirthDate());
			profileDto.setCountry(user.getCountry());
			
			if (user.getAppAccount() != null)
			{
				appAccountDto.setEmail(user.getAppAccount().getEmail());
				appAccountDto.setPassword(user.getAppAccount().getPassword());
			
			}
			profileDto.setAppAccountDto(appAccountDto);
			profileDto.setAmountBalance(user.getAccountPayMyBuddy().getAmountBalance());
			utility.createResponseWithSuccess(response, profileDto);
			return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
