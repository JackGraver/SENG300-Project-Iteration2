/* 
 * Christopher Thomson - 30186596
 * Darpal Patel - 30088795
 * 
 * 
 */

package com.thelocalmarketplace.software.AddItem;

import java.util.Scanner;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.OverloadedDevice;
import com.jjjwelectronics.scale.AbstractElectronicScale;
import com.jjjwelectronics.scale.ElectronicScaleBronze;
import com.jjjwelectronics.scale.ElectronicScaleGold;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.ElectronicScaleSilver;
import com.jjjwelectronics.scale.IElectronicScale;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;

public class AddOwnBags extends AddItemWithDiscrepancyController {
	public boolean addBags = false;
	public boolean nextItemIsBags = false;
	SelfCheckoutStationGold stationGold;
	

	public AddOwnBags(SelfCheckoutStationGold ssg, StartSession startSess) {
		super(ssg);
		this.stationGold = ssg;   
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