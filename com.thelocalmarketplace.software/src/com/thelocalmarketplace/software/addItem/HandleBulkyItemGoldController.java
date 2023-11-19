package controllers;

import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;
import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Item;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.jjjwelectronics.scale.ElectronicScaleGold;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;
import com.jjjwelectronics.scanner.Barcode;
import com.jjjwelectronics.scanner.BarcodeScannerGold;
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

public class HandleBulkyItemGoldController{
	private final SelfCheckoutStationGold bronzeStation;
	private boolean approval;
	private double totalWeight;
	private boolean isBlocked;
	
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

	public HandleBulkyItemGoldController(SelfCheckoutStationGold checkout) {
		bronzeStation = checkout;
		approval = false;
		totalWeight = 0;
		
	}
	
	public void signalAttendant(Barcode barcode) {
		BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
		System.out.println(product.getDescription()); //intended to be displayed of attendants screen
		block();
		System.out.println("** Attendant has been notified **");
	}
	
	public void setAttendantApproval(boolean choice) {
		approval = choice;
	}
	
	public boolean getAttendantApproval() {
		return approval;
	}
	
	public void reduceWeight(Barcode barcode) {	//assumes total weight has been set with the products actual weight
		BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);	//obtain product via barcode
		double subWeight = product.getExpectedWeight();	//obtain expected weight which will be subtracted from the total weight
		setTotalWeight(getTotalWeight() - subWeight);	//total weight is set to be the same as if the product was never added
	}
	
	public void handleBulkyItem(Barcode barcode) {	//customer presses the "I do not wish to bag this item." button
		signalAttendant(barcode);
		if (getAttendantApproval() == true) {	//checks if attendant approves of the request
			reduceWeight(barcode);	//if so reduce the expected weight of the item
			unblock();
		} else {
			System.out.println("Request Denied. Please place item in bagging area.");
		}
	}
	
	public boolean weightDiscreptancyCheck() {
		Mass before = new Mass(getTotalWeight());
		
		Mass After = null;
		
		try {
			After =  ((AbstractElectronicScale) bronzeStation.baggingArea).getCurrentMassOnTheScale();
		} catch (OverloadedDevice e) {
			e.printStackTrace();
		}
		
		if (After.compareTo(before) != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setTotalWeight(double weightTotal) {
		totalWeight = weightTotal;
	}
	
	public double getTotalWeight() {
		return totalWeight;
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
}
