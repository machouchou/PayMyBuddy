package com.paymybuddy;


import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.paymybuddy.model.AppAccount;
import com.paymybuddy.service.AppAccountServiceImpl;
import com.paymybuddy.service.IAppAccountService;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@EnableEncryptableProperties
@SpringBootApplication 
public class Application {
	
	private static final Logger LOGGER = LogManager.getLogger("Application");

	public static void main(String[] args) throws UnsupportedEncodingException {
		LOGGER.info("Initializing PayMyBuddy");
		
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		
		// Connexion test of SpringData JPA
		IAppAccountService appAccountService = context.getBean(AppAccountServiceImpl.class);
		
		List<AppAccount> listAppAccount = appAccountService.findAll();
		
		for (AppAccount appAccount : listAppAccount) {
			
			
			appAccount.setPassword(new String(Base64.encodeBase64(appAccount.getPassword().getBytes()), "UTF-8"));
			
			// appAccountService.save(appAccount);
		}
		
	}
}
