package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;

import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;

import com.thelocalmarketplace.software.StartSession;
import powerutility.PowerGrid;




public class StartSessionTest {

    
    PowerGrid powerGrid;
    SelfCheckoutStationGold stationGold;
	SelfCheckoutStationSilver stationSilver;
	SelfCheckoutStationBronze stationBronze;
	StartSession testObject;

	BigDecimal[] banknoteDenominationsConfiguration = {new BigDecimal("5"),
            new BigDecimal("10"), new BigDecimal("20"),
            new BigDecimal("50"),new BigDecimal("100")};
    BigDecimal[] coinDenominationsConfigurationArray = {new BigDecimal("2"),
            new BigDecimal("1"), new BigDecimal("0.25"),
            new BigDecimal("0.1"),new BigDecimal("0.05")} ;
 
    

    @Before
    public void setUp() {
    	SelfCheckoutStationGold.configureCurrency(Currency.getInstance("CAD"));
        SelfCheckoutStationGold.configureBanknoteDenominations(banknoteDenominationsConfiguration);
        SelfCheckoutStationGold.configureBanknoteStorageUnitCapacity(7);
        SelfCheckoutStationGold.configureCoinDenominations(coinDenominationsConfigurationArray);
        SelfCheckoutStationGold.configureCoinDispenserCapacity(5);
        SelfCheckoutStationGold.configureCoinTrayCapacity(5);
        SelfCheckoutStationGold.configureScaleMaximumWeight(5);
        SelfCheckoutStationGold.configureReusableBagDispenserCapacity(6);
        SelfCheckoutStationGold.configureScaleSensitivity(9);
        SelfCheckoutStationGold.configureCoinStorageUnitCapacity(5);
        
        stationGold = new SelfCheckoutStationGold(); 
        
        
        SelfCheckoutStationSilver.configureCurrency(Currency.getInstance("CAD"));
        SelfCheckoutStationSilver.configureBanknoteDenominations(banknoteDenominationsConfiguration);
        SelfCheckoutStationSilver.configureBanknoteStorageUnitCapacity(7);
        SelfCheckoutStationSilver.configureCoinDenominations(coinDenominationsConfigurationArray);
        SelfCheckoutStationSilver.configureCoinDispenserCapacity(5);
        SelfCheckoutStationSilver.configureCoinTrayCapacity(5);
        SelfCheckoutStationSilver.configureScaleMaximumWeight(5);
        SelfCheckoutStationSilver.configureReusableBagDispenserCapacity(6);
        SelfCheckoutStationSilver.configureScaleSensitivity(9);
        SelfCheckoutStationSilver.configureCoinStorageUnitCapacity(5);
        
        stationSilver = new SelfCheckoutStationSilver(); 
        
         

        SelfCheckoutStationBronze.configureCurrency(Currency.getInstance("CAD"));
        SelfCheckoutStationBronze.configureBanknoteDenominations(banknoteDenominationsConfiguration);
        SelfCheckoutStationBronze.configureBanknoteStorageUnitCapacity(7);
        SelfCheckoutStationBronze.configureCoinDenominations(coinDenominationsConfigurationArray);
        SelfCheckoutStationBronze.configureCoinDispenserCapacity(5);
        SelfCheckoutStationBronze.configureCoinTrayCapacity(5);
        SelfCheckoutStationBronze.configureScaleMaximumWeight(5);
        SelfCheckoutStationBronze.configureReusableBagDispenserCapacity(6);
        SelfCheckoutStationBronze.configureScaleSensitivity(9);
        SelfCheckoutStationBronze.configureCoinStorageUnitCapacity(5);
        
        stationBronze = new SelfCheckoutStationBronze(); 
        
    }

   
    
    @Test
    public void testGoldStartSession() {
    	
    	testObject = new StartSession(stationGold);
    	
    	//Not in session yet
    	assertFalse(testObject.isInSession());
    	  
    	testObject.startSession(PowerGrid.instance());

    	//In session
        assertTrue(testObject.isInSession());
    }
    
    @Test
    public void testSilverStartSession() {
    	
    	testObject = new StartSession(stationSilver);
    	
    	//Not in session yet
    	assertFalse(testObject.isInSession());
    	  
    	testObject.startSession(PowerGrid.instance());

    	//In session
        assertTrue(testObject.isInSession());
    }
    
    
    @Test
    public void testBronzeStartSession() {
    	
    	testObject = new StartSession(stationBronze);
    	
    	//Not in session yet
    	assertFalse(testObject.isInSession());
    	  
    	testObject.startSession(PowerGrid.instance());

    	//In session
        assertTrue(testObject.isInSession());
    }

    
   
}
