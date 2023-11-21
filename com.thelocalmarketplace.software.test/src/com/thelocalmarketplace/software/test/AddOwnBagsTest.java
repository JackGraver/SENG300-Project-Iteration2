/* 
 * Darpal Patel - 30088795
 * Christopher Thomson - 30186596
 * 
 * 
 */

package com.thelocalmarketplace.software.test;

import java.math.BigDecimal;
import java.util.Currency;

import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerGold;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.AddItem.AddOwnBags;
import com.thelocalmarketplace.software.AddItem.StartSession;

import powerutility.PowerGrid;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)


// The only two variables that we have for testing are the TotalWeight and the TotalPrice
// Every test checks the expected behaviour of these two
// We test each branch of the function for true, false, and null values
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
public class AddOwnBagsTest {
	SelfCheckoutStationGold stationGold;
	SelfCheckoutStationSilver stationSilver;
	SelfCheckoutStationBronze stationBronze;
	
	AddOwnBags testObject;
	
	
	BarcodeScannerGold scanner;
	StartSession startSession;
	
	Numeral[] barcodeDigitsChips= new Numeral[] { Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five };
	Barcode barcodeChips = new Barcode(barcodeDigitsChips);
	String descriptionChips = "bag of chips";
	long priceChips = (long) 2.00;
	double weightChips = 1;
	
	Numeral[] barcodeDigitsEggs= new Numeral[] { Numeral.two, Numeral.one, Numeral.three, Numeral.four, Numeral.five };
	Barcode barcodeEggs = new Barcode(barcodeDigitsEggs);
	String descriptionEggs = "carton of eggs";
	long priceEggs = (long) 6.00;
	double weightEggs = 4;

	
	Numeral[] barcodeDigitsMilk= new Numeral[] { Numeral.two, Numeral.one, Numeral.four, Numeral.three, Numeral.five };
	Barcode barcodeMilk = new Barcode(barcodeDigitsMilk);
	String descriptionMilk = "jug of milk";
	long priceMilk = (long) 5.00;
	double weightMilk = 3;
	
	Numeral[] barcodeDigitsHippo= new Numeral[] { Numeral.three, Numeral.one, Numeral.four, Numeral.three, Numeral.five };
	Barcode barcodeHippo = new Barcode(barcodeDigitsHippo);
	String descriptionHippo = "A big Hippo has appeared.... :O !!!";
	long priceHippo = (long) 1100.00;
	double weightHippo = 1000000000;
	
	
	double weightBags = 2;
	
	BarcodedProduct productBarChips = new BarcodedProduct(barcodeChips, descriptionChips, priceChips, weightChips);
	BarcodedProduct productBarEggs = new BarcodedProduct(barcodeEggs, descriptionEggs, priceEggs, weightEggs);
	BarcodedProduct productBarMilk = new BarcodedProduct(barcodeMilk, descriptionMilk, priceMilk, weightMilk);
	BarcodedProduct productBarHippo = new BarcodedProduct(barcodeHippo, descriptionHippo, priceHippo, weightHippo);
	
    @Before
	public void setUp() {
    
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
	    SelfCheckoutStationGold.configureScaleMaximumWeight(500);
	    SelfCheckoutStationGold.configureReusableBagDispenserCapacity(6);
	    SelfCheckoutStationGold.configureScaleSensitivity(9);
	    SelfCheckoutStationGold.configureCoinStorageUnitCapacity(5);
    	stationGold = new SelfCheckoutStationGold();
    	stationGold.plugIn(PowerGrid.instance());
    	stationGold.turnOn();
        startSession = new StartSession(stationGold);
        startSession.startSession(PowerGrid.instance());
        
        SelfCheckoutStationSilver.configureCurrency(Currency.getInstance("CAD"));
		SelfCheckoutStationSilver.configureBanknoteDenominations(banknoteDenominationsConfiguration);
	    SelfCheckoutStationSilver.configureBanknoteStorageUnitCapacity(7);
	    SelfCheckoutStationSilver.configureCoinDenominations(coinDenominationsConfigurationArray);
	    SelfCheckoutStationSilver.configureCoinDispenserCapacity(5);
	    SelfCheckoutStationSilver.configureCoinTrayCapacity(5);
	    SelfCheckoutStationSilver.configureScaleMaximumWeight(500);
	    SelfCheckoutStationSilver.configureReusableBagDispenserCapacity(6);
	    SelfCheckoutStationSilver.configureScaleSensitivity(9);
	    SelfCheckoutStationSilver.configureCoinStorageUnitCapacity(5);
    	stationSilver = new SelfCheckoutStationSilver();
    	stationSilver.plugIn(PowerGrid.instance());
    	stationSilver.turnOn();
        startSession = new StartSession(stationSilver);
        startSession.startSession(PowerGrid.instance());
        
        SelfCheckoutStationBronze.configureCurrency(Currency.getInstance("CAD"));
		SelfCheckoutStationBronze.configureBanknoteDenominations(banknoteDenominationsConfiguration);
	    SelfCheckoutStationBronze.configureBanknoteStorageUnitCapacity(7);
	    SelfCheckoutStationBronze.configureCoinDenominations(coinDenominationsConfigurationArray);
	    SelfCheckoutStationBronze.configureCoinDispenserCapacity(5);
	    SelfCheckoutStationBronze.configureCoinTrayCapacity(5);
	    SelfCheckoutStationBronze.configureScaleMaximumWeight(500);
	    SelfCheckoutStationBronze.configureReusableBagDispenserCapacity(6);
	    SelfCheckoutStationBronze.configureScaleSensitivity(9);
	    SelfCheckoutStationBronze.configureCoinStorageUnitCapacity(5);
    	stationBronze = new SelfCheckoutStationBronze();
    	stationBronze.plugIn(PowerGrid.instance());
    	stationBronze.turnOn();
        startSession = new StartSession(stationBronze);
        startSession.startSession(PowerGrid.instance());
    	
    	
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeChips, productBarChips);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeEggs, productBarEggs);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeMilk, productBarMilk);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeHippo, productBarHippo);
        
    } 
    
    class ConcreteItem extends Item {
	    public ConcreteItem(Mass mass) {
	        super(mass);
	 }
	}
    
    @Test 
    public void testAddBagsAfterChoosing() {
    	System.out.println();
    	System.out.println("Test 1:");
    	
    	//Test scan of normal chips (no discrepancy should be found)
    	testObject = new AddOwnBags(stationGold, startSession);
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeEggs, new Mass (weightEggs)));
    	stationGold.baggingArea.addAnItem(new ConcreteItem(new Mass (weightEggs)));
    	assertFalse(testObject.CheckWeightDescrepency());
    	
    	testObject.chooseAddOwnBag(true);
    	assertTrue(testObject.nextItemIsBags);
    	
    	//No weight discrepancy even after adding bags
    	stationGold.baggingArea.addAnItem(new ConcreteItem(new Mass (weightBags)));
    	assertFalse(testObject.CheckWeightDescrepency());
    
    	
    	
    	
    }
    
    @Test
    public void testAddBagsWithoutChoosing() {
    	System.out.println();
    	System.out.println("Test 2:");
    	
    	//Test scan of normal chips (no discrepancy should be found)
    	testObject = new AddOwnBags(stationGold, startSession);
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeEggs, new Mass (weightEggs)));
    	stationGold.baggingArea.addAnItem(new ConcreteItem(new Mass (weightEggs)));
    	assertFalse(testObject.CheckWeightDescrepency());
    	
    	//Do not select Choose Own Bag
    	testObject.chooseAddOwnBag(false);
    	assertFalse(testObject.nextItemIsBags);
    	
    	//Weight discrepancy should be found after adding bags
    	stationGold.baggingArea.addAnItem(new ConcreteItem(new Mass (weightBags)));
    	assertTrue(testObject.CheckWeightDescrepency());
     	
    	
    	
    }
    
}
