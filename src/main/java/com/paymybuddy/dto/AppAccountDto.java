package com.paymybuddy.dto;

//import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
public class AppAccountDto {
	private String email;
	@JsonIgnoreProperties
	private String password;
	private boolean isAuthenticated;
}
