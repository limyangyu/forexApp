package com.forexapp.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forexapp.model.Currency;
import com.forexapp.model.Holding;
import com.forexapp.model.LimitOrder;
import com.forexapp.model.MarketOrder;
import com.forexapp.model.Transaction;
import com.forexapp.model.User;
import com.forexapp.repo.HoldingRepository;
import com.forexapp.repo.LimitOrderRepository;

@Service
public class MarketOrderService {
	
	private HoldingRepository holdingRepo;
	
	private LimitOrderRepository limitOrderRepo;
	
	private HoldingService holdingService;
	
	private TransactionService transactionService;
	
	private LimitOrderService limitOrderService;
	
	private String orderAddedSuccessfullyMessage = "Market order successfully placed.";

	private String orderNotSuccessfullyAddedMessage = "There are no matching orders.";
	
    public MarketOrderService() {}
	
	@Autowired
	public MarketOrderService(HoldingRepository holdingRepo, LimitOrderRepository limitOrderRepo,
			HoldingService holdingService, TransactionService transactionService,
			LimitOrderService limitOrderService) {
		this.holdingRepo = holdingRepo;
		this.limitOrderRepo = limitOrderRepo;
		this.holdingService = holdingService;
		this.transactionService = transactionService;
		this.limitOrderService = limitOrderService;
	}

	public String addAndProcessMarketOrder(MarketOrder marketOrder, long userId) {
		// check if user has enough balance to process trade
		long fromCurrencyId = marketOrder.getFromCurrency().getCurrencyId();
		long toCurrencyId = marketOrder.getToCurrency().getCurrencyId();
		Optional<Holding> fromCurrencyHoldingOptional = holdingRepo.findByUser_userIdAndCurrency_currencyId(userId,
				fromCurrencyId);
		
		List<LimitOrder> matchedOrders = limitOrderRepo
				.findByCurrencyIdOrderByFromPriceLimitAscAndInitializationDateAsc(
						toCurrencyId, fromCurrencyId);
		if (matchedOrders.isEmpty()) return orderNotSuccessfullyAddedMessage;
		
		double fromQuantitySubmitted = marketOrder.getFromQuantity(); //sgd
		double totalToQty = 0;

		for (LimitOrder limitOrder : matchedOrders) {
			double inverseMatchedOrderFromPriceLimit = 1.0 / limitOrder.getFromPriceLimit();// usd to sgd
			double toCurrencyQtyToBeReceived = fromQuantitySubmitted / inverseMatchedOrderFromPriceLimit; // usd john shld get
			double matchedOrderFromCurrencyHold = limitOrder.getFromCurrencyHold();// peter's usd qty
//			                                     usd john should get          usd peter has 
			double minToQtyToTransact = Math.min(toCurrencyQtyToBeReceived, matchedOrderFromCurrencyHold);
			double fromQuantityToTransact = minToQtyToTransact * inverseMatchedOrderFromPriceLimit;// sgd
			totalToQty += minToQtyToTransact;

			fromQuantitySubmitted -= fromQuantityToTransact;
			if (fromQuantitySubmitted == 0) {
				// execute transaction
				executeOrder(marketOrder, limitOrder, fromQuantityToTransact, minToQtyToTransact); // deducts and adds amts
				archiveMarketOrder(marketOrder, limitOrder, totalToQty);
				return orderAddedSuccessfullyMessage; 
			} 
			executeOrder(marketOrder, limitOrder, fromQuantityToTransact, minToQtyToTransact);
		}
		
		holdingService.addHoldingAmount(userId, fromCurrencyId, fromQuantitySubmitted);
		return orderAddedSuccessfullyMessage;
	}

	private void archiveMarketOrder(MarketOrder marketOrder, LimitOrder limitOrder, double totalToQty) {
		User firstUser = marketOrder.getUser();
		User secondUser = limitOrder.getUser();
		Currency fromCurrency = marketOrder.getFromCurrency();
		Currency toCurrency = marketOrder.getToCurrency();
		double fromQuantity = marketOrder.getFromQuantity();
		double toQuantity = totalToQty;
		
		Timestamp transactionDate = new Timestamp(System.currentTimeMillis());

		Transaction archivedOrder = new Transaction(firstUser, secondUser, fromCurrency, toCurrency, fromQuantity, toQuantity, transactionDate);
		transactionService.save(archivedOrder);
	}

	private void executeOrder(MarketOrder marketOrder, LimitOrder limitOrder, double fromQuantityToTransact,
			double minToQtyToTransact) {
		holdingService.addHoldingAmount(marketOrder.getUser().getUserId(), 
				marketOrder.getToCurrency().getCurrencyId(), 
				minToQtyToTransact);
		holdingService.reduceHoldingAmount(marketOrder.getUser().getUserId(), 
				marketOrder.getFromCurrency().getCurrencyId(), 
				fromQuantityToTransact);

		holdingService.addHoldingAmount(limitOrder.getUser().getUserId(), 
				limitOrder.getToCurrency().getCurrencyId(), 
				fromQuantityToTransact);
		holdingService.reduceHoldingAmount(limitOrder.getUser().getUserId(), 
				limitOrder.getFromCurrency().getCurrencyId(), 
				minToQtyToTransact);
		limitOrder.setToCurrencyQuantity( // peter's sgd to be fulfilled
				limitOrder.getToCurrencyQuantity() - fromQuantityToTransact);

		// checking if the limitOrder parameter object instance is completed (toCurrencyQuantity attribute of limitOrder equals 0)
		if (limitOrder.getToCurrencyQuantity() == 0) {
			long fromCurrencyId = limitOrder.getFromCurrency().getCurrencyId();
			holdingService.addHoldingAmount(limitOrder.getUser().getUserId(), fromCurrencyId, limitOrder.getFromCurrencyHold());
			limitOrderService.archiveLimitOrder(limitOrder, marketOrder);
		}
	}
}
