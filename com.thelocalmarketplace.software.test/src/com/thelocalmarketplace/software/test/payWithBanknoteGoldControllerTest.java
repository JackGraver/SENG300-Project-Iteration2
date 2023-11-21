package com.thelocalmarketplace.software.test;

import com.jjjwelectronics.Numeral;
import com.jjjwelectronics.scanner.Barcode;
import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.banknote.*;
import com.thelocalmarketplace.hardware.BarcodedProduct;
import com.thelocalmarketplace.hardware.Product;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.external.ProductDatabases;
import com.thelocalmarketplace.software.AddItem.StartSession;
import com.thelocalmarketplace.software.Cart;
import com.thelocalmarketplace.software.payWithBanknoteGoldController;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import powerutility.PowerGrid;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
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
public class payWithBanknoteGoldControllerTest {
    payWithBanknoteGoldController testObject;
    SelfCheckoutStationGold station;
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
    Barcode[] barcodes;
    final Banknote Banknote5 = new Banknote(Currency.getInstance("CAD"),new BigDecimal("5"));
    final Banknote Banknote10 = new Banknote(Currency.getInstance("CAD"),new BigDecimal("10"));
    final Banknote Banknote20 = new Banknote(Currency.getInstance("CAD"),new BigDecimal("20"));
    final Banknote Banknote50 = new Banknote(Currency.getInstance("CAD"),new BigDecimal("50"));
    final Banknote Banknote100 = new Banknote(Currency.getInstance("CAD"),new BigDecimal("100"));
    BarcodedProduct product1;
    BarcodedProduct product2;
    BarcodedProduct product3;
    Cart cart;

    @Before
    public void setUp() {
        SelfCheckoutStationGold.resetConfigurationToDefaults();
        SelfCheckoutStationGold.configureCurrency(Currency.getInstance("CAD"));
        SelfCheckoutStationGold.configureBanknoteDenominations(banknoteDenominationsConfiguration);
        SelfCheckoutStationGold.configureBanknoteStorageUnitCapacity(7);
        SelfCheckoutStationGold.configureCoinDenominations(coinDenominationsConfigurationArray);
        SelfCheckoutStationGold.configureCoinDispenserCapacity(5);
        SelfCheckoutStationGold.configureCoinTrayCapacity(5);
        SelfCheckoutStationGold.configureScaleMaximumWeight(5);
        SelfCheckoutStationGold.configureReusableBagDispenserCapacity(6);
        SelfCheckoutStationGold.configureScaleSensitivity(9);
        station = new SelfCheckoutStationGold();
        station.plugIn(PowerGrid.instance());
        station.turnOn();
        testObject = new payWithBanknoteGoldController(station);
        StartSession startSession = new StartSession(station);
        Map<Barcode, BarcodedProduct> BARCODED_PRODUCT_DATABASE = ProductDatabases.BARCODED_PRODUCT_DATABASE;

        Barcode barcode1 = new Barcode(new Numeral[]{Numeral.one, Numeral.two, Numeral.three, Numeral.four});
        Barcode barcode2 = new Barcode(new Numeral[]{Numeral.five, Numeral.six, Numeral.seven, Numeral.eight});
        Barcode barcode3 = new Barcode(new Numeral[]{Numeral.nine, Numeral.zero, Numeral.one, Numeral.two});

        product1 = new BarcodedProduct(barcode1, "Product A", 5, 150.0);
        product2 = new BarcodedProduct(barcode2, "Product B", 50, 200.0);
        product3 = new BarcodedProduct(barcode3, "Product C", 11, 100.0);

        BARCODED_PRODUCT_DATABASE.put(barcode1, product1);
        BARCODED_PRODUCT_DATABASE.put(barcode2, product2);
        BARCODED_PRODUCT_DATABASE.put(barcode3, product3);

        barcodes = new Barcode[3];
        barcodes[1]=barcode1;
        barcodes[2]=barcode2;
        barcodes[0]=barcode3;

        // 创建Cart对象并传递其他对象作为参数
        cart = new Cart(station, PowerGrid.instance(), startSession);
    }

    //add a correct set of banknotes
    @Test
    public void test1() throws DisabledException, CashOverloadException {
        cart.addItem(product1, 1);
        cart.addItem(product2, 1);
        testObject.setCart(cart);
        testBanknote = new Banknote[]{Banknote5, Banknote50};
        testObject.payWithBanknote(Banknote5, Banknote50);
        Assert.assertEquals(2, testObject.banknoteInsertedNum);
        Assert.assertEquals(2, testObject.goodBanknoteNum);
        Assert.assertEquals(0, testObject.badBanknoteNum);
        Assert.assertEquals(2, station.banknoteStorage.getBanknoteCount());
    }

    // add a bad banknote
    @Test
    public void test2() throws DisabledException, CashOverloadException {
        cart.addItem(product1, 1);
        cart.addItem(product2, 1);
        testObject.setCart(cart);
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
        cart.addItem(product1, 8);
        testObject.setCart(cart);
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

    @Test
    public void test4() throws DisabledException, CashOverloadException {
        cart.addItem(product1, 1);
        cart.addItem(product2, 1);
        testObject.setCart(cart);
        testObject.payWithBanknote(Banknote5);
        testObject.payWithBanknote(Banknote50);
        Assert.assertEquals(2, testObject.banknoteInsertedNum);
        Assert.assertEquals(2, testObject.goodBanknoteNum);
        Assert.assertEquals(0, testObject.badBanknoteNum);
        Assert.assertEquals(2, station.banknoteStorage.getBanknoteCount());
    }

    //test a correct change
    @Test
    public void test5() throws DisabledException, CashOverloadException {
        cart.addItem(product1, 2);
        cart.addItem(product2, 1);
        testObject.setCart(cart);
        BigDecimal actualChangeAmount = new BigDecimal(BigInteger.ZERO);
        station.banknoteStorage.load(Banknote5, Banknote10, Banknote20, Banknote10, Banknote20);
        Assert.assertEquals(5, station.banknoteStorage.getBanknoteCount());
        testObject.payWithBanknote(Banknote100);
        Assert.assertEquals(1, testObject.banknoteInsertedNum);
        Assert.assertEquals(1, testObject.goodBanknoteNum);
        Assert.assertEquals(0, testObject.badBanknoteNum);
        Assert.assertEquals(3, station.banknoteStorage.getBanknoteCount());
        Banknote[] actualChange = testObject.getChange();
        for (Banknote banknote: actualChange) {
            Assert.assertEquals(banknoteCurrencyConfiguration, banknote.getCurrency());
            actualChangeAmount = actualChangeAmount.add(banknote.getDenomination());
        }
        Assert.assertEquals(new BigDecimal("40"), actualChangeAmount);
        //station.banknoteOutput.removeDanglingBanknotes();
        Assert.assertTrue(testObject.banknotesRemoved);
    }

    //cannot change
    @Test
    public void test6() throws DisabledException, CashOverloadException {
        cart.addItem(product3, 1);
        testObject.setCart(cart);
        station.banknoteStorage.load(Banknote5, Banknote10, Banknote20, Banknote10, Banknote20);
        Assert.assertEquals(5, station.banknoteStorage.getBanknoteCount());
        testObject.payWithBanknote(Banknote100);
        Assert.assertEquals(1, testObject.banknoteInsertedNum);
        Assert.assertEquals(1, testObject.goodBanknoteNum);
        Assert.assertEquals(0, testObject.badBanknoteNum);
        Assert.assertEquals(6, station.banknoteStorage.getBanknoteCount());
        Assert.assertFalse(testObject.canChange);
    }
}
