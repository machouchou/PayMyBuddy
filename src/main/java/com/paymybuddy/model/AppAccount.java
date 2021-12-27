package com.paymybuddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;


@Entity
@Table(name="app_account")
@Data
public class AppAccount {
	
	@Id 
	@Column(name = "email", length = 100)
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_fk", nullable = false)
	private User user;
	
	@Transient
	private boolean authenticated;

	public AppAccount() {
		super();
	}

	public AppAccount(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
}