package controllers;

public class CreditCard {
	
	private double balance;
	private String pin; 
	private String companyName;
	private long maxHolds;
	
	public CreditCard(String pin, String companyName, long maxHolds) {
		this.pin = pin;
		this.companyName = companyName;
		this.maxHolds = maxHolds;
	} 
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public double getBalance() {
		return this.balance;
	}
	
	public String getPin() {
		return this.pin;
	}
		
	public String getCompanyName() {
		return this.companyName;
	}

	public long getMaxHolds() {
		return this.maxHolds;
	}
	
}