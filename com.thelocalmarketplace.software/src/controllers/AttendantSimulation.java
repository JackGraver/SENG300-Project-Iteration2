package controllers;

import com.jjjwelectronics.OverloadedDevice;

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