package com.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.Friend;

@Repository
public interface FriendsRepository extends CrudRepository<Friend, Integer>{

}
