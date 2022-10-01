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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public AppAccountDto getAppAccountDto() {
		return appAccountDto;
	}

	public void setAppAccountDto(AppAccountDto appAccountDto) {
		this.appAccountDto = appAccountDto;
	}
	
	
}
