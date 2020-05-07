package com.example.springboot;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
	List<Transaction> findByTransactionDateBetween(Date startDate, Date endDate);
}
