package com.paymybuddy.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.model.AppAccount;
import com.paymybuddy.repository.AppAccountRepository;

@Service
public class AppAccountServiceImpl implements IAppAccountService {
	
	final Logger logger = LogManager.getLogger(AppAccountServiceImpl.class);

	@Autowired
	private AppAccountRepository appAccountRepository;
	
	@Override
	public AppAccount findByEmail(String email) {
		return appAccountRepository.findByEmail(email);
	}

	@Override
	public boolean save(AppAccount appAccount) {
		logger.debug("save(AppAccount : {})", appAccount);
		
		boolean isDuplicateAppAccount = appAccountRepository.findAll()
				.stream()
				.anyMatch(p -> p.getEmail().equalsIgnoreCase(appAccount.getEmail())
						&& 
						p.getPassword().equalsIgnoreCase(appAccount.getPassword()));
		if (isDuplicateAppAccount) {
			logger.error("This {} already exists, record failed.", appAccount);
		return false;
		}
		
		appAccountRepository.save(appAccount);
		return true;
	}

	@Override
	public List<AppAccount> findAll() {
		return appAccountRepository.findAll();
	}
}
