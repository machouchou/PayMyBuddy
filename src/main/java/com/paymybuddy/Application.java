package com.paymybuddy;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@EnableEncryptableProperties
@SpringBootApplication 
public class Application {
	
	private static final Logger LOGGER = LogManager.getLogger("Application");

	public static void main(String[] args) {
		LOGGER.info("Initializing PayMyBuddy");
		
		SpringApplication.run(Application.class, args);
		
	}
}
