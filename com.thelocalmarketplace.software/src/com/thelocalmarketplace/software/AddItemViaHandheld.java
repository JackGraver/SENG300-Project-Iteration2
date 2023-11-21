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
import com.thelocalmarketplace.software.AddItem.StartSession;

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
    private boolean itemAdded;

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
        if(StartSession.isInSession()) {

            AddItemWithDiscrepancyController.block();

            BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
            if(product != null) {
                double weight = product.getExpectedWeight();
                long cost = product.getPrice();
                
                AddItemWithDiscrepancyController addItemController = new AddItemWithDiscrepancyController(checkoutStation);
                addItemController.AddItemToHandleDiscrepancy(new BarcodedItem(barcode, new Mass(weight)));
            }
            AddItemWithDiscrepancyController.unblock();
            itemAdded = true;
        } else {
            itemAdded = false;
        }
    }

    public boolean getItemAdded() {
        return itemAdded;
    }
}
