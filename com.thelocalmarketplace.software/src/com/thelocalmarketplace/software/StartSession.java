package com.thelocalmarketplace.software;

public class StartSession {

	
	
    private SelfCheckoutStation selfCheckoutStation;
    // Precondition: System is not in a session
    private boolean inSession = false;

    
    
    public StartSession(SelfCheckoutStation station) {
        selfCheckoutStation = station;
    }

    
    
    public void startSession(PowerGrid powerGrid) {

        // Plug in the devices to the power grid
        selfCheckoutStation.plugIn(powerGrid);

        // Turn on the devices
        selfCheckoutStation.turnOn();

        // Now, the system is in a session
        inSession = true;
    }

    
    
    public boolean isInSession() {
        return inSession;
    }
}