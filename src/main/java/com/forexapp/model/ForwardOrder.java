package com.forexapp.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class ForwardOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
	@SequenceGenerator(name = "user_generator", sequenceName = "user_seq_generator", allocationSize = 1)
	private long forwardOrderId;
	
	@ManyToOne
	@JoinColumn(name = "FK_initiator_user")
	private User initiatorUser;

	@ManyToOne
	@JoinColumn(name = "FK_acceptor_user")
	private User acceptorUser;

	@ManyToOne
	@JoinColumn(name = "FK_from_currency_id")
	private Currency fromCurrency;
	
	@ManyToOne
	@JoinColumn(name = "FK_to_currency_id")
	private Currency toCurrency;	

	// from currency: sgd
	// to currency: usd
	// he is using x sgd (per unit) to buy y units of usd
	// x sgd - fromcurrencyprice
	// y usd - tocurrencyquantity
	private double fromCurrencyPrice; //2 sgd
	
	private double toCurrencyQuantity;
	
	private Date transactionDate;

	public ForwardOrder() {
	}

	public long getForwardOrderId() {
		return forwardOrderId;
	}

	public void setForwardOrderId(long forwardOrderId) {
		this.forwardOrderId = forwardOrderId;
	}

	public User getInitiatorUser() {
		return initiatorUser;
	}

	public void setInitiatorUser(User initiatorUser) {
		this.initiatorUser = initiatorUser;
	}

	public User getAcceptorUser() {
		return acceptorUser;
	}

	public void setAcceptorUser(User acceptorUser) {
		this.acceptorUser = acceptorUser;
	}

	public Currency getFromCurrency() {
		return fromCurrency;
	}

	public void setFromCurrency(Currency fromCurrency) {
		this.fromCurrency = fromCurrency;
	}

	public Currency getToCurrency() {
		return toCurrency;
	}

	public void setToCurrency(Currency toCurrency) {
		this.toCurrency = toCurrency;
	}

	public double getFromCurrencyPrice() {
		return fromCurrencyPrice;
	}

	public void setFromCurrencyPrice(double fromCurrencyPrice) {
		this.fromCurrencyPrice = fromCurrencyPrice;
	}
	
	public double getToCurrencyQuantity() {
		return toCurrencyQuantity;
	}

	public void setToCurrencyQuantity(double toCurrencyQuantity) {
		this.toCurrencyQuantity = toCurrencyQuantity;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	

}
