package com.thelocalmarketplace.software.addItem;

import java.util.Scanner;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.Mass;
import com.jjjwelectronics.scale.ElectronicScaleGold;
import com.jjjwelectronics.scale.ElectronicScaleListener;
import com.jjjwelectronics.scale.IElectronicScale;

public class AddOwnBags implements ElectronicScaleListener {
	boolean addBags = false;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void theMassOnTheScaleHasExceededItsLimit(IElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void theMassOnTheScaleNoLongerExceedsItsLimit(IElectronicScale scale) {
		// TODO Auto-generated method stub
		
	}

}
