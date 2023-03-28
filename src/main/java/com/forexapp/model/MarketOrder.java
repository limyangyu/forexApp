package com.forexapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class MarketOrder implements Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
	@SequenceGenerator(name = "user_generator", sequenceName = "user_seq_generator", allocationSize = 1)
	private long marketOrderId;
	
	@ManyToOne
	@JoinColumn(name = "FK_user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "FK_from_currency_id")
	private Currency fromCurrency;
	
	@ManyToOne
	@JoinColumn(name = "FK_to_currency_id")
	private Currency toCurrency;
	
	private double fromQuantity;

	public MarketOrder() {
	}

	public long getMarketOrderId() {
		return marketOrderId;
	}

	public void setMarketOrderId(long marketOrderId) {
		this.marketOrderId = marketOrderId;
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

	public double getFromQuantity() {
		return fromQuantity;
	}

	public void setFromQuantity(double fromQuantity) {
		this.fromQuantity = fromQuantity;
	}

}
