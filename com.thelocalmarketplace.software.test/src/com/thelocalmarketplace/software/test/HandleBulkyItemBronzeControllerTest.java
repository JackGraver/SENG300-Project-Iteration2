package com.thelocalmarketplace.software.test;

import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import java.util.Currency;
import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerBronze;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.AddItem.HandleBulkyItemBronzeController;

import powerutility.PowerGrid;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
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
public class HandleBulkyItemBronzeControllerTest {
    private HandleBulkyItemBronzeController handleBulkyItemController;
    private SelfCheckoutStationBronze scs;
    BarcodeScannerBronze scanner;
    
    Numeral[] barcodeDigitsChips= new Numeral[] { Numeral.one, Numeral.two, Numeral.three, Numeral.four, Numeral.five };
	Barcode barcodeChips = new Barcode(barcodeDigitsChips);
	String descriptionChips = "Bag of chips";
	long priceChips = (long) 2.00;
	double weightChips = 0.1;
	
	BarcodedProduct productBarChips = new BarcodedProduct(barcodeChips, descriptionChips, priceChips, weightChips);
    
    @Before
    public void setUp() {
    	 SelfCheckoutStationBronze.resetConfigurationToDefaults();
    	 SelfCheckoutStationBronze.configureCurrency(Currency.getInstance("CAD"));
    	 SelfCheckoutStationBronze.configureBanknoteStorageUnitCapacity(7);
    	 SelfCheckoutStationBronze.configureCoinDispenserCapacity(5);
    	 SelfCheckoutStationBronze.configureCoinTrayCapacity(5);
    	 SelfCheckoutStationBronze.configureScaleMaximumWeight(5);
    	 SelfCheckoutStationBronze.configureReusableBagDispenserCapacity(6);
    	 SelfCheckoutStationBronze.configureScaleSensitivity(5);
         scs = new SelfCheckoutStationBronze();
         scs.plugIn(PowerGrid.instance());
         scs.turnOn();
         scanner = new BarcodeScannerBronze();
         handleBulkyItemController = new HandleBulkyItemBronzeController(scs);
         
         ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcodeChips, productBarChips);
         
         
    }
    
    @Test
    public void testaBarcodeHasBeenScanned() {
    	handleBulkyItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	assertEquals(handleBulkyItemController.getTotalCost(), 2L);
    	double delta = 0.01;
    	assertEquals(handleBulkyItemController.getTotalWeight(), 0.1, delta);
    }
    
    @Test
    public void testReduceWeight() {
    	handleBulkyItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	handleBulkyItemController.reduceWeight();
    	double delta = 0.01;
    	assertEquals(handleBulkyItemController.getTotalWeight(), 0, delta);
    }
    
    @Test
    public void testSignalAttendant() {
    	handleBulkyItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	handleBulkyItemController.signalAttendant();
    	assertTrue(handleBulkyItemController.getSignal());
    }
    
    @Test
    public void testAttendantApproval() {
    	handleBulkyItemController.setAttendantApproval(true);
    	assertTrue(handleBulkyItemController.getAttendantApproval());
    }
    
    @Test
    public void testBlock() {
    	handleBulkyItemController.block();
    	handleBulkyItemController.unblock();
    	assertFalse(handleBulkyItemController.isBlocked());
    }
    
    @Test
    public void testHandleBulkyItemApproved() {
    	handleBulkyItemController.setAttendantApproval(true);
    	handleBulkyItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	double delta = 0.01;
    	handleBulkyItemController.handleBulkyItem();
    	assertEquals(handleBulkyItemController.getTotalWeight(), 0, delta);
    	assertFalse(handleBulkyItemController.isBlocked());
    	assertFalse(handleBulkyItemController.getSignal());
    }
    
    @Test
    public void testHandleBulkyItemDisapproved() {
    	handleBulkyItemController.setAttendantApproval(false);
    	handleBulkyItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	double delta = 0.01;
    	handleBulkyItemController.handleBulkyItem();
    	assertNotEquals(handleBulkyItemController.getTotalWeight(), 0, delta);
    	assertTrue(handleBulkyItemController.isBlocked());
    	assertTrue(handleBulkyItemController.getSignal());
    }
    
    @Test
    public void testweightDiscreptancyCheckFalse() {
    	handleBulkyItemController.setAttendantApproval(false);
    	handleBulkyItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	handleBulkyItemController.handleBulkyItem();
    	assertFalse(handleBulkyItemController.weightDiscreptancyCheck());
    }
    
    @Test
    public void testweightDiscreptancyCheckTrue() {
    	handleBulkyItemController.setAttendantApproval(true);
    	handleBulkyItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	handleBulkyItemController.handleBulkyItem();
    	Mass weight = new Mass(200);
    	Item item = new ConcreteItem(weight);
    	scs.baggingArea.addAnItem(item);
    	assertTrue(handleBulkyItemController.weightDiscreptancyCheck());
    }
    
    @Test
    public void testDevices() {
    	scs.turnOff();
    	scs.turnOn();
    	scs.baggingArea.disable();
    	scs.baggingArea.enable();
    }
    
    
    @Test
    public void testMassChangedOnScaleMassChange() {
    	handleBulkyItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	Mass massOnScale = new Mass(200000);
    	handleBulkyItemController.theMassOnTheScaleHasChanged(scs.baggingArea, massOnScale);
    	assertFalse(handleBulkyItemController.isBlocked());
    }
    
    @Test
    public void testTheMassOnTheScaleHasExceededItsLimit() {
    	handleBulkyItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	handleBulkyItemController.theMassOnTheScaleHasExceededItsLimit(scs.baggingArea);
    	assertFalse(handleBulkyItemController.isBlocked());
    }
    
    @Test
    public void testTheMassOnTheScaleNoLongerExceedsItsLimit() {
    	handleBulkyItemController.aBarcodeHasBeenScanned(scanner, barcodeChips);
    	handleBulkyItemController.theMassOnTheScaleNoLongerExceedsItsLimit(scs.baggingArea);
    	assertFalse(handleBulkyItemController.isBlocked());
    }
    
    class ConcreteItem extends Item {
	    public ConcreteItem(Mass mass) {
	        super(mass);
	    }
    }
}