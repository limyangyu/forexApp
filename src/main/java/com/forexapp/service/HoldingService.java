package com.forexapp.service;

import java.util.List;
import java.util.Optional;

import com.forexapp.exception.CurrencyNotFoundException;
import com.forexapp.exception.HoldingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forexapp.exception.UserNotFoundException;
import com.forexapp.model.Currency;
import com.forexapp.model.Holding;
import com.forexapp.model.User;
import com.forexapp.repo.CurrencyRepository;
import com.forexapp.repo.HoldingRepository;
import com.forexapp.repo.UserRepository;

@Service
public class HoldingService {

	private UserRepository userRepo;
	
	private HoldingRepository holdingRepo;
	
	private CurrencyRepository currencyRepo;

	@Autowired
	public HoldingService(UserRepository userRepo, HoldingRepository holdingRepo, CurrencyRepository currencyRepo) {
		this.userRepo = userRepo;
		this.holdingRepo = holdingRepo;
		this.currencyRepo = currencyRepo;
	}

	public void addHoldingAmount(String username, long currencyId, double addAmount) throws UserNotFoundException, CurrencyNotFoundException {
		Optional<User> userOptional = userRepo.findByUsername(username);
		
		User user = userOptional.orElseThrow(() -> new UserNotFoundException("User \"" + username + "\" not found"));
		long userId = user.getUserId();
		Optional<Holding> holdingOptional = holdingRepo.findByUser_userIdAndCurrency_currencyId(userId, currencyId);
		
		if (holdingOptional.isPresent()) {
			Holding holding = holdingOptional.get();
			double currentAmount = holding.getAmount();
			holdingRepo.changeHoldingAmount(userId, currencyId, currentAmount + addAmount);
		} else {
			Currency currency = currencyRepo.findById(currencyId).orElseThrow(() -> new CurrencyNotFoundException("CurrencyId \"" + currencyId + "\" not found"));
			Holding holding = new Holding(currency, user, addAmount);
			holdingRepo.save(holding);
		}
	}
	
	public void addHoldingAmount(long userId, long currencyId, double addAmount) throws UserNotFoundException, CurrencyNotFoundException{
		Optional<User> userOptional = userRepo.findById(userId);
		
		User user = userOptional.orElseThrow(() -> new UserNotFoundException("UserId \"" + userId + "\" not found"));
		Optional<Holding> holdingOptional = holdingRepo.findByUser_userIdAndCurrency_currencyId(userId, currencyId);
		
		if (holdingOptional.isPresent()) {
			Holding holding = holdingOptional.get();
			double currentAmount = holding.getAmount();
			holdingRepo.changeHoldingAmount(userId, currencyId, currentAmount + addAmount);
		} else {
			Currency currency = currencyRepo.findById(currencyId).orElseThrow(() -> new CurrencyNotFoundException("CurrencyId \"" + currencyId + "\" not found"));
			Holding holding = new Holding(currency, user, addAmount);
			holdingRepo.save(holding);
		}
	}

	public void reduceHoldingAmount(String username, long currencyId, double reduceAmount) throws UserNotFoundException, HoldingNotFoundException {
	    Optional<User> userOptional = userRepo.findByUsername(username);
		
		User user = userOptional.orElseThrow(() -> new UserNotFoundException("User \"" + username + "\" not found"));
		long userId = user.getUserId();
		Optional<Holding> holdingOptional = holdingRepo.findByUser_userIdAndCurrency_currencyId(userId, currencyId);
		
		holdingOptional.orElseThrow(() -> new HoldingNotFoundException("User does not have Holding of CurrencyId \"" + currencyId + "\""));
		Holding holding = holdingOptional.get();
		double currentAmount = holding.getAmount();
		double balance = currentAmount - reduceAmount;
		if (currentAmount - reduceAmount < 0) balance = currentAmount;
		holdingRepo.changeHoldingAmount(userId, currencyId, balance);
	}
	
	public void reduceHoldingAmount(long userId, long currencyId, double reduceAmount) throws UserNotFoundException, HoldingNotFoundException{
		Optional<Holding> holdingOptional = holdingRepo.findByUser_userIdAndCurrency_currencyId(userId, currencyId);
		
		holdingOptional.orElseThrow(() -> new HoldingNotFoundException("User does not have Holding of CurrencyId \"" + currencyId + "\""));
		Holding holding = holdingOptional.get();
		double currentAmount = holding.getAmount();
		double balance = currentAmount - reduceAmount;
		if (currentAmount - reduceAmount < 0) balance = currentAmount;
		holdingRepo.changeHoldingAmount(userId, currencyId, balance);
	}

	public List<Holding> findAllByUserId(long userId) {
		return holdingRepo.findAllByUser_userId(userId);
	}
	
}
