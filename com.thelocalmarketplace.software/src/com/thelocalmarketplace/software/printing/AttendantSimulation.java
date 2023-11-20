package com.thelocalmarketplace.software.printing;

import com.jjjwelectronics.OverloadedDevice;

public class AttendantSimulation {
	
	private ReceiptPrinterController rpc;

	public AttendantSimulation(ReceiptPrinterController rpc) {
		this.rpc = rpc;
	}
	
	public void addInk() throws OverloadedDevice {
		rpc.scs.printer.addInk(1 << 20);
		
	}
	
	public void addPaper() throws OverloadedDevice {
		rpc.scs.printer.addPaper(1 << 10);
	}
	
	public void printDuplicateReceipt(String receiptToPrint) {
		rpc.printReceipt(receiptToPrint);
	}
	
}
