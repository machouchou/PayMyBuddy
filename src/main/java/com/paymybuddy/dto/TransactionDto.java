package com.paymybuddy.dto;

import lombok.Data;

@Data
public class TransactionDto {
	
	private String senderEmail;
	private String receiverEmail;
	private String description;
	private Double transactionAmount;
}
