package com.paymybuddy.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class FriendRelationship implements Serializable {
	
	private static final long serialVersionUID = 1L;

	
	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "friend_id")
	private int friendId;

	public FriendRelationship() {
		super();
	}

	@Override
	public String toString() {
		return "FriendsId [userId=" + userId + ", friendId=" + friendId + "]";
	}

	
}
