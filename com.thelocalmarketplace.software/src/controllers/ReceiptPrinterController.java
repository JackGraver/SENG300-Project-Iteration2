package controllers;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.ReceiptPrinterListener;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
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
public class ReceiptPrinterController implements ReceiptPrinterListener {

	public AbstractSelfCheckoutStation scs;
	private String receiptToPrint;
	private AttendantSimulation attendant;
	private String printedReceipt;

	public ReceiptPrinterController(AbstractSelfCheckoutStation scs) {
		this.scs = scs;
		this.scs.printer.register(this);
		this.attendant = new AttendantSimulation(this);
	}

	public void printReceipt(String receipt) {
		setReceiptToPrint(receipt);
		for (int i = 0; i < receiptToPrint.length(); i++) {
			try {
				scs.printer.print(receiptToPrint.charAt(i));

			} catch (EmptyDevice e) {
				if (e.getMessage().equals("There is no paper in the printer.")) {
					thePrinterIsOutOfPaper();
				} else {
					thePrinterIsOutOfInk();

				}
			} catch (OverloadedDevice e) {

				System.out.println("Line too long");
			}

		}

		scs.printer.cutPaper();
		this.setPrintedReceipt(getReceiptToPrint().charAt(0) + scs.printer.removeReceipt());

	}

	private void setPrintedReceipt(String printedReceipt) {
		this.printedReceipt = printedReceipt;
	}

	public String getPrintedReceipt() {
		return this.printedReceipt;
	}

	public String getReceiptToPrint() {
		return this.receiptToPrint;
	}

	public void setReceiptToPrint(String receiptToPrint) {
		this.receiptToPrint = receiptToPrint;
	}

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void thePrinterIsOutOfPaper() {
		try {
			attendant.addPaper();
		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		attendant.printDuplicateReceipt(getReceiptToPrint());

	}

	@Override
	public void thePrinterIsOutOfInk() {
		try {
			attendant.addInk();
		} catch (OverloadedDevice e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		attendant.printDuplicateReceipt(getReceiptToPrint());

	}

	@Override
	public void thePrinterHasLowInk() {
		// TODO Auto-generated method stub

	}

	@Override
	public void thePrinterHasLowPaper() {
		// TODO Auto-generated method stub

	}

	@Override
	public void paperHasBeenAddedToThePrinter() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inkHasBeenAddedToThePrinter() {
		// TODO Auto-generated method stub

	}

}