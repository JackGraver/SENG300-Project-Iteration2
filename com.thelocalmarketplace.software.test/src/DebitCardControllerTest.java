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
       
    }
    
    @Test
    public void testValidSwipe() throws IOException {
    	testObject = new DebitCardController(stationGold);
    	testObject.setAmountDue(10);
    	testObject.setBank(cardIssuerRBC);
    	stationGold.cardReader.swipe(debitCardHighFund);
    	Assert.assertEquals("123456", testObject.cardData.getNumber());
   
    }
    
    
    
}