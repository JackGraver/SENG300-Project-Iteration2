package com.thelocalmarketplace.software.printing;

import com.jjjwelectronics.OverloadedDevice;

/**
 * The `AttendantSimulation` class simulates the actions of an attendant in
 * response to various situations, such as adding ink or paper to the printer
 * and printing duplicate receipts. It interacts with the associated
 * `ReceiptPrinterController` to perform these actions.
 *
 * @author Raja Muhammed Omar
 */
public class AttendantSimulation {

	/** The `ReceiptPrinterController` associated with this simulation. */
	private ReceiptPrinterController rpc;

	/**
	 * Constructs a new `AttendantSimulation` with the specified
	 * `ReceiptPrinterController`.
	 *
	 * @param rpc The associated `ReceiptPrinterController`.
	 */
	public AttendantSimulation(ReceiptPrinterController rpc) {
		this.rpc = rpc;
	}

	/**
	 * Adds ink to the printer, handling the `OverloadedDevice` exception.
	 *
	 * @throws OverloadedDevice If the device is overloaded while adding ink.
	 */
	public void addInk() throws OverloadedDevice {
		rpc.scs.printer.addInk(1 << 20);
	}

	/**
	 * Adds paper to the printer, handling the `OverloadedDevice` exception.
	 *
	 * @throws OverloadedDevice If the device is overloaded while adding paper.
	 */
	public void addPaper() throws OverloadedDevice {
		rpc.scs.printer.addPaper(1 << 10);
	}

	/**
	 * Prints a duplicate receipt using the associated `ReceiptPrinterController`.
	 *
	 * @param receiptToPrint The receipt to be printed.
	 */
	public void printDuplicateReceipt(String receiptToPrint) {
		rpc.printReceipt(receiptToPrint);
	}
}
