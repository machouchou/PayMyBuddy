package com.paymybuddy.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name="user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	@NotNull
	private int userId;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "birth_date")
	private LocalDate birthDate;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "country")
	private String country;
	
	@OneToMany(
		cascade = CascadeType.ALL,
		orphanRemoval = true,
		fetch = FetchType.EAGER
	)
	@JoinColumn(name = "user_id")
	List<Friend> friends = new ArrayList<>();
	
	
	@OneToMany(cascade = CascadeType.ALL, 
			fetch = FetchType.LAZY,
			mappedBy = "user")
	private List<Transaction> transactions = new ArrayList<>();
	
	@OneToOne(cascade = CascadeType.ALL, 
		fetch = FetchType.LAZY,
		mappedBy =  "user"
	)
	private AppAccount appAccount;

	@OneToOne(mappedBy =  "user", fetch = FetchType.LAZY,
			cascade = CascadeType.PERSIST)
	private Account accountPayMyBuddy;

	public User() {
		super();
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", birthDate=" + birthDate + ", address="
				+ address + ", country=" + country + "]";
	}
}
