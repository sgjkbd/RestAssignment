package com.example.springboot;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.example.springboot.TransactionService.CustomerMonth;

public class TransactionServiceTest {
	
	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private TransactionService transactionService;
	
	@Spy
	private TransactionService spyTransactionService;
	
	List<Transaction> transactionList;
	Calendar calendar;
	java.sql.Date endDate;
	java.sql.Date startDate;
	
	@BeforeEach
	public void onSetUp() {
		MockitoAnnotations.initMocks(this);
		calendar = Calendar.getInstance();
		java.sql.Date transactionDate = new java.sql.Date(calendar.getTimeInMillis());
		Transaction transaction = new Transaction(1, 100, transactionDate);
		Mockito.when(transactionRepository.save(transaction)).thenReturn(null);
		transactionService = new TransactionService(transactionRepository);
		spyTransactionService = Mockito.spy(transactionService);
		transactionList = new ArrayList<Transaction>();
		transactionList.add(transaction);
		calendar.set(2020, 5, 1);
		endDate = new java.sql.Date(calendar.getTimeInMillis());
		calendar.set(2020, 4, 1);
		startDate = new java.sql.Date(calendar.getTimeInMillis());
	}
	
	@Test
	public void testGetPoints() {
		Mockito.when(transactionRepository.findByTransactionDateBetween(Mockito.any(), Mockito.any())).thenReturn(transactionList);
		HashMap<Integer, Integer> mockAnswer = new HashMap<Integer, Integer>();
		mockAnswer.put(1, 50);
		Mockito.doReturn(mockAnswer).when(spyTransactionService).getCustomerPoints(Mockito.any(), Mockito.any(), Mockito.any());
		Customer customer = new Customer(1);
		customer.setMonth3Points(50);
		customer.setTotalPoints(50);
		List<Customer> actual = transactionService.returnPoints(endDate);
		actual.stream()
		.forEach(c -> {
			assertEquals(customer.getMonth1Points(), c.getMonth1Points());
			assertEquals(customer.getMonth2Points(), c.getMonth2Points());
			assertEquals(customer.getMonth3Points(), c.getMonth3Points());
			assertEquals(customer.getTotalPoints(), c.getTotalPoints());
		});
	}
	
	@Test
	public void testGetCustomerPoints() {
		HashMap<Integer, Integer> expected = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> actual = new HashMap<Integer, Integer>();
		HashMap<Integer, ArrayList<Integer>> mockAnswer = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<Integer> mockValue = new ArrayList<Integer>();
		mockValue.add(100);
		mockAnswer.put(1, mockValue);
		Mockito.doReturn(mockAnswer).when(spyTransactionService).filterCustomerPoints(Mockito.any(), Mockito.any(), Mockito.any());
		actual = transactionService.getCustomerPoints(transactionList, endDate, CustomerMonth.TOTAL);
		expected.put(1, 50);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testFilterCustomerPoints() {
		HashMap<Integer, ArrayList<Integer>> expected = new HashMap<Integer, ArrayList<Integer>>();
		HashMap<Integer, ArrayList<Integer>> actual = new HashMap<Integer, ArrayList<Integer>>();
		actual = transactionService.filterCustomerPoints(transactionList, startDate, endDate);
		ArrayList<Integer> expectedValue = new ArrayList<Integer>();
		expectedValue.add(100);
		expected.put(1, expectedValue);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testCalcCustomerTotalPointsPerMonth() {
		HashMap<Integer, Integer> expected = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> actual = new HashMap<Integer, Integer>();
		HashMap<Integer, ArrayList<Integer>> input = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<Integer> value = new ArrayList<Integer>();
		value.add(100);
		input.put(1, value);
		actual = transactionService.calcCustomerTotalPointsOneMonth(input);
		expected.put(1, 50);
		assertEquals(expected, actual);
	}
	

}
