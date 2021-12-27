package com.paymybuddy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{
	
	Optional<Account> findByAccountNumber(String accountNumber);

}
