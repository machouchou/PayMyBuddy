package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
}
