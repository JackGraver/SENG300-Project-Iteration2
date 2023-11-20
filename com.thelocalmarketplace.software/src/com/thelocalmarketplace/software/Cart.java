/*
 * Md Abu Sinan 
 * Jack Graver
Christopher Thomson
Shaim Momin
Raja Muhammed Omar
Michael Hoang
Fei Ding
Dylan Dizon
Shenuk Perera
Darpal Patel

Iteration 2 SENG 300 , Md Abu Sinan worked on this part for the Remove Item use case
*/





package com.thelocalmarketplace.software;
import powerutility.PowerGrid;
import java.util.HashMap;
import java.util.Map;
import com.thelocalmarketplace.software.addItem.*;// for start session logic 
import com.thelocalmarketplace.hardware.*;//  product
import com.thelocalmarketplace.hardware.external.*;// product database ; 


public class Cart {
	long totalCost; // creating an instance
	private Map <Product, Integer> products; // creating a hashmap for individual products and its quantity
	public int currentQuantity =0;
	public StartSession startSession; // for session logic
	public SelfCheckoutStationGold selfCheckoutStation;// to check if selfcheckout sation is on or not
	
	public PowerGrid powerGrid;// to check if selfcheckoutStation is on or not 
	public static boolean isBlocked = false; // session is started and is not blocked 
	
	
	public Cart(SelfCheckoutStationGold ss, PowerGrid pg, StartSession startSess) {
		this.selfCheckoutStation = ss;
		this.powerGrid = pg;
		this.startSession = startSess;
		
		this.products = new HashMap <>();
		this.totalCost = 0; // initialized the total cost
	}
	
	public void addItem(Product product, int quantity) {
		if(product == null && quantity == 0) {
			System.out.println("Scan a product / product quantity cannot be 0"); // signals to the customer that the product is null 
			return;
		}
		else if (quantity <= 0) {
			System.out.println("Product quantity cannot be 0 or negative");//signals to the customer that the product quantity cannot be negative
			return;
		}
		else {
			products.put(product,quantity);
			totalCost += product.getPrice()*quantity;
		}
	}
	
	
	public void removeItem(Product product, int quantity) {
		if(product != null && products.containsKey(product)&& quantity >0) 
			 currentQuantity = products.get(product);
			
			int newQuantity = currentQuantity - quantity;
			
			if(newQuantity > 0) {
			products.put(product, newQuantity);
		}else {
			products.remove(product);
			
		}
		
		totalCost -= product.getPrice()*quantity;
		// totalCost does not fall below 0 
		
		if(totalCost<0) {
			totalCost = 0; 
		}
		
		
	}
	
	
}
