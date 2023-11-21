package com.thelocalmarketplace.software.AddItem;

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
import com.jjjwelectronics.scanner.BarcodeScannerListener;
import com.jjjwelectronics.scanner.IBarcodeScanner;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import com.thelocalmarketplace.hardware.external.ProductDatabases;

import powerutility.PowerGrid;

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
public class AddItemWithDiscrepancyController implements BarcodeScannerListener, ElectronicScaleListener {
	public SelfCheckoutStationGold selfCheckoutStationGold;
	public SelfCheckoutStationBronze selfCheckoutStationBronze;
	public SelfCheckoutStationSilver selfCheckoutStationSilver;
	public AbstractSelfCheckoutStation selfCheckoutStation;
	public PowerGrid powerGrid; 
	private long totalCost;
	private double totalWeight;
	private Mass expectedWeight;
	public ElectronicScaleGold electronicScale;
	public StartSession startSession;
	public Mass currentMass;
	public boolean foundDiscrepancy;
	public boolean isEnabled;
	public boolean isOn;
	public boolean isOverloaded;
	public static boolean isBlocked = false;
	
	public AddItemWithDiscrepancyController (SelfCheckoutStationGold ssg, StartSession startSess) {
		this.selfCheckoutStation= ssg;
        this.selfCheckoutStation.mainScanner.register(this);
        this.selfCheckoutStation.baggingArea.register(this);
        
        
		
		startSession = startSess;
		isBlocked = false;
		foundDiscrepancy = false;
	
	}
	
	public AddItemWithDiscrepancyController (SelfCheckoutStationSilver sss, StartSession startSess ) {
		this.selfCheckoutStation= sss;
        this.selfCheckoutStation.mainScanner.register(this);
        this.selfCheckoutStation.baggingArea.register(this);
        
		
		startSession = startSess;
		isBlocked = false;
		foundDiscrepancy = false;
	
	}
	
	public AddItemWithDiscrepancyController (SelfCheckoutStationBronze ssb, StartSession startSess ) {
		this.selfCheckoutStation= ssb;
        this.selfCheckoutStation.mainScanner.register(this);
        this.selfCheckoutStation.baggingArea.register(this);
        
		
		startSession = startSess;
		isBlocked = false;
		foundDiscrepancy = false;
	
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
		if(this.startSession.isInSession() && !AddItemWithDiscrepancyController.isBlocked()) {	
			
			AddItemWithDiscrepancyController.block();			// Block further customer interaction 
			
			 BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
			 if (product != null) {
				 
		
				 	
		            setTotalCost(product.getPrice()+ getTotalCost());
		            
		            if (new Mass(product.getExpectedWeight()+ getTotalWeight()).compareTo(this.selfCheckoutStation.baggingArea.getMassLimit()) == -1) {
		            	setTotalWeight(product.getExpectedWeight()+ getTotalWeight()); 		//updates expected weight
		            	System.out.println("Please place '" + product.getDescription() +"' in bagging area.");		//Signals to Customer to place item in bagging area
		            }
		            else {
		            	System.out.println("Item too heavy. Do NOT place in bagging area!");
		            }
		           
		            
		            //Mass weight = new Mass(product.getExpectedWeight());
		            //System.out.println(weight.inMicrograms());
		        
		             
		            
			 } else {
			        System.out.println("Barcode not found: " + barcode.toString());
			 } 
		}
		AddItemWithDiscrepancyController.unblock();		//Unblock

	}
	
	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		isEnabled = true;
	}
	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		isEnabled = false;
		
	}
	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		isOn = true;
		
	}
	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		isOn = false;
		
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
		
		if (scale instanceof AbstractElectronicScale) {
		    try {
		        currentMass = ((AbstractElectronicScale) scale).getCurrentMassOnTheScale();
		        //System.out.println(currentMass.toString());
		    } catch (OverloadedDevice e) {
		    }
		    
		    CheckWeightDescrepency();
		}
		
	}


	
	/**
	 * Implements logic of weight discrepancy detected
	 *
	 * @param electronic scale hardware and mass on scale
	 * @return void
	 */
	public boolean CheckWeightDescrepency() {
		BigInteger difference;
		if (currentMass == null) {
			difference= new Mass(getTotalWeight()).inMicrograms();
		}
		else {
			difference = (new Mass(getTotalWeight())).inMicrograms().subtract(currentMass.inMicrograms()).abs();
		}
		
        
		//System.out.println((new Mass(getTotalWeight()).inMicrograms()).toString());
        //System.out.println(currentMass.inMicrograms().toString());
        
        BigInteger sens = selfCheckoutStation.baggingArea.getSensitivityLimit().inMicrograms();

        if (difference.compareTo(sens) > 0) {
			System.out.println("Discrepancy found! Attendant notified!");
			foundDiscrepancy = true;
			AddItemWithDiscrepancyController.block();
			return true;
		}
        else {
        	return false;
        }
        //System.out.println(difference);
        //System.out.println(sens);
        
		
	}
	
	/**
	 * Implements logic of handling weight discrepancy adding item (Option 1: add item)
	 *
	 * @param electronic scale hardware and mass on scale
	 * @return void
	 */
	public void AddItemToHandleDiscrepancy(Item itemToAdd){
		//System.out.println(itemToRemove.getMass().toString());
		System.out.println(currentMass);
		System.out.println(new Mass(getTotalWeight()));
		
		if (foundDiscrepancy) {	
			System.out.print(itemToAdd.getClass().getName());
			selfCheckoutStation.baggingArea.addAnItem(itemToAdd);
	
			if(!CheckWeightDescrepency()) {
				foundDiscrepancy = false; 
				System.out.println("Discrepancy SOLVED after removing item!");
			}
			
		}
		
	}
	
	
	/**
	 * Implements logic of handling weight discrepancy adding item (Option 2: do not bag item) 
	 *
	 * @param electronic scale hardware and mass on scale
	 * @return void
	 */
	public void DoNotBagToHandleDiscrepancy(Item notBag){
		AddItemWithDiscrepancyController.unblock();
		foundDiscrepancy = false;
		
		//Change expected total weight value if not bagging items
		//System.out.println(getTotalWeight());
		setTotalWeight(getTotalWeight() - notBag.getMass().inGrams().doubleValue());
		
		//System.out.println(getTotalWeight());
	
	}
	/**
	 * Implements logic of handling weight discrepancy adding item (Option 3: Attendant overrride) 
	 *
	 * @param none
	 * @return void
	 */
	public void AttendentOverrideToHandleDiscrepancy(){			
			AddItemWithDiscrepancyController.unblock();
			foundDiscrepancy = false;
			
			//Reset Expected Weight to match Actual Weight after override
			try {
		        currentMass = ((AbstractElectronicScale) selfCheckoutStation.baggingArea).getCurrentMassOnTheScale();
		        setTotalWeight(currentMass.inGrams().doubleValue());
		    } catch (OverloadedDevice e) {
			
		    	
			
		    }
	}

	@Override
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {
		isOverloaded = true;
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
