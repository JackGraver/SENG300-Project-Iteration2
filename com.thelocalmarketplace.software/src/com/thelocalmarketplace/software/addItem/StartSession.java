package com.thelocalmarketplace.software.addItem;


import java.io.IOException;
import com.jjjwelectronics.card.InvalidPINException;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.software.printing.ReceiptPrinterController;

import powerutility.PowerGrid;

public class StartSession {
	
//	private PayViaSwipeCreditBronze cc;
	private ReceiptPrinterController prc;
	private AbstractSelfCheckoutStation selfCheckoutStation;
	// Precondition: System is not in a session
	private boolean inSession = false;

	public StartSession(AbstractSelfCheckoutStation station) {
		selfCheckoutStation = station;
	}

	/**
	 * Starts session
	 * 
	 * @param powergrid
	 * @return void
	 * @throws InsufficientFundsException 
	 * @throws InvalidPINException 
	 * @throws IOException 
	 */
	public void startSession(PowerGrid powerGrid) throws InvalidPINException, IOException {
		selfCheckoutStation.plugIn(powerGrid); // Plug in the devices to the power grid

		selfCheckoutStation.turnOn(); // Turn on the devices

		inSession = true;

		
	}
	
	public boolean isInSession() {
		return inSession;
	} 
	
}