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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Double getDepositMoney() {
		return depositMoney;
	}
	public void setDepositMoney(Double depositMoney) {
		this.depositMoney = depositMoney;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getUserAccountBalance() {
		return userAccountBalance;
	}
	public void setUserAccountBalance(double userAccountBalance) {
		this.userAccountBalance = userAccountBalance;
	}
	public Double getFees() {
		return fees;
	}
	public void setFees(Double fees) {
		this.fees = fees;
	}
	
	
}
