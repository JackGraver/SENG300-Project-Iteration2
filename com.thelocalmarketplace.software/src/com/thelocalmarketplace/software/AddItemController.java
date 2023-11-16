package com.thelocalmarketplace.software;

import com.jjjwelectronics.Mass;

import powerutility.PowerGrid;

public class AddItemController implements BarcodeScannerListener, ElectronicScaleListener {
	public SelfCheckoutStation selfCheckoutStation;
	public PowerGrid powerGrid; 
	private long totalCost;
	private double totalWeight;
	private Mass actualWeight;
	private Mass expectedWeight;
	public ElectronicScale electronicScale;
	public StartSession startSession;
	
	public AddItemController (SelfCheckoutStation ss, PowerGrid pg, StartSession startSess) {
		selfCheckoutStation = ss;
		powerGrid = pg;
		ss.plugIn(pg);
		ss.turnOn();
		setTotalCost(0);
		setTotalWeight(0);
		
		ss.scanner.plugIn(pg);
		ss.scanner.turnOn();
		ss.scanner.register(this);
		//electronicScale = new ElectronicScale();
		
		ss.baggingArea.plugIn(pg);
		ss.baggingArea.turnOn();
		ss.baggingArea.register(this);
		
		startSession = startSess;
		
		

	}
	
	
	@Override
	public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode barcode) {
		if(startSession.isInSession()) {
			
			//try {
		       // synchronized (selfCheckoutStation) {
		            //selfCheckoutStation.wait(); // Block session
		       // }
		    //} catch (InterruptedException e) {
		       // e.printStackTrace();
		    //}
			
			Mass Before = null;
			
			try {
				Before = selfCheckoutStation.baggingArea.getCurrentMassOnTheScale();
			} catch (OverloadedDevice e) {
				e.printStackTrace();
			}
	
			 BarcodedProduct product = ProductDatabases.BARCODED_PRODUCT_DATABASE.get(barcode);
			 if (product != null) {
	
				 	System.out.println("Scanned: " + product.getDescription() + "Cost: "+ product.getPrice());	
		            setTotalCost(product.getPrice()+ getTotalCost());
		            
		            setTotalWeight(product.getExpectedWeight()+ getTotalWeight()); 		//updates expected weight
		            
		            System.out.println("Please place item in bagging area.");		//Signals to Customer to place item in bagging area
		            
		            try {															//Give Customer 5 seconds
		                Thread.sleep(1000);
		            } catch (InterruptedException e) {
		                e.printStackTrace();
		            }
		            
		            Mass After = null;
		            try {
						After = selfCheckoutStation.baggingArea.getCurrentMassOnTheScale();
					} catch (OverloadedDevice e) {
						e.printStackTrace();
					}
		            
		            if (Before.compareTo(After) != 0) {
		    			WeightDescrepency();
		    		}
		               
		            
			 } else {
			        System.out.println("Barcode not found: " + barcode.toString());
			 } 
			 
			 //synchronized (selfCheckoutStation) {
			       // selfCheckoutStation.notifyAll(); // Unblock session
			    //}
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


	@Override
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		expectedWeight = new Mass(getTotalWeight());
		if (expectedWeight.compareTo(mass) != 0) {
			WeightDescrepency();
		}
		
	}
	
	public boolean WeightDescrepency() {
	
		System.out.println("Discrepency found!");
		System.out.println("** Attend has been notified **");
		return true;
		
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
