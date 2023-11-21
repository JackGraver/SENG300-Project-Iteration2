package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.card.Card;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import models.CreditCard;
import powerutility.PowerGrid;

import com.jjjwelectronics.card.CardReaderBronze;
import com.jjjwelectronics.card.CardReaderGold;
import com.jjjwelectronics.card.CardReaderSilver;
import com.jjjwelectronics.card.MagneticStripeFailureException;

import controllers.PayViaSwipeCreditBronze;
import controllers.PayViaSwipeCreditGold;
import controllers.PayViaSwipeCreditSilver;
import exceptions.HoldNotAcceptedException;
import exceptions.OverCreditException;
import exceptions.PriceIsZeroOrNegativeException;
/**
	Jack Graver - 10187274
	Christopher Thomson - 30186596
	Shaim Momin - 30184418
	Raja Muhammed Omar - 30159575
	Michael Hoang - 30123605
	Fei Ding - 30225995
	Dylan Dizon - 30173525
	Shenuk Perera - 30086618
	Darpal Patel - 30088795
	Md Abu Sinan - 30154627
 */
public class PayViaCreditCardTest {

	private CardReaderBronze cardReaderBronze = new CardReaderBronze();
	private CardReaderSilver cardReaderSilver = new CardReaderSilver();
	private CardReaderGold cardReaderGold = new CardReaderGold();
	private SelfCheckoutStationBronze bronzeStation;
	private SelfCheckoutStationSilver silverStation;
	private SelfCheckoutStationGold goldStation;

	
	@Before
	public void setUp() {
		PowerGrid.engageUninterruptiblePowerSource();
		PowerGrid.instance().forcePowerRestore();

		Currency currency = Currency.getInstance(Locale.CANADA);
		BigDecimal[] denominations = { new BigDecimal(0.05), new BigDecimal(0.10), new BigDecimal(0.25),
				new BigDecimal(1.00), new BigDecimal(2.00), new BigDecimal(5.00), new BigDecimal(10.00),
				new BigDecimal(20.00), new BigDecimal(50.00), new BigDecimal(100.00) };
		
		BigDecimal[] banknoteDenominationsConfiguration = {new BigDecimal("5"),
	            new BigDecimal("10"), new BigDecimal("20"),
	            new BigDecimal("50"),new BigDecimal("100")};
	    BigDecimal[] coinDenominationsConfigurationArray = {new BigDecimal("2"),
	            new BigDecimal("1"), new BigDecimal("0.25"),
	            new BigDecimal("0.1"),new BigDecimal("0.05")}; 
	    
	    
	    SelfCheckoutStationGold.configureCurrency(Currency.getInstance("CAD"));
	    SelfCheckoutStationGold.configureBanknoteDenominations(banknoteDenominationsConfiguration);
	    SelfCheckoutStationGold.configureBanknoteStorageUnitCapacity(7);
	    SelfCheckoutStationGold.configureCoinDenominations(coinDenominationsConfigurationArray);
	    SelfCheckoutStationGold.configureCoinDispenserCapacity(5);
	    SelfCheckoutStationGold.configureCoinTrayCapacity(5);
	    SelfCheckoutStationGold.configureScaleMaximumWeight(5);
	    SelfCheckoutStationGold.configureReusableBagDispenserCapacity(6);
	    SelfCheckoutStationGold.configureScaleSensitivity(9);
	    SelfCheckoutStationGold.configureCoinStorageUnitCapacity(5);
	    
	    goldStation = new SelfCheckoutStationGold();
	    goldStation.plugIn(PowerGrid.instance());
	    goldStation.turnOn();
	    
	    SelfCheckoutStationSilver.configureCurrency(Currency.getInstance("CAD"));
	    SelfCheckoutStationSilver.configureBanknoteDenominations(banknoteDenominationsConfiguration);
	    SelfCheckoutStationSilver.configureBanknoteStorageUnitCapacity(7);
	    SelfCheckoutStationSilver.configureCoinDenominations(coinDenominationsConfigurationArray);
	    SelfCheckoutStationSilver.configureCoinDispenserCapacity(5);
	    SelfCheckoutStationSilver.configureCoinTrayCapacity(5);
	    SelfCheckoutStationSilver.configureScaleMaximumWeight(5);
	    SelfCheckoutStationSilver.configureReusableBagDispenserCapacity(6);
	    SelfCheckoutStationSilver.configureScaleSensitivity(9);
	    SelfCheckoutStationSilver.configureCoinStorageUnitCapacity(5);
	    
	    silverStation = new SelfCheckoutStationSilver();
	    silverStation.plugIn(PowerGrid.instance());
	    silverStation.turnOn();
	    
	    SelfCheckoutStationBronze.configureCurrency(Currency.getInstance("CAD"));
	    SelfCheckoutStationBronze.configureBanknoteDenominations(banknoteDenominationsConfiguration);
	    SelfCheckoutStationBronze.configureBanknoteStorageUnitCapacity(7);
	    SelfCheckoutStationBronze.configureCoinDenominations(coinDenominationsConfigurationArray);
	    SelfCheckoutStationBronze.configureCoinDispenserCapacity(5);
	    SelfCheckoutStationBronze.configureCoinTrayCapacity(5);
	    SelfCheckoutStationBronze.configureScaleMaximumWeight(5);
	    SelfCheckoutStationBronze.configureReusableBagDispenserCapacity(6);
	    SelfCheckoutStationBronze.configureScaleSensitivity(9);
	    SelfCheckoutStationBronze.configureCoinStorageUnitCapacity(5);
	    
	    bronzeStation = new SelfCheckoutStationBronze();
	    bronzeStation.plugIn(PowerGrid.instance());
	    bronzeStation.turnOn();
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
	 * @throws IOException                    is the exception for the CreditCard
	 * @throws OverCreditException            is exception for the
	 *                                        PayViaCreditCardViaSwipe
	 * @throws HoldNotAcceptedException       is exception for the hold not being
	 *                                        accepted
	 * @throws PriceIsZeroOrNegativeException
	 */
	@Test
	public void bronzeTest1()
			throws IOException, OverCreditException, HoldNotAcceptedException, PriceIsZeroOrNegativeException {

		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * bronze card reader. Might throw an exception which will be caught.
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
			BigDecimal totalCostOfGroceries = new BigDecimal(20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditBronze payViaCredit = new PayViaSwipeCreditBronze(creditCard, totalCostOfGroceries, bronzeStation);
			Assert.assertTrue(payViaCredit.creditCardDataRead);
			Assert.assertTrue(payViaCredit.creditCardSwiped);
			
		} catch (MagneticStripeFailureException | HoldNotAcceptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On bronze card reader, a credit card with
	 * valid data, but creditLimit is below the total cost of groceries, and with
	 * all listeners as well.
	 * 
	 * @throws IOException                    is the exception for the CreditCard
	 * @throws OverCreditException            is the OverCredit Exception for the
	 *                                        usage of a on over the limit credit
	 *                                        card
	 * @throws HoldNotAcceptedException
	 * @throws PriceIsZeroOrNegativeException
	 */
	@Test
	public void bronzeTest2()
			throws IOException, OverCreditException, HoldNotAcceptedException, PriceIsZeroOrNegativeException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * bronze card reader. Might throw an exception which will be caught.
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
			double creditLimit = 10;
			BigDecimal creditLimitInBigDecimal = new BigDecimal(creditLimit);
			BigDecimal totalCostOfGroceries = new BigDecimal(20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditBronze payViaCredit = new PayViaSwipeCreditBronze(creditCard, totalCostOfGroceries, bronzeStation);

		} catch (MagneticStripeFailureException | OverCreditException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On bronze card reader, a credit card with
	 * valid data, and with no listeners
	 * 
	 * @throws IOException                    is the exception for the CreditCard
	 * @throws HoldNotAcceptedException       is exception for the hold not being
	 *                                        accepted
	 * @throws PriceIsZeroOrNegativeException
	 * @throws OverCreditException
	 */
	@Test
	public void bronzeTest3()
			throws IOException, HoldNotAcceptedException, PriceIsZeroOrNegativeException, OverCreditException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * bronze card reader. Might throw an exception which will be caught.
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
			double creditLimit = 1000;
			BigDecimal creditLimitInBigDecimal = new BigDecimal(creditLimit);
			BigDecimal totalCostOfGroceries = new BigDecimal(20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditBronze payViaCredit = new PayViaSwipeCreditBronze(creditCard, totalCostOfGroceries, bronzeStation);
			cardReaderBronze.deregisterAll();

		} catch (MagneticStripeFailureException | HoldNotAcceptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On bronze card reader, a credit card with
	 * negative total cost of groceries value, and with listeners
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
			BigDecimal totalCostOfGroceries = new BigDecimal(-20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditBronze payViaCredit = new PayViaSwipeCreditBronze(creditCard, totalCostOfGroceries, bronzeStation);

		} catch (PriceIsZeroOrNegativeException e) {
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
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

		} catch (InvalidArgumentSimulationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On bronze card reader, a credit card with a
	 * zero dollars total cost of groceries value, and with listeners
	 * 
	 * @throws IOException              is the exception for the CreditCard
	 * @throws OverCreditException      is the exception for usage of the credit
	 *                                  card without enough credit
	 * @throws HoldNotAcceptedException
	 */
	@Test
	public void bronzeTest6() throws IOException, OverCreditException, HoldNotAcceptedException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * bronze card reader. Might throw an exception which will be caught.
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
			double creditLimit = 1000;
			BigDecimal creditLimitInBigDecimal = new BigDecimal(creditLimit);
			BigDecimal totalCostOfGroceries = BigDecimal.ZERO;
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);
			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditBronze payViaCredit = new PayViaSwipeCreditBronze(creditCard, totalCostOfGroceries, bronzeStation);

		} catch (PriceIsZeroOrNegativeException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This test case involves a Powered On bronze card reader, a credit card with
	 * negative max hold, and with listeners
	 * 
	 * @throws IOException              is the exception for the CreditCard
	 * @throws OverCreditException      is the exception for usage of the credit
	 *                                  card without enough credit
	 * @throws HoldNotAcceptedException
	 */
	@Test
	public void bronzeTest7() throws IOException, OverCreditException, HoldNotAcceptedException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * bronze card reader. Might throw an exception which will be caught.
		 */
		try {

			/**
			 * Setting up Card Data
			 */
			long maxHolds = (long) -10000;
			CardIssuer bank = new CardIssuer("MasterCard", maxHolds);

		} catch (InvalidArgumentSimulationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On Silver card reader, a credit card with
	 * valid data, and with all listeners as well.
	 * 
	 * @throws IOException                    is the exception for the CreditCard
	 * @throws OverCreditException            is exception for the
	 *                                        PayViaCreditCardViaSwipe
	 * @throws HoldNotAcceptedException       is exception for the hold not being
	 *                                        accepted
	 * @throws PriceIsZeroOrNegativeException
	 */
	@Test
	public void silverTest1()
			throws IOException, OverCreditException, HoldNotAcceptedException, PriceIsZeroOrNegativeException {

		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * Silver card reader. Might throw an exception which will be caught.
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
			BigDecimal totalCostOfGroceries = new BigDecimal(20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditSilver payViaCredit = new PayViaSwipeCreditSilver(creditCard, totalCostOfGroceries, silverStation);
			Assert.assertTrue(payViaCredit.creditCardDataRead);
			Assert.assertTrue(payViaCredit.creditCardSwiped);
			
		} catch (MagneticStripeFailureException | HoldNotAcceptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On Silver card reader, a credit card with
	 * valid data, but creditLimit is below the total cost of groceries, and with
	 * all listeners as well.
	 * 
	 * @throws IOException                    is the exception for the CreditCard
	 * @throws OverCreditException            is the OverCredit Exception for the
	 *                                        usage of a on over the limit credit
	 *                                        card
	 * @throws HoldNotAcceptedException
	 * @throws PriceIsZeroOrNegativeException
	 */
	@Test
	public void silverTest2()
			throws IOException, OverCreditException, HoldNotAcceptedException, PriceIsZeroOrNegativeException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * Silver card reader. Might throw an exception which will be caught.
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
			double creditLimit = 10;
			BigDecimal creditLimitInBigDecimal = new BigDecimal(creditLimit);
			BigDecimal totalCostOfGroceries = new BigDecimal(20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditSilver payViaCredit = new PayViaSwipeCreditSilver(creditCard, totalCostOfGroceries, silverStation);

		} catch (MagneticStripeFailureException | OverCreditException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On Silver card reader, a credit card with
	 * valid data, and with no listeners
	 * 
	 * @throws IOException                    is the exception for the CreditCard
	 * @throws HoldNotAcceptedException       is exception for the hold not being
	 *                                        accepted
	 * @throws PriceIsZeroOrNegativeException
	 * @throws OverCreditException
	 */
	@Test
	public void silverTest3()
			throws IOException, HoldNotAcceptedException, PriceIsZeroOrNegativeException, OverCreditException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * Silver card reader. Might throw an exception which will be caught.
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
			double creditLimit = 1000;
			BigDecimal creditLimitInBigDecimal = new BigDecimal(creditLimit);
			BigDecimal totalCostOfGroceries = new BigDecimal(20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditSilver payViaCredit = new PayViaSwipeCreditSilver(creditCard, totalCostOfGroceries, silverStation);
			cardReaderSilver.deregisterAll();

		} catch (MagneticStripeFailureException | HoldNotAcceptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On Silver card reader, a credit card with
	 * negative total cost of groceries value, and with listeners
	 * 
	 * @throws IOException              is the exception for the CreditCard
	 * @throws OverCreditException      is the exception for usage of the credit
	 *                                  card without enough credit
	 * @throws HoldNotAcceptedException
	 */
	@Test
	public void silverTest4() throws IOException, OverCreditException, HoldNotAcceptedException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * Silver card reader. Might throw an exception which will be caught.
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
			BigDecimal totalCostOfGroceries = new BigDecimal(-20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditSilver payViaCredit = new PayViaSwipeCreditSilver(creditCard, totalCostOfGroceries, silverStation);

		} catch (PriceIsZeroOrNegativeException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This test case involves a Powered On Silver card reader, a credit card with
	 * negative credit limit, and with listeners
	 * 
	 * @throws IOException              is the exception for the CreditCard
	 * @throws OverCreditException      is the exception for usage of the credit
	 *                                  card without enough credit
	 * @throws HoldNotAcceptedException
	 */
	@Test
	public void silverTest5() throws IOException, OverCreditException, HoldNotAcceptedException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * Silver card reader. Might throw an exception which will be caught.
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
			double creditLimit = -1000;
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

		} catch (InvalidArgumentSimulationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On Silver card reader, a credit card with a
	 * zero dollars total cost of groceries value, and with listeners
	 * 
	 * @throws IOException              is the exception for the CreditCard
	 * @throws OverCreditException      is the exception for usage of the credit
	 *                                  card without enough credit
	 * @throws HoldNotAcceptedException
	 */
	@Test
	public void silverTest6() throws IOException, OverCreditException, HoldNotAcceptedException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * Silver card reader. Might throw an exception which will be caught.
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
			double creditLimit = 1000;
			BigDecimal creditLimitInBigDecimal = new BigDecimal(creditLimit);
			BigDecimal totalCostOfGroceries = BigDecimal.ZERO;
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);
			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditSilver payViaCredit = new PayViaSwipeCreditSilver(creditCard, totalCostOfGroceries, silverStation);

		} catch (PriceIsZeroOrNegativeException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This test case involves a Powered On Silver card reader, a credit card with
	 * negative max hold, and with listeners
	 * 
	 * @throws IOException              is the exception for the CreditCard
	 * @throws OverCreditException      is the exception for usage of the credit
	 *                                  card without enough credit
	 * @throws HoldNotAcceptedException
	 */
	@Test
	public void silverTest7() throws IOException, OverCreditException, HoldNotAcceptedException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * Silver card reader. Might throw an exception which will be caught.
		 */
		try {

			/**
			 * Setting up Card Data
			 */
			long maxHolds = (long) -10000;
			CardIssuer bank = new CardIssuer("MasterCard", maxHolds);

		} catch (InvalidArgumentSimulationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On Gold card reader, a credit card with
	 * valid data, and with all listeners as well.
	 * 
	 * @throws IOException                    is the exception for the CreditCard
	 * @throws OverCreditException            is exception for the
	 *                                        PayViaCreditCardViaSwipe
	 * @throws HoldNotAcceptedException       is exception for the hold not being
	 *                                        accepted
	 * @throws PriceIsZeroOrNegativeException
	 */
	@Test
	public void goldTest1()
			throws IOException, OverCreditException, HoldNotAcceptedException, PriceIsZeroOrNegativeException {

		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * Gold card reader. Might throw an exception which will be caught.
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
			BigDecimal totalCostOfGroceries = new BigDecimal(20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditGold payViaCredit = new PayViaSwipeCreditGold(creditCard, totalCostOfGroceries, goldStation);
			Assert.assertTrue(payViaCredit.creditCardDataRead);
			Assert.assertTrue(payViaCredit.creditCardSwiped);

		} catch (MagneticStripeFailureException | HoldNotAcceptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On Gold card reader, a credit card with
	 * valid data, but creditLimit is below the total cost of groceries, and with
	 * all listeners as well.
	 * 
	 * @throws IOException                    is the exception for the CreditCard
	 * @throws OverCreditException            is the OverCredit Exception for the
	 *                                        usage of a on over the limit credit
	 *                                        card
	 * @throws HoldNotAcceptedException
	 * @throws PriceIsZeroOrNegativeException
	 */
	@Test
	public void goldTest2()
			throws IOException, OverCreditException, HoldNotAcceptedException, PriceIsZeroOrNegativeException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * Gold card reader. Might throw an exception which will be caught.
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
			double creditLimit = 10;
			BigDecimal creditLimitInBigDecimal = new BigDecimal(creditLimit);
			BigDecimal totalCostOfGroceries = new BigDecimal(20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditGold payViaCredit = new PayViaSwipeCreditGold(creditCard, totalCostOfGroceries, goldStation);
			
		} catch (MagneticStripeFailureException | OverCreditException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On Gold card reader, a credit card with
	 * valid data, and with no listeners
	 * 
	 * @throws IOException                    is the exception for the CreditCard
	 * @throws HoldNotAcceptedException       is exception for the hold not being
	 *                                        accepted
	 * @throws PriceIsZeroOrNegativeException
	 * @throws OverCreditException
	 */
	@Test
	public void goldTest3()
			throws IOException, HoldNotAcceptedException, PriceIsZeroOrNegativeException, OverCreditException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * Gold card reader. Might throw an exception which will be caught.
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
			double creditLimit = 1000;
			BigDecimal creditLimitInBigDecimal = new BigDecimal(creditLimit);
			BigDecimal totalCostOfGroceries = new BigDecimal(20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditGold payViaCredit = new PayViaSwipeCreditGold(creditCard, totalCostOfGroceries, goldStation);
			payViaCredit.cardReaderGold.deregisterAll();
	
		} catch (MagneticStripeFailureException | HoldNotAcceptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On Gold card reader, a credit card with
	 * negative total cost of groceries value, and with listeners
	 * 
	 * @throws IOException              is the exception for the CreditCard
	 * @throws OverCreditException      is the exception for usage of the credit
	 *                                  card without enough credit
	 * @throws HoldNotAcceptedException
	 */
	@Test
	public void goldTest4() throws IOException, OverCreditException, HoldNotAcceptedException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * Gold card reader. Might throw an exception which will be caught.
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
			BigDecimal totalCostOfGroceries = new BigDecimal(-20.00);
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditGold payViaCredit = new PayViaSwipeCreditGold(creditCard, totalCostOfGroceries, goldStation);
			
		} catch (PriceIsZeroOrNegativeException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This test case involves a Powered On Gold card reader, a credit card with
	 * negative credit limit, and with listeners
	 * 
	 * @throws IOException              is the exception for the CreditCard
	 * @throws OverCreditException      is the exception for usage of the credit
	 *                                  card without enough credit
	 * @throws HoldNotAcceptedException
	 */
	@Test
	public void goldTest5() throws IOException, OverCreditException, HoldNotAcceptedException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * Gold card reader. Might throw an exception which will be caught.
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
			double creditLimit = -1000;
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);

		} catch (InvalidArgumentSimulationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This test case involves a Powered On Gold card reader, a credit card with a
	 * zero dollars total cost of groceries value, and with listeners
	 * 
	 * @throws IOException              is the exception for the CreditCard
	 * @throws OverCreditException      is the exception for usage of the credit
	 *                                  card without enough credit
	 * @throws HoldNotAcceptedException
	 */
	@Test
	public void goldTest6() throws IOException, OverCreditException, HoldNotAcceptedException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * Gold card reader. Might throw an exception which will be caught.
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
			double creditLimit = 1000;
			BigDecimal creditLimitInBigDecimal = new BigDecimal(creditLimit);
			BigDecimal totalCostOfGroceries = BigDecimal.ZERO;
			bank.addCardData("1234567890123456", "Dylan", expiryDate, "099", creditLimit);
			/**
			 * Swiping the credit card
			 */
			Card card = new Card("Mastercard", "1234567890123456", "Dylan", "099");
			CreditCard creditCard = new CreditCard(card, creditLimitInBigDecimal, maxHolds, bank);
			PayViaSwipeCreditGold payViaCredit = new PayViaSwipeCreditGold(creditCard, totalCostOfGroceries, goldStation);
		} catch (PriceIsZeroOrNegativeException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This test case involves a Powered On Gold card reader, a credit card with
	 * negative max hold, and with listeners
	 * 
	 * @throws IOException              is the exception for the CreditCard
	 * @throws OverCreditException      is the exception for usage of the credit
	 *                                  card without enough credit
	 * @throws HoldNotAcceptedException
	 */
	@Test
	public void goldTest7() throws IOException, OverCreditException, HoldNotAcceptedException {
		/**
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 *
		 * Customer: Swipes a credit Card. The system will try to send the data with the
		 * Gold card reader. Might throw an exception which will be caught.
		 */
		try {

			/**
			 * Setting up Card Data
			 */
			long maxHolds = (long) -10000;
			CardIssuer bank = new CardIssuer("MasterCard", maxHolds);

		} catch (InvalidArgumentSimulationException e) {
			e.printStackTrace();
		}
	}

}
