package com.paymybuddy.dto;

import com.paymybuddy.model.AppAccount;
import com.paymybuddy.model.User;

import lombok.Data;

@Data
public class UserRegistrationDto {
	
	private User user;
	private AppAccount appAccount;
	
	public UserRegistrationDto() {
		super();
	}
	
	public UserRegistrationDto(User user, AppAccount appAccount) {
		super();
		this.user = user;
		this.appAccount = appAccount;
	}
}
