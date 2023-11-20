package com.thelocalmarketplace.software.addItem;

import com.jjjwelectronics.EmptyDevice;
import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.printer.ReceiptPrinterListener;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;

import powerutility.PowerGrid;

public class ReceiptPrinterController implements ReceiptPrinterListener {

	private SelfCheckoutStationBronze scs;
	public String recieptToPrint;

	public ReceiptPrinterController(SelfCheckoutStationBronze scs) {
		this.scs = scs;
		this.scs.printer.register(this);
	}


	public void printReceipt(String reciept) {
		for (int i = 0; i < reciept.length(); i++) {
			try {
				scs.printer.print(reciept.charAt(i));
				
			} catch (EmptyDevice e) {
				
				System.out.println("Out of ink or paper");

			} catch (OverloadedDevice e) {
				
				System.out.println("Line too long");
			}

		}
	}

	public String getReciept() {
		this.scs.printer.cutPaper();
		return this.scs.printer.removeReceipt();
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
		// TODO Auto-generated method stub

	}

	@Override
	public void thePrinterIsOutOfInk() {
		// TODO Auto-generated method stub

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
