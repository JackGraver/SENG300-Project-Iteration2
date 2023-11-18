package controllers;

import java.io.IOException;
import java.math.BigDecimal;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.Card.CardSwipeData;
import com.jjjwelectronics.card.CardReaderBronze;
import com.jjjwelectronics.card.CardReaderGold;
import com.jjjwelectronics.card.CardReaderSilver;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import exceptions.HoldNotAcceptedException;
import exceptions.OverCreditException;
import models.CreditCard;
import observers.CreditCardObserver;
import powerutility.PowerGrid;

public class PayViaSwipeCreditBronze implements CreditCardObserver {

	private BigDecimal totalCostOfGroceries;
	private BigDecimal creditLimit;
	private CreditCard creditCard;
	private CardData creditCardData;
	private CardIssuer bank;
	private long creditCardMaxHolds;

	public boolean creditCardSwiped;
	public boolean creditCardDataRead;
	public boolean creditCardPaymentSuccessful;
	public boolean systemAcceptsHoldNumber;

//	Scenario pay by credit
//	1. Customer: Swipes a credit Card
//	2. System: Signals to the Bank the details of the credit card and the amount to be charged.
//	3. Bank: Signals to the System the hold number against the account of the credit card.
//	4. System: Signals to the Bank that the transaction identified with the hold number should be posted, reducing the amount of credit available.
//	5. Bank: Signals to the System that the transaction was successful.
//	6. System: Updates the amount due displayed to the customer.
//	7. <Print Receipt extension point>.
//	Exceptions:
//	1. If the Bank does not approve the transaction, the remaining amount due will not change.

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
	}

	@Override
	public void theDataFromACardHasBeenRead(CardData data) {
		this.creditCardDataRead = true;
		this.creditCardSwiped = false;
	}

	public PayViaSwipeCreditBronze(CreditCard creditCard, CardData creditCardData, BigDecimal totalCostOfGroceries,
			BigDecimal creditLimitInBigDecimal) throws OverCreditException, IOException, HoldNotAcceptedException {

		/**
		 * this initializes the credit card, and the card data variable and sets the
		 * total cost of groceries
		 */
		setTotalCostOfGroceries(totalCostOfGroceries);
		this.creditCard = creditCard;
		this.creditCardData = creditCardData;
		this.creditLimit = creditLimitInBigDecimal;

		/**
		 * Notifies that a card has been swiped
		 */
		aCardHasBeenSwiped();
		customerSwipesCard(creditCard, creditCardData, creditLimitInBigDecimal.doubleValue());

		/**
		 * If the credit card data is not null, it checks if the system accepts the hold
		 * number
		 */
		systemAcceptsHoldNumber(creditLimitInBigDecimal);

	}

	/**
	 * If the listener catches an event which a creditCard was swiped, the system
	 * will signal to the Bank the details of the credit card and the amount to be
	 * charged.
	 */
	private void customerSwipesCard(CreditCard creditCard, CardData creditCardData, double creditLimit)
			throws IOException {

		this.creditCardData = creditCardData;
		this.creditCardMaxHolds = creditCard.getMaxHolds();
		this.bank = new CardIssuer(this.creditCardData.getType(), this.creditCardMaxHolds);

	}

	/**
	 * Bank: Signals to the System the hold number against the account of the credit
	 * card.
	 * 
	 * @throws OverCreditException
	 * @throws HoldNotAcceptedException
	 */
	private void systemAcceptsHoldNumber(BigDecimal creditLimit) throws OverCreditException, HoldNotAcceptedException {

		if (getCreditCardData() != null) {
			systemSignalsTheAmountOfCreditAvailable(totalCostOfGroceries, creditLimit);
			System.out.println("Transaction Successful");
		} else {
			this.bank.block(this.creditCardData.getNumber());
			throw new HoldNotAcceptedException();

		}
	}

	/**
	 * System: Signals to the Bank that the transaction identified with the hold
	 * number should be posted, reducing the amount of credit available.
	 * 
	 * @throws OverCreditException
	 */
	public void systemSignalsTheAmountOfCreditAvailable(BigDecimal totalCostOfGroceries,
			BigDecimal creditLimitInBigDecimal) throws OverCreditException {
		theDataFromACardHasBeenRead(this.creditCardData);
		if (creditLimitInBigDecimal.compareTo(totalCostOfGroceries) == 1
				|| creditLimitInBigDecimal.compareTo(totalCostOfGroceries) == 0) {
			this.creditCard.setCreditLimit(creditLimitInBigDecimal.subtract(totalCostOfGroceries));
		} else {
			setTotalCostOfGroceries(totalCostOfGroceries.subtract(creditLimitInBigDecimal));
			throw new OverCreditException("You still need $" + totalCostOfGroceries.subtract(creditLimitInBigDecimal));
		}
	}

	public void setTotalCostOfGroceries(BigDecimal totalCostOfGroceries) {
		this.totalCostOfGroceries = totalCostOfGroceries;
	}

	public CardData getCreditCardData() {
		return this.creditCardData;

	}

}