import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.*;
import com.jjjwelectronics.card.*;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.printer.*;
import com.tdc.*;

import com.thelocalmarketplace.*;
import com.thelocalmarketplace.hardware.*;
import com.thelocalmarketplace.hardware.external.*;
import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import ca.ucalgary.seng300.simulation.SimulationException;

import powerutility.PowerGrid;

public class DebitCardControllerTest {
	
	SelfCheckoutStationGold stationGold;
	SelfCheckoutStationSilver stationSilver;
	SelfCheckoutStationBronze stationBronze;
	
	CardIssuer cardIssuerCIBC = new CardIssuer("CIBC", 1);
	CardIssuer cardIssuerRBC = new CardIssuer("RBC", 5);
	
	Card debitCardHighFund = new Card("Debit", "123456", "John Doe", "123");
	Card debitCardLowFund = new Card("Debit", "000000", "Jane Doe", "100");
	Card notDebitCard = new Card("Credit", "101010", "Jon Doe", "456");
	
	
	DebitCardController testObject; 
	
    @Before
    public void setUp() {
    	
    
    Calendar calendar = new GregorianCalendar();
    calendar.set(Calendar.YEAR, 2026);
    calendar.set(Calendar.MONTH, Calendar.NOVEMBER); // Note: Months are 0-based, so November is 10
    
    //Debit Cards
    cardIssuerCIBC.addCardData(debitCardHighFund.number, debitCardHighFund.cardholder, calendar, debitCardHighFund.cvv, 1000);
    cardIssuerCIBC.addCardData(debitCardLowFund.number, debitCardLowFund.cardholder, calendar, debitCardLowFund.cvv, 10);
    
    
    cardIssuerRBC.addCardData(debitCardHighFund.number, debitCardHighFund.cardholder, calendar, debitCardHighFund.cvv, 1000);
    cardIssuerRBC.addCardData(debitCardLowFund.number, debitCardLowFund.cardholder, calendar, debitCardLowFund.cvv, 10);
    
    //Credit Cards
    cardIssuerRBC.addCardData(notDebitCard.number, notDebitCard.cardholder, calendar, notDebitCard.cvv, 999);
    
    BigDecimal[] banknoteDenominationsConfiguration = {new BigDecimal("5"),
            new BigDecimal("10"), new BigDecimal("20"),
            new BigDecimal("50"),new BigDecimal("100")};
    BigDecimal[] coinDenominationsConfigurationArray = {new BigDecimal("2"),
            new BigDecimal("1"), new BigDecimal("0.25"),
            new BigDecimal("0.1"),new BigDecimal("0.05")}; 
    
    
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
    stationGold.plugIn(PowerGrid.instance());
    stationGold.turnOn();
    
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
    stationSilver.plugIn(PowerGrid.instance());
    stationSilver.turnOn();
    
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
    stationBronze.plugIn(PowerGrid.instance());
    stationBronze.turnOn();
       
    }
    
    @Test
    public void testValidSwipe() throws IOException {
    	testObject = new DebitCardController(stationGold);
    	testObject.setAmountDue(10);
    	testObject.setBank(cardIssuerRBC);
    	stationGold.cardReader.swipe(debitCardHighFund);
    	assertEquals("123456", testObject.cardData.getNumber());
    	assertEquals(0, testObject.getAmountDue(), 0.0001);
    	assertTrue(testObject.completePayment);
   
    }
    
    @Test
    public void testDebitCardControllerGoldStation() {
        DebitCardController controller = new DebitCardController(stationGold);
        assertNotNull(controller);
    }
    
    @Test
    public void testDebitCardControllerSilverStation() {
        DebitCardController controller = new DebitCardController(stationSilver);
        assertNotNull(controller);
    }
    
    @Test
    public void testDebitCardControllerBronzeStation() {
        DebitCardController controller = new DebitCardController(stationBronze);
        assertNotNull(controller);
    }
    
    @Test
    public void testCardSwipedGoldStation() {
        DebitCardController controller = new DebitCardController(stationGold);
        controller.aCardHasBeenSwiped();
    }
    
    @Test
    public void testCardSwipedSilverStation() {
        DebitCardController controller = new DebitCardController(stationSilver);
        controller.aCardHasBeenSwiped();
    }
    
    @Test
    public void testCardSwipedBronzeStation() {
        DebitCardController controller = new DebitCardController(stationBronze);
        controller.aCardHasBeenSwiped();
    }
    
    @Test
    public void BankValidation() {
    	boolean v = false;
    	testObject = new DebitCardController(stationGold);
    	testObject.setBank(cardIssuerRBC);
    	if (testObject.getBank()==cardIssuerRBC)
    		v = true;
        assertTrue(v);
    }
    
    
    @Test
    public void InvalidBank() throws InvalidArgumentSimulationException{
        DebitCardController testObject = new DebitCardController(stationGold);
        assertThrows(InvalidArgumentSimulationException.class, () -> {
        	testObject.setBank(new CardIssuer("000",0));
        });
    }
    
    @Test
    public void CardSwipeNotDebit() throws IOException{
    	testObject = new DebitCardController(stationGold);
    	testObject.setAmountDue(10);
    	testObject.setBank(cardIssuerRBC);
    	stationGold.cardReader.swipe(notDebitCard);
    	//Payment through DebitCardController not possible if not debit
    	assertFalse(testObject.completePayment);
    	
    }
    
    @Test
    public void testInvalidHold() throws IOException {
    	testObject = new DebitCardController(stationGold);
    	
    	//Invalid amount - will cause hold fail
    	testObject.setAmountDue(-10);
    	testObject.setBank(cardIssuerRBC);
    	stationGold.cardReader.swipe(debitCardHighFund);
    	assertEquals("123456", testObject.cardData.getNumber());
  
    	//Invalid hold, so no payment
    	assertFalse(testObject.completePayment);
   
    }

    
    @Test
    public void testDeviceEnable() throws IOException {
    	testObject = new DebitCardController(stationGold);
    	stationGold.cardReader.enable();
    	assertTrue(testObject.isEnabled);
    	stationGold.cardReader.disable();
    	assertFalse(testObject.isEnabled);	
    	
    }
    
    @Test
    public void testDeviceOn() throws IOException {
    	testObject = new DebitCardController(stationGold);
    	stationGold.cardReader.turnOff();
    	assertFalse(testObject.isOn);
    	stationGold.cardReader.turnOn();
    	assertTrue(testObject.isOn);
    	
   
    	
    }
     
    
}
