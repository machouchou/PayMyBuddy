package com.payMyBuddy.serviceTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;
import org.junit.rules.ExpectedException;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.paymybuddy.dto.AppAccountDto;
import com.paymybuddy.dto.FriendDto;
import com.paymybuddy.dto.UserDto;
import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.Friend;
import com.paymybuddy.model.FriendRelationship;
import com.paymybuddy.model.Response;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.repository.FriendsRepository;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.IPayMyBuddyService;
import com.paymybuddy.service.IUserService;
import com.paymybuddy.utility.Constant;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@EnableRuleMigrationSupport
public class UserServiceTest {
	
	@Autowired
	IUserService userService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	IPayMyBuddyService payService;
	
	@Autowired
	private FriendsRepository friendRepository;
	
	@Autowired
	AppAccountRepository appAccountRepository;
	
	@Rule
	public ExpectedException exceptionThrown = ExpectedException.none();
	
		List<UserDto> lUser;
	
	@Test
	public void findAllUsers() throws Exception {
		// Arrange
		String firstName = "John";
		
		// Act
		List<UserDto> lUser = userService.findAllUsers();
		
		Assertions.assertNotEquals(Collections.EMPTY_LIST, lUser.size());
		
		assertTrue(lUser.stream().anyMatch(user ->firstName.equalsIgnoreCase(user.getFirstName())));
	}
	
	@Test
	public void save_userDtoWithUnacceptableDateOfBirth_ThrowsParseException() {
	// Arrange
		AppAccountDto appAccountDto = new AppAccountDto();
		appAccountDto.setEmail("mb@gmail.com");
		appAccountDto.setPassword("mbmb");
		String dateString = "toto";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		UserDto userDto = new UserDto();		userDto.setFirstName("Marie");
		userDto.setLastName("Baudrous");
//		userDto.setAddress("5 Avenue du Général De Gaulle 78600 Maison Laffitte ");
//		userDto.setCountry("France");
//		userDto.setAppAccountDto(appAccountDto);
		
		Assertions.assertThrows(ParseException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				userDto.setBirthDate(formatter.parse(dateString));
			}
		});
	}
	
	@Test
	public void save_newValidUserDto_UserExistsInLUserFindAllUsers() throws ParseException {
	// Arrange
		AppAccountDto appAccountDto = new AppAccountDto();
		appAccountDto.setEmail("mb@gmail.com");
		appAccountDto.setPassword("mbmb");
		String dateString = "05/11/1973";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		UserDto userDto = new UserDto();
		userDto.setFirstName("Marie");
		userDto.setLastName("Baudrous");
		userDto.setBirthDate(formatter.parse(dateString));
		userDto.setAddress("5 Avenue du Général De Gaulle 78600 Maison Laffitte ");
		userDto.setCountry("France");
		userDto.setAppAccountDto(appAccountDto);

		User user = userService.getUserFromAppAccount("mb@gmail.com");
		userRepository.delete(user);
		
		// Act
		ResponseEntity<Response> saveResult = userService.save(userDto);
		List<UserDto> lUser = userService.findAllUsers();
		
		// Assert
		Assertions.assertNotEquals(Collections.EMPTY_LIST, lUser.size());
		Assertions.assertEquals(HttpStatus.OK.value(), saveResult.getStatusCodeValue());
		assertNotNull(saveResult.getBody().getData());
		assertNull(saveResult.getBody().getErrorCode());
		assertNull(saveResult.getBody().getErrorDescription());
		assertTrue(lUser.stream().anyMatch(result -> userDto.getFirstName().equals(result.getFirstName())
				&& userDto.getLastName().equals(result.getLastName()) 
				&& userDto.getBirthDate().equals(result.getBirthDate())
				&& userDto.getAddress().equals(result.getAddress())
				&& userDto.getCountry().equals(result.getCountry())
				&& userDto.getAppAccountDto().getEmail().equals(result.getAppAccountDto().getEmail())
				&& userDto.getAppAccountDto().getPassword().equals(result.getAppAccountDto().getPassword())));
	}
	
	@Test
	public void update_validUser_userUpdated() throws Exception {
		// Arrange
		User user = userService.getUserFromAppAccount("jb@gmail.com");
		user.setFirstName("James");
		user.setAddress("8 rue Marcadet 75018 Paris");
		// Act
		User userSaved = userService.save(user);
		// Assert
		assertEquals("James", userSaved.getFirstName());
		assertEquals("8 rue Marcadet 75018 Paris", userSaved.getAddress());
	}
	
	/*@Test
	public void save_nullUser_userNotSaved() throws Exception {
		// Arrange
		User user =userService.getUserFromAppAccount("toto");
		//user.setFirstName("");
		//user.setAddress("");
		// Act
		User userSaved = userService.save(user);
		// Assert
		Assertions.assertEquals(null, userSaved);
		//assertEquals(null, userSaved.getAddress());
	}*/
	
	@Test
	public void logUser_emailNodeNull_returnsLoginFailed() throws Exception {
		
		// Arrange
		String body = "{\"password\":\"alal\"}";
		
		// Act
		ResponseEntity<Response> result = userService.logUser(body);
		
		// Assert
		assertNull(result.getBody().getData());
		assertNotNull(result.getBody().getErrorCode());
		assertEquals(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, result.getBody().getErrorCode());
		assertEquals("Authentication failed, your email is required", result.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
	}
	
	@Test
	public void logUser_passwordNodeNull_returnsLoginFailed() throws Exception {
		
		// Arrange
		String body = "{\"email\":\"at@live.fr\"}";
		
		// Act
		ResponseEntity<Response> result = userService.logUser(body);
		
		// Assert
		assertNull(result.getBody().getData());
		assertNotNull(result.getBody().getErrorCode());
		assertEquals(Constant.ERROR_MESSAGE_PASSWORD_REQUIRED, result.getBody().getErrorCode());
		assertEquals("Authentication failed, your password is required", result.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
	}
	
	@Test
	public void logUser_appAccountNull_returnsLoginFailed() throws Exception {
		
		// Arrange
		String body = "{\"email\":\"tata\", \"password\":\"toto\"}";
		
		// Act
		ResponseEntity<Response> result = userService.logUser(body);
		
		// Assert
		assertNull(result.getBody().getData());
		assertNotNull(result.getBody().getErrorCode());
		assertEquals(Constant.ERROR_MESSAGE_USER_NOT_FOUND, result.getBody().getErrorCode());
		assertEquals("Authentication : false !", result.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
	}
	@Test
	public void logUser_emailNodeEmpty_returnsLoginFailed() throws Exception {
		
		// Arrange
		String body = "{\"email\":\"\", \"password\":\"alal\"}";
		
		// Act
		ResponseEntity<Response> result = userService.logUser(body);
		
		// Assert
		assertNull(result.getBody().getData());
		assertNotNull(result.getBody().getErrorCode());
		assertEquals(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, result.getBody().getErrorCode());
		assertEquals("email is required !", result.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
	}
	
	@Test
	public void logUser_passwordNodeEmpty_returnsLoginFailed() throws Exception {
		
		// Arrange
		String body = "{\"email\":\"at@live.fr\", \"password\":\"\"}";
		
		// Act
		ResponseEntity<Response> result = userService.logUser(body);
		
		// Assert
		assertNull(result.getBody().getData());
		assertNotNull(result.getBody().getErrorCode());
		assertEquals(Constant.ERROR_MESSAGE_PASSWORD_REQUIRED, result.getBody().getErrorCode());
		assertEquals("password is required !", result.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
	}
	
	@Test
	public void logUser_validAppAccount_returnsLoginSuccessful() throws Exception {
		
		// Arrange
		String body = "{\"email\":\"rg@gmail.com\", \"password\":\"rgrg\"}";
		
		// Act
		ResponseEntity<Response> result = userService.logUser(body);
		
		// Assert
		assertNotNull(result.getBody().getData());
		assertNull(result.getBody().getErrorCode());
		assertNull(result.getBody().getErrorDescription());
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}
	
	@Test
	public void addFriend_userEmailNodeNull_returnsAddingFriendImpossible() throws Exception {
		
		// Arrange
		String body = "{\"friendEmail\":\"mama@gmail.com\"}";
		
		// Act
		ResponseEntity<Response> result = userService.addFriend(body);
		
		// Assert
		assertNull(result.getBody().getData());
		assertNotNull(result.getBody().getErrorCode());
		assertEquals(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, result.getBody().getErrorCode());
		assertEquals("userEmail is required !", result.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
	}
	
	@Test
	public void addFriend_friendEmailNodeNull_returnsAddingFriendImpossible() throws Exception {
		
		// Arrange
		String body = "{\"userEmail\":\"sj@free.fr\"}";
		
		// Act
		ResponseEntity<Response> result = userService.addFriend(body);
		
		// Assert
		assertNull(result.getBody().getData());
		assertNotNull(result.getBody().getErrorCode());
		assertEquals(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, result.getBody().getErrorCode());
		assertEquals("friendEmail is required !", result.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
	}
	
	@Test
	public void addFriend_userEmailNodeEmpty_returnsAddingFriendImpossible() throws Exception {
		
		// Arrange
		String body = "{\"userEmail\":\"\", \"friendEmail\":\"mama@gmail.com\"}";
		
		// Act
		ResponseEntity<Response> result = userService.addFriend(body);
		
		// Assert
		assertNull(result.getBody().getData());
		assertNotNull(result.getBody().getErrorCode());
		assertEquals(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, result.getBody().getErrorCode());
		assertEquals("A valid user email is required", result.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
	}
	
	@Test
	public void addFriend_friendEmailNodeEmpty_returnsAddingFriendImpossible() throws Exception {
		
		// Arrange
		String body = "{\"userEmail\":\"tc@gmail.com\", \"friendEmail\":\"\"}";
		
		// Act
		ResponseEntity<Response> result = userService.addFriend(body);
		
		// Assert
		assertNull(result.getBody().getData());
		assertNotNull(result.getBody().getErrorCode());
		assertEquals(Constant.ERROR_MESSAGE_EMAIL_REQUIRED, result.getBody().getErrorCode());
		assertEquals("A valid friend email is required", result.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
	}
	
	@Test
	public void addFriend_userNull_returnsAddingFriendImpossible() throws Exception {
		
		// Arrange
		String body = "{\"userEmail\":\"alex\", \"friendEmail\":\"mama@gmail.com\"}";
		
		// Act
		ResponseEntity<Response> result = userService.addFriend(body);
		
		// Assert
		assertNull(result.getBody().getData());
		assertNotNull(result.getBody().getErrorCode());
		assertEquals(Constant.ERROR_NO_USER_FOUND, result.getBody().getErrorCode());
		assertEquals("A user with this email :alex is not found.", result.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
	}
	
	@Test
	public void addFriend_userFriendNull_returnsAddingFriendImpossible() throws Exception {
		
		// Arrange
		String body = "{\"userEmail\":\"hc@gmail.com\", \"friendEmail\":\"mama\"}";
		
		// Act
		ResponseEntity<Response> result = userService.addFriend(body);
		
		// Assert
		assertNull(result.getBody().getData());
		assertNotNull(result.getBody().getErrorCode());
		assertEquals(Constant.ERROR_NO_USER_FOUND, result.getBody().getErrorCode());
		assertEquals("A user friend with this email :mama is not found.", result.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
	}
	
	@Test
	public void getUserFriends_ValidEmail_returnsUserFriends() throws Exception {
		
		// Arrange
		String email = "rg@gmail.com"; 
		AppAccount userAppAccount = appAccountRepository.findByEmail(email);
		User user = userAppAccount.getUser();
		List<FriendRelationship> friendRelationshipList = user.getFriends()
				.stream().map(Friend::getFriendRelationship).collect(Collectors.toList());
		
		List<Integer> friendIds = friendRelationshipList.stream()
				.map(FriendRelationship::getFriendId).collect(Collectors.toList());
		
		List<UserDto> listUserFriend = new ArrayList<>();
		
		for (Integer id : friendIds) {
			Optional<User> friend = userRepository.findById(id);
			
			UserDto friendDto = new UserDto();
			friendDto.setFirstName(friend.get().getFirstName());
			friendDto.setLastName(friend.get().getLastName());
			friendDto.setBirthDate(friend.get().getBirthDate());
			friendDto.setAddress(friend.get().getAddress());
			friendDto.setCountry(friend.get().getCountry());
			listUserFriend.add(friendDto);
		// Act
		ResponseEntity <Response> result = userService.getUserFriends(email);
		
		// Assert
		assertNotNull(result.getBody().getData());
		assertNull(result.getBody().getErrorCode());
		assertNull(result.getBody().getErrorDescription());
		assertEquals(HttpStatus.OK, result.getStatusCode());
		}
	}
	
	@Test
	public void getUserFriends_userAppAccountNull_returnsGettingUserFriendImpossible() throws Exception {
		
		// Arrange
		String email = "toto"; 
		//AppAccount userAppAccount = appAccountRepository.findByEmail(email);
		
		// Act
		ResponseEntity <Response> result = userService.getUserFriends(email);
		
		// Assert
		assertNull(result.getBody().getData());
		assertNotNull(result.getBody().getErrorCode());
		assertEquals("Getting friends", result.getBody().getErrorCode());
		assertEquals("There is no user registered with this email (toto).", result.getBody().getErrorDescription());
		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
	}
}
