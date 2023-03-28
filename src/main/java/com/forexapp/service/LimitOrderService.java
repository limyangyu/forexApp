package com.forexapp.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import com.forexapp.exception.CurrencyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forexapp.model.Currency;
import com.forexapp.model.Holding;
import com.forexapp.model.LimitOrder;
import com.forexapp.model.Order;
import com.forexapp.model.Transaction;
import com.forexapp.model.User;
import com.forexapp.repo.HoldingRepository;
import com.forexapp.repo.LimitOrderRepository;

@Service
public class LimitOrderService {

	private HoldingRepository holdingRepo;

	private LimitOrderRepository limitOrderRepo;

	private HoldingService holdingService;

	private UserService userService;
	
	private TransactionService transactionService;

	private String isOrderAddedSuccessfullyMessage = "Limit order successfully placed.";

	private String orderNotSuccessfullyAddedMessage = "You do not have enough funds in your wallet.";

	@Autowired
	public LimitOrderService(HoldingRepository holdingRepo, HoldingService holdingService, UserService userService,
			LimitOrderRepository limitOrderRepo, TransactionService transactionService) {
		this.holdingRepo = holdingRepo;
		this.holdingService = holdingService;
		this.userService = userService;
		this.limitOrderRepo = limitOrderRepo;
		this.transactionService = transactionService;
	}

	public String addLimitOrder(LimitOrder limitOrder, long userId) throws CurrencyNotFoundException {
		// check if user has enough balance to process trade
		long fromCurrencyId = limitOrder.getFromCurrency().getCurrencyId();
		Optional<Holding> fromCurrencyHoldingOptional = holdingRepo.findByUser_userIdAndCurrency_currencyId(userId,
				fromCurrencyId);
		Holding fromCurrencyHolding = fromCurrencyHoldingOptional
				.orElseThrow(() -> new CurrencyNotFoundException("CurrencyId \"" + fromCurrencyId + "\" not found"));

		double balanceAvailable = fromCurrencyHolding.getAmount();
		double balanceRequired = limitOrder.getToCurrencyQuantity() * limitOrder.getFromPriceLimit();
		double balance = balanceAvailable - balanceRequired;

		if (balance < 0)
			return orderNotSuccessfullyAddedMessage;

		// deduct from his wallet
		holdingService.reduceHoldingAmount(userId, fromCurrencyId, balance);

		// add to the limit order hold
		limitOrder.setFromCurrencyHold(balanceRequired);

		// initialize order
		User user = userService.findByUserId(userId);
		limitOrder.setUser(user);
		limitOrder.setOriginalFromQuantity(balanceRequired);
		limitOrder.setOriginalToQuantity(limitOrder.getToCurrencyQuantity());
		limitOrder.setInitializationDate(new Timestamp(System.currentTimeMillis()));
		limitOrderRepo.save(limitOrder);
		return isOrderAddedSuccessfullyMessage;
	}

	/**
	 * Match and execute orders if matching orders exist.
	 * 
	 * @param limitOrder The LimitOrder instance to be processed
	 */
	public void processLimitOrder(LimitOrder limitOrder) {
		long toCurrencyId = limitOrder.getToCurrency().getCurrencyId();
		long fromCurrencyId = limitOrder.getFromCurrency().getCurrencyId();
		double inverseFromPriceLimit = 1.0 / limitOrder.getFromPriceLimit();
		List<LimitOrder> matchedOrders = limitOrderRepo
				.findByCurrencyIdAndInverseFromPriceLimitOrderByInverseFromPriceLimitDescAndInitializationDateAsc(
						toCurrencyId, fromCurrencyId, inverseFromPriceLimit);

		double quantityToFufill = limitOrder.getToCurrencyQuantity();

		for (LimitOrder matchedOrder : matchedOrders) {
			if (quantityToFufill > 0) {
				// !!! to add execution of orders for both matching parties
				executeLimitOrders(limitOrder, matchedOrder);
				quantityToFufill = limitOrder.getToCurrencyQuantity();
			} else {
				break;
			}
		}
	}

	/**
	 * Executes the limitorder based on the limitOrder parameter.
	 * 
	 * @param limitOrder   The limitorder object instance to reference
	 * @param matchedOrder The limitOrder object instance that matches limitOrder
	 */
	private void executeLimitOrders(LimitOrder limitOrder, LimitOrder matchedOrder) {
		double toCurrencyQuantity = limitOrder.getToCurrencyQuantity();
		double matchedOrderFromCurrencyQuantity = matchedOrder.getFromCurrencyHold();
		double qtyToFulfill = 0;

		if ((toCurrencyQuantity - matchedOrderFromCurrencyQuantity) >= 0) {
			qtyToFulfill = matchedOrderFromCurrencyQuantity;
		} else {
			qtyToFulfill = toCurrencyQuantity;
		}
		double totalFromCurrencyTransferred = qtyToFulfill * (1.0 / matchedOrder.getFromPriceLimit());
		executeLimitOrder(limitOrder, matchedOrder, totalFromCurrencyTransferred, qtyToFulfill);

		executeLimitOrder(matchedOrder, limitOrder, qtyToFulfill, totalFromCurrencyTransferred);
	}

	/**
	 * Executes a LimitOrder based on the quantity parameters.
	 * @param limitOrder LimitOrder object instance to be changed
	 * @param fromQuantity The fromCurrency quantity to be changed of the limitOrder instance
	 * @param toQuantity The toCurrency quantity to be changed of the limitOrder instance
	 */
	private void executeLimitOrder(LimitOrder limitOrder, LimitOrder matchedOrder, double fromQuantity, double toQuantity) {
		// changing fromCurrencyHold attribute of limitOrder parameter
		double fromCurrencyHoldBalance = limitOrder.getFromCurrencyHold() - fromQuantity;
		limitOrder.setFromCurrencyHold(fromCurrencyHoldBalance);
		
		// adding to toCurrency holding of user in limitOrder parameter
		String username = limitOrder.getUser().getUsername();
		long toCurrencyId = limitOrder.getToCurrency().getCurrencyId();
		holdingService.addHoldingAmount(username, toCurrencyId, toQuantity);
		
		// updating the toCurrencyQuantity of the limitOrder parameter
		double existingToCurrencyQuantity = limitOrder.getToCurrencyQuantity(); 
		limitOrder.setToCurrencyQuantity(existingToCurrencyQuantity - toQuantity);
	    limitOrderRepo.save(limitOrder);
		
		// checking if the limitOrder parameter object instance is completed (toCurrencyQuantity attribute of limitOrder equals 0)
		if (limitOrder.getToCurrencyQuantity() == 0) {
			long fromCurrencyId = limitOrder.getFromCurrency().getCurrencyId();
			holdingService.addHoldingAmount(username, fromCurrencyId, limitOrder.getFromCurrencyHold());
			archiveLimitOrder(limitOrder, matchedOrder);
		}
	}

	public void archiveLimitOrder(LimitOrder limitOrder, Order matchedOrder) {
		User firstUser = limitOrder.getUser();
		User secondUser = matchedOrder.getUser();
		Currency fromCurrency = limitOrder.getFromCurrency();
		Currency toCurrency = limitOrder.getToCurrency();
		double fromQuantity = limitOrder.getOriginalFromQuantity();
		double toQuantity = limitOrder.getOriginalToQuantity();
		Timestamp transactionDate = new Timestamp(System.currentTimeMillis());

		Transaction archivedOrder = new Transaction(firstUser, secondUser, fromCurrency, toCurrency, fromQuantity, toQuantity, transactionDate);
		transactionService.save(archivedOrder);
		
		limitOrderRepo.delete(limitOrder);
	}

}
