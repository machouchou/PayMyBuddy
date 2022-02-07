package com.paymybuddy.dto;

import lombok.Data;

@Data
public class TransactionDto {
	
	private String firstName;
	private String email;
	private String description;
	private Double amount;
	
}
