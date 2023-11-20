package com.thelocalmarketplace.software.addItem;

import com.jjjwelectronics.OverloadedDevice;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;

public class AttendantSimulation {
	
	private SelfCheckoutStationGold scsg;
	private ReceiptPrinterController rpc;

	public AttendantSimulation(SelfCheckoutStationGold scsg) {
		this.scsg = scsg;
	}
	
	public void outOfInk() throws OverloadedDevice {
		scsg.printer.addInk(1 << 10);
	}
	
	public void outOfPaper() throws OverloadedDevice {
		scsg.printer.addPaper(1 << 20);
	}
	
	public void printDuplicateReceipt() {
		rpc.printReceipt(rpc.recieptToPrint);
	}
}
