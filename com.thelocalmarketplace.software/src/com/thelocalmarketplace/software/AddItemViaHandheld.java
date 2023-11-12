package com.thelocalmarketplace.software;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scanner.BarcodeScannerBronze;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;

public class AddItemViaHandheld {

    SelfCheckoutStationBronze scs;

    public AddItemViaHandheld(SelfCheckoutStationBronze scs) {
        this.scs = scs;
    }

    public void addItem(BarcodedProduct product) {
        BarcodedItem item = new BarcodedItem(product.getBarcode(), new Mass(product.getExpectedWeight()));
        scs.mainScanner.scan(item);
    }
}
