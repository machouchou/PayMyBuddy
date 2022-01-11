package com.paymybuddy.model;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="friend")
@Data
public class Friend {
	
	
	@EmbeddedId
	FriendRelationship friendRelationship;

	@ManyToOne(
			cascade = CascadeType.ALL
			)
	@JoinColumn(name = "user_id", insertable=false, updatable=false)
	private User user;
	
	
	public Friend() {
		super();
	}

	@Override
	public String toString() {
		return "Friend [friendId=" + friendRelationship + "]";
	}
}
