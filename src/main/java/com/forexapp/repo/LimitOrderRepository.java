package com.forexapp.repo;

import java.util.List;

import com.forexapp.model.LimitOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LimitOrderRepository extends JpaRepository<LimitOrder, Long>{

	@Query("SELECT lo FROM LimitOrder lo "
			+ "WHERE lo.fromCurrency.currencyId = :toCurrencyId "
			+ "AND lo.toCurrency.currencyId = :fromCurrencyId "
			+ "AND lo.fromPriceLimit >= :inverseFromPriceLimit "
			+ "ORDER BY lo.fromPriceLimit DESC, lo.initializationDate ASC")
	List<LimitOrder> findByCurrencyIdAndInverseFromPriceLimitOrderByInverseFromPriceLimitDescAndInitializationDateAsc(
			long toCurrencyId, long fromCurrencyId, double inverseFromPriceLimit);
	
	@Query("SELECT lo FROM LimitOrder lo "
			+ "WHERE lo.fromCurrency.currencyId = :toCurrencyId "
			+ "AND lo.toCurrency.currencyId = :fromCurrencyId "
			+ "ORDER BY lo.fromPriceLimit, lo.initializationDate ASC")
	List<LimitOrder> findByCurrencyIdOrderByFromPriceLimitAscAndInitializationDateAsc(
			long toCurrencyId, long fromCurrencyId);
	
//	List<LimitOrder> findByToCurrency_currencyIdAndFromCurrency_currencyIdAndFromPriceLimitOrderByFromPriceLimitDescInitializationDateAsc(
//			long toCurrencyId, long fromCurrencyId, double inverseFromPriceLimit);
	
}
