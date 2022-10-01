package com.payMyBuddy.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.migrationsupport.rules.EnableRuleMigrationSupport;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.paymybuddy.model.AppAccount;
import com.paymybuddy.repository.AppAccountRepository;
import com.paymybuddy.service.IAppAccountService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@EnableRuleMigrationSupport
public class AppAccountServiceTest {
	
	@Autowired
	AppAccountRepository appAccountRepository;
	
	@Autowired
	IAppAccountService appAccountService;

	@Test
	public void findByEmail() throws Exception {
		// Arrange
		String email = "sj@free.fr";
		
		// Act
		AppAccount appAccount = appAccountService.findByEmail(email);
		
		// Assert
		Assertions.assertNotEquals(Collections.EMPTY_LIST, appAccount);
		
		assertEquals("Smith", appAccount.getUser().getFirstName());
	}
	
	@Test
	public void findAll() throws Exception {
		// Arrange
		String email = "jd@hotmail.com";
		// Act
		List<AppAccount> lAppAccount = appAccountService.findAll();
		
		Assertions.assertNotEquals(Collections.EMPTY_LIST, lAppAccount.size());
		
		assertTrue(lAppAccount.stream().anyMatch(appAccount ->email.equalsIgnoreCase(appAccount.getEmail())));
	}
}
