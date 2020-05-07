package com.example.springboot;

public class Customer {
	
	public Customer(int customerID) {
		this.customerID = customerID;
	}
	
	private int customerID;
	public int getCustomerID() {
		return customerID;
	}
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	private int month1Points;
	public int getMonth1Points() {
		return month1Points;
	}
	public void setMonth1Points(int month1Points) {
		this.month1Points = month1Points;
	}
	private int month2Points;
	public int getMonth2Points() {
		return month2Points;
	}
	public void setMonth2Points(int month2Points) {
		this.month2Points = month2Points;
	}
	private int month3Points;
	public int getMonth3Points() {
		return month3Points;
	}
	public void setMonth3Points(int month3Points) {
		this.month3Points = month3Points;
	}
	private int totalPoints;
	public int getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
	
}
