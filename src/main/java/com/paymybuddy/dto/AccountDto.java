package com.paymybuddy.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
public class AccountDto {
	
	private String email;
	private Double depositMoney;
	private String description;
	@JsonIgnoreProperties
	double userAccountBalance;
	private Double fees;
}
