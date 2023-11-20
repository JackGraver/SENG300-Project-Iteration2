package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.*;

import com.thelocalmarketplace.software.StartSession;
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

        startSession.startSession(powerGrid);

        assertTrue(startSession.isInSession());
    }

    
   
}
