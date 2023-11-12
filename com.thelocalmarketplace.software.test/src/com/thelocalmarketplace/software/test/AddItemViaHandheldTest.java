package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.BarcodedItem;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.software.AddItemViaHandheld;

import powerutility.PowerGrid;

public class AddItemViaHandheldTest {
    
    SelfCheckoutStationBronze checkoutStation = new SelfCheckoutStationBronze();

    AddItemViaHandheld testing = new AddItemViaHandheld(checkoutStation);

    private BarcodeScannerListenerStub listener;

    @Before
    public void setup() {
        checkoutStation.plugIn(PowerGrid.instance());
        checkoutStation.turnOn();
        
        listener = new BarcodeScannerListenerStub();
        checkoutStation.mainScanner.register(listener);
    }



    @Test
    public void add() {
        Barcode barcode = new Barcode(new Numeral[] {Numeral.one, Numeral.two, Numeral.three});
        BarcodedProduct product = new BarcodedProduct(barcode, "Product", 10, 15.0);
        testing.addItem(product);
        
        assertTrue(listener.itemScanned);
    }
}


class BarcodeScannerListenerStub implements BarcodeScannerListener {

    public boolean itemScanned;

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
        System.out.println("listener activated");
        itemScanned = true;
    }

}
