package com.thelocalmarketplace.software.AddItem;

import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.jjjwelectronics.scale.IElectronicScale;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
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
public class AddOwnBags extends AddItemWithDiscrepancyController {
	public boolean addBags = false;
	public boolean nextItemIsBags = false;
	AbstractSelfCheckoutStation checkoutStation;
	

	public AddOwnBags(AbstractSelfCheckoutStation checkoutStation, StartSession startSess) {
		super(checkoutStation);
		this.checkoutStation = checkoutStation;   
        //this.stationGold.baggingArea.register(this);
	
	}
	
	public void chooseAddOwnBag(boolean addBags){
		//Precondition of system is ready to detect weight discrepancy
		if (StartSession.isInSession() && !isBlocked()) {
			if (addBags) {
				System.out.println("Place your bags in bagging area");
				//manageAddBags(); 
				nextItemIsBags = true;
  
			}else {
				System.out.println("You selected not to add bags");
			}
				
		}
		
	}
	
	public void manageAddBags() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		if (nextItemIsBags) {
			if (scale instanceof AbstractElectronicScale) {
			    try {
			        currentMass = ((AbstractElectronicScale) scale).getCurrentMassOnTheScale();
			        //System.out.println(currentMass.toString());
			    } catch (OverloadedDevice e) {
			    }	   
			}
			if (scale instanceof AbstractElectronicScale) {
			    try {
			       setTotalWeight(((AbstractElectronicScale) scale).getCurrentMassOnTheScale().inGrams().doubleValue());
			       //System.out.println(currentMass.toString());
			       //System.out.println(getTotalWeight());
			    } catch (OverloadedDevice e) {
			   }
				
				nextItemIsBags = false;
        }
		}
		else {
			super.theMassOnTheScaleHasChanged(scale, mass);
        }
		
	}

		

}