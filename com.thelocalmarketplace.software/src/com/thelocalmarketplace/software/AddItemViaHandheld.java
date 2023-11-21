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
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.AddItem.AddItemWithDiscrepancyController;

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
public class AddItemViaHandheld implements BarcodeScannerListener {

    private AbstractSelfCheckoutStation checkoutStation;

    public AddItemViaHandheld(AbstractSelfCheckoutStation checkoutStation) {
        this.checkoutStation = checkoutStation;
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

        //not allow scan if customer session in progress already
            //Need to check if StartSession inSession variable is true, either needs to be static or some type of implementation like singleton

        //following steps from use case:
        //Block?
        AddItemWithDiscrepancyController.block();

        BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
        if(product != null) {
            //determine characterisitcs (weight and cost) of product associated with barcode
            double weight = product.getExpectedWeight();
            long cost = product.getPrice();
            
            //update expected weight from bagging area
                checkoutStation.baggingArea.addAnItem(new BarcodedItem(barcode, new Mass(weight)));
                //use add item controller?
                //not sure how to update weight otherwise
                //handle weight discrepancy?

            //Signal to Customer to place the scanned item in the bagging area

            //Signals to System the weight has changed
        }

        //Unblock? 
        AddItemWithDiscrepancyController.unblock();
    }
}
