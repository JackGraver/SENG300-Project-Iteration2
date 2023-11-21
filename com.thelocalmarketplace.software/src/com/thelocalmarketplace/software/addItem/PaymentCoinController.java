package controllers;


import java.math.BigDecimal;

import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.coin.Coin;
import com.tdc.coin.CoinSlot;
import com.tdc.coin.CoinSlotObserver;
import com.tdc.coin.CoinStorageUnit;
import com.tdc.coin.CoinValidator;
import com.tdc.coin.CoinValidatorObserver;
import com.thelocalmarketplace.hardware.SelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;

import powerutility.PowerGrid;
//Michael Svoboda (30039040)
//Shenuk Perera (30086618)
//Marvellous Chukwukelu (30197270)
//Kyuyop Andrew Park(10046592)
//Darpal Patel (30088795)


/**
 * Controls the logic of coin insertion. Tracks amount due on bill as coins are inserted
 * during the current transaction.
 */
public class PaymentCoinController implements CoinSlotObserver, CoinValidatorObserver {
    private SelfCheckoutStationGold selfCheckoutStation;
    private PowerGrid powerGrid;
    private Coin coin;
    private BigDecimal amountDue;
    private boolean weightDiscrepancyDetected = false; //keep track of weight discrepancy 

    /**
     * Basic constructor.
     * 
     * @param ss SelfCheckoutStation that is a part of this.
     * @param pg PowerGrid that is a part of this.
     * @param c Coin that is a part of this.
     * @param amountDue The initial amount due.
     */
    public PaymentCoinController(SelfCheckoutStationGold ss, PowerGrid pg, Coin c, BigDecimal amountDue) {
        selfCheckoutStation = ss;
        powerGrid = pg;
        coin = c;
        this.amountDue = amountDue;
        ss.plugIn(pg);
        ss.turnOn();
        ss.coinSlot.enable();
        ss.coinSlot.attach(this);
        ss.coinValidator.attach(this);
    }
    
    

    /**
     * Signals to the customer the updated amount due after coin insertion.
     * 
     * @param amountDue The amount due to be displayed to the customer.
     */
    public void updateCustomer(BigDecimal amountDue) {
        System.out.println("The amount due: " + amountDue);
    }

    /**
     * @return the total value of the remaining amount due on the bill.
     */
    public BigDecimal getAmount() {
        return amountDue;
    }

    /**
     * Receipt customer receives. Not fully implemented yet.
     */
    private void printReceipt() {
        System.out.println("Transaction Complete");
    }

    @Override
    public void enabled(IComponent<? extends IComponentObserver> component) {
        // TODO Auto-generated method stub
    }

    @Override
    public void disabled(IComponent<? extends IComponentObserver> component) {
        // TODO Auto-generated method stub
    }

    @Override
    public void turnedOn(IComponent<? extends IComponentObserver> component) {
        // TODO Auto-generated method stub
    }

    @Override
    public void turnedOff(IComponent<? extends IComponentObserver> component) {
        // TODO Auto-generated method stub
    }

    @Override
    public void coinInserted(CoinSlot slot) {
        // Ignore for now, later iteration?
    }

    @Override
    public void validCoinDetected(CoinValidator validator, BigDecimal value) {
    	if (weightDiscrepancyDetected == false) {
    		amountDue = amountDue.subtract(value);
            if (amountDue.compareTo(BigDecimal.ZERO) <= 0) {
                printReceipt();
            } else {
                updateCustomer(amountDue);
            }
    	}
        System.out.println("SYSTEM BLOCKED");

        
    }
    
    public boolean CheckForWeightDiscrepancy(double expectedWeight, double actualWeight) {
		if (expectedWeight != actualWeight) {
			return true; // a weight discrepancy has been detected
		} else {
			return false; // a weight discrepancy has not been detected
		}
	}
    
    public void updateWeightDiscrepancy(double expectedWeight, double actualWeight) {
    	weightDiscrepancyDetected = CheckForWeightDiscrepancy(expectedWeight,actualWeight);

    }

    @Override
    public void invalidCoinDetected(CoinValidator validator) {
        // Rejection of coin, to be implemented in a later iteration?
    }
}