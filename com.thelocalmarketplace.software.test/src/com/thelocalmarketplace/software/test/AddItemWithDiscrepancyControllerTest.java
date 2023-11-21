package com.thelocalmarketplace.software.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.ElectronicScaleGold;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerGold;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.AddItem.AddItemWithDiscrepancyController;
import com.thelocalmarketplace.software.AddItem.StartSession;

import powerutility.PowerGrid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)

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
// The only two variables that we have for testing are the TotalWeight and the TotalPrice
// Every test checks the expected behaviour of these two
// We test each branch of the function for true, false, and null values
public class AddItemWithDiscrepancyControllerTest {
	SelfCheckoutStationGold stationGold;
	SelfCheckoutStationSilver stationSilver;
	SelfCheckoutStationBronze stationBronze;
	
	AddItemWithDiscrepancyController testObject;
	
	
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
    
    
    @Test 
    public void testNoBarcodeFound() {
    	System.out.println();
    	System.out.println("Test 0:");
    	
    	Numeral[] barcodeDigitsUnknown= new Numeral[] { Numeral.one, Numeral.one, Numeral.one, Numeral.four, Numeral.five };
    	Barcode barcodeUnknown = new Barcode(barcodeDigitsUnknown);
    	
    	testObject = new AddItemWithDiscrepancyController(stationGold);
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeUnknown, new Mass (weightChips)));
    	
    	assertFalse(AddItemWithDiscrepancyController.isBlocked());
    }
    
    // Testing that the TotalCost is updated correctly after an item is scanned
    @Test 
    public void testTotalCostGold() {
    	System.out.println();
    	System.out.println("Test 1.1:");
    	
    	testObject = new AddItemWithDiscrepancyController(stationGold);
    	
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeChips, new Mass (weightChips)));
    	
    	assertEquals(2L, testObject.getTotalCost());
    }
    
 // Testing that the TotalCost is updated correctly after an item is scanned
    @Test 
    public void testTotalCostSilver() {
    	System.out.println();
    	System.out.println("Test 1.2:");
    	
    	testObject = new AddItemWithDiscrepancyController(stationSilver);
    	
    	stationSilver.mainScanner.scan(new BarcodedItem (barcodeChips, new Mass (weightChips)));
    	
    	assertEquals(2L, testObject.getTotalCost());
    }
    
    // Testing that the TotalCost is updated correctly after an item is scanned
    @Test 
    public void testTotalCostBronze() {
    	System.out.println();
    	System.out.println("Test 1.3:");
    	
    	testObject = new AddItemWithDiscrepancyController(stationBronze);
    	
    	stationBronze.mainScanner.scan(new BarcodedItem (barcodeChips, new Mass (weightChips)));
    	
    	assertEquals(2L, testObject.getTotalCost());
    }
    
   
   
 // Testing that the TotalWeight is updated correctly after an item is scanned
	@Test 
    public void testTotalWeight() {
    	System.out.println();
    	System.out.println("Test 2:");
    	
		testObject = new AddItemWithDiscrepancyController(stationGold);
		
		stationGold.mainScanner.scan(new BarcodedItem (barcodeChips, new Mass (weightChips)));
    	assertEquals(1, testObject.getTotalWeight(), 0.01);
    }
	
	

    @Test 
    public void barcodeHasBeenScannedInSessionNotBlocked() {
    	System.out.println();
    	System.out.println("Test 3:");
    	
    	testObject = new AddItemWithDiscrepancyController(stationGold);
    	
    	assertFalse(AddItemWithDiscrepancyController.isBlocked());
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeEggs, new Mass (weightEggs)));

    	assertEquals(6L, testObject.getTotalCost());
    	assertEquals(4, testObject.getTotalWeight(), 0.01);
    }
   
    
    @Test 
    public void barcodeHasBeenScannedNotInSessionNotBlocked() {
    	System.out.println();
    	System.out.println("Test 4:");
    	
    	
    	StartSession startSessionF = new StartSession(stationGold);
    	assertEquals(false, startSessionF.isInSession());
    	
    	testObject = new AddItemWithDiscrepancyController(stationGold);
    	
    	assertTrue(!AddItemWithDiscrepancyController.isBlocked());
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeEggs, new Mass (weightEggs)));
    	
    	//Should NOT scan because not in Start Session
    	assertNotEquals(6L, testObject.getTotalCost());
    	assertNotEquals(4,  testObject.getTotalWeight(), 0.01);
    }
    
    
    @Test 
    public void barcodeHasBeenScannedInSessionBlocked() {
    	System.out.println();
    	System.out.println("Test 5:");
    	System.out.println();
    	assertEquals(true, startSession.isInSession());
    	
    	testObject = new AddItemWithDiscrepancyController(stationGold);
    	AddItemWithDiscrepancyController.block();
    	assertTrue(AddItemWithDiscrepancyController.isBlocked());
    	
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeEggs, new Mass (weightEggs)));
    	
    	//Should NOT scan because Session Blocked
    	assertNotEquals(6L, testObject.getTotalCost());
    	assertNotEquals(4,  testObject.getTotalWeight(), 0.01);
    }
    
    
    
    
    @Test 
    public void testSelfCheckoutBlocked() {
    	System.out.println();
    	System.out.println("Test 6:");
    	System.out.println();
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeEggs, new Mass (weightEggs)));
    	AddItemWithDiscrepancyController.block();
    	assertTrue(AddItemWithDiscrepancyController.isBlocked());
    }
    
    
    
    @Test 
    public void testSelfCheckoutNotBlocked() {
    	System.out.println();
    	System.out.println("Test 7:");
    	
    	testObject = new AddItemWithDiscrepancyController(stationGold);
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeEggs, new Mass (weightEggs)));
    	AddItemWithDiscrepancyController.unblock();
    	assertFalse(AddItemWithDiscrepancyController.isBlocked());
    }
    

    
    @Test
    public void testBarcodeScannedBaggingAreaOverloaded() {
    	System.out.println();
    	System.out.println("Test 8:");
    	
    	testObject = new AddItemWithDiscrepancyController(stationGold);
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeHippo, new Mass (weightHippo)));
 
    	//Expected weight should NOT change because over system limit
    	assertNotEquals(1000000000, testObject.getTotalWeight(), 0.01);
    	assertEquals(0, testObject.getTotalWeight(), 0.01);
    	
    	//Total cost would still change after scanning
    	assertEquals(1100L, testObject.getTotalCost()); 
    }
    
    
    
    class ConcreteItem extends Item {
	    public ConcreteItem(Mass mass) {
	        super(mass);
	 }
	}
	
	
    @Test
    public void testWeightDiscrepancyNoScan() {
    	System.out.println();
    	System.out.println("Test 9:");
  
    	testObject = new AddItemWithDiscrepancyController(stationGold);
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeEggs, new Mass (weightEggs)));
    	stationGold.baggingArea.addAnItem(new ConcreteItem(new Mass (weightEggs)));
 
    	//Should block session if this is executed (Expect < Actual)
    	stationGold.baggingArea.addAnItem(new ConcreteItem(new Mass (weightMilk)));
    	
    	assertTrue(testObject.foundDiscrepancy);
    }
   
    
    @Test
    public void testNoWeightDiscrepancy() {
    	System.out.println();
    	System.out.println("Test 10:");
    	testObject = new AddItemWithDiscrepancyController(stationGold);
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeEggs, new Mass (weightEggs)));
    	stationGold.baggingArea.addAnItem(new ConcreteItem(new Mass (weightEggs)));
 
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeMilk, new Mass (weightMilk)));
    	stationGold.baggingArea.addAnItem(new ConcreteItem(new Mass (weightMilk)));
    	
    	assertFalse(testObject.foundDiscrepancy);
    }
    
    @Test
    public void testWeightDiscrepancyWrongScan() {
    	System.out.println();
    	System.out.println("Test 11:");
  
    	testObject = new AddItemWithDiscrepancyController(stationGold);
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeEggs, new Mass (weightEggs)));
    	stationGold.baggingArea.addAnItem(new ConcreteItem(new Mass (weightEggs)));
 
    	//Incorrect item (milk) placed in bagging area - should detect discrepancy
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeEggs, new Mass (weightEggs)));
    	stationGold.baggingArea.addAnItem(new ConcreteItem(new Mass (weightMilk)));
    	
    	assertTrue(testObject.foundDiscrepancy);
    }
    
    
    @Test
    public void testAttendantOverrideOnWeightDiscrepancy() {
    	System.out.println();
    	System.out.println("Test 12:");
  
    	testObject = new AddItemWithDiscrepancyController(stationGold);
 
    	//Incorrect item (milk) placed in bagging area - should detect discrepancy
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeEggs, new Mass (weightEggs)));
    	stationGold.baggingArea.addAnItem(new ConcreteItem(new Mass (weightMilk)));
    	assertTrue(testObject.foundDiscrepancy);
    	
    	//Override discrepancy
    	testObject.AttendentOverrideToHandleDiscrepancy();
    	
    	//Scan 2nd item normally
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeMilk, new Mass (weightMilk)));
    	stationGold.baggingArea.addAnItem(new ConcreteItem(new Mass (weightMilk)));
    	
    	assertFalse(testObject.foundDiscrepancy);
    }
    
    
    @Test
    public void testBarcodeScannedProductNull() {
    	System.out.println();
    	System.out.println("Test 13:");
    	
    	Numeral[] barcodeDigitsNull= new Numeral[] { Numeral.six, Numeral.six, Numeral.six, Numeral.six, Numeral.six };
    	Barcode barcodeNull = new Barcode(barcodeDigitsNull);
    	testObject = new AddItemWithDiscrepancyController(stationGold);
    	testObject.aBarcodeHasBeenScanned(scanner, barcodeNull);
    	double delta = 0.01;
    	assertNotEquals(2L, testObject.getTotalCost());
    	assertNotEquals(0.1, testObject.getTotalWeight(), delta);
    	
    }
    
    @Test
    public void testDoNotBagOnWeightDiscrepancy() {
    	System.out.println();
    	System.out.println("Test 14:");
  
    	testObject = new AddItemWithDiscrepancyController(stationGold);
 
    	//Scan Item (should have weight discrepancy as it is not added to bagging area)
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeEggs, new Mass (weightEggs)));
    	assertTrue(testObject.CheckWeightDescrepency());
    	
    	//Customer Selects option of Do Not Bag (should NOT have weight discrepancy as item will be placed in bagging area)
    	testObject.DoNotBagToHandleDiscrepancy(new ConcreteItem(new Mass (weightEggs)));
    	assertFalse(testObject.CheckWeightDescrepency());
    	
    
    }
    
    @Test
    public void testDeviceEnable() throws IOException {
    	testObject = new AddItemWithDiscrepancyController(stationGold);
    	stationGold.mainScanner.enable();
    	assertTrue(testObject.isEnabled);
    	stationGold.mainScanner.disable();
    	assertFalse(testObject.isEnabled);	
    	
    }
    
    @Test
    public void testDeviceOn() throws IOException {
    	testObject = new AddItemWithDiscrepancyController(stationGold);
    	stationGold.mainScanner.turnOff();
    	assertFalse(testObject.isOn);
    	stationGold.mainScanner.turnOn();
    	assertTrue(testObject.isOn);
   
    	
    }
    
	@Test
    public void testOverload() throws IOException {  
    	testObject = new AddItemWithDiscrepancyController(stationGold);
 
    	//Heavy item placed in bagging area - should cause Overload
    	stationGold.baggingArea.addAnItem(new ConcreteItem(new Mass (weightHippo)));
    	assertTrue(testObject.isOverloaded);
    	
    	
    }
    
	
	@Test
    public void testAddItemToHandleDiscrepancy() throws IOException { 
		System.out.println();
    	System.out.println("Test 15:");
    	
    	testObject = new AddItemWithDiscrepancyController(stationGold);
    	 
    	//Item (milk) NOT placed in bagging area - should detect discrepancy
    	stationGold.mainScanner.scan(new BarcodedItem (barcodeEggs, new Mass (weightEggs)));
    	assertTrue(testObject.CheckWeightDescrepency());
    	
    	//Add item to fix discrepancy
    	testObject.AddItemToHandleDiscrepancy(new ConcreteItem(new Mass (weightEggs)));
    	assertFalse(testObject.CheckWeightDescrepency());
    }
}