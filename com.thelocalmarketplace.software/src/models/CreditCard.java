package models;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;

import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.Card.CardSwipeData;
import com.thelocalmarketplace.hardware.external.CardIssuer;

public class CreditCard {

	private Card card;
	private CardSwipeData creditCardData;
	private long maxHolds;
	private BigDecimal creditLimit;
	private String creditCardCompany;
	private String creditCardNumber;
	private String creditCardHolder;
	private String creditCardCVV;
	private String creditCardChip;

	public CreditCard(Card card, BigDecimal creditLimit, String creditCardChip, long maxHolds) throws IOException {
		this.maxHolds = maxHolds;
		
		this.creditCardChip = creditCardChip;
		this.card = card;
		setCreditLimit(creditLimit); 
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	public BigDecimal getCreditLimit() {
		return this.creditLimit;
	}

	public long getMaxHolds() {
		return this.maxHolds;
	}
	
	public Card getCard() {
		return this.card;
	}

	public String getCreditCardChip() {
		return this.creditCardChip;
	}

}