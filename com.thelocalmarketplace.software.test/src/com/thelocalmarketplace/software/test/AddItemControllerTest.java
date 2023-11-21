package com.thelocalmarketplace.software.test;

import java.io.IOException;
import java.util.List;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.card.InvalidPINException;
import com.jjjwelectronics.scale.ElectronicScaleGold;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerGold;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
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
public class AddItemControllerTest {
	private SelfCheckoutStationGold selfCheckoutStation;
	public PowerGrid powerGrid;
	ElectronicScaleGold electronicScale;
	private List<Barcode> AddedItems;  
	private long totalCost;
	private double totalWeight;
	AddItemWithDiscrepancyController addItemController;
	BarcodeScannerGold scanner;
	StartSession startSession;
	
	Numeral[] barcodeDigitsChips= new Numeral[] { Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five };
	Barcode barcodeChips = new Barcode(barcodeDigitsChips);
	String descriptionChips = "Bag of chips";
	long priceChips = (long) 2.00;
	double weightChips = 0.1;
	
	Numeral[] barcodeDigitsEggs= new Numeral[] { Numeral.two, Numeral.one, Numeral.three, Numeral.four, Numeral.five };
	Barcode barcodeEggs = new Barcode(barcodeDigitsEggs);
	String descriptionEggs = "Carton of eggs";
	long priceEggs = (long) 6.00;
	double weightEggs = 0.4;

	
	Numeral[] barcodeDigitsMilk= new Numeral[] { Numeral.two, Numeral.one, Numeral.four, Numeral.three, Numeral.five };
	Barcode barcodeMilk = new Barcode(barcodeDigitsMilk);
	String descriptionMilk = "Jug of milk";
	long priceMilk = (long) 5.00;
	double weightMilk = 0.5;
	
	Numeral[] barcodeDigitsHippo= new Numeral[] { Numeral.three, Numeral.one, Numeral.four, Numeral.three, Numeral.five };
	Barcode barcodeHippo = new Barcode(barcodeDigitsHippo);
	String descriptionHippo = "A big Hippo has appeared.... :O !!!";
	long priceHippo = (long) 1100.00;
	double weightHippo = 1000000000;
	
	BarcodedProduct productBarChips = new BarcodedProduct(barcodeChips, descriptionChips, priceChips, weightChips);
	BarcodedProduct productBarEggs = new BarcodedProduct(barcodeEggs, descriptionEggs, priceEggs, weightEggs);
	BarcodedProduct productBarMilk = new BarcodedProduct(barcodeMilk, descriptionMilk, priceMilk, weightMilk);
	
    @Before
	public void setUp() {
    
        selfCheckoutStation = new SelfCheckoutStationGold();
        powerGrid = PowerGrid.instance();

        scanner = new BarcodeScannerGold();
        
        startSession = new StartSession(selfCheckoutStation);
    	try {
			startSession.startSession(powerGrid);
		} catch (InvalidPINException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	addItemController = new AddItemWithDiscrepancyController(selfCheckoutStation, startSession);
    	
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeChips, productBarChips);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeEggs, productBarEggs);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeMilk, productBarMilk);
        
    } 
    
    // Testing that the TotalCost is updated correctly after an item is scanned
    @Test 
    public void testTotalCost() {
    	System.out.println();
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	System.out.println(addItemController.getTotalCost());
    	assertEquals(2L, addItemController.getTotalCost());
    }
    
    
   
 // Testing that the TotalWeight is updated correctly after an item is scanned
	@Test 
    public void testTotalWeight() {
		System.out.println();
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeEggs);
    	double delta = 0.01;
    	assertEquals(0.4, addItemController.getTotalWeight(), delta);
    }
	
	

    @Test 
    public void barcodeHasBeenScannedInSessionNotBlocked() {
    	System.out.println();
    	assertFalse(AddItemWithDiscrepancyController.isBlocked());
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeEggs);
    	double delta = 0.01;

    	assertEquals(6L, addItemController.getTotalCost());
    	assertEquals(0.4, addItemController.getTotalWeight(), delta);
    }
    
    @Test 
    public void barcodeHasBeenScannedNotInSessionNotBlocked() {
    	System.out.println();
    	StartSession startSessionF = new StartSession(selfCheckoutStation);
    	assertEquals(false, startSessionF.isInSession());
    	addItemController = new AddItemWithDiscrepancyController(selfCheckoutStation, startSessionF);
    	AddItemWithDiscrepancyController.unblock();
    	assertFalse(AddItemWithDiscrepancyController.isBlocked());
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
        // Make assertions on the captured output
    	double delta = 0.01;
    	assertNotEquals(2L, addItemController.getTotalCost());
    	assertNotEquals(0.1, addItemController.getTotalWeight(), delta);
    }
    
    
    @Test 
    public void barcodeHasBeenScannedInSessionBlocked() {
    	System.out.println();
    	StartSession startSessionT = new StartSession(selfCheckoutStation);
    	try {
			startSessionT.startSession(powerGrid);
		} catch (InvalidPINException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	assertEquals(true, startSessionT.isInSession());
    	addItemController = new AddItemWithDiscrepancyController(selfCheckoutStation, startSessionT);
    	AddItemWithDiscrepancyController.block();
    	assertTrue(AddItemWithDiscrepancyController.isBlocked());
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	double delta = 0.01;
    	assertNotEquals(2L, addItemController.getTotalCost());
    	assertNotEquals(0.1, addItemController.getTotalWeight(), delta);
    }
    
    
    @Test 
    public void barcodeHasBeenScannedNotInSessionBlocked() {
    	System.out.println();
    	// It would be good to have a listener to check quickly if an item has been scanned
    	StartSession startSessionF = new StartSession(selfCheckoutStation);
    	assertEquals(false, startSessionF.isInSession());
    	addItemController = new AddItemWithDiscrepancyController(selfCheckoutStation, startSessionF);
    	AddItemWithDiscrepancyController.block();
    	assertTrue(AddItemWithDiscrepancyController.isBlocked());
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
        // Make assertions on the captured output
    	double delta = 0.01;
    	assertNotEquals(2L, addItemController.getTotalCost());
    	assertNotEquals(0.1, addItemController.getTotalWeight(), delta);
    }
    
   
    @Test 
    public void barcodeHasBeenScannedSessionNullBlocked() {
    	System.out.println();
    	StartSession startSessionF = null;
    	assertEquals(null, startSessionF);
    	addItemController = new AddItemWithDiscrepancyController(selfCheckoutStation, startSession);
    	AddItemWithDiscrepancyController.block();
    	assertTrue(AddItemWithDiscrepancyController.isBlocked());
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
        // Make assertions on the captured output
    	double delta = 0.01;
    	assertNotEquals(2L, addItemController.getTotalCost());
    	assertNotEquals(0.1 , addItemController.getTotalWeight(), delta);
    }
    
    
    @Test 
    public void testSelfCheckoutBlocked() {
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	AddItemWithDiscrepancyController.block();
    	assertTrue(AddItemWithDiscrepancyController.isBlocked());
    }
    
    @Test 
    public void testSelfCheckoutNotBlocked() {
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	AddItemWithDiscrepancyController.unblock();
    	assertFalse(AddItemWithDiscrepancyController.isBlocked());
    }
    
    @Test
    public void testBarcodeScannedBaggingAreaOverloaded() {
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeHippo);
    	System.out.println("ACTUAL PRICE!!!!");
    	System.out.println(addItemController.getTotalCost());
    	assertEquals(0L, addItemController.getTotalCost()); // Should be 0 because Hippo is too heavy for bagging area
    	
    }
    
    @Test
    public void testBarcodeScannedProductNull() {
    	Numeral[] barcodeDigitsNull= new Numeral[] { Numeral.six, Numeral.six, Numeral.six, Numeral.six, Numeral.six };
    	Barcode barcodeNull = new Barcode(barcodeDigitsNull);
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeNull);
    	double delta = 0.01;
    	assertNotEquals(2L, addItemController.getTotalCost());
    	assertNotEquals(0.1, addItemController.getTotalWeight(), delta);
    	
    }
    
    @Test
    public void testMassChangedOnScaleZeroChange() {
    	
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	Mass massOnScale = new Mass(0.1);
    	addItemController.theMassOnTheScaleHasChanged(selfCheckoutStation.baggingArea, massOnScale);
    	assertFalse(AddItemWithDiscrepancyController.isBlocked());
    	
    }
    
    
    @Test
    public void testMassChangedOnScaleMassChange() {
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	Mass massOnScale = new Mass(200000);
    	addItemController.theMassOnTheScaleHasChanged(selfCheckoutStation.baggingArea, massOnScale);
    	assertFalse(AddItemWithDiscrepancyController.isBlocked());
    }
    
    @Test
    public void testHandleDiscBlocked() {
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	AddItemWithDiscrepancyController.block();
    	addItemController.AddOrRemoveItemToHandleDiscrepancy();
    	assertTrue(AddItemWithDiscrepancyController.isBlocked());
    }
    
    @Test
    public void testHandleDiscNotBlocked() {
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	AddItemWithDiscrepancyController.unblock();
    	addItemController.AddOrRemoveItemToHandleDiscrepancy();
    	assertFalse(AddItemWithDiscrepancyController.isBlocked());
    }
}