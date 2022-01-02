package com.paymybuddy.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UserDto {
	private String firstName;
	private String lastName;
	private Date birthDate;
	private String address;
	private String country;
	private AppAccountDto appAccountDto;
	
	public UserDto() {
		super();
	}
	
	public UserDto(String firstName, String lastName, Date birthDate, String address, String country) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.address = address;
		this.country = country;
	}
}
