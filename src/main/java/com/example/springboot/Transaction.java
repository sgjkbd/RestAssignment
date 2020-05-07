package com.example.springboot;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Transaction {
	
	public Transaction() {
		
	}
	
	public Transaction(int customerID, int transactionAmount, Date transactionDate) {
		this.customerID = customerID;
		this.transactionAmount = transactionAmount;
		this.transactionDate = transactionDate;
	}
	
	@Id
	@GeneratedValue
	private Long id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private int customerID;
	public int getCustomerID() {
		return this.customerID;
	}
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	public int getTransactionAmount() {
		return transactionAmount;
	}
	private int transactionAmount;
	public void setTransactionAmount(int transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	private Date transactionDate;
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("customerID " + getCustomerID() + "\n");
		sb.append("transactionAmount " + getTransactionAmount() + "\n");
		sb.append("transactionDate " + getTransactionDate());		
		return sb.toString();
	}
}
