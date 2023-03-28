package com.forexapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forexapp.model.Transaction;
import com.forexapp.repo.TransactionRepository;

@Service
public class TransactionService {

	private TransactionRepository transactionRepo;

	public TransactionService() {}

	@Autowired
	public TransactionService(TransactionRepository transactionRepo) {
		this.transactionRepo = transactionRepo;
	}

	public void save(Transaction order) {
        transactionRepo.save(order);
	}

	

}
