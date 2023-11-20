package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Currency;

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

import powerutility.PowerGrid;

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

        //Activate bronze checkout station
        checkoutStationBronze.plugIn(PowerGrid.instance());
        checkoutStationBronze.turnOn();

        //Activate silver checkout station
        checkoutStationSilver.plugIn(PowerGrid.instance());
        checkoutStationSilver.turnOn();

        //Activate Gold (primary test) checkout station
        checkoutStation.plugIn(PowerGrid.instance());
        checkoutStation.turnOn();

        // ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product1.getBarcode(), product1);
        // ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product2.getBarcode(), product2);
        // System.out.println("test: " + ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode1));
    }



    @Test
    public void scanItemNormal() {
        BarcodedProduct product1 = new BarcodedProduct(barcode1, "Item 1", 10, 15);
        BarcodedProduct product2 = new BarcodedProduct(barcode2, "Item 2", 5, 3);

        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product1.getBarcode(), product1);
        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product2.getBarcode(), product2);
        // System.out.println("test: " + ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode1));

        Barcode barcode = new Barcode(new Numeral[] {Numeral.one, Numeral.two});
        BarcodedItem item = new BarcodedItem(barcode, new Mass(10));

        checkoutStation.handheldScanner.scan(item);
    }

    // @Test
    // public void scanNoSession() {
    //     BarcodedItem item = new BarcodedItem(barcode1, new Mass(5));
    //     checkoutStation.handheldScanner.scan(item);
    // }

    // @Test
    // public void testBlock() {
    //     //?
    // }

}