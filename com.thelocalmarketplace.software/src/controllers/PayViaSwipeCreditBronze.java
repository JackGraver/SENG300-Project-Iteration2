package controllers;

import java.io.IOException;
import java.math.BigDecimal;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.CardReaderBronze;
import com.jjjwelectronics.card.CardReaderListener;
import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import exceptions.HoldNotAcceptedException;
import exceptions.OverCreditException;
import exceptions.PriceIsZeroOrNegativeException;
import models.CreditCard;
import powerutility.PowerGrid;

/**
 * This class is the controller for bronze credit card reader
 * 	Scenario pay by credit
 * 1. Customer: Swipes a credit Card
 * 2. System: Signals to the Bank the details of the credit card and the amount to be charged.
 * 3. Bank: Signals to the System the hold number against the account of the credit card.
 * 4. System: Signals to the Bank that the transaction identified with the hold number should be posted, reducing the amount of credit available.
 * 5. Bank: Signals to the System that the transaction was successful.
 * 6. System: Updates the amount due displayed to the customer.
 * 7. <Print Receipt extension point>.
 * Exceptions:
 * 1. If the Bank does not approve the transaction, the remaining amount due will not change.
 */
public class PayViaSwipeCreditBronze implements CardReaderListener {

	private BigDecimal totalCostOfGroceries;
	private BigDecimal creditLimit;
	private CreditCard creditCard;
	private CardData creditCardData;
	private CardIssuer bank;
	private Card card;
	public CardReaderBronze cardReaderBronze = new CardReaderBronze();
	private SelfCheckoutStationBronze bronzeStation;
	private ReceiptPrinterController receiptPrinter;
	
	public boolean creditCardSwiped;
	public boolean creditCardDataRead;

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
	}

	/** 
	 * This is the constructor for this controller
	 * @param creditCard is the credit card identity of the user
	 * @param totalCostOfGroceries is the cost of the groceries in total
	 * @throws OverCreditException is the exception for going over the limited credit
	 * @throws IOException is the exception for the swipe method
	 * @throws HoldNotAcceptedException is the exception for when the bank does not accept the hold
	 * @throws PriceIsZeroOrNegativeException is the exception for when a price is zero or negative
	 */
	public PayViaSwipeCreditBronze(CreditCard creditCard, BigDecimal totalCostOfGroceries,  SelfCheckoutStationBronze bronzeStation)
			throws OverCreditException, IOException, HoldNotAcceptedException, PriceIsZeroOrNegativeException {

		/**
		 * Turning on the card reader and connecting it to the station, along with the printer
		 */
		this.bronzeStation = bronzeStation;
		cardReaderBronze.plugIn(PowerGrid.instance());
		cardReaderBronze.turnOn();
		cardReaderBronze.enable();
		this.receiptPrinter = new ReceiptPrinterController(bronzeStation);
		
		/**
		 * Registering listeners
		 */
		this.bronzeStation.cardReader.register(this);
		cardReaderBronze.register(this);

		/**
		 * Notifies that a card has been swiped
		 */
		aCardHasBeenSwiped();

		/**
		 * this initializes the credit card, and the card data variable and sets the
		 * total cost of groceries
		 */
		if (totalCostOfGroceries.doubleValue() < 0) {
			throw new PriceIsZeroOrNegativeException("Price cannot be negative");
		} else if (totalCostOfGroceries == BigDecimal.ZERO) {
			throw new PriceIsZeroOrNegativeException("Cannot pay for an empty order");
		} else {
			setTotalCostOfGroceries(totalCostOfGroceries);
		}
		this.creditCard = creditCard;
		this.card = creditCard.getCard();
		
		this.creditCardData = this.cardReaderBronze.swipe(card);
		this.creditLimit = creditCard.getCreditLimit();
		this.bank = creditCard.getBank();

		/**
		 * System: Signals to the Bank the details of the credit card and the amount to
		 * be charged.
		 * 
		 * Bank: Signals to the System the hold number against the account of the credit
		 * card.
		 * 
		 * System: Signals to the Bank that the transaction identified with the hold
		 * number should be posted, reducing the amount of credit available.
		 */
		systemAcceptsHoldNumber(this.creditLimit);

		
	}

	/**
	 * 
	 * System: Signals to the Bank the details of the credit card and the amount to
	 * be charged.
	 * 
	 * @throws OverCreditException is the exception for under credit scenarios
	 * @throws HoldNotAcceptedException is the exception when the hold is not accepted by the bank
	 */
	private void systemAcceptsHoldNumber(BigDecimal creditLimit) throws OverCreditException, HoldNotAcceptedException {
		boolean holdReleased = bank.releaseHold(this.creditCardData.getNumber(), creditCard.getMaxHolds());
		long holdAuthorized = bank.authorizeHold(this.creditCardData.getNumber(), totalCostOfGroceries.doubleValue());
		boolean holdAcceptedAndSent = (holdReleased == true && holdAuthorized > 0);
		if (holdAcceptedAndSent == true) {
			systemSignalsTheAmountOfCreditAvailable(totalCostOfGroceries, creditLimit);
		} else if (creditLimit.compareTo(totalCostOfGroceries) == -1) {
			throw new OverCreditException("You still need $" + totalCostOfGroceries.subtract(creditLimit));
		} else {
			throw new HoldNotAcceptedException();
		}
	}

	/**
	 * System: Signals to the Bank that the transaction identified with the hold
	 * number should be posted, reducing the amount of credit available.
	 * 
	 * @throws OverCreditException
	 */
	private void systemSignalsTheAmountOfCreditAvailable(BigDecimal totalCostOfGroceries,
			BigDecimal creditLimitInBigDecimal) throws OverCreditException {
		theDataFromACardHasBeenRead(this.creditCardData);
		this.creditCard.setCreditLimit(creditLimitInBigDecimal.subtract(totalCostOfGroceries));
		System.out.println("Transaction Successful");	
		this.receiptPrinter.printReceipt("Receipt\n" + "Total $" + totalCostOfGroceries + "\n" + "By Credit");
		setTotalCostOfGroceries(creditLimitInBigDecimal.subtract(totalCostOfGroceries));
	}

	/**
	 * Sets the total cost of groceries
	 * @param totalCostOfGroceries is the total cost of groceries
	 */
	private void setTotalCostOfGroceries(BigDecimal totalCostOfGroceries) {
		this.totalCostOfGroceries = totalCostOfGroceries;
	}

}