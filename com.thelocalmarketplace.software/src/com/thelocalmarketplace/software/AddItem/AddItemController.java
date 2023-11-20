package com.thelocalmarketplace.software.addItem.AddItem;
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
//import com.thelocalmarketplace.software.test.AddItemControllerTest.ConcreteItem;

import powerutility.PowerGrid;


public class AddItemController implements BarcodeScannerListener, ElectronicScaleListener {
	public SelfCheckoutStationGold selfCheckoutStation;
	public PowerGrid powerGrid; 
	private long totalCost;
	private double totalWeight;
	private Mass expectedWeight;
	public ElectronicScaleGold electronicScale;
	public StartSession startSession;
	
	public static boolean isBlocked = false;
	
	public AddItemController (SelfCheckoutStationGold ss, PowerGrid pg, StartSession startSess) {
		selfCheckoutStation = ss;
		powerGrid = pg;
		ss.plugIn(pg);
		ss.turnOn();
		setTotalCost(0);
		setTotalWeight(0);
		
		ss.mainScanner.plugIn(pg);
		ss.mainScanner.turnOn();
		ss.mainScanner.register(this);
		//electronicScale = new ElectronicScale();
		
		ss.baggingArea.plugIn(pg);
		ss.baggingArea.turnOn();
		ss.baggingArea.register(this);
		
		startSession = startSess;
		isBlocked = false;
	
	}
	
	/**
	 * Implements logic for scan of barcoded product 
	 *
	 * @param barcodeScanner hardware and barcode being scanned
	 * @return void
	 */
	
	
	@Override
	public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode barcode) {
		
		//Ignores logic if session has not been started or session is blocked 
		if(startSession.isInSession() && !AddItemController.isBlocked()) {	
			
			AddItemController.block();			// Block further customer interaction 
			
		
	
			 BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
			 if (product != null) {
				 
				 	class ConcreteItem extends Item {
					    public ConcreteItem(Mass mass) {
					        super(mass);
					    }
					}
		
		            setTotalCost(product.getPrice()+ getTotalCost());
		            
		            setTotalWeight(product.getExpectedWeight()+ getTotalWeight()); 		//updates expected weight
		            
		            System.out.println("Please place item in bagging area.");		//Signals to Customer to place item in bagging area
		            
		            Mass weight = new Mass(product.getExpectedWeight()*1000); 
		        	Item item = new ConcreteItem(weight);
		        	
		        	selfCheckoutStation.baggingArea.addAnItem(item);
		        	
		        	
		        	Mass After = null;
		        	
		            try {
						After = ((AbstractElectronicScale) selfCheckoutStation.baggingArea).getCurrentMassOnTheScale();
					} catch (OverloadedDevice e) {
						e.printStackTrace();
					}
		            
		            
		          
		            BigInteger difference = weight.inMicrograms().subtract(After.inMicrograms()).abs();
		            BigInteger sens = selfCheckoutStation.baggingArea.getSensitivityLimit().inMicrograms();

		            if (difference.compareTo(sens) > 0) {
		    			WeightDescrepency();
		    		}
		               
		            
			 } else {
			        System.out.println("Barcode not found: " + barcode.toString());
			 } 
		}
		AddItemController.unblock();		//Unblock

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
	public long getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(long l) {
		this.totalCost = l;
	}
	public double getTotalWeight() {
		return totalWeight;
	}
	public void setTotalWeight(double d) {
		this.totalWeight = d;
	}


	/**
	 * Implements logic for change of mass on scale
	 *
	 * @param electronic scale hardware and mass on scale
	 * @return void
	 */
	@Override
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		expectedWeight = new Mass(getTotalWeight());
		
	}
	
	/**
	 * Implements logic of weight discrepancy detected
	 *
	 * @param electronic scale hardware and mass on scale
	 * @return void
	 */
	public boolean WeightDescrepency() {
		System.out.println("Discrepency found!");
		System.out.println("** Attendant has been notified **");
		AddItemController.block();
		
		return true;
		
	}

	/**
	 * Implements logic of handling weight discrepancy adding item (Option 1: add/remove item)
	 *
	 * @param electronic scale hardware and mass on scale
	 * @return void
	 */
	public void AddOrRemoveItemToHandleDiscrepancy(){
		if (AddItemController.isBlocked()) {				
			expectedWeight = new Mass(getTotalWeight());
			
			try {
				if (expectedWeight.compareTo(((AbstractElectronicScale) selfCheckoutStation.baggingArea).getCurrentMassOnTheScale()) == 0) {
					AddItemController.unblock();	
				}
				else {
					WeightDescrepency();
				}
			} catch (OverloadedDevice e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Implements logic of handling weight discrepancy adding item (Option 2: do not bag item) 
	 *
	 * @param electronic scale hardware and mass on scale
	 * @return void
	 */
	public void DoNotBagToHandleDiscrepancy(boolean doNotBag){
		if (AddItemController.isBlocked()) {				
			if (doNotBag) {
				AddItemController.unblock();	
			}
			else {
				WeightDescrepency();
			}
		}
	}
	
	/**
	 * Implements logic of handling weight discrepancy adding item (Option 3: Attendant overrride) 
	 *
	 * @param electronic scale hardware and mass on scale
	 * @return void
	 */
	public void AttendentOverrideToHandleDiscrepancy(boolean override){
		if (AddItemController.isBlocked()) {				
			if (override) {
				AddItemController.unblock();	
			}
			else {
				WeightDescrepency();
			}
		
		}
	}

	@Override
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {
		System.out.println("Scale overload! Please remove excess weight.");
	}


	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {
		System.out.println("You can continue scanning items.");
		
	}
	
	/**
	 * Setter to block session
	 *
	 * @return void
	 */
	 public static void block() {
	        isBlocked = true;
	    }

	 /**
	 * Setter to unblock session
	 *
	 * @return void
	 */
	 public static void unblock() {
		 isBlocked = false;
	 }
	
	 /**
	 * Getting for block status
	 *
	 * @return void
	 */
	public static boolean isBlocked() {
        return isBlocked;
    }
}

