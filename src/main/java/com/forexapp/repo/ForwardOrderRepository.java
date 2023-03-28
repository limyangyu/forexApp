package com.forexapp.repo;

import com.forexapp.model.ForwardOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForwardOrderRepository extends JpaRepository<ForwardOrder, Long>{

	

}
