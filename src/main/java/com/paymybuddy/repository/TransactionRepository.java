package com.paymybuddy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.paymybuddy.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	
	@Query(value="SELECT t FROM Transaction t WHERE t.user.appAccount.email=:email", 
			countQuery="SELECT count(*) FROM Transaction t WHERE t.user.appAccount.email=:email")
	public Page<Transaction>paginate(@Param("email")String email, Pageable pageable);

}
