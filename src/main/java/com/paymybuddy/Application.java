package com.paymybuddy;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.paymybuddy.service.IUserService;
import com.paymybuddy.service.UserServiceImpl;

@SpringBootApplication 
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		
		// Connexion test of SpringData JPA
		IUserService userService = context.getBean(UserServiceImpl.class);
		System.out.println("User list : " + userService.findAllUsers());
	}
}
