package com.forexapp.repo;

import com.forexapp.model.MarketOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketOrderRepository extends JpaRepository<MarketOrder, Long>{
	
	

}
