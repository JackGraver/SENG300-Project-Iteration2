package com.thelocalmarketplace.software.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Test;

import com.jjjwelectronics.card.Card;
import com.jjjwelectronics.card.MagneticStripeFailureException;
import com.thelocalmarketplace.hardware.external.CardIssuer;

import ca.ucalgary.seng300.simulation.InvalidArgumentSimulationException;
import controllers.PayViaSwipeCreditGold;
import exceptions.HoldNotAcceptedException;
import exceptions.OverCreditException;
import exceptions.PriceIsZeroOrNegativeException;
import models.CreditCard;

public class PayViaCreditTest {


}
