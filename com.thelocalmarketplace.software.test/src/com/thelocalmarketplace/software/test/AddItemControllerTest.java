package com.thelocalmarketplace.software.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.AddItemController;
import com.thelocalmarketplace.software.StartSession;

import powerutility.PowerGrid;

public class AddItemControllerTest {
	private SelfCheckoutStation selfCheckoutStation;
	public PowerGrid powerGrid;
	private List<Barcode> AddedItems;  
	private long totalCost;
	private double totalWeight;
	AddItemController addItemController;
	BarcodeScanner scanner;
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
	
	BarcodedProduct productBarChips = new BarcodedProduct(barcodeChips, descriptionChips, priceChips, weightChips);
	BarcodedProduct productBarEggs = new BarcodedProduct(barcodeEggs, descriptionEggs, priceEggs, weightEggs);
	BarcodedProduct productBarMilk = new BarcodedProduct(barcodeMilk, descriptionMilk, priceMilk, weightMilk);
    
    @Before
	public void setUp() {
    	
    	
    
        selfCheckoutStation = new SelfCheckoutStation();
        powerGrid = PowerGrid.instance();

        scanner = new BarcodeScanner();
        
        startSession = new StartSession(selfCheckoutStation);
    	startSession.startSession(powerGrid);
    	addItemController = new AddItemController(selfCheckoutStation, powerGrid, startSession);
    	
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeChips, productBarChips);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeEggs, productBarEggs);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeMilk, productBarMilk);
        
        //Product productChips = new Product((long)5.00, true){};
        ///Product productEggs = new Product((long)5.00, true){};
        //Product productMilk = new Product((long)5.00, true){};
       
        //ProductDatabases.INVENTORY.put(productChips, 3); 
        //ProductDatabases.INVENTORY.put(productEggs, 4);  
        //ProductDatabases.INVENTORY.put(productMilk, 5);   
    }
    
    // Testing that the TotalCost is updated correctly after an item is scanned
    @Test 
    public void testTotalCost() {
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	assertEquals(2L, addItemController.getTotalCost());
        //assertEquals(0.1, addItemController.getTotalWeight());
    }
    
 // Testing that the TotalCost is updated correctly after an item is scanned
	@Test 
    public void testTotalWeight() {
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeEggs);
    	double delta = 0.01;
    	assertEquals(0.4, addItemController.getTotalWeight(), delta);
        //assertEquals(0.1, addItemController.getTotalWeight());
    }
    
    
    @Test 
    public void testSelfCheckoutInaccessible() {
    	Barcode barcodeScanned1 = new Barcode(new Numeral[] {Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five});
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeScanned1);
    	assertEquals(2L, addItemController.getTotalCost());
        //assertEquals(0.1, addItemController.getTotalWeight());
    }
    
    @Test 
    public void testSelfCheckoutAccessible() {
    	Barcode barcodeScanned1 = new Barcode(new Numeral[] {Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five});
    	addItemController.aBarcodeHasBeenScanned(scanner, barcodeScanned1);
    	assertEquals(2L, addItemController.getTotalCost());
        //assertEquals(0.1, addItemController.getTotalWeight());
    }
    
    
    
    

}
