package com.forexapp.repo;

import java.util.List;
import java.util.Optional;

import com.forexapp.model.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long>{
	
	Optional<Holding> findByUser_userIdAndCurrency_currencyId(long userId, long currencyId);

	@Transactional
	@Modifying
	@Query("UPDATE Holding h SET h.amount = :amount WHERE h.user.userId = :userId AND h.currency.currencyId = :currencyId")
	void changeHoldingAmount(@Param("userId") long userId, @Param("currencyId") long currencyId, @Param("amount") Double amount);

	List<Holding> findAllByUser_userId(long userId);

	
}
