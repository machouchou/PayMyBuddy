package com.paymybuddy.dto;

import lombok.Data;

@Data
public class TransactionDto {
	
	private String emailSender;
	private String emailReceiver;
	private String description;
	private Double transactionAmount;
	
}
