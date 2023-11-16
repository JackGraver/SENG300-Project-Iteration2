package com.thelocalmarketplace.software.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.software.StartSession;

import powerutility.PowerGrid;

public class StartSessionTest {

    private SelfCheckoutStation selfCheckoutStation;
    private StartSession startSession;
    private PowerGrid powerGrid;

    
    
    @Before
    public void setUp() {
        selfCheckoutStation = new SelfCheckoutStation();
        startSession = new StartSession(selfCheckoutStation);
        powerGrid = PowerGrid.instance(); // Get the unique instance of PowerGrid
    }

   
    
    @Test
    public void testStartSession() {
        assertFalse(startSession.isInSession()); // Preconditions: System is not in a session

        startSession.startSession(powerGrid);

        assertTrue(startSession.isInSession());
    }

    
   
}
