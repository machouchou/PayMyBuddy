package com.paymybuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="account")
@Data
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private int accountId;
	
	@Column(name = "account_number")
	private String accountNumber;
	
	@Column(name = "amount_balance")
	private double amountBalance;
	
	@Column(name = "currency")
	private String currency;
	
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_fk", nullable = false)
	private User user;

	public Account() {
		super();
	}

	
	public Account(String accountNumber) {
		super();
		this.accountNumber = accountNumber;
	}
	

}
