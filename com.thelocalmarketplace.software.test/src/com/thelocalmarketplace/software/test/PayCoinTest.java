package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.tdc.coin.Coin;
import com.tdc.coin.CoinValidator;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.PaymentCoinController;

import powerutility.PowerGrid;

public class PayCoinTest {
	public SelfCheckoutStationGold selfCheckoutStation;
	public PaymentCoinController paymentCoinController;
	public PowerGrid powerGrid;
	public Coin coin;
	public BigDecimal amountDue;
	public CoinValidator coinValidator;
	public Currency cad; 
	public List<BigDecimal> coinDenomination;
	@Before
	public void Setup() {
		selfCheckoutStation = new SelfCheckoutStationGold();
		powerGrid = PowerGrid.instance();
		PowerGrid.engageUninterruptiblePowerSource();//makes it so powergrid always works
		cad = Currency.getInstance("CAD");
		coinDenomination = new ArrayList<>();
		coinDenomination.add(new BigDecimal("0.25"));// could add more
		coinValidator = new CoinValidator(cad, coinDenomination);	
	}
	
	
	@Test
	public void PaymentNotComplete() {
		amountDue = new BigDecimal("0.75");
		BigDecimal quarter = new BigDecimal("0.25");
		coin = new Coin(cad, quarter);
		paymentCoinController = new PaymentCoinController(selfCheckoutStation, powerGrid, coin, amountDue);
		paymentCoinController.validCoinDetected(coinValidator, coin.getValue());
		paymentCoinController.validCoinDetected(coinValidator, coin.getValue());
		assertTrue(new BigDecimal("0.25").compareTo(paymentCoinController.getAmount()) == 0);//the result should be 0.25. thus when comparing 0.25 to 0.25, it should be 0
	}
	
	@Test
	public void PaymentComplete() {
		amountDue = new BigDecimal("0.75");
		BigDecimal quarter = new BigDecimal("0.25");
		coin = new Coin(cad, quarter);
		paymentCoinController = new PaymentCoinController(selfCheckoutStation, powerGrid, coin, amountDue);
		paymentCoinController.validCoinDetected(coinValidator, coin.getValue());
		paymentCoinController.validCoinDetected(coinValidator, coin.getValue());
		paymentCoinController.validCoinDetected(coinValidator, coin.getValue());
		paymentCoinController.validCoinDetected(coinValidator, coin.getValue());
		BigDecimal expectedPayment = new BigDecimal("-0.25");
		assertEquals(0, paymentCoinController.getAmount().compareTo(expectedPayment));
	}
	
	
	@Test
	public void weightDiscrepancyDetectedPay() {
		amountDue = new BigDecimal("0.75");
		BigDecimal initalAmountDue = new BigDecimal("0.75");
		BigDecimal quarter = new BigDecimal("0.25");
		coin = new Coin(cad, quarter);
		paymentCoinController = new PaymentCoinController(selfCheckoutStation, powerGrid, coin, amountDue);
		paymentCoinController.updateWeightDiscrepancy(12, 1);
		paymentCoinController.validCoinDetected(coinValidator, coin.getValue());
		paymentCoinController.validCoinDetected(coinValidator, coin.getValue());
		assertTrue(initalAmountDue.compareTo(paymentCoinController.getAmount()) == 0); //when comparing intial amount due to the amount after inserting coins,the difference should be 0. because cop
		//since there is a weight discrepancy. you should not be able to pay
	}
	
	@Test
	public void NotweightDiscrepancyDetectedPay() {
		amountDue = new BigDecimal("0.75");
		BigDecimal quarter = new BigDecimal("0.25");
		coin = new Coin(cad, quarter);
		paymentCoinController = new PaymentCoinController(selfCheckoutStation, powerGrid, coin, amountDue);
		paymentCoinController.updateWeightDiscrepancy(12, 12);
		paymentCoinController.validCoinDetected(coinValidator, coin.getValue());
		paymentCoinController.validCoinDetected(coinValidator, coin.getValue());
		BigDecimal expectedPayment = new BigDecimal("0.25");
		assertTrue(expectedPayment.compareTo(paymentCoinController.getAmount()) == 0); //when comparing expectedamountdue to the amount after inserting coins,the difference should be 0. because cop
		//since there is a weight discrepancy. you should not be able to pay
	}
}
