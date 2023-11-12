package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.software.AddItemViaHandheld;

import powerutility.PowerGrid;

public class AddItemViaHandheldTest {
    
    SelfCheckoutStationBronze checkoutStationBronze = new SelfCheckoutStationBronze();
    SelfCheckoutStationSilver checkoutStationSilver = new SelfCheckoutStationSilver();
    SelfCheckoutStationGold checkoutStation = new SelfCheckoutStationGold();

    AddItemViaHandheld testing = new AddItemViaHandheld(checkoutStation.handheldScanner);

    private BarcodeScannerListenerStub listener;

    /**
     * Test setup class
     */
    @Before
    public void setup() {
        //Activate bronze checkout station
        checkoutStationBronze.plugIn(PowerGrid.instance());
        checkoutStationBronze.turnOn();

        //Activate silver checkout station
        checkoutStationSilver.plugIn(PowerGrid.instance());
        checkoutStationSilver.turnOn();

        //Activate Gold (primary test) checkout station
        checkoutStation.plugIn(PowerGrid.instance());
        checkoutStation.turnOn();
        
        //Instantiate listener stub for BarcodeScanner and attach to scanner
        listener = new BarcodeScannerListenerStub();
        checkoutStation.handheldScanner.register(listener);
    }



    @Test
    public void add() {
        Barcode barcode = new Barcode(new Numeral[] {Numeral.one, Numeral.two, Numeral.three});
        BarcodedProduct product = new BarcodedProduct(barcode, "Product", 10, 15.0);
        testing.addItem(product);
        
        if(listener.itemScanned) {
            assertEquals(listener.barcodeScannerUsed, checkoutStation.handheldScanner);
            assertEquals(listener.barcodeScanned, barcode);
        }   
    }
}

/**
 * BarcodeScannerListener stub
 */
class BarcodeScannerListenerStub implements BarcodeScannerListener {

    public boolean itemScanned;
    public IBarcodeScanner barcodeScannerUsed;
    public Barcode barcodeScanned;

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
        itemScanned = true;
        barcodeScannerUsed = barcodeScanner;
        barcodeScanned = barcode;
    }

}
