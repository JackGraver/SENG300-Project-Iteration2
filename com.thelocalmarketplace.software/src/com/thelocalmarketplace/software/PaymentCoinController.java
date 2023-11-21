package com.thelocalmarketplace.software;

import java.math.BigDecimal;

import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.tdc.coin.Coin;
import com.tdc.coin.CoinDispenserObserver;
import com.tdc.coin.CoinSlot;
import com.tdc.coin.CoinSlotObserver;
import com.tdc.coin.CoinValidator;
import com.tdc.coin.CoinValidatorObserver;
import com.tdc.coin.ICoinDispenser;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;

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
 
    Controls the logic of coin insertion. Tracks amount due on bill as coins are inserted
    during the current transaction.
 */
public class PaymentCoinController implements CoinSlotObserver, CoinValidatorObserver, CoinDispenserObserver {
    private AbstractSelfCheckoutStation selfCheckoutStation;
    private PowerGrid powerGrid;
    private Coin coin;
    private BigDecimal amountDue;
    private BigDecimal dispenserAmount = new BigDecimal("0.00");
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
    
    public BigDecimal getDispenserAmount() {
        return dispenserAmount;
    }

    /**
     * Receipt customer receives. Not fully implemented yet.
     */
    private void printReceipt() {
        System.out.println("Transaction Complete");
    }
    
    
    /**
     * Evaluting the weight discrepancy 
     */
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
    public void coinInserted(CoinSlot slot) {
        // Ignore for now, later iteration?
    }

    @Override
    public void validCoinDetected(CoinValidator validator, BigDecimal value) {
    	//we assume its a valid coin everytime this function is called. thus, no need to valid coin check
    	if (weightDiscrepancyDetected == false) {
    		amountDue = amountDue.subtract(value);
            if (amountDue.compareTo(BigDecimal.ZERO) == 0) {
                printReceipt();
            }
            else if(amountDue.compareTo(BigDecimal.ZERO) < 0) {
            	
            	dispenserAmount = amountDue.abs();
            	
            	
                printReceipt();

            }
           
            else {
                updateCustomer(amountDue);
            }
    	}
        System.out.println("SYSTEM BLOCKED");

        
    }
    
    

    @Override
    public void invalidCoinDetected(CoinValidator validator) {
        // Rejection of coin, to be implemented in a later iteration?
    }



	@Override
	public void coinsFull(ICoinDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void coinsEmpty(ICoinDispenser dispenser) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void coinAdded(ICoinDispenser dispenser, Coin coin) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void coinRemoved(ICoinDispenser dispenser, Coin coin) {
		
		
	}



	@Override
	public void coinsLoaded(ICoinDispenser dispenser, Coin... coins) {
		// TODO Auto-generated method stub
		
	}


	/**
     * where the coins that are considered change will be unloaded
     */
	@Override
	public void coinsUnloaded(ICoinDispenser dispenser, Coin... coins) {
		// TODO Auto-generated method stub
		
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
}