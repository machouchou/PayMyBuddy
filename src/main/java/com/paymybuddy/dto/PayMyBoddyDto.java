package com.paymybuddy.dto;

import lombok.Data;

@Data
public class PayMyBoddyDto {
	double senderAccountBalance;
	double receiverAccountBalance;
	private Double fees;
	public double getSenderAccountBalance() {
		return senderAccountBalance;
	}
	public void setSenderAccountBalance(double senderAccountBalance) {
		this.senderAccountBalance = senderAccountBalance;
	}
	public double getReceiverAccountBalance() {
		return receiverAccountBalance;
	}
	public void setReceiverAccountBalance(double receiverAccountBalance) {
		this.receiverAccountBalance = receiverAccountBalance;
	}
	public Double getFees() {
		return fees;
	}
	public void setFees(Double fees) {
		this.fees = fees;
	}
	
	
}
