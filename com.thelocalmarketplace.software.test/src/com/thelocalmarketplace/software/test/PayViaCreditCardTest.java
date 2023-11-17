package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import models.CreditCard;
import powerutility.PowerGrid;

import com.jjjwelectronics.card.CardReaderBronze;
import com.jjjwelectronics.card.CardReaderGold;
import com.jjjwelectronics.card.CardReaderSilver;
import com.jjjwelectronics.card.MagneticStripeFailureException;

import controllers.PayViaCreditCardViaSwipe;
import exceptions.HoldNotAcceptedException;
import exceptions.OverCreditException;

public class PayViaCreditCardTest {

	private CardData cardDataBronze;
	private CardData cardDataSilver;
	private CardData cardDataGold;
	private CardReaderBronze cardReaderBronze = new CardReaderBronze();
	private CardReaderSilver cardReaderSilver = new CardReaderSilver();
	private CardReaderGold cardReaderGold = new CardReaderGold();

	
	@Before
	public void setUp() {
		cardReaderBronze.plugIn(PowerGrid.instance());
		cardReaderSilver.plugIn(PowerGrid.instance());
		cardReaderGold.plugIn(PowerGrid.instance());
		cardReaderBronze.turnOn();
		cardReaderSilver.turnOn();
		cardReaderGold.turnOn();
	}
	
	/**
	 * This test case involves a Powered On bronze card reader, 
	 * @throws IOException is the exception for the CreditCard
	 * @throws OverCreditException is exception for the PayViaCreditCardViaSwipe
	 * @throws HoldNotAcceptedException is exception for the hold not being accepted
	 */
	@Test
	public void test1() throws IOException, OverCreditException, HoldNotAcceptedException {
		
		/**
		 * Bank: Signals to the System the hold number against the account of the credit card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the bronze card reader.
		 * Might throw an exception which will be caught.
		 */ 
		try {	
			/**
			 * Setting up Card Data
			 */
			long maxHolds = (long) 10000;
			CardIssuer bank = new CardIssuer("MasterCard", maxHolds);
			Calendar expiryDate = Calendar.getInstance();
	        // Set the date to January 1, 2028
			expiryDate.set(Calendar.YEAR, 2028);
			expiryDate.set(Calendar.MONTH, Calendar.JANUARY);
			expiryDate.set(Calendar.DAY_OF_MONTH, 1);
			double creditLimit = 2500;
			BigDecimal creditLimitInBigDecimal = new BigDecimal(creditLimit);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);
			
			
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			this.cardDataBronze = this.cardReaderBronze.swipe(card);
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, "Bronze", maxHolds);
			BigDecimal totalCostOfGroceries = new BigDecimal(20.00);
			PayViaCreditCardViaSwipe payViaCredit = new PayViaCreditCardViaSwipe(creditCard, this.cardDataBronze, totalCostOfGroceries, creditLimitInBigDecimal);
			Assert.assertEquals("Bronze", creditCard.getCreditCardChip());
			Assert.assertEquals(payViaCredit.creditCardSwiped, true);
			Assert.assertEquals(payViaCredit.creditCardDataRead, true);
		} catch (MagneticStripeFailureException e) {
			e.printStackTrace();
		}
	}

}
