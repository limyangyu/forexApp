package com.forexapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.forexapp.model.User;
import com.forexapp.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	
	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/")
	public String goToIndexPage() {
		return "index";
	}

	@GetMapping("/register")
	public String goToRegisterPage(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}
	
	@PostMapping("/register")
	public String registerUser(User user) {
		if (userService.registerNewUser(user)) {
			return "redirect:/login";
		}
		else {
			return "register";
		}
	}
	
	@GetMapping("/login")
	public String goToLoginPage() {
		return "login";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		return "index";
	}
	
}
