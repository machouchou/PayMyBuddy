package com.paymybuddy.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="transaction")
@Data
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id")
	private int transactionId;
	
	@Column(name = "email_sender")
	private String emailSender;
	
	@Column(name = "email_receiver")
	private String emailReceiver;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "transaction_date")
	private Date transactionDate;
	
	@Column(name = "amount")
	private double amount;
	
	@Column(name = "fees")
	private double fees;
	
	@Column(name = "fk_transaction_user")
	private int fkTransactionUser;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "userId")
	private User user;

	public Transaction() {
		super();
	}
}
