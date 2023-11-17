package com.thelocalmarketplace.software;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.BarcodedProduct;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'aDeviceHasBeenEnabled'");
    }

    @Override
    public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'aDeviceHasBeenDisabled'");
    }

    @Override
    public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'aDeviceHasBeenTurnedOn'");
    }

    @Override
    public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'aDeviceHasBeenTurnedOff'");
    }

    @Override
    public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode barcode) {
        Mass mass = new Mass(ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode).getExpectedWeight());
        BarcodedItem item = new BarcodedItem(barcode, mass);
        barcodeScanner.scan(item);
    }

    /*
     * Add a BarcodedProduct by using SelfCheckoutStation secondary (handheld) scanner
     */
    // public void addItem(BarcodedProduct product) {
    //     BarcodedItem item = new BarcodedItem(product.getBarcode(), new Mass(product.getExpectedWeight()));
    //     scanner.scan(item);
    // }
}
