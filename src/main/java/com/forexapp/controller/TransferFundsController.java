package com.forexapp.controller;

import java.util.List;

import com.forexapp.exception.CurrencyNotFoundException;
import com.forexapp.exception.HoldingNotFoundException;
import com.forexapp.exception.UserNotFoundException;
import com.forexapp.model.Currency;
import com.forexapp.service.CurrencyService;
import com.forexapp.service.HoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class TransferFundsController {
	
	private HoldingService holdingService;
	
	private CurrencyService currencyService;
	
	@Autowired
    public TransferFundsController(HoldingService holdingService, CurrencyService currencyService) {
		this.holdingService = holdingService;
		this.currencyService = currencyService;
	}

	@GetMapping("/transferfunds")
    public String goToTransferFundsPage(HttpSession session, Model model) {
		long userId = (long) session.getAttribute("userId");
		List<Currency> depositCurrencies = currencyService.findAll();
		List<Currency> withdrawCurrencies = currencyService.findAllByUserId(userId);
		model.addAttribute("depositCurrencies", depositCurrencies);
		model.addAttribute("withdrawCurrencies", withdrawCurrencies);
    	return "transferfunds";
    }
    
    @PostMapping("/deposit")
    public String deposit(HttpSession session, @RequestParam long currencyId, @RequestParam double depositAmount) {
    	String username = (String) session.getAttribute("username");
    	try {
    		holdingService.addHoldingAmount(username, currencyId, depositAmount);
		} catch (UserNotFoundException | CurrencyNotFoundException e) {
			e.printStackTrace();
		}
    	return "redirect:/transferfunds";
    }
    
    @PostMapping("/withdraw")
    public String withdraw(HttpSession session, @RequestParam long currencyId, @RequestParam double withdrawAmount) {
    	String username = (String) session.getAttribute("username");
    	try {
    	    holdingService.reduceHoldingAmount(username, currencyId, withdrawAmount);
		} catch (UserNotFoundException | HoldingNotFoundException e) {
			e.printStackTrace();
		}
    	return "redirect:/transferfunds";
    }

}
