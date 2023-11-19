package com.thelocalmarketplace.software;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.addItem.AddItemController;

/*
 * Jack Graver - 10187274
 * .
 * .
 * .
 */
public class AddItemViaHandheld implements BarcodeScannerListener {

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

        //not allow scan if customer session in progress already

        //following steps from use case:
        //Block?

        BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
        if(product != null) {
            //determine characterisitcs (weight and cost) of product associated with barcode
            double weight = product.getExpectedWeight();
            long cost = product.getPrice();
            
            //update expected weight from bagging area
                //use add item controller?
                //not sure how to update weight otherwise
                //handle weight discrepancy?

            //Signal to Customer to place the scanned item in the bagging area

            //Signals to System the weight has changed
        }

        //Unblock? 
    }
}
