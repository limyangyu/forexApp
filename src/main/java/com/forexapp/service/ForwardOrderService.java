package com.forexapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.forexapp.exception.CurrencyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forexapp.model.ForwardOrder;
import com.forexapp.model.Holding;
import com.forexapp.model.User;
import com.forexapp.repo.ForwardOrderRepository;
import com.forexapp.repo.HoldingRepository;

@Service
public class ForwardOrderService {
	
	@Autowired
	private ForwardOrderRepository forwardOrderRepo;
	
	@Autowired
	private HoldingRepository holdingRepo;
	
	@Autowired
	private UserService userService;
	
	
	public boolean addForwardOrder(ForwardOrder forwardOrder, long userId) {
		
		long fromCurrencyId = forwardOrder.getFromCurrency().getCurrencyId();
		Optional<Holding> fromCurrencyHoldingOptional = holdingRepo.findByUser_userIdAndCurrency_currencyId(userId, fromCurrencyId);
		Holding fromCurrencyHolding = fromCurrencyHoldingOptional.orElseThrow(() -> new CurrencyNotFoundException("CurrencyId \"" + fromCurrencyId + "\" not found"));
		
		double balanceAvailable = fromCurrencyHolding.getAmount();
		double balanceRequired = forwardOrder.getFromCurrencyPrice() * forwardOrder.getToCurrencyQuantity();
		double balance = balanceAvailable - balanceRequired;
		
		if (balance < 0) return false;
		
		User user = userService.findByUserId(userId);
		forwardOrder.setInitiatorUser(user);
		forwardOrderRepo.save(forwardOrder);
		return true;
	}
	
	public void acceptForwardOrder(ForwardOrder forwardOrder, long userId) {
		
		User user = userService.findByUserId(userId);
		forwardOrder.setAcceptorUser(user);
		forwardOrderRepo.save(forwardOrder);
		
	}
	
	public List<ForwardOrder> getAllForwardOrders(){
		return forwardOrderRepo.findAll();
	}
	
	public List<ForwardOrder> getAllUnacceptedForwardOrders(){
		
		List<ForwardOrder> allForwardOrders = forwardOrderRepo.findAll();
		List<ForwardOrder> allUnacceptedForwardOrders = new ArrayList<ForwardOrder>();
		
		for (ForwardOrder order : allForwardOrders) {
			if (Objects.isNull(order.getAcceptorUser())) {
				allUnacceptedForwardOrders.add(order);
			}
		}
		return allUnacceptedForwardOrders;
		
	}
	
	public ForwardOrder findForwardOrderById(long forwardOrderId) {
		
		Optional<ForwardOrder> forwardOrderOptional = forwardOrderRepo.findById(forwardOrderId);
		ForwardOrder forwardOrder = forwardOrderOptional.orElse(new ForwardOrder());
		return forwardOrder;
		
	}
	

}
