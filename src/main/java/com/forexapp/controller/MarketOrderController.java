package com.forexapp.controller;

import com.forexapp.model.MarketOrder;
import com.forexapp.model.User;
import com.forexapp.service.MarketOrderService;
import com.forexapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class MarketOrderController {
	
	private MarketOrderService marketOrderService;

	private UserService userService;

	@Autowired
	public MarketOrderController(MarketOrderService marketOrderService, UserService userService) {
		this.marketOrderService = marketOrderService;
		this.userService = userService;
	}

	@PostMapping("/marketorders")
	public String processLimitOrderForm(@ModelAttribute("marketOrder") MarketOrder marketOrder, HttpSession session, Model model) {
		long userId = (long) session.getAttribute("userId");
		
		User user = userService.findByUserId(userId);
		marketOrder.setUser(user);
		String isOrderAddedSuccessfullyMessage = marketOrderService.addAndProcessMarketOrder(marketOrder, userId);
		model.addAttribute("alert", isOrderAddedSuccessfullyMessage);

		return "redirect:/orders";
	}

}
