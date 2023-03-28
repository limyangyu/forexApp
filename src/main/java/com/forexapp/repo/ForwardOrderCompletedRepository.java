package com.forexapp.repo;

import com.forexapp.model.ForwardOrderCompleted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForwardOrderCompletedRepository extends JpaRepository<ForwardOrderCompleted, Long> {

}
