package com.payMyBuddy.controllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class BankControllerTest {

		@Autowired
		MockMvc mvc;
		
		@Autowired
		private WebApplicationContext webContext;
		
		@Test
		public void addMoneyOnPayMyBuddyAccount_WithValidBody_AddingMoneySuccessful() throws Exception {
			// Arrange
			String body = "{\"email\":\"at@live.fr\", \"description\":\"Alimentation Compte Perso\", \"depositMoney\":30.0}";
			try {
				// Act
				this.mvc.perform(MockMvcRequestBuilders
						.post("/depositAmount")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
						.andExpect(status().isOk());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		@Test
		public void transferMoney_WithValidBody_TransferSuccessful() throws Exception {
			// Arrange
			String body = "{\"senderEmail\":\"at@live.fr\",\"receiverEmail\":\"jb@gmail.com\", \"description\":\"Premier transfert\", \"transactionAmount\":30.0}";
			try {
				// Act
				this.mvc.perform(MockMvcRequestBuilders
						.post("/transferMoney")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
						.andExpect(status().isOk());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		@Test
		public void sendMoneyToMyBankAccount_WithValidBody_SendingMoneySuccessful() throws Exception {
			// Arrange
			String body = "{\"email\":\"at@live.fr\", \"description\":\"Alimentation compte en banque\", \"depositMoney\":30.0}";
			try {
				// Act
				this.mvc.perform(MockMvcRequestBuilders
						.post("/sendMoney")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
						.andExpect(status().isOk());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

}
