
package com.forexapp.service;

import com.forexapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.forexapp.model.ForwardOrder;
import com.forexapp.model.ForwardOrderCompleted;
import com.forexapp.model.User;
import com.forexapp.repo.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UserService {

	private UserRepository userRepo;

//	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UserService(UserRepository userRepo, HoldingService holdingService
//			, BCryptPasswordEncoder bCryptPasswordEncoder
			) {
		this.userRepo = userRepo;
//		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

//    public boolean registerNewUser(User user) {
//        Optional<User> userOptional = userRepo.findByUsername(user.getUsername());
//        
//        if (userOptional.isEmpty()) {
//            String pw = user.getPassword();
//            String encryptedPw = bCryptPasswordEncoder.encode(pw);
//            user.setPassword(encryptedPw);
//            userRepo.save(user);
//            return true;
//        } else {
//            return false;
//        }
//    }

	public boolean registerNewUser(User user) {
		Optional<User> userOptional = userRepo.findByUsername(user.getUsername());
		if (userOptional.isEmpty()) {
			userRepo.save(user);
			return true;
		} else {
			return false;
		}
	}

	public boolean verifyUserCredentials(String username, String password) { 
		Optional<User> userOptional = userRepo.findByUsername(username); 
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			return user.getPassword().equals(password);
			}
		return false;
	}

	public User findByUsername(String username) throws UserNotFoundException {
		Optional<User> userOptional = userRepo.findByUsername(username);

		return userOptional.orElseThrow(() -> new UserNotFoundException("User \"" + username + "\" not found"));
	}

	public User findByUserId(long userId) throws UserNotFoundException {
		Optional<User> userOptional = userRepo.findById(userId);

		return userOptional.orElseThrow(() -> new UserNotFoundException("UserId \"" + userId + "\" not found"));
	}
	
	public List<ForwardOrderCompleted> getCompletedForwardOrdersGivenUsername(String username){
		
		Optional<User> userOptional = userRepo.findByUsername(username);
		
		User user = userOptional.orElse(new User());
		List<ForwardOrderCompleted> completedForwardOrders = user.getForwardOrdersCompleted();
		
		return completedForwardOrders;
		
	}
	
	public List<ForwardOrder> getForwardOrdersAcceptedGivenUserId(long userId){
		
		Optional<User> userOptional = userRepo.findById(userId);
		
		User user = userOptional.orElse(new User());
		List<ForwardOrder> forwardOrdersAccepted = user.getForwardOrdersAccepted();
		
		return forwardOrdersAccepted;
		
	}
	
	public List<ForwardOrder> getForwardOrdersInitiatedGivenUserId(long userId){
		
		Optional<User> userOptional = userRepo.findById(userId);
		
		User user = userOptional.orElse(new User());
		List<ForwardOrder> forwardOrdersInitiated = user.getForwardOrdersInitiated();
		
		return forwardOrdersInitiated;
	}
	
	public List<ForwardOrder> getUnacceptedForwardOrdersInitiatedGivenUserId(long userId){
		
		Optional<User> userOptional = userRepo.findById(userId);
		
		User user = userOptional.orElse(new User());
		List<ForwardOrder> forwardOrdersInitiated = user.getForwardOrdersInitiated();
		
		List<ForwardOrder> unacceptedInitiated = new ArrayList<ForwardOrder>();
		
		for (ForwardOrder order : forwardOrdersInitiated) {
			if (Objects.isNull(order.getAcceptorUser())) {
				unacceptedInitiated.add(order);
			}
		}
		
		return unacceptedInitiated;
	}
	
	public List<ForwardOrder> getAcceptedForwardOrdersInitiatedGivenUserId(long userId){
		
		Optional<User> userOptional = userRepo.findById(userId);
		
		User user = userOptional.orElse(new User());
		List<ForwardOrder> forwardOrdersInitiated = user.getForwardOrdersInitiated();
		
		List<ForwardOrder> acceptedInitiated = new ArrayList<ForwardOrder>();
		
		for (ForwardOrder order : forwardOrdersInitiated) {
			if (Objects.nonNull(order.getAcceptorUser())) {
				acceptedInitiated.add(order);
			}
		}
		
		return acceptedInitiated;
	}
}
