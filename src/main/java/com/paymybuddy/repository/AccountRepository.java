package com.paymybuddy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{
	
//	@Query("SELECT a FROM Account a WHERE a.accountNumber = ?1" )
//  @Query("FROM Account a WHERE a.accountNumber = ?1")
	@Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNum" )
	Optional<Account> findByAccountNumber(@Param("accountNum") String accountNumber);
	
	

}
