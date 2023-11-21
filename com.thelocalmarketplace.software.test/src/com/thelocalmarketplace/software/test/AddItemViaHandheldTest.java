package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.AddItemViaHandheld;
import com.thelocalmarketplace.software.AddItem.StartSession;

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
public class AddItemViaHandheldTest {
    
    SelfCheckoutStationBronze checkoutStationBronze;
    SelfCheckoutStationSilver checkoutStationSilver;
    SelfCheckoutStationGold checkoutStation;

    AddItemViaHandheld listener;
    AddItemViaHandheld listenerBronze;
    AddItemViaHandheld listenerSilver;

    Barcode barcode1 = new Barcode(new Numeral[]{Numeral.one, Numeral.two});
    Barcode barcode2 = new Barcode(new Numeral[]{Numeral.three, Numeral.four});

    BarcodedProduct product1 = new BarcodedProduct(barcode1, "Item 1", 10, 15);
    BarcodedProduct product2 = new BarcodedProduct(barcode2, "Item 2", 5, 3);

    StartSession session;
    StartSession sessionSilver;
    StartSession sessionBronze;

    /**
     * Test setup class
     */
    @Before
    public void setup() {
        AbstractSelfCheckoutStation.configureCurrency(Currency.getInstance("CAD"));
        AbstractSelfCheckoutStation.configureCoinDenominations(new BigDecimal[] {new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25")});
        AbstractSelfCheckoutStation.configureBanknoteDenominations(new BigDecimal[] {new BigDecimal("1"), new BigDecimal("2")});
        AbstractSelfCheckoutStation.configureCoinStorageUnitCapacity(5);
        AbstractSelfCheckoutStation.configureCoinDispenserCapacity(5);
        AbstractSelfCheckoutStation.configureBanknoteStorageUnitCapacity(5);
        AbstractSelfCheckoutStation.configureCoinTrayCapacity(5);

        checkoutStation = new SelfCheckoutStationGold();
        checkoutStationBronze = new SelfCheckoutStationBronze();
        checkoutStationSilver = new SelfCheckoutStationSilver();

        listener = new AddItemViaHandheld(checkoutStation);
        listenerBronze = new AddItemViaHandheld(checkoutStationBronze);
        listenerSilver = new AddItemViaHandheld(checkoutStationSilver);

        checkoutStation.handheldScanner.register(listener);
        checkoutStationBronze.handheldScanner.register(listenerBronze);
        checkoutStationSilver.handheldScanner.register(listenerSilver);

        session = new StartSession(checkoutStation);
        sessionSilver = new StartSession(checkoutStationSilver);
        sessionBronze = new StartSession(checkoutStationBronze);

        //Activate bronze checkout station
        checkoutStationBronze.plugIn(PowerGrid.instance());
        checkoutStationBronze.turnOn();

        //Activate silver checkout station
        checkoutStationSilver.plugIn(PowerGrid.instance());
        checkoutStationSilver.turnOn();

        //Activate Gold (primary test) checkout station
        checkoutStation.plugIn(PowerGrid.instance());
        checkoutStation.turnOn();
    }

    @After
    public void teardown() {
        StartSession.endSession();
    }

    @Test
    public void scanItemNormalGold() {
        session.startSession(PowerGrid.instance());

        BarcodedProduct product1 = new BarcodedProduct(barcode1, "Item 1", 10, 15);
        BarcodedProduct product2 = new BarcodedProduct(barcode2, "Item 2", 5, 3);

        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product1.getBarcode(), product1);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product2.getBarcode(), product2);

        Barcode barcode = new Barcode(new Numeral[] {Numeral.one, Numeral.two});
        BarcodedItem item = new BarcodedItem(barcode, new Mass(10));

        checkoutStation.handheldScanner.scan(item);

        assertTrue(listener.getItemAdded());
    }

    @Test
    public void scanItemNormalSilver() {
        sessionSilver.startSession(PowerGrid.instance());

        BarcodedProduct product1 = new BarcodedProduct(barcode1, "Item 1", 10, 15);
        BarcodedProduct product2 = new BarcodedProduct(barcode2, "Item 2", 5, 3);

        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product1.getBarcode(), product1);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product2.getBarcode(), product2);

        Barcode barcode = new Barcode(new Numeral[] {Numeral.one, Numeral.two});
        BarcodedItem item = new BarcodedItem(barcode, new Mass(10));

        checkoutStationSilver.handheldScanner.scan(item);

        assertTrue(listenerSilver.getItemAdded());
    }

    @Test
    public void scanItemNormalBronze() {
        sessionBronze.startSession(PowerGrid.instance());
        
        BarcodedProduct product1 = new BarcodedProduct(barcode1, "Item 1", 10, 15);
        BarcodedProduct product2 = new BarcodedProduct(barcode2, "Item 2", 5, 3);

        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product1.getBarcode(), product1);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product2.getBarcode(), product2);

        Barcode barcode = new Barcode(new Numeral[] {Numeral.one, Numeral.two});
        BarcodedItem item = new BarcodedItem(barcode, new Mass(10));

        checkoutStationBronze.handheldScanner.scan(item);

        assertTrue(listenerBronze.getItemAdded());
    }

    @Test
    public void scanNoSession() {
        BarcodedProduct product1 = new BarcodedProduct(barcode1, "Item 1", 10, 15);
        BarcodedProduct product2 = new BarcodedProduct(barcode2, "Item 2", 5, 3);

        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product1.getBarcode(), product1);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product2.getBarcode(), product2);

        Barcode barcode = new Barcode(new Numeral[] {Numeral.one, Numeral.two});
        BarcodedItem item = new BarcodedItem(barcode, new Mass(10));
        checkoutStation.handheldScanner.scan(item);

        assertFalse(listener.getItemAdded());
    }

    @Test
    public void scanNoSessionSilver() {
      BarcodedProduct product1 = new BarcodedProduct(barcode1, "Item 1", 10, 15);
        BarcodedProduct product2 = new BarcodedProduct(barcode2, "Item 2", 5, 3);

        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product1.getBarcode(), product1);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product2.getBarcode(), product2);

        Barcode barcode = new Barcode(new Numeral[] {Numeral.one, Numeral.two});
        BarcodedItem item = new BarcodedItem(barcode, new Mass(10));
        checkoutStationSilver.handheldScanner.scan(item);

        assertFalse(listenerSilver.getItemAdded());
    }

    @Test
    public void scanNoSessionBronze() {
      BarcodedProduct product1 = new BarcodedProduct(barcode1, "Item 1", 10, 15);
        BarcodedProduct product2 = new BarcodedProduct(barcode2, "Item 2", 5, 3);

        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product1.getBarcode(), product1);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product2.getBarcode(), product2);

        Barcode barcode = new Barcode(new Numeral[] {Numeral.one, Numeral.two});
        BarcodedItem item = new BarcodedItem(barcode, new Mass(10));
        checkoutStationBronze.handheldScanner.scan(item);

        assertFalse(listenerBronze.getItemAdded());
    }
}