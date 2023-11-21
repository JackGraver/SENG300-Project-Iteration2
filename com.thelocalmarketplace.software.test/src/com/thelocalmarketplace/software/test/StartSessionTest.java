package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.card.InvalidPINException;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.software.AddItem.*;

import powerutility.PowerGrid;

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
		}

        assertTrue(startSession.isInSession());
    }

    
   
}
