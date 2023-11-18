package com.thelocalmarketplace.software.addItem;

import java.util.Scanner;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scale.ElectronicScaleGold;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;

public class AddOwnBags extends AddItemController implements ElectronicScaleListener {
	boolean addBags = false;
	private Mass expectedWeight;

	public AddOwnBags() {
		ElectronicScaleGold goldScale = new ElectronicScaleGold();
		
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
		if (addBags == true) {
			expectedWeight = new Mass(getTotalWeight());
			setTotalWeight(expectedWeight);
			
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

}
