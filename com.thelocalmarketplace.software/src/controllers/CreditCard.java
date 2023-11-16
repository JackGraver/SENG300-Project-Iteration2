package controllers;

import java.math.BigDecimal;

import com.jjjwelectronics.card.Card.CardData;

public class CreditCard implements CardData {

	private String pin;
	private String companyName;
	private long maxHolds;
	private BigDecimal balance;
	private String creditCardType;
	private String creditCardNumber;
	private String creditCardHolder;
	private String creditCardCVV;

	public CreditCard(String pin, String companyName, long maxHolds, String creditCardType, String creditCardNumber,
			String creditCardHolder, String creditCardCVV, BigDecimal balance) {
		this.pin = pin;
		this.companyName = companyName;
		this.maxHolds = maxHolds;
		this.creditCardType = creditCardType;
		this.creditCardNumber = creditCardNumber;
		this.creditCardHolder = creditCardHolder;
		this.creditCardCVV = creditCardCVV;
		setBalance(balance);
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getBalance() {
		return this.balance;
	}

	public String getPin() {
		return this.pin;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public long getMaxHolds() {
		return this.maxHolds;
	}

	/**
	 * Gets the type of the card.
	 * 
	 * @return The type of the card.
	 */
	@Override
	public String getType() {
		return this.creditCardType;
	}

	/**
	 * Gets the number of the card.
	 * 
	 * @return The number of the card.
	 */
	@Override
	public String getNumber() {
		return this.creditCardNumber;
	}

	/**
	 * Gets the cardholder's name.
	 * 
	 * @return The cardholder's name.
	 */
	@Override
	public String getCardholder() {
		return this.creditCardHolder;
	}

	/**
	 * Gets the card verification value (CVV) of the card.
	 * 
	 * @return The CVV of the card.
	 * @throws UnsupportedOperationException If this operation is unsupported by
	 *                                       this object.
	 */
	@Override
	public String getCVV() {
		return this.creditCardCVV;
	}

}