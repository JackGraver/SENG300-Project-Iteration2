package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.jjjwelectronics.scanner.IBarcodeScanner;
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

    AddItemViaHandheld toTestListener;


    Barcode barcode1 = new Barcode(new Numeral[]{Numeral.one, Numeral.two});
    Barcode barcode2 = new Barcode(new Numeral[]{Numeral.three, Numeral.four});

    /**
     * Test setup class
     */
    @Before
    public void setup() {
        SelfCheckoutStationGold.configureCurrency(Currency.getInstance("CAD"));
        SelfCheckoutStationGold.configureCoinDenominations(new BigDecimal[] {new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25")});
        SelfCheckoutStationGold.configureBanknoteDenominations(new BigDecimal[] {new BigDecimal("1"), new BigDecimal("2")});
        SelfCheckoutStationGold.configureCoinStorageUnitCapacity(5);
        SelfCheckoutStationGold.configureCoinDispenserCapacity(5);
        SelfCheckoutStationGold.configureBanknoteStorageUnitCapacity(5);
        SelfCheckoutStationGold.configureCoinTrayCapacity(5);

        // SelfCheckoutStationBronze.configureCurrency(Currency.getInstance("CAD"));
        // SelfCheckoutStationBronze.configureCoinDenominations(new BigDecimal[] {new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"), new BigDecimal("1"), new BigDecimal("2")});

        // SelfCheckoutStationSilver.configureCurrency(Currency.getInstance("CAD"));
        // SelfCheckoutStationSilver.configureCoinDenominations(new BigDecimal[] {new BigDecimal("0.05"), new BigDecimal("0.10"), new BigDecimal("0.25"), new BigDecimal("1"), new BigDecimal("2")});


        // checkoutStationBronze = new SelfCheckoutStationBronze();
        // checkoutStationSilver = new SelfCheckoutStationSilver();
        checkoutStation = new SelfCheckoutStationGold();

        // toTestListener = new AddItemViaHandheld(checkoutStation.handheldScanner);
        checkoutStation.handheldScanner.register(toTestListener);
        

        

        

        //Activate bronze checkout station
        // checkoutStationBronze.plugIn(PowerGrid.instance());
        // checkoutStationBronze.turnOn();

        //Activate silver checkout station
        // checkoutStationSilver.plugIn(PowerGrid.instance());
        // checkoutStationSilver.turnOn();

        //Activate Gold (primary test) checkout station
        checkoutStation.plugIn(PowerGrid.instance());
        checkoutStation.turnOn();

        // Barcode barcode1 = new Barcode(new Numeral[]{Numeral.one, Numeral.two});
        // Barcode barcode2 = new Barcode(new Numeral[]{Numeral.three, Numeral.four});

        // BarcodedProduct product1 = new BarcodedProduct(barcode1, "Item 1", 10, 15);
        // BarcodedProduct product2 = new BarcodedProduct(barcode2, "Item 2", 5, 3);

        // ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product1.getBarcode(), product1);
        // ProductDatabases.BARCODED_PRODUCT_DATABASE.put(product2.getBarcode(), product2);
        // System.out.println("test: " + ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode1));

        //Instantiate listener stub for BarcodeScanner and attach to scanner
        // listener = new BarcodeScannerListenerStub();
        // checkoutStation.handheldScanner.register(listener);
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

    @Test
    public void scanNullBarcode() {
        Barcode barcodeNull = null;
        BarcodedProduct nullBarcodeProduct = new BarcodedProduct(barcodeNull, "Null Barcode Itemn", 0, 1);

        ProductDatabases.BARCODED_PRODUCT_DATABASE.put(nullBarcodeProduct.getBarcode(), nullBarcodeProduct);

        BarcodedItem nullBarcodeItem = new BarcodedItem(barcodeNull, new Mass(5));
        checkoutStation.handheldScanner.scan(nullBarcodeItem);
    }

    @Test
    public void scanNoSession() {
        BarcodedItem item = new BarcodedItem(barcode1, new Mass(5));
        checkoutStation.handheldScanner.scan(item);
    }

    @Test
    public void testBlock() {
        //?
    }

}