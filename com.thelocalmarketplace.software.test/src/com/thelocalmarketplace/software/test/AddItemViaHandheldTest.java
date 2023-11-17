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
import com.thelocalmarketplace.software.AddItemViaHandheld;

import powerutility.PowerGrid;

public class AddItemViaHandheldTest {
    
    SelfCheckoutStationBronze checkoutStationBronze;
    SelfCheckoutStationSilver checkoutStationSilver;
    SelfCheckoutStationGold checkoutStation;

    AddItemViaHandheld toTestListener;

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

        toTestListener = new AddItemViaHandheld(checkoutStation.handheldScanner);
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
        
        //Instantiate listener stub for BarcodeScanner and attach to scanner
        // listener = new BarcodeScannerListenerStub();
        // checkoutStation.handheldScanner.register(listener);
    }



    @Test
    public void add() {
        Barcode barcode = new Barcode(new Numeral[] {Numeral.one, Numeral.two, Numeral.three});
        BarcodedItem item = new BarcodedItem(barcode, new Mass(10));

        checkoutStation.handheldScanner.scan(item);
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
