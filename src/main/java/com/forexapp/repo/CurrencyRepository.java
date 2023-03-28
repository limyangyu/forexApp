package com.forexapp.repo;

import java.util.List;

import com.forexapp.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long>{

	List<Currency> findAllByCurrencyIdIn(List<Long> userAvailCurrencyIds);

}
