package com.thelocalmarketplace.software;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.BarcodedProduct;

/*
 * Jack Graver - 10187274
 * .
 * .
 * .
 */
public class AddItemViaHandheld {

    IBarcodeScanner scanner;

    /*
     * Constructor
     */
    public AddItemViaHandheld(IBarcodeScanner scanner) {
        this.scanner = scanner;
    }

    /*
     * Add a BarcodedProduct by using SelfCheckoutStation secondary (handheld) scanner
     */
    public void addItem(BarcodedProduct product) {
        BarcodedItem item = new BarcodedItem(product.getBarcode(), new Mass(product.getExpectedWeight()));
        scanner.scan(item);
    }
}
