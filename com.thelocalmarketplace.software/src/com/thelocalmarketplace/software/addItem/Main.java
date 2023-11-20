package com.thelocalmarketplace.software.addItem;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;


public class Main {
    public static void main(String[] args) {

        // Create and use the self-checkout station
        ReceiptPrinterController prc = new ReceiptPrinterController(new SelfCheckoutStationBronze());
        prc.printReceipt("This is a receipt");
        System.out.println(prc.getReciept());
    }
}
