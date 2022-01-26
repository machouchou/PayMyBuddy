package com.paymybuddy.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserDto {
	private String firstName;
	private String lastName;
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate birthDate;
	private String address;
	private String country;
	private AppAccountDto appAccountDto;
	
	public UserDto() {
		super();
	}
	
	public UserDto(String firstName, String lastName, LocalDate birthDate, String address, String country) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.address = address;
		this.country = country;
	}
}
