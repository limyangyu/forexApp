package com.forexapp.model;

import java.sql.Date;
import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class LimitOrder implements Order{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
	@SequenceGenerator(name = "user_generator", sequenceName = "user_seq_generator", allocationSize = 1)
	private long limitOrderId;
	
	@ManyToOne
	@JoinColumn(name = "FK_user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "FK_from_currency_id")
	private Currency fromCurrency;
	
	@ManyToOne
	@JoinColumn(name = "FK_to_currency_id")
	private Currency toCurrency;
	
	private double originalFromQuantity;
	
	private double originalToQuantity;
	
	private double fromPriceLimit;
	
	private double toCurrencyQuantity;

	private Date expirationDate;
	
	private double fromCurrencyHold; // amount to freeze 
	
	private Timestamp initializationDate;

	public LimitOrder() {
	}

	public long getLimitOrderId() {
		return limitOrderId;
	}

	public void setLimitOrderId(long limitOrderId) {
		this.limitOrderId = limitOrderId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public double getOriginalFromQuantity() {
		return originalFromQuantity;
	}

	public void setOriginalFromQuantity(double originalFromQuantity) {
		this.originalFromQuantity = originalFromQuantity;
	}

	public double getOriginalToQuantity() {
		return originalToQuantity;
	}

	public void setOriginalToQuantity(double originalToQuantity) {
		this.originalToQuantity = originalToQuantity;
	}

	public double getFromPriceLimit() {
		return fromPriceLimit;
	}

	public void setFromPriceLimit(double fromPriceLimit) {
		this.fromPriceLimit = fromPriceLimit;
	}

	public double getToCurrencyQuantity() {
		return toCurrencyQuantity;
	}

	public void setToCurrencyQuantity(double toCurrencyQuantity) {
		this.toCurrencyQuantity = toCurrencyQuantity;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public double getFromCurrencyHold() {
		return fromCurrencyHold;
	}

	public void setFromCurrencyHold(double fromCurrencyHold) {
		this.fromCurrencyHold = fromCurrencyHold;
	}

	public Timestamp getInitializationDate() {
		return initializationDate;
	}

	public void setInitializationDate(Timestamp initializationDate) {
		this.initializationDate = initializationDate;
	}

}
