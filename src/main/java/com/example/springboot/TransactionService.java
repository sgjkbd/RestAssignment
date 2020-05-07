package com.example.springboot;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
	private TransactionRepository transactionRepository;
	
	public TransactionService() {
		
	}
	
	@Autowired
	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}
	
	public void saveTransaction(Transaction transaction) {
		transactionRepository.save(transaction);
	}

	
	public List<Customer> returnPoints(Date endDate) {
		List<Customer> customers = new ArrayList<Customer>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		calendar.add(Calendar.MONTH, -3);
		java.sql.Date startDate = new java.sql.Date(calendar.getTimeInMillis());	
		List<Transaction> transactionList = new ArrayList<Transaction>();
		transactionList = transactionRepository.findByTransactionDateBetween(startDate, endDate);
		
		HashMap<Integer, Integer> customerTotalTotals = getCustomerPoints(transactionList, endDate, CustomerMonth.TOTAL);
		HashMap<Integer, Integer> customerTotalsMonthOne = getCustomerPoints(transactionList, endDate, CustomerMonth.MONTH_ONE);
		HashMap<Integer, Integer> customerTotalsMonthTwo = getCustomerPoints(transactionList, endDate, CustomerMonth.MONTH_TWO);
		HashMap<Integer, Integer> customerTotalsMonthThree = getCustomerPoints(transactionList, endDate, CustomerMonth.MONTH_THREE);
		
		customerTotalTotals.entrySet().stream()
		.forEach(c -> {
			Customer customer = new Customer(c.getKey());
			customer.setTotalPoints(c.getValue());
			if (customerTotalsMonthOne.get(c.getKey()) != null) {				
				customer.setMonth1Points(customerTotalsMonthOne.get(c.getKey()));
			}
			if (customerTotalsMonthTwo.get(c.getKey()) != null) {				
				customer.setMonth2Points(customerTotalsMonthTwo.get(c.getKey()));
			}
			if (customerTotalsMonthThree.get(c.getKey()) != null) {				
				customer.setMonth3Points(customerTotalsMonthThree.get(c.getKey()));
			}
			customers.add(customer);
		});
		return customers;
	}
	
	public enum CustomerMonth{
		MONTH_ONE, MONTH_TWO, MONTH_THREE, TOTAL
	}
		
	HashMap<Integer, Integer> getCustomerPoints(List<Transaction> transactionList, Date endDate, CustomerMonth customerMonth) {
		HashMap<Integer, ArrayList<Integer>> customerPoints = null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		if (customerMonth.equals(CustomerMonth.MONTH_ONE)) {
			calendar.add(Calendar.MONTH, -3);
			java.sql.Date startDate = new java.sql.Date(calendar.getTimeInMillis());
			calendar.add(Calendar.MONTH, 1);
			endDate = new java.sql.Date(calendar.getTimeInMillis());
			customerPoints = filterCustomerPoints(transactionList, startDate, endDate);
		}
		else if (customerMonth.equals(CustomerMonth.MONTH_TWO)) {
			calendar.add(Calendar.MONTH, -2);
			java.sql.Date startDate = new java.sql.Date(calendar.getTimeInMillis());
			calendar.add(Calendar.MONTH, 1);
			endDate = new java.sql.Date(calendar.getTimeInMillis());
			customerPoints = filterCustomerPoints(transactionList, startDate, endDate);
		}
		else if (customerMonth.equals(CustomerMonth.MONTH_THREE)) {
			calendar.add(Calendar.MONTH, -1);
			java.sql.Date startDate = new java.sql.Date(calendar.getTimeInMillis());
			customerPoints = filterCustomerPoints(transactionList, startDate, endDate);
		}
		else if (customerMonth.equals(CustomerMonth.TOTAL)) {
			calendar.add(Calendar.MONTH, -3);
			java.sql.Date startDate = new java.sql.Date(calendar.getTimeInMillis());
			customerPoints = filterCustomerPoints(transactionList, startDate, endDate);
		}
		return calcCustomerTotalPointsOneMonth(customerPoints);
	}
	
	public HashMap<Integer, ArrayList<Integer>> filterCustomerPoints(List<Transaction> transactionList, Date startDate, Date endDate) {
		HashMap<Integer, ArrayList<Integer>> customerPoints = new HashMap<Integer, ArrayList<Integer>>();
		transactionList.stream()
		.filter(c -> (c.getTransactionDate().compareTo(startDate) >= 0))
		.filter(c -> (c.getTransactionDate().compareTo(endDate) < 0))
		.forEach(c -> {
			if (customerPoints.containsKey(c.getCustomerID())) {
				ArrayList<Integer> points = (ArrayList<Integer>) customerPoints.get(c.getCustomerID());
				points.add(c.getTransactionAmount());
				customerPoints.put(c.getCustomerID(), points);
			}
			else {
				ArrayList<Integer> points = new ArrayList<Integer>();
				points.add(c.getTransactionAmount());
				customerPoints.put(c.getCustomerID(), points);
			}
		});
		return customerPoints;
	}

	HashMap<Integer, Integer> calcCustomerTotalPointsOneMonth(HashMap<Integer, ArrayList<Integer>> customerPoints){
		HashMap<Integer, Integer> customerPointMonth = new HashMap<Integer, Integer>();
		customerPoints.entrySet().stream()
		.forEach(c -> {
			int customerID = c.getKey();
			customerPointMonth.put(customerID, 0);
			c.getValue().stream()
			.filter(p -> (p > 50))
			.forEach(p -> {
				int points = (p > 100 ? 50 + 2 * (p - 100) : p - 50) + customerPointMonth.get(customerID);
				customerPointMonth.put(customerID, points);
			});
		});
		return customerPointMonth;
	}

}
