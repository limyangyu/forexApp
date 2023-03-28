package com.forexapp.controller;

import java.util.List;

import com.forexapp.exception.CurrencyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.forexapp.model.Currency;
import com.forexapp.model.LimitOrder;
import com.forexapp.model.MarketOrder;
import com.forexapp.service.CurrencyService;
import com.forexapp.service.LimitOrderService;
import com.forexapp.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LimitOrderController {
	
	private UserService userService;
	
	private CurrencyService currencyService;
	
	private LimitOrderService limitOrderService;
	
    public LimitOrderController() {}

	@Autowired
	public LimitOrderController(UserService userService, CurrencyService currencyService,
			LimitOrderService limitOrderService) {
		this.userService = userService;
		this.currencyService = currencyService;
		this.limitOrderService = limitOrderService;
	}

	@GetMapping("/orders")
	public String showLimitOrderForm(HttpSession session, Model model) { 
		String userName = (String) session.getAttribute("username");
		long userId = userService.findByUsername(userName).getUserId();
	    
	    List<Currency> userAvailCurrency = currencyService.findAllByUserId(userId);
	    List<Currency> allCurrency = currencyService.findAll(); 

	    LimitOrder limitOrder = new LimitOrder();
	    MarketOrder marketOrder = new MarketOrder();
	    model.addAttribute("availableCurrency", userAvailCurrency);
	    model.addAttribute("allCurrency", allCurrency);
	    model.addAttribute("limitOrder", limitOrder);
	    model.addAttribute("marketOrder", marketOrder);
	    return "orders";
	}
	
	@PostMapping("/limitorders")
	public String processLimitOrderForm(@ModelAttribute("limitOrder") LimitOrder limitOrder, HttpSession session, Model model) { 	
		long userId = (long) session.getAttribute("userId");
		String isOrderAddedSuccessfullyMessage = "limitOrderId: " + limitOrder.getLimitOrderId() + " not successfully added";
		try {
		    isOrderAddedSuccessfullyMessage = limitOrderService.addLimitOrder(limitOrder, userId);
		} catch (CurrencyNotFoundException e) {
			e.printStackTrace();
		}
		model.addAttribute("alert", isOrderAddedSuccessfullyMessage);
		limitOrderService.processLimitOrder(limitOrder);

		return "redirect:/orders";
	}
	
}


