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
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {}

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
		AddItemWithDiscrepancyController.block();
		
	}

	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {
		System.out.println("You can continue scanning items.");
		AddItemWithDiscrepancyController.unblock();
		
	}
	
	public boolean WeightDescrepency() {
		System.out.println("Discrepency found!");
		System.out.println("** Attendant has been notified **");
		AddItemWithDiscrepancyController.block();
		
		return true;
		
	}
	
	public void AttendentOverrideToHandleDiscrepancy(boolean override){
		if (AddItemWithDiscrepancyController.isBlocked()) {				
			if (override) {
				AddItemWithDiscrepancyController.unblock();	
			}
			else {
				WeightDescrepency();
			}
		
		}
	}
}
