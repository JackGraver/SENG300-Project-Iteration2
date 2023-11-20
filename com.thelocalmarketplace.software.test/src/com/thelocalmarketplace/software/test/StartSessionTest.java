package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.card.InvalidPINException;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.addItem.*;

import powerutility.PowerGrid;




public class StartSessionTest {

    private SelfCheckoutStationGold selfCheckoutStation;
    private StartSession startSession;
    private PowerGrid powerGrid;

    
    
    @Before
    public void setUp() {
        selfCheckoutStation = new SelfCheckoutStationGold();
        startSession = new StartSession(selfCheckoutStation);
        powerGrid = PowerGrid.instance(); // Get the unique instance of PowerGrid
    }

   
    
    @Test
    public void testStartSession() {
        assertFalse(startSession.isInSession()); // Preconditions: System is not in a session

        try {
			startSession.startSession(powerGrid);
		} catch (InvalidPINException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        assertTrue(startSession.isInSession());
    }

    
   
}
