package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.CardReaderBronze;
import com.jjjwelectronics.card.CardReaderGold;
import com.jjjwelectronics.card.CardReaderSilver;

import controllers.CreditCard;

public class PayViaCreditCardTest {

	private Card creditCard;
	private CreditCard bronzeCreditCardData;
	private CreditCard silverCreditCardData;
	private CreditCard goldCreditCardData;
	private CardReaderBronze cardReaderBronze;
	private CardReaderSilver cardReaderSilver;
	private CardReaderGold cardReaderGold;

	@Before
	public void setUp() throws IOException {
		bronzeCreditCardData = (CreditCard) cardReaderBronze.swipe(creditCard);
		silverCreditCardData = (CreditCard) cardReaderSilver.swipe(creditCard);
		goldCreditCardData = (CreditCard) cardReaderGold.swipe(creditCard);
	}

}
