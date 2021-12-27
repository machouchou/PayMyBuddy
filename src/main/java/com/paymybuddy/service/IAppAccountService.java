package com.paymybuddy.service;

import java.util.List;

import com.paymybuddy.model.AppAccount;

public interface IAppAccountService {
	
	AppAccount findByEmail(String email);
	
	List<AppAccount> findAll();
	
	public boolean save(AppAccount appAccount);
	
	
}
