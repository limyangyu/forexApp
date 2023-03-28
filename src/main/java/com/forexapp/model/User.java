package com.forexapp.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

import jakarta.persistence.Entity;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
	@SequenceGenerator(name = "user_generator", sequenceName = "user_seq_generator", allocationSize = 1)
	private long userId; 
	
	@Column(unique = true)
	private String username;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	@OneToMany(mappedBy = "user")
	private List<Holding> holdings;
	
	@OneToMany(mappedBy = "initiatorUser")
	private List<ForwardOrder> forwardOrdersInitiated;
	
	@OneToMany(mappedBy = "acceptorUser")
	private List<ForwardOrder> forwardOrdersAccepted;
	
	@OneToMany(mappedBy = "forwardOrderId")
	private List<ForwardOrderCompleted> forwardOrdersCompleted;
	
	@OneToMany(mappedBy = "user")
	private List<LimitOrder> limitOrderPending;
	
	@OneToMany(mappedBy = "user")
	private List<MarketOrder> marketOrderPending;
	
	public User() {
	}
	
	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public long getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Holding> getHoldings() {
		return holdings;
	}

	public void setHoldings(List<Holding> holdings) {
		this.holdings = holdings;
	}

	public List<ForwardOrder> getForwardOrdersInitiated() {
		return forwardOrdersInitiated;
	}

	public void setForwardOrdersInitiated(List<ForwardOrder> forwardOrdersInitiated) {
		this.forwardOrdersInitiated = forwardOrdersInitiated;
	}

	public List<ForwardOrder> getForwardOrdersAccepted() {
		return forwardOrdersAccepted;
	}

	public void setForwardOrdersAccepted(List<ForwardOrder> forwardOrdersAccepted) {
		this.forwardOrdersAccepted = forwardOrdersAccepted;
	}

	public List<ForwardOrderCompleted> getForwardOrdersCompleted() {
		return forwardOrdersCompleted;
	}

	public void setForwardOrdersCompleted(List<ForwardOrderCompleted> forwardOrdersCompleted) {
		this.forwardOrdersCompleted = forwardOrdersCompleted;
	}

	public List<LimitOrder> getLimitOrderPending() {
		return limitOrderPending;
	}

	public void setLimitOrderPending(List<LimitOrder> limitOrderPending) {
		this.limitOrderPending = limitOrderPending;
	}

	public List<MarketOrder> getMarketOrderPending() {
		return marketOrderPending;
	}

	public void setMarketOrderPending(List<MarketOrder> marketOrderPending) {
		this.marketOrderPending = marketOrderPending;
	}

}
