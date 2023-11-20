import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.banknote.*;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import powerutility.PowerGrid;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class payWithBanknoteSilverControllerTest {
    payWithBanknoteSilverController testObject;
    SelfCheckoutStationSilver station;
    Currency banknoteCurrencyConfiguration = Currency.getInstance("CAD");
    BigDecimal[] banknoteDenominationsConfiguration = {new BigDecimal("5"),
            new BigDecimal("10"), new BigDecimal("20"),
            new BigDecimal("50"),new BigDecimal("100")};
    int banknoteStorageUnitCapacityConfiguration = 5;
    BigDecimal[] coinDenominationsConfigurationArray = {new BigDecimal("2"),
            new BigDecimal("1"), new BigDecimal("0.25"),
            new BigDecimal("0.1"),new BigDecimal("0.05")};
    int coinStorageUnitCapacityConfiguration = 5;
    Banknote[] testBanknote;
    final Banknote Banknote5 = new Banknote(Currency.getInstance("CAD"),new BigDecimal("5"));
    final Banknote Banknote10 = new Banknote(Currency.getInstance("CAD"),new BigDecimal("10"));
    final Banknote Banknote20 = new Banknote(Currency.getInstance("CAD"),new BigDecimal("20"));
    final Banknote Banknote50 = new Banknote(Currency.getInstance("CAD"),new BigDecimal("50"));
    final Banknote Banknote100 = new Banknote(Currency.getInstance("CAD"),new BigDecimal("100"));

    @Before
    public void setUp() {
        SelfCheckoutStationSilver.resetConfigurationToDefaults();
        SelfCheckoutStationSilver.configureCurrency(Currency.getInstance("CAD"));
        SelfCheckoutStationSilver.configureBanknoteDenominations(banknoteDenominationsConfiguration);
        SelfCheckoutStationSilver.configureBanknoteStorageUnitCapacity(7);
        SelfCheckoutStationSilver.configureCoinDenominations(coinDenominationsConfigurationArray);
        SelfCheckoutStationSilver.configureCoinDispenserCapacity(5);
        SelfCheckoutStationSilver.configureCoinTrayCapacity(5);
        SelfCheckoutStationSilver.configureScaleMaximumWeight(5);
        SelfCheckoutStationSilver.configureReusableBagDispenserCapacity(6);
        SelfCheckoutStationSilver.configureScaleSensitivity(9);
        station = new SelfCheckoutStationSilver();
        station.plugIn(PowerGrid.instance());
        station.turnOn();
        testObject = new payWithBanknoteSilverController(station);
    }

    //add a correct set of banknotes
    @Test
    public void test1() throws DisabledException, CashOverloadException {
        testBanknote = new Banknote[]{Banknote5, Banknote50};
        testObject.setTotalPrice(new BigDecimal("55"));
        testObject.payWithBanknote(Banknote5, Banknote50);
        Assert.assertEquals(2, testObject.banknoteInsertedNum);
        Assert.assertEquals(2, testObject.goodBanknoteNum);
        Assert.assertEquals(0, testObject.badBanknoteNum);
        Assert.assertEquals(2, station.banknoteStorage.getBanknoteCount());
    }

    // add a bad banknote
    @Test
    public void test2() throws DisabledException, CashOverloadException {
        testObject.setTotalPrice(new BigDecimal("55"));
        testObject.payWithBanknote(Banknote5, new Banknote(banknoteCurrencyConfiguration, new BigDecimal("59")));
        Assert.assertTrue(testObject.banknoteEjected);
        testObject.getStation().banknoteInput.removeDanglingBanknote();
        testObject.payWithBanknote(Banknote50);
        Assert.assertEquals(3, testObject.banknoteInsertedNum);
        Assert.assertEquals(2, testObject.goodBanknoteNum);
        Assert.assertEquals(1, testObject.badBanknoteNum);
        Assert.assertEquals(2, station.banknoteStorage.getBanknoteCount());
    }

    //make storage unit full
    @Test
    public void test3() throws DisabledException, CashOverloadException {
        testObject.setTotalPrice(new BigDecimal("40"));
        testObject.payWithBanknote(Banknote5, Banknote5, Banknote5, Banknote5, Banknote5, Banknote5, Banknote5);
        //Assert.assertTrue(testObject.banknoteEjected);
        //testObject.getStation().banknoteInput.removeDanglingBanknote();
        //testObject.payWithBanknote(Banknote5);
        Assert.assertEquals(7, testObject.banknoteInsertedNum);
        Assert.assertEquals(7, testObject.goodBanknoteNum);
        Assert.assertEquals(0, testObject.badBanknoteNum);
        Assert.assertEquals(7, station.banknoteStorage.getBanknoteCount());
        /*ArrayList<Banknote> expectedArrayList = new ArrayList<>(List.of(Banknote5));
        ArrayList<Banknote> actuallArrayList = new ArrayList<>(station.banknoteOutput.danglingDispensedBanknotes);
        for (int i = 0; i < Math.max(expectedArrayList.size(), actuallArrayList.size()); i++) {
            Assert.assertEquals(expectedArrayList.get(i).getCurrency(), actuallArrayList.get(i).getCurrency());
            Assert.assertEquals(expectedArrayList.get(i).getDenomination(), actuallArrayList.get(i).getDenomination());
        }*/
        Assert.assertTrue(testObject.banknotesFull);
    }

    //need to make change
    @Test
    public void test4() throws DisabledException, CashOverloadException {
        testObject.setTotalPrice(new BigDecimal("30"));
        testObject.payWithBanknote(Banknote5, Banknote5, Banknote5, Banknote20);
        Assert.assertEquals(4, testObject.banknoteInsertedNum);
        Assert.assertEquals(4, testObject.goodBanknoteNum);
        Assert.assertEquals(0, testObject.badBanknoteNum);
        Assert.assertEquals(3, station.banknoteStorage.getBanknoteCount());
        ArrayList<Banknote> expectedArrayList = new ArrayList<>(List.of(Banknote5));
        ArrayList<Banknote> actuallArrayList = new ArrayList<>(station.banknoteOutput.danglingDispensedBanknotes);
        for (int i = 0; i < Math.max(expectedArrayList.size(), actuallArrayList.size()); i++) {
            Assert.assertEquals(expectedArrayList.get(i).getCurrency(), actuallArrayList.get(i).getCurrency());
            Assert.assertEquals(expectedArrayList.get(i).getDenomination(), actuallArrayList.get(i).getDenomination());
        }
    }

    @Test
    public void test5() throws DisabledException, CashOverloadException {
        testObject.setTotalPrice(new BigDecimal("55"));
        testObject.payWithBanknote(Banknote5);
        testObject.payWithBanknote(Banknote50);
        Assert.assertEquals(2, testObject.banknoteInsertedNum);
        Assert.assertEquals(2, testObject.goodBanknoteNum);
        Assert.assertEquals(0, testObject.badBanknoteNum);
        Assert.assertEquals(2, station.banknoteStorage.getBanknoteCount());
    }

    //test a correct change
    @Test
    public void test6() throws DisabledException, CashOverloadException {
        BigDecimal actualChangeAmount = new BigDecimal(BigInteger.ZERO);
        station.banknoteStorage.load(Banknote5, Banknote10, Banknote20, Banknote10, Banknote20);
        testObject.setTotalPrice(new BigDecimal("60"));
        Assert.assertEquals(5, station.banknoteStorage.getBanknoteCount());
        testObject.payWithBanknote(Banknote100);
        Assert.assertEquals(1, testObject.banknoteInsertedNum);
        Assert.assertEquals(1, testObject.goodBanknoteNum);
        Assert.assertEquals(0, testObject.badBanknoteNum);
        Assert.assertEquals(3, station.banknoteStorage.getBanknoteCount());
        Banknote[] actualChange = station.banknoteOutput.danglingDispensedBanknotes.toArray(new Banknote[0]);
        for (Banknote banknote: actualChange) {
            Assert.assertEquals(banknoteCurrencyConfiguration, banknote.getCurrency());
            actualChangeAmount = actualChangeAmount.add(banknote.getDenomination());
        }
        Assert.assertEquals(new BigDecimal("40"), actualChangeAmount);
        station.banknoteOutput.removeDanglingBanknotes();
        Assert.assertTrue(testObject.banknotesRemoved);

    }

    //cannot change
    @Test
    public void test7() throws DisabledException, CashOverloadException {
        station.banknoteStorage.load(Banknote5, Banknote10, Banknote20, Banknote10, Banknote20);
        testObject.setTotalPrice(new BigDecimal("99"));
        Assert.assertEquals(5, station.banknoteStorage.getBanknoteCount());
        testObject.payWithBanknote(Banknote100);
        Assert.assertEquals(1, testObject.banknoteInsertedNum);
        Assert.assertEquals(1, testObject.goodBanknoteNum);
        Assert.assertEquals(0, testObject.badBanknoteNum);
        Assert.assertEquals(6, station.banknoteStorage.getBanknoteCount());
        Assert.assertFalse(testObject.canChange);
    }
}
