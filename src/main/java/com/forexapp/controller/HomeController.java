package com.forexapp.controller;

import com.forexapp.security.UserPrincipal;
import com.forexapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//import com.fdm.forexapp.security.UserPrincipal;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	private UserService userService;

	@Autowired
	public HomeController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/home")
	public String goToHomePage(HttpSession session) {
		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userPrincipal.getUsername();
		long userId = userService.findByUsername(username).getUserId();
		session.setAttribute("username", username);
		session.setAttribute("userId", userId);
		return "home";
	}

}
