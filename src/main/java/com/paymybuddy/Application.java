package com.paymybuddy;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;

import com.paymybuddy.model.Response;
import com.paymybuddy.service.IPayMyBuddyService;
import com.paymybuddy.service.IUserService;
import com.paymybuddy.service.PayMyBuddyServiceImpl;
import com.paymybuddy.service.UserServiceImpl;

@SpringBootApplication 
public class Application {
	
	private static final Logger LOGGER = LogManager.getLogger("Application");

	public static void main(String[] args) {
		LOGGER.info("Initializing PayMyBuddy");
		
		/*ConfigurableApplicationContext context =*/ SpringApplication.run(Application.class, args);
		
		// Connexion test of SpringData JPA
		/*IUserService userService = context.getBean(UserServiceImpl.class);
		IPayMyBuddyService payService = context.getBean(PayMyBuddyServiceImpl.class);
		System.out.println("User list : " + userService.findAllUsers());
		
		var friends = userService.getUserFriends("tc@gmail.com");
		
		ResponseEntity<Response> moneyTransfer = null;*/
		try {
			// moneyTransfer = payService.transferMoneyToBuddy(null, "at@live.fr", "Premier transfert", 280.0);
			// moneyTransfer = payService.transferMoneyToBuddy("jb@gmail.com", null, "Premier transfert", 280.0);
			// moneyTransfer = payService.transferMoneyToBuddy("jb@gmail.com", "at@live.fr", "Premier transfert", -20.0);
			
			// moneyTransfer = payService.transferMoneyToBuddy("at@live.fr", "jb@gmail.com", "Premier transfert", 30.0);
			
			// moneyTransfer = payService.transferMoneyToBuddy("{\"senderEmail\":\"at@live.fr\", \"receiverEmail\":\"jb@gmail.com\", \"description\":\"Premier transfert\", \"transactionAmount\":30.0}");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
