package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.card.Card;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.CardIssuer;
import com.thelocalmarketplace.software.printing.ReceiptPrinterController;

import powerutility.PowerGrid;
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
public class ReceiptPrinterControllerTest {
	
        SelfCheckoutStationGold stationGold;
	SelfCheckoutStationSilver stationSilver;
	SelfCheckoutStationBronze stationBronze;
	
	CardIssuer cardIssuerCIBC = new CardIssuer("CIBC", 1);
	CardIssuer cardIssuerRBC = new CardIssuer("RBC", 5);
	
	Card debitCardHighFund = new Card("Debit", "123456", "John Doe", "123");
	Card debitCardLowFund = new Card("Debit", "000000", "Jane Doe", "100");
	Card notDebitCard = new Card("Credit", "101010", "Jon Doe", "456");
	
    @Before
    public void setUp() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, 2026);
        calendar.set(Calendar.MONTH, Calendar.NOVEMBER); // Note: Months are 0-based, so November is 10
        
        //Debit Cards
        cardIssuerCIBC.addCardData(debitCardHighFund.number, debitCardHighFund.cardholder, calendar, debitCardHighFund.cvv, 1000);
        cardIssuerCIBC.addCardData(debitCardLowFund.number, debitCardLowFund.cardholder, calendar, debitCardLowFund.cvv, 10);
        
        
        cardIssuerRBC.addCardData(debitCardHighFund.number, debitCardHighFund.cardholder, calendar, debitCardHighFund.cvv, 1000);
        cardIssuerRBC.addCardData(debitCardLowFund.number, debitCardLowFund.cardholder, calendar, debitCardLowFund.cvv, 10);
        
        //Credit Cards
        cardIssuerRBC.addCardData(notDebitCard.number, notDebitCard.cardholder, calendar, notDebitCard.cvv, 999);
        
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
        
        stationGold = new SelfCheckoutStationGold();
        stationGold.plugIn(PowerGrid.instance());
        stationGold.turnOn();
        
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
        
        stationSilver = new SelfCheckoutStationSilver();
        stationSilver.plugIn(PowerGrid.instance());
        stationSilver.turnOn();
        
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
        
        stationBronze = new SelfCheckoutStationBronze();
        stationBronze.plugIn(PowerGrid.instance());
        stationBronze.turnOn();
    }
    
    /**
     * For these tests, the printer does not have any ink/paper
     * by default and so if these tests pass, it means that the
     * attendant simulation successfully adds ink and paper and
     * reprints the receipt so there is no need to test the methods
     * which add ink/paper
     */
    
    @Test public void testReceiptPrinterController() {
    	ReceiptPrinterController rpc = new ReceiptPrinterController(stationBronze);
    	assertNotNull(rpc);
    }
    
    @Test public void testPrintReceipt1() {
    	ReceiptPrinterController rpc = new ReceiptPrinterController(stationBronze);
    	rpc.printReceipt("T");
    	assertEquals("T", rpc.getPrintedReceipt());
    }
    
    @Test public void testPrintReceipt2() {
    	ReceiptPrinterController rpc = new ReceiptPrinterController(stationBronze);
    	rpc.printReceipt("Test");
    	assertEquals("Test", rpc.getPrintedReceipt());
    }
    @Test public void testPrintReceipt3() {
    	ReceiptPrinterController rpc = new ReceiptPrinterController(stationGold);
    	rpc.printReceipt("\nTest");
    	assertEquals("\nTest", rpc.getPrintedReceipt());
    }
    @Test public void testPrintReceipt4() {
    	ReceiptPrinterController rpc = new ReceiptPrinterController(stationSilver);
    	rpc.printReceipt("\nTest\nshould\nwork");
    	assertEquals("\nTest\nshould\nwork", rpc.getPrintedReceipt());
    }
}