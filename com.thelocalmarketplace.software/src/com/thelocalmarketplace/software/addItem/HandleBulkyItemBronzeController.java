package com.thelocalmarketplace.software.AddItem;

import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import java.math.BigInteger;
import java.util.ArrayList;
import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

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
public class HandleBulkyItemBronzeController implements BarcodeScannerListener, ElectronicScaleListener{
	private final SelfCheckoutStationBronze scs;
	private boolean approval;
	private long totalCost;
	private double totalWeight;
	private boolean isBlocked;
	private boolean signal;
	private BarcodedProduct product;
	private ArrayList<Product> itemList = new ArrayList<>();
	private Mass expectedWeight;
	
	/** Handle Bulky Item Scenario
	 * 1. customer: adds an item(most likely through look up or handheld scan)
	 * 2. system: displays "Please place item in bagging area." along with a button that reads "I do not wish to bag this item."
	 * 3. customer presses the "I do not wish to bag this item." button
	 * 4. system: blocks customer interaction 
	 * 5. system: displays "** Attendant has been notified **"
	 * 6. attendant: approves customer's request
	 * 7. system: subtracts expected weight in the bagging area by the expected weight of the product
	 * 8. system: successfully adds item to the customers order
	 * 9. system: unblocks station
	 * 
	 * Exceptions:
	 * 1. if the customer adds item to the bagging area anyway the system will detect the weight of the item and compare it to the expected weight 
	 * which should be the same as if the they had added no item at all
	 * 2. Should the attendant not approve the request the system will display a "Request Denied. Please place item in bagging area."
	 * the system will then add the weight of the item to the total weight
	 */

	public HandleBulkyItemBronzeController(SelfCheckoutStationBronze checkout) {
		scs = checkout;
		approval = false;
		totalWeight = 0;
		isBlocked = false;
		
		checkout.baggingArea.register(this);
		checkout.mainScanner.register(this);

	}
	
	public void signalAttendant() {
		System.out.println(product.getDescription()); //intended to be displayed on attendants screen
		block();
		signal = true;
		System.out.println("** Attendant has been notified **");
	}
	
	public void setAttendantApproval(boolean choice) {
		approval = choice;
	}
	
	public boolean getAttendantApproval() {
		return approval;
	}
	
	@Override
	public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode barcode) {
		product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
		setTotalCost(product.getPrice() + getTotalCost());
		setTotalWeight(product.getExpectedWeight()+ getTotalWeight());
		itemList.add(product);	//add product to list of added items
		System.out.println("Please place the item in the bagging area");
		
	}
	
	public void reduceWeight() {	//assumes total weight has been set with the products actual weight
		double subWeight = product.getExpectedWeight();	//obtain expected weight which will be subtracted from the total weight
		setTotalWeight(getTotalWeight() - subWeight);	//total weight is set to be the same as if the product was never added
	}
	
	public void handleBulkyItem() {	//customer presses the "I do not wish to bag this item." button. Meant to be used after barcodeScanned
		signalAttendant();
		if (getAttendantApproval() == true) {	//checks if attendant approves of the request
			reduceWeight();	//if so reduce the expected weight of the item
			signal = false;
			unblock();	//unblock the station
		} else {
			System.out.println("Request Denied. Please place item in bagging area."); //if attendant does not approve the customer must add the item to the bagging area
		}
	}
	
	public boolean weightDiscreptancyCheck() {	//to be used after customers request has been approved
		
		Mass After = null;
		
		 try {
				After = ((AbstractElectronicScale) scs.baggingArea).getCurrentMassOnTheScale();
			} catch (OverloadedDevice e) {
				e.printStackTrace();
			}
		 
		 
		 Mass weight = new Mass(product.getExpectedWeight()*1000);
		 
         
		 if (getAttendantApproval() == false) {
			 weight = new Mass(0);
		 }
		 
       
         BigInteger difference = weight.inMicrograms().subtract(After.inMicrograms()).abs();
         BigInteger sens = scs.baggingArea.getSensitivityLimit().inMicrograms();

         if (difference.compareTo(sens) > 0) {
 			return true;
 		} else {
 			return false;
 		}
	}
	
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		System.out.println("Device enabled");
	}
	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		System.out.println("Device disabled");
		
	}
	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		System.out.println("Device turned on");
		
	}
	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		System.out.println("Device turned off");
	}
	
	public void setTotalWeight(double weightTotal) {
		totalWeight = weightTotal;
	}
	
	public double getTotalWeight() {
		return totalWeight;
	}
	
	public void setTotalCost(long cost) {
		totalCost = cost;
	}
	
	public long getTotalCost() {
		return totalCost;
	}
	
	public void block() {
        isBlocked = true;
    }

	public void unblock() {
		isBlocked = false;
	}

	public boolean isBlocked() {
		return isBlocked;
	}
	
	public boolean getSignal() {
		return signal;
	}
	

	@Override
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		expectedWeight = new Mass(getTotalWeight());
		
	}

	@Override
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {
		System.out.println("Scale overload! Please remove excess weight.");
		
	}

	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {
		System.out.println("You can continue scanning items.");
		
	}
}
