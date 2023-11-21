package com.thelocalmarketplace.software.printing;
import java.math.BigDecimal;
import java.util.Currency;

import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;

import powerutility.PowerGrid;


public class TemporaryClassForTestingReceiptPrinter {
    public static void main(String[] args) {
    	
    	BigDecimal[] banknoteDenominationsConfiguration = {new BigDecimal("5"),
                new BigDecimal("10"), new BigDecimal("20"),
                new BigDecimal("50"),new BigDecimal("100")};
        BigDecimal[] coinDenominationsConfigurationArray = {new BigDecimal("2"),
                new BigDecimal("1"), new BigDecimal("0.25"),
                new BigDecimal("0.1"),new BigDecimal("0.05")};


        SelfCheckoutStationGold.configureCurrency(Currency.getInstance("CAD"));
        SelfCheckoutStationGold.configureBanknoteDenominations(banknoteDenominationsConfiguration);
        SelfCheckoutStationGold.configureBanknoteStorageUnitCapacity(7);
        SelfCheckoutStationGold.configureCoinDenominations(coinDenominationsConfigurationArray);
        SelfCheckoutStationGold.configureCoinDispenserCapacity(5);
        SelfCheckoutStationGold.configureCoinTrayCapacity(5);
        SelfCheckoutStationGold.configureScaleMaximumWeight(5);
        SelfCheckoutStationGold.configureReusableBagDispenserCapacity(6);
        SelfCheckoutStationGold.configureScaleSensitivity(9);
        SelfCheckoutStationGold.configureCoinStorageUnitCapacity(5);

        SelfCheckoutStationGold stationGold = new SelfCheckoutStationGold();
        stationGold.plugIn(PowerGrid.instance());
        stationGold.turnOn();
        // Create and use the self-checkout station
        ReceiptPrinterController prc = new ReceiptPrinterController(stationGold);
        
        prc.printReceipt("This is a receipt");
        System.out.println(prc.getPrintedReceipt());
    }
}
