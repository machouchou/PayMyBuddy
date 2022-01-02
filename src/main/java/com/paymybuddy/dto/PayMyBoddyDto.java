package com.paymybuddy.dto;

import lombok.Data;

@Data
public class PayMyBoddyDto {
	double senderAccountBalance;
	double receiverAccountBalance;
	private Double fees;
}
