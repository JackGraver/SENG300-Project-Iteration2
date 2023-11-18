package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.InvalidPINException;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import exceptions.OverCreditException;
import models.CreditCard;
import powerutility.PowerGrid;

public class StartSession {
	
	private PayViaSwipeCreditBronze cc;
//	private AbstractSelfCheckoutStation selfCheckoutStation;
//	// Precondition: System is not in a session
//	private boolean inSession = false;
//
//	public StartSession(AbstractSelfCheckoutStation station) {
//		selfCheckoutStation = station;
//	}
//
//	/**
//	 * Starts session
//	 * 
//	 * @param powergrid
//	 * @return void
//	 * @throws InsufficientFundsException 
//	 * @throws InvalidPINException 
//	 * @throws IOException 
//	 */
//	public void startSession(PowerGrid powerGrid) throws InvalidPINException, IOException {
//		selfCheckoutStation.plugIn(powerGrid); // Plug in the devices to the power grid
//
//		selfCheckoutStation.turnOn(); // Turn on the devices
//
//		inSession = true;
//
//		
//	}
//	
//	public boolean isInSession() {
//		return inSession;
//	} 
	
	public static void main(String[] args) throws IOException, OverCreditException {
		/**
		 * This is creating a test card within the CardIssuer
		 */
		long maxHolds = (long) 1000000;
		CardIssuer cardIssuer = new CardIssuer("MasterCard", maxHolds);
		Calendar expiryDate = Calendar.getInstance();
        // Set the date to January 1, 2028
		expiryDate.set(Calendar.YEAR, 2028);
		expiryDate.set(Calendar.MONTH, Calendar.JANUARY);
		expiryDate.set(Calendar.DAY_OF_MONTH, 1);
		double creditLimit = 2500;
		BigDecimal creditLimitInBigDecimal = new BigDecimal(creditLimit);
		cardIssuer.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);
		
		
		Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
		CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, "Bronze", maxHolds);
		BigDecimal totalCostOfGroceries = new BigDecimal(20.00);
		
		/**
		 * Customer: Swipes a credit Card. The sysem will try to 
		 */
		
		
	}
	
	
}