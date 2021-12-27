package com.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.AppAccount;

@Repository
public interface AppAccountRepository extends JpaRepository<AppAccount, String> {
	
	AppAccount findByEmail(String email);
	
}
