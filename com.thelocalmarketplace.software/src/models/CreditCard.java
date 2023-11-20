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
	private long maxHolds;
	private BigDecimal creditLimit;
	private CardIssuer bank;

	public CreditCard(Card card, BigDecimal creditLimit, long maxHolds, CardIssuer bank) throws IOException {
		this.maxHolds = maxHolds;
		this.card = card;
		this.bank = bank;
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
	
	public CardIssuer getBank() {
		return this.bank;
	}

}