package com.thelocalmarketplace.software;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

/*
 * Jack Graver - 10187274
 * .
 * .
 * .
 */
public class AddItemViaHandheld implements BarcodeScannerListener {

    IBarcodeScanner scanner;

    /*
     * Constructor
     */
    public AddItemViaHandheld(IBarcodeScanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
    }

    @Override
    public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
    }

    @Override
    public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
    }

    @Override
    public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
    }

    @Override
    public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode barcode) {
        System.out.println("my implemented listener"); 
        //follow steps from use case
        //Block?

        //determine characterisitcs (weight and cost) of product associated with barcode

        //update expected weight from bagging area

        //Signal to Customer to place the scanned item in the bagging area

        //Signals to System the weight has changed

        //Unblock? 
    }

    /*
     * Add a BarcodedProduct by using SelfCheckoutStation secondary (handheld) scanner
     */
    // public void addItem(BarcodedProduct product) {
    //     BarcodedItem item = new BarcodedItem(product.getBarcode(), new Mass(product.getExpectedWeight()));
    //     scanner.scan(item);
    // }
}
