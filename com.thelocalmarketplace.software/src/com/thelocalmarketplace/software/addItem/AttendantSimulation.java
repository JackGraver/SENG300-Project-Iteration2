package com.thelocalmarketplace.software.addItem;

import com.jjjwelectronics.OverloadedDevice;

public class AttendantSimulation {
	
	private ReceiptPrinterController rpc;

	public AttendantSimulation(ReceiptPrinterController rpc) {
		this.rpc = rpc;
	}
	
	public void outOfInk() throws OverloadedDevice {
		rpc.scs.printer.addInk(1000);
		removePreviousReceipt();
		
	}
	
	public void outOfPaper() throws OverloadedDevice {
		rpc.scs.printer.addPaper(1000);
		removePreviousReceipt();
	}
	
	
	public void removePreviousReceipt() {
		rpc.scs.printer.cutPaper();
		rpc.scs.printer.removeReceipt();

	}

	public void reprintReceipt(String reciept) {
		rpc.printReceipt(reciept);
	}
}
