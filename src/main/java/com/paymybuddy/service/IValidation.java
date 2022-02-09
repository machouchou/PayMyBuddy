package com.paymybuddy.service;

import com.paymybuddy.model.User;

public interface IValidation {
	public boolean checkFriendExistence(User userFriend, User user);
}
