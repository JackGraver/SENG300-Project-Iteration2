/* 
 * Christopher Thomson - 30186596
 * 
 * 
 * 
 */

package com.thelocalmarketplace.software.addItem;

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

public class AddOwnBags extends AbstractElectronicScale implements ElectronicScaleListener {
	boolean addBags = false;
	

	public AddOwnBags() {
		
		super(massLimit, sensitivityLimit);
		
	}
	
	public boolean addBagsChoice(boolean addBags) {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Would you like to add your own bags");
			System.out.println("1 - Yes");
			System.out.println("2 - No");
			String userInput = scanner.nextLine();
			if (userInput == "1") {
				addBags = true; 
				return addBags;
			}
		}
		return addBags;
	}

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		
		
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
	public void theMassOnTheScaleHasChanged(IElectronicScale scale, Mass mass) {
		if (addBags) {
			try {
				getCurrentMassOnTheScale();
			} catch (OverloadedDevice e) {
				// TODO Auto-generated catch block
				notifyOverload();
			}
		} else {
			WeightDescrepency();
		}
		
	}

	@Override
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {
		System.out.println("Scale overload! Please remove excess weight.");
		AddItemController.block();
		
	}

	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {
		System.out.println("You can continue scanning items.");
		AddItemController.unblock();
		
	}
	
	public boolean WeightDescrepency() {
		System.out.println("Discrepency found!");
		System.out.println("** Attendant has been notified **");
		AddItemController.block();
		
		return true;
		
	}
	
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
	
	

}
