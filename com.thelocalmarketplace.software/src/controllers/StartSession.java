package controllers;

import com.thelocalmarketplace.hardware.SelfCheckoutStation;
import powerutility.PowerGrid;

public class StartSession {

	
	
    private SelfCheckoutStation selfCheckoutStation;
    // Precondition: System is not in a session
    private boolean inSession = false;

    
    
    public StartSession(SelfCheckoutStation station) {
        selfCheckoutStation = station;
    }

    
    /**
     * Starts session
     * @param powergrid 
     * @return void
     */
    public void startSession(PowerGrid powerGrid) { 	
        selfCheckoutStation.plugIn(powerGrid); 	// Plug in the devices to the power grid

        
        selfCheckoutStation.turnOn();		// Turn on the devices

        inSession = true;
    
 	
        
    }

    
    
    public boolean isInSession() {
        return inSession;
    }
}