package com.forexapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Holding {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
	@SequenceGenerator(name = "user_generator", sequenceName = "user_seq_generator", allocationSize = 1)
	private long holdingId; 

	@ManyToOne
	@JoinColumn(name = "FK_currency_id")
	private Currency currency;
	
	private double amount;
	
	@ManyToOne
	@JoinColumn(name = "FK_user_id")
	private User user;

	public Holding() {
	}
	
	public Holding(Currency currency, User user, double amount) {
		this.currency = currency;
		this.user = user;
		this.amount = amount;
	}

	public long getHoldingId() {
		return holdingId;
	}

	public void setHoldingId(long holdingId) {
		this.holdingId = holdingId;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
