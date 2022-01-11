package com.paymybuddy.dto;

import lombok.Data;

@Data
public class FriendDto {
	
	private String userEmail;
	private String friendEmail;
	private int userId;
	private int friendId;
}
