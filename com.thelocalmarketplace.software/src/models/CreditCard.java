package models;

import java.io.IOException;
import java.math.BigDecimal;

import com.jjjwelectronics.card.Card;
import com.thelocalmarketplace.hardware.external.CardIssuer;
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