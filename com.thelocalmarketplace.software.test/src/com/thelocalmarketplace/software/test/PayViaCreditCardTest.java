package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import models.CreditCard;
import observers.CreditCardObserver;
import powerutility.PowerGrid;

import com.jjjwelectronics.card.CardReaderBronze;
import com.jjjwelectronics.card.CardReaderGold;
import com.jjjwelectronics.card.CardReaderListener;
import com.jjjwelectronics.card.CardReaderSilver;
import com.jjjwelectronics.card.MagneticStripeFailureException;

import controllers.PayViaSwipeCreditBronze;
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
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();
	}

	@Test
	public void testPlugInAndUnplug() {
		assertEquals(false, cardReaderBronze.isPluggedIn());
		assertEquals(false, cardReaderBronze.isPoweredUp());

		cardReaderBronze.plugIn(PowerGrid.instance());
		assertEquals(true, cardReaderBronze.isPluggedIn());
		assertEquals(false, cardReaderBronze.isPoweredUp());

		cardReaderBronze.unplug();
		assertEquals(false, cardReaderBronze.isPluggedIn());
		assertEquals(false, cardReaderBronze.isPoweredUp());

		assertEquals(false, cardReaderSilver.isPluggedIn());
		assertEquals(false, cardReaderSilver.isPoweredUp());

		cardReaderSilver.plugIn(PowerGrid.instance());
		assertEquals(true, cardReaderSilver.isPluggedIn());
		assertEquals(false, cardReaderSilver.isPoweredUp());

		cardReaderSilver.unplug();
		assertEquals(false, cardReaderSilver.isPluggedIn());
		assertEquals(false, cardReaderSilver.isPoweredUp());

		assertEquals(false, cardReaderGold.isPluggedIn());
		assertEquals(false, cardReaderGold.isPoweredUp());

		cardReaderGold.plugIn(PowerGrid.instance());
		assertEquals(true, cardReaderGold.isPluggedIn());
		assertEquals(false, cardReaderGold.isPoweredUp());

		cardReaderGold.unplug();
		assertEquals(false, cardReaderGold.isPluggedIn());
		assertEquals(false, cardReaderGold.isPoweredUp());
	}

	/**
	 * This test case involves a Powered On bronze card reader, a credit card with
	 * valid data, and with all listeners as well.
	 * 
	 * @throws IOException              is the exception for the CreditCard
	 * @throws OverCreditException      is exception for the
	 *                                  PayViaCreditCardViaSwipe
	 * @throws HoldNotAcceptedException is exception for the hold not being accepted
	 */
	@Test
	public void bronzeTest1() throws IOException, OverCreditException, HoldNotAcceptedException {

		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * bronze card reader. Might throw an exception which will be caught.
		 */
		try {

			/**
			 * Registering listeners
			 */
			cardReaderBronze.register(new CreditCardObserver() {

				@Override
				public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aCardHasBeenSwiped() {
				}

				@Override
				public void theDataFromACardHasBeenRead(CardData data) {
				}

			});

			/**
			 * Turning on the bronze card reader
			 */
			cardReaderBronze.plugIn(PowerGrid.instance());
			cardReaderBronze.turnOn();
			cardReaderBronze.enable();

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
			BigDecimal totalCostOfGroceries = new BigDecimal(20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			this.cardDataBronze = this.cardReaderBronze.swipe(card);
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditBronze payViaCredit = new PayViaSwipeCreditBronze(creditCard, this.cardDataBronze,
					totalCostOfGroceries, creditLimitInBigDecimal);
			Assert.assertTrue(cardReaderBronze.listeners().contains(new CreditCardObserver() {

				@Override
				public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aCardHasBeenSwiped() {
				}

				@Override
				public void theDataFromACardHasBeenRead(CardData data) {
				}

			}));
//			boolean holdReleased = bank.releaseHold(cardDataBronze.getNumber(), creditCard.getMaxHolds());
//			long holdAuthorized = bank.authorizeHold(cardDataBronze.getNumber(), totalCostOfGroceries.doubleValue());
//			boolean holdAcceptedAndSent = (holdReleased == true && holdAuthorized > 0);

		} catch (MagneticStripeFailureException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On bronze card reader, a credit card with
	 * valid data, but creditLimit is below the total cost of groceries, and with
	 * all listeners as well.
	 * 
	 * @throws IOException         is the exception for the CreditCard
	 * @throws OverCreditException is the OverCredit Exception for the usage of a on
	 *                             over the limit credit card
	 */
	@Test
	public void bronzeTest2() throws IOException, OverCreditException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * bronze card reader. Might throw an exception which will be caught.
		 */
		try {

			/**
			 * Registering listeners
			 */
			cardReaderBronze.register(new CreditCardObserver() {

				@Override
				public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aCardHasBeenSwiped() {
				}

				@Override
				public void theDataFromACardHasBeenRead(CardData data) {
				}

			});

			/**
			 * Turning on the bronze card reader
			 */
			cardReaderBronze.plugIn(PowerGrid.instance());
			cardReaderBronze.turnOn();
			cardReaderBronze.enable();

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
			double creditLimit = 10;
			BigDecimal creditLimitInBigDecimal = new BigDecimal(creditLimit);
			BigDecimal totalCostOfGroceries = new BigDecimal(20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			this.cardDataBronze = this.cardReaderBronze.swipe(card);
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditBronze payViaCredit = new PayViaSwipeCreditBronze(creditCard, this.cardDataBronze,
					totalCostOfGroceries, creditLimitInBigDecimal);

		} catch (MagneticStripeFailureException | HoldNotAcceptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On bronze card reader, a credit card with
	 * valid data, and with no listeners
	 * 
	 * @throws IOException              is the exception for the CreditCard
	 * @throws HoldNotAcceptedException is exception for the hold not being accepted
	 */
	@Test
	public void bronzeTest3() throws IOException, HoldNotAcceptedException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * bronze card reader. Might throw an exception which will be caught.
		 */
		try {

			/**
			 * Registering listeners and deregistering listeners
			 */
			cardReaderBronze.register(new CreditCardObserver() {

				@Override
				public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aCardHasBeenSwiped() {
				}

				@Override
				public void theDataFromACardHasBeenRead(CardData data) {
				}

			});
			cardReaderBronze.deregisterAll();

			/**
			 * Turning on the bronze card reader
			 */
			cardReaderBronze.plugIn(PowerGrid.instance());
			cardReaderBronze.turnOn();
			cardReaderBronze.enable();

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
			double creditLimit = 1000;
			BigDecimal creditLimitInBigDecimal = new BigDecimal(creditLimit);
			BigDecimal totalCostOfGroceries = new BigDecimal(20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			this.cardDataBronze = this.cardReaderBronze.swipe(card);
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditBronze payViaCredit = new PayViaSwipeCreditBronze(creditCard, this.cardDataBronze,
					totalCostOfGroceries, creditLimitInBigDecimal);

		} catch (MagneticStripeFailureException | OverCreditException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On bronze card reader, a credit card with
	 * null values, and with listeners
	 * 
	 * @throws IOException              is the exception for the CreditCard
	 * @throws OverCreditException      is the exception for usage of the credit
	 *                                  card without enough credit
	 * @throws HoldNotAcceptedException
	 */
	@Test
	public void bronzeTest4() throws IOException, OverCreditException, HoldNotAcceptedException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * bronze card reader. Might throw an exception which will be caught.
		 */
		try {

			/**
			 * Registering listeners and deregistering listeners
			 */
			cardReaderBronze.register(new CreditCardObserver() {

				@Override
				public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aCardHasBeenSwiped() {
				}

				@Override
				public void theDataFromACardHasBeenRead(CardData data) {
				}

			});
		

			/**
			 * Turning on the bronze card reader
			 */
			cardReaderBronze.plugIn(PowerGrid.instance());
			cardReaderBronze.turnOn();
			cardReaderBronze.enable();

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
			double creditLimit = 1000;
			BigDecimal creditLimitInBigDecimal = new BigDecimal(creditLimit);
			BigDecimal totalCostOfGroceries = new BigDecimal(20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			this.cardDataBronze = null;
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditBronze payViaCredit = new PayViaSwipeCreditBronze(creditCard, this.cardDataBronze,
					totalCostOfGroceries, creditLimitInBigDecimal);

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * This test case involves a Powered On bronze card reader, a credit card with
	 * negative credit limit, and with listeners
	 * 
	 * @throws IOException              is the exception for the CreditCard
	 * @throws OverCreditException      is the exception for usage of the credit
	 *                                  card without enough credit
	 * @throws HoldNotAcceptedException
	 */
	@Test
	public void bronzeTest5() throws IOException, OverCreditException, HoldNotAcceptedException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * bronze card reader. Might throw an exception which will be caught.
		 */
		try {

			/**
			 * Registering listeners and deregistering listeners
			 */
			cardReaderBronze.register(new CreditCardObserver() {

				@Override
				public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
				}

				@Override
				public void aCardHasBeenSwiped() {
				}

				@Override
				public void theDataFromACardHasBeenRead(CardData data) {
				}

			});

			/**
			 * Turning on the bronze card reader
			 */
			cardReaderBronze.plugIn(PowerGrid.instance());
			cardReaderBronze.turnOn();
			cardReaderBronze.enable();

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
			double creditLimit = -1000;
			BigDecimal creditLimitInBigDecimal = new BigDecimal(creditLimit);
			BigDecimal totalCostOfGroceries = new BigDecimal(20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

		} catch (InvalidArgumentSimulationException e) {
			e.printStackTrace();
		}
	}

}
