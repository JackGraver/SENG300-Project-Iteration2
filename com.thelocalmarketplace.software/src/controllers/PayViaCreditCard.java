package controllers;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.InvalidPINException;
import com.thelocalmarketplace.hardware.AbstractSelfCheckoutStation;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import observers.CreditCardObserver;

public class PayViaCreditCard extends CardIssuer {

	private CreditCard creditCard;
	private String pin = "";
	private AbstractSelfCheckoutStation user;
	private CreditCardObserver creditCardInsertObserver;
	private long totalCostOfGroceries;
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

	/*
	 * PayViaCreditException Exception thrown when observers do not have expected
	 * fields for payment.
	 */
	public class PayViaCreditException extends Exception {
		public PayViaCreditException(String errorType) {
			super("Something is wrong in credit payment. " + errorType);
		}
	}


	public PayViaCreditCard(CreditCard creditCard)
			throws InsufficientFundsException, InvalidPINException, PayViaCreditException {
		
		super(creditCard.getCompanyName(), creditCard.getMaxHolds());
		this.creditCard = creditCard;
		this.pin = creditCard.getPin();

		if (creditCard.getBalance() < this.totalCostOfGroceries) {
			throw new InsufficientFundsException(
					"You still need " + "$" + (this.totalCostOfGroceries - creditCard.getBalance()));
		}

		if (creditCardInsertObserver.bankNotifier != "transaction successful") {
			throw new PayViaCreditException("System Denied Payment.");
		}

		if (pinIsValid(this.pin)) {
			if (this.totalCostOfGroceries != 0 && creditCardInsertObserver.bankNotifier == "transaction successful") {
				creditCard.setBalance(creditCard.getBalance() - this.totalCostOfGroceries);
			}
		}

		// 1. If the customer enters the wrong PIN three times in a row, the card will
		// be blocked.
		for (int i = 0; i <= 3; i++) {
			if (!pinIsValid(this.pin)) {
				throw new InvalidPINException();
			}
		}
	}

	public void setTotalCostOfGroceries(long totalCostOfGroceries) {
		this.totalCostOfGroceries = totalCostOfGroceries;
	}

	public long getTotalCostOfGroceries() {
		return this.totalCostOfGroceries;
	}

	public boolean pinIsValid(String pinInput) {
		boolean pinIsValid = this.pin == pinInput;
		return pinIsValid;
	}

}