package com.example.springboot;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
			
	@GetMapping(value = "/transaction/points/{endDate}")
	public List<Customer> returnPoints(@PathVariable Date endDate) {		
		return transactionService.returnPoints(endDate);
	}
			
	@PostMapping(value = "/transaction")
	public HttpStatus createTransaction(@RequestBody Transaction transaction) {
		if (transaction.getTransactionAmount() < 0 || transaction.getCustomerID() < 0) {
			return HttpStatus.BAD_REQUEST;
		}
		Calendar calendar = Calendar.getInstance();
		java.sql.Date now = new java.sql.Date(calendar.getTimeInMillis());
		if (transaction.getTransactionDate().compareTo(now) > 0) {
			return HttpStatus.BAD_REQUEST;
		}
		transactionService.saveTransaction(transaction);
		return HttpStatus.CREATED;		
	}
}
