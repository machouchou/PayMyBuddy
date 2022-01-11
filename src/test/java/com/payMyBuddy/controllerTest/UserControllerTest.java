package com.payMyBuddy.controllerTest;

import java.text.SimpleDateFormat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.paymybuddy.dto.AppAccountDto;
import com.paymybuddy.dto.UserDto;
import com.paymybuddy.model.User;
import com.paymybuddy.repository.UserRepository;
import com.paymybuddy.service.IUserService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)

public class UserControllerTest {
	
	@Autowired
	MockMvc mvc;
	
	@Autowired
	private WebApplicationContext webContext;
	
	@MockBean
	IUserService userService;
	
	
	@BeforeEach
	public void config() {
		mvc = MockMvcBuilders.webAppContextSetup(webContext).build();
	}
	
	@Test
	@Tag("RetrieveUserList")
	void getUserDtoList() throws Exception {
		// Arrange
		String dateString = "01/29/2007";
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		
		AppAccountDto appAccount = new AppAccountDto();
		appAccount.setEmail("ad@gmail.com");
		appAccount.setPassword("adad");
		
		List<UserDto> lUser = new ArrayList<>();
		UserDto userDto = new UserDto();
		userDto.setFirstName("Aude");
		userDto.setLastName("Dupont");
		//userDto.setBirthDate(formatter.parse(dateString));
		userDto.setAddress("21 Rue Fontaine 75009 Paris");
		userDto.setCountry("France");
		userDto.setAppAccountDto(appAccount);
		lUser.add(userDto);
		
		// Act & Assert
		try {
			this.mvc.perform(MockMvcRequestBuilders
		
				.get("/users")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
				/*.andExpect(content().string("[{\"firstName\":\"Aude\",\"lastName\":\"Dupont\",\"address\":\"21 Rue Fontaine 75009 Paris\",\"Country\":\"France\","
						+ "}]"));*/
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	@Tag("saveUser")
	void save_ValidUserDto_SavingSucceded() throws Exception {
		try {
			// Act
			this.mvc.perform(MockMvcRequestBuilders
					.post("/user")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"firstName\":\"James\", \"lastName\":\"Boyd\", \"birthDate\":\"01/29/2007\", \"address\":\"21 Rue Fontaine 75009 Paris\", \"Country\":\"France\"}"));
					//.andExpect(status().isOk());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
		
		@Test
		@Tag("userLogin")
		void authenticateUser_ValidAppAccountDto_loginSucceded() throws Exception {
			try {
				// Act
				this.mvc.perform(MockMvcRequestBuilders
						.post("/userLogin")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"email\":\"sj@free.fr\", \"password\":\"5sjsj\"}"))
						.andExpect(status().isOk());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
	}
	
		@Test
		@Tag("addFriend")
		void addFriend_ValidFriendDto_addFriendSucceded() throws Exception {
			try {
				// Act
				this.mvc.perform(MockMvcRequestBuilders
						.post("/addFriend")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"userEmail\":\"sj@free.fr\", \"friendEmail\":\"hc@gmail.com\"}"))
						.andExpect(status().isOk());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
	}

		@Test
		@Tag("listFriend")
		void getUserFriends_ValidEmail_listFriendDisplayed() throws Exception {
			try {
				// Act
				this.mvc.perform(MockMvcRequestBuilders
						.get("/listFriend")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"email\":\"sj@free.fr\"}"))
						.andExpect(status().isOk());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
	}
}
