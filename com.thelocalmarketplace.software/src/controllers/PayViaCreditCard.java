package controllers;


import java.math.BigDecimal;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.BlockedCardException;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.InvalidPINException;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import observers.CreditCardObserver;
import powerutility.PowerGrid;

public class PayViaCreditCard extends CardIssuer implements CreditCardObserver {

	private CreditCard creditCard;
	private String pin = "";
	private AbstractSelfCheckoutStation user;
	private BigDecimal totalCostOfGroceries;
	private int pinFailTrialCounter;
	private boolean creditCardSwiped;
	private int creditCardSwipedCounter = 0;
	private boolean creditCardDataRead;
	private int creditCardDataReadCounter = 0;
	private boolean creditCardPaymentSuccessful;
	private boolean creditCardPinValid;

	// Scenario pay by credit:
	// 1. Customer: Inserts a credit card and types the corresponding PIN.
	// 2. System: Validates the PIN against the credit card.
	// 3. System: Signals to the Bank the details of the credit card and the amount
	// to be charged.
	// 4. Bank: Signals to the System the hold number against the account of the
	// credit card.
	// 5. System: Signals to the Bank that the transaction identified with the hold
	// number should be posted,
	// reducing the amount of credit available.
	// 6. Bank: Signals to the System that the transaction was successful.
	// 7. System: Updates the amount due displayed to the customer.
	// 8. <Print Receipt extension point>.
	// Exceptions:
	// 1. If the customer enters the wrong PIN three times in a row, the card will
	// be blocked.
	// 2. If the Bank does not approve the transaction, the remaining amount due
	// will not change.

	/*
	 * Exception thrown when not enough cash in wallet to pay for owed funds.
	 */
	public class InsufficientFundsException extends Exception {
		InsufficientFundsException(String errorType) {
			super("Insufficient Funds. " + errorType);
		}
	}

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
	}

	@Override
	public void aCardHasBeenSwiped() {
		this.creditCardSwiped = true;
		this.creditCardSwipedCounter++;
		this.creditCardDataRead = false;
	}

	@Override
	public void theDataFromACardHasBeenRead(CardData data) {
		this.creditCardDataRead = true;
		this.creditCardDataReadCounter++;
		this.creditCardSwiped = false;
	}

	@Override
	public void transactionCompleted() {
		this.creditCardPaymentSuccessful = true;
	}

	@Override
	public void creditCardPinAccepted() {
		this.creditCardPinValid = true;
		this.creditCardPaymentSuccessful = false;
		this.pinFailTrialCounter = 0;
	}

	public PayViaCreditCard(CreditCard creditCard, String pinInput, BigDecimal totalCostOfGroceries)
			throws InsufficientFundsException, InvalidPINException {

		super(creditCard.getCompanyName(), creditCard.getMaxHolds());
		this.creditCard = creditCard;
		this.pin = creditCard.getPin();
		setTotalCostOfGroceries(totalCostOfGroceries);
		
		checkForPinValidity(pinInput);
		checkForPaymentValidity();
	}

	/**
	 * This method checks for payment validity if the transaction was completed or
	 * not
	 * 
	 * @throws InsufficientFundsException for insufficient funds
	 */
	private void checkForPaymentValidity() throws InsufficientFundsException {
		/**
		 * This if statement throws an exception if the balance in the credit card is
		 * not enough
		 */
		if (this.creditCard.getBalance().compareTo(this.totalCostOfGroceries) == -1) {
			setTotalCostOfGroceries(this.totalCostOfGroceries.subtract(this.creditCard.getBalance()));
			throw new InsufficientFundsException(
					"You still need " + "$" + (this.totalCostOfGroceries.subtract(this.creditCard.getBalance())));
		} else if (this.creditCard.getBalance().compareTo(this.totalCostOfGroceries) == 0) {
			transactionCompleted();
			this.creditCard.setBalance(this.creditCard.getBalance().subtract(this.totalCostOfGroceries));
		} else if (this.creditCard.getBalance().compareTo(this.totalCostOfGroceries) == 1) {
			transactionCompleted();
			this.creditCard.setBalance(this.creditCard.getBalance().subtract(this.totalCostOfGroceries));
		}
	}

	private void checkForPinValidity(String pinInput) {
		/**
		 * If the customer enters the wrong PIN three times in a row, the card will be
		 * blocked. If the pin is invalid, the pinTrialCounter increments by 1, and an exception
		 * is thrown
		 */
		if (pinFailTrialCounter == 3) {
			throw new BlockedCardException();
		}
		
		if (pinIsValid(pinInput) && this.totalCostOfGroceries.compareTo(BigDecimal.ZERO) == 1) {
			creditCardPinAccepted();
			this.creditCard.setBalance(this.creditCard.getBalance().subtract(this.totalCostOfGroceries));
		} else if (!pinIsValid(this.pin)) {
			this.pinFailTrialCounter++;
			throw new InvalidPINException();
		}
	}

	public void setTotalCostOfGroceries(BigDecimal totalCostOfGroceries) {
		this.totalCostOfGroceries = totalCostOfGroceries;
	}

	public BigDecimal getTotalCostOfGroceries() {
		return this.totalCostOfGroceries;
	}

	private boolean pinIsValid(String pinInput) {
		boolean pinIsValid = this.pin == pinInput;
		return pinIsValid;
	}
}