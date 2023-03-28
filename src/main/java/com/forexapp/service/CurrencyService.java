package com.forexapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forexapp.model.Currency;
import com.forexapp.model.Holding;
import com.forexapp.repo.CurrencyRepository;

@Service
public class CurrencyService {

	private HoldingService holdingService;

	private CurrencyRepository currencyRepo;

	@Autowired
	public CurrencyService(HoldingService holdingService, CurrencyRepository currencyRepo) {
		this.holdingService = holdingService;
		this.currencyRepo = currencyRepo;
	}

	public List<Currency> findAllByCurrencyIds(List<Long> userAvailCurrencyIds) {
		return currencyRepo.findAllByCurrencyIdIn(userAvailCurrencyIds);
	}

	public List<Currency> findAll() {
		return currencyRepo.findAll();
	}

	/**
	 * Returns List of Currency that a user has in their Holdings.
	 * @param userId The id of the User
	 * @return List of Currency Objects
	 */
	public List<Currency> findAllByUserId(long userId) {
		List<Holding> holdingsOfUser = holdingService.findAllByUserId(userId);
		List<Long> userAvailCurrencyIds = new ArrayList<>();
		for (Holding holding : holdingsOfUser) {
			userAvailCurrencyIds.add(holding.getCurrency().getCurrencyId());
		}
	    return findAllByCurrencyIds(userAvailCurrencyIds);
	}

}
