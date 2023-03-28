package com.forexapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.forexapp.exception.CurrencyNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.forexapp.exception.CurrencyNotFoundException;
import com.forexapp.exception.HoldingNotFoundException;
import com.forexapp.exception.UserNotFoundException;
import com.forexapp.model.Currency;
import com.forexapp.model.Holding;
import com.forexapp.model.User;
import com.forexapp.repo.CurrencyRepository;
import com.forexapp.repo.HoldingRepository;
import com.forexapp.repo.UserRepository;

@ExtendWith(MockitoExtension.class)
public class HoldingServiceTest {

	@Mock UserRepository userRepo;
	
	@Mock HoldingRepository holdingRepo;
	
	@Mock CurrencyRepository currencyRepo;
	
	HoldingService holdingService;
	
	long id = 1;
	
	String username = "Abe";
	
	String email = "abe@email.com";
	
	String password = "123";
	
	long currencyId = 1;
	
	double amount = 12.3;
	
	User user;
	
	Optional<User> userOptional; 
	
	Holding holding;
	
	Optional<Holding> holdingOptional;
	
	Currency currency = new Currency();

	@BeforeEach
	void setup() {
		holdingService = new HoldingService(userRepo, holdingRepo, currencyRepo);
		user = new User(username, email, password);
		userOptional = Optional.of(user);
		holding = new Holding(currency, user, amount);
		holdingOptional = Optional.of(holding);
		user.setUserId(id);
		holding.setHoldingId(id);
	}
	
	@Test
	@DisplayName("test addHoldingAmount, should call changeHoldingAmount method of HoldingRepository")
	void addHoldingAmountTestExistingUserAndCurrency() {
		when(userRepo.findByUsername(username)).thenReturn(userOptional);
		when(holdingRepo.findByUser_userIdAndCurrency_currencyId(id, currencyId)).thenReturn(holdingOptional);
		
		holdingService.addHoldingAmount(username, currencyId, amount);
		
		verify(holdingRepo).changeHoldingAmount(id, currencyId, amount+amount);
	}

	@Test
	@DisplayName("test addHoldingAmount, should throw UsernotFoundException")
	void addHoldingAmountTestNonExistentUserAndExistingCurrency() {
		when(userRepo.findByUsername(username)).thenReturn(Optional.empty());
		Exception expected = assertThrows(UserNotFoundException.class, () -> {
			holdingService.addHoldingAmount(username, currencyId, amount);
		});
		String expectedMessage = "User \"" + username + "\" not found";

		assertEquals(expected.getMessage(), expectedMessage);
	}
	
	@Test
	@DisplayName("test addHoldingAmount, should throw CurrencyNotFoundException")
	void addHoldingAmountTestExistingUserAndNonExistentCurrency() {
		when(userRepo.findByUsername(username)).thenReturn(userOptional);
		when(holdingRepo.findByUser_userIdAndCurrency_currencyId(id, currencyId)).thenReturn(Optional.empty());
		Exception expected = assertThrows(CurrencyNotFoundException.class, () -> {
			holdingService.addHoldingAmount(username, currencyId, amount);
		});
		String expectedMessage = "CurrencyId \"" + currencyId + "\" not found";

		assertEquals(expected.getMessage(), expectedMessage);
	}

	@Test
	@DisplayName("test reduceHoldingAmount, should call changeHoldingAmount method of HoldingRepository")
	void reduceHoldingAmountTestExistingUserAndCurrency() {
		when(userRepo.findByUsername(username)).thenReturn(userOptional);
		when(holdingRepo.findByUser_userIdAndCurrency_currencyId(id, currencyId)).thenReturn(holdingOptional);
		
		holdingService.reduceHoldingAmount(username, currencyId, amount);
		
		verify(holdingRepo).changeHoldingAmount(id, currencyId, amount-amount);
	}

	@Test
	@DisplayName("test reduceHoldingAmount, should throw UsernotFoundException")
	void reduceHoldingAmountTestNonExistentUserAndExistingCurrency() {
		when(userRepo.findByUsername(username)).thenReturn(Optional.empty());
		Exception expected = assertThrows(UserNotFoundException.class, () -> {
			holdingService.reduceHoldingAmount(username, currencyId, amount);
		});
		String expectedMessage = "User \"" + username + "\" not found";

		assertEquals(expected.getMessage(), expectedMessage);
	}

	@Test
	@DisplayName("test reduceHoldingAmount, should throw HoldingnotFoundException")
	void reduceHoldingAmountTestExistingUserAndNonExistentCurrency() {
		when(userRepo.findByUsername(username)).thenReturn(userOptional);
		when(holdingRepo.findByUser_userIdAndCurrency_currencyId(id, currencyId)).thenReturn(Optional.empty());
		Exception expected = assertThrows(HoldingNotFoundException.class, () -> {
			holdingService.reduceHoldingAmount(username, currencyId, amount);
		});
		String expectedMessage = "User does not have Holding of CurrencyId \"" + currencyId + "\""; 

		assertTrue(expected.getMessage().equals(expectedMessage));
	}
	
	@Test
	@DisplayName("test reduceHoldingAmount userId instead of username parameter, should call changeHoldingAmount method of HoldingRepository")
	void reduceHoldingAmountTestExistingUserAndCurrencyWithUserIdParamter() {
		when(holdingRepo.findByUser_userIdAndCurrency_currencyId(id, currencyId)).thenReturn(holdingOptional);
		
		holdingService.reduceHoldingAmount(id, currencyId, amount);
		
		verify(holdingRepo).changeHoldingAmount(id, currencyId, amount-amount);
	}

}
