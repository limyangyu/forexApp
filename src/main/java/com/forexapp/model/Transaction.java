package com.forexapp.model;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
	@SequenceGenerator(name = "user_generator", sequenceName = "user_seq_generator", allocationSize = 1)
	private long transactionId;	
	
	@ManyToOne
	@JoinColumn(name = "FK_first_user_id")
	private User firstUser;

	@ManyToOne
	@JoinColumn(name = "FK_second_user_id") 
	private User secondUser;

	@ManyToOne
	@JoinColumn(name = "FK_first_user_from_currency_id")
	private Currency firstUserFromCurrency;

	@ManyToOne
	@JoinColumn(name = "FK_first_user_to_currency_id")
	private Currency firstUserToCurrency;
	
	private double firstUserFromQuantity;

	private double firstUserToQuantity;
	
	private Timestamp transactionDate;

	public Transaction() {
	}

	public Transaction(User firstUser, User secondUser, Currency firstUserFromCurrency, Currency firstUserToCurrency,
			double firstUserFromQuantity, double firstUserToQuantity, Timestamp transactionDate) {
		this.firstUser = firstUser;
		this.secondUser = secondUser;
		this.firstUserFromCurrency = firstUserFromCurrency;
		this.firstUserToCurrency = firstUserToCurrency;
		this.firstUserFromQuantity = firstUserFromQuantity;
		this.firstUserToQuantity = firstUserToQuantity;
		this.transactionDate = transactionDate;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public User getFirstUser() {
		return firstUser;
	}

	public void setFirstUser(User firstUser) {
		this.firstUser = firstUser;
	}

	public User getSecondUser() {
		return secondUser;
	}

	public void setSecondUser(User secondUser) {
		this.secondUser = secondUser;
	}

	public Currency getFirstUserFromCurrency() {
		return firstUserFromCurrency;
	}

	public void setFirstUserFromCurrency(Currency firstUserFromCurrency) {
		this.firstUserFromCurrency = firstUserFromCurrency;
	}

	public Currency getFirstUserToCurrency() {
		return firstUserToCurrency;
	}

	public void setFirstUserToCurrency(Currency firstUserToCurrency) {
		this.firstUserToCurrency = firstUserToCurrency;
	}

	public double getFirstUserFromQuantity() {
		return firstUserFromQuantity;
	}

	public void setFirstUserFromQuantity(double firstUserFromQuantity) {
		this.firstUserFromQuantity = firstUserFromQuantity;
	}

	public double getFirstUserToQuantity() {
		return firstUserToQuantity;
	}

	public void setFirstUserToQuantity(double firstUserToQuantity) {
		this.firstUserToQuantity = firstUserToQuantity;
	}

	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

}
