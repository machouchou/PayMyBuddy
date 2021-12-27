package com.paymybuddy.dto;

import lombok.Data;

@Data
public class AppAccountDto {
	private String email;
	private String password;
	private boolean isAuthenticated;
}
