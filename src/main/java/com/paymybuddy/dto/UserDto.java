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
}
