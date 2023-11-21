package com.thelocalmarketplace.software.printing;

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

 * The `ReceiptPrinterController` class is responsible for controlling the printing process
 * in a self-checkout station. It interacts with the printer device and handles events related
 * to paper, ink, and device status. This class uses an attendant simulation to manage situations
 * where the printer is out of paper or ink.
 *
 */
public class ReceiptPrinterController implements ReceiptPrinterListener {

    /** The self-checkout station associated with this controller. */
    public AbstractSelfCheckoutStation scs;

    /** The receipt to be printed. */
    private String receiptToPrint;

    /** The attendant simulation for handling various events. */
    private AttendantSimulation attendant;

    /** The printed receipt after the printing process. */
    private String printedReceipt;

    /**
     * Constructs a new `ReceiptPrinterController` with the specified self-checkout station.
     *
     * @param scs The self-checkout station to associate with the controller.
     */
    public ReceiptPrinterController(AbstractSelfCheckoutStation scs) {
        this.scs = scs;
        this.scs.printer.register(this);
        this.attendant = new AttendantSimulation(this);
    }

    /**
     * Prints the specified receipt by interacting with the printer device.
     *
     * @param receipt The receipt to be printed.
     */
    public void printReceipt(String receipt) {
        setReceiptToPrint(receipt);
        for (int i = 0; i < receiptToPrint.length(); i++) {
            try {
                scs.printer.print(receiptToPrint.charAt(i));
            } catch (EmptyDevice e) {
                handleEmptyDevice(e);
            } catch (OverloadedDevice e) {
                handleOverloadedDevice(e);
            }
        }
        scs.printer.cutPaper();
        this.setPrintedReceipt(getReceiptToPrint().charAt(0) + scs.printer.removeReceipt());
    }

    /**
     * Gets the printed receipt after the printing process.
     *
     * @return The printed receipt.
     */
    public String getPrintedReceipt() {
        return this.printedReceipt;
    }

    /**
     * Gets the receipt to be printed.
     *
     * @return The receipt to be printed.
     */
    public String getReceiptToPrint() {
        return this.receiptToPrint;
    }

    /**
     * Sets the receipt to be printed.
     *
     * @param receiptToPrint The receipt to be printed.
     */
    public void setReceiptToPrint(String receiptToPrint) {
        this.receiptToPrint = receiptToPrint;
    }

    // Implementation of ReceiptPrinterListener interface methods

    @Override
    public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
        // Handle device enabled event if needed
    }

    @Override
    public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
        // Handle device disabled event if needed
    }

    @Override
    public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
        // Handle device turned on event if needed
    }

    @Override
    public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
        // Handle device turned off event if needed
    }

    @Override
    public void thePrinterIsOutOfPaper() {
        handleOutOfPaper();
    }

    @Override
    public void thePrinterIsOutOfInk() {
        handleOutOfInk();
    }

    @Override
    public void thePrinterHasLowInk() {
        // Handle low ink event if needed
    }

    @Override
    public void thePrinterHasLowPaper() {
        // Handle low paper event if needed
    }

    @Override
    public void paperHasBeenAddedToThePrinter() {
        // Handle paper added event if needed
    }

    @Override
    public void inkHasBeenAddedToThePrinter() {
        // Handle ink added event if needed
    }

    // Private methods for handling specific situations

    private void handleEmptyDevice(EmptyDevice e) {
        if (e.getMessage().equals("There is no paper in the printer.")) {
            handleOutOfPaper();
        } else {
            handleOutOfInk();
        }
    }

    private void handleOutOfPaper() {
        try {
            attendant.addPaper();
        } catch (OverloadedDevice e) {
            e.printStackTrace();
        }
        attendant.printDuplicateReceipt(getReceiptToPrint());
    }

    private void handleOutOfInk() {
        try {
            attendant.addInk();
        } catch (OverloadedDevice e) {
            e.printStackTrace();
        }
        attendant.printDuplicateReceipt(getReceiptToPrint());
    }

    private void handleOverloadedDevice(OverloadedDevice e) {
        System.out.println("Line too long");
    }

    private void setPrintedReceipt(String printedReceipt) {
        this.printedReceipt = printedReceipt;
    }
}
