import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import com.jjjwelectronics.*;
import com.jjjwelectronics.card.*;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.printer.*;
import com.jjjwelectronics.scale.*;
import com.tdc.*;
import com.tdc.banknote.*;
import com.tdc.coin.*;
import com.thelocalmarketplace.*;
import com.thelocalmarketplace.hardware.*;
import com.thelocalmarketplace.hardware.external.*;

public class DebitCard {
	
	private Card DebitCard;
	private CardIssuer Bank;
	private BufferedImage signature = new BufferedImage(1,1,1);
	
	public DebitCard(Card DebitCard, double AvailableFunds,CardIssuer Bank, Calendar expiry) throws IOException {
		this.DebitCard = DebitCard;
		this.Bank = Bank;
		CardData data = null; //set it to null to placate the compiler, should have an instance of CardData by the time it's used
		boolean swipeSuccesfull = false;
		while(!swipeSuccesfull) {
			try {
				data = DebitCard.swipe();
			} catch (MagneticStripeFailureException e) {
				continue;
			}
			swipeSuccesfull = true;
		}
		
		if(cardDataType(data) != "debit") {
			throw new IOException("Wrong type of card");
		}
		this.Bank.addCardData(cardDataNumber(data), cardDataCardholder(data), expiry, cardDataCVV(data), AvailableFunds);
	}
	
	/**
	 * To minimize the chance that the output is corrupted, this method will get the desired information 10 times and output the 
	 * most common result
	 * @param data
	 * @return mostCommonOutput
	 */
	private String cardDataType(CardData data) {
		HashMap<String, Integer> outputs = new HashMap<String, Integer>(); //records the number of times a certain output was given
		
		//get the output 10 times
		for(int i=0; i<=9; i++) {
			String currentOutput = data.getType();
			outputs.containsKey(1);
			if(outputs.containsKey(currentOutput)) {
				outputs.put(currentOutput, outputs.get(currentOutput) + 1); //increment value for existing output
			}
			else {
				outputs.put(currentOutput, 1);//add new output to the HashMap;
			}
		}
		
		//find the most common output
		String mostCommonOutput = "";
		int keyOccurence = 0;
		for(Map.Entry<CardData, String> current : outputs.entrySet()) {
			if((current.getValue() > keyOccurence) {
				mostCommonOutput = current.getKey();
				keyOccurence = current.getValue();
			}
		}
		return mostCommonOutput;
	}
	
	/**
	 * To minimize the chance that the output is corrupted, this method will get the desired information 10 times and output the 
	 * most common result
	 * @param data
	 * @return mostCommonOutput
	 */
	private String cardDataNumber(CardData data) {
		HashMap<String, Integer> outputs = new HashMap<String, Integer>(); //records the number of times a certain output was given
		
		//get the output 10 times
		for(int i=0; i<=9; i++) {
			String currentOutput = data.getNumber();
			if(outputs.containsKey(currentOutput)) {
				outputs.put(currentOutput, outputs.get(currentOutput) + 1); //increment value for existing output
			}
			else {
				outputs.put(currentOutput, 1); //add new output to the HashMap
			}
		}
		
		//find the most common output
		String mostCommonOutput = "";
		int keyOccurence = 0;
		for(Map.Entry<String, Integer> current : outputs.entrySet()) {
			if(current.getValue() > keyOccurence) {
				mostCommonOutput = current.getKey();
				keyOccurence = current.getValue();
			}
		}
		return mostCommonOutput;
	}
	
	/**
	 * To minimize the chance that the output is corrupted, this method will get the desired information 10 times and output the 
	 * most common result
	 * @param data
	 * @return mostCommonOutput
	 */
	private String cardDataCardholder(CardData data) {
		HashMap<String, Integer> outputs = new HashMap<String, Integer>(); //records the number of times a certain output was given
		
		//get the output 10 times
		for(int i=0; i<=9; i++) {
			String currentOutput = data.getCardholder();
			if(outputs.containsKey(currentOutput)) {
				outputs.put(currentOutput, outputs.get(currentOutput) + 1); //increment value for existing output
			}
			else {
				outputs.put(currentOutput, 1); //add new output to the HashMap
			}
		}
		
		//find the most common output
		String mostCommonOutput = "";
		int keyOccurence = 0;
		for(Map.Entry<String, Integer> current : outputs.entrySet()) {
			if(current.getValue() > keyOccurence) {
				mostCommonOutput = current.getKey();
				keyOccurence = current.getValue();
			}
		}
		return mostCommonOutput;
	}
	
	/**
	 * To minimize the chance that the output is corrupted, this method will get the desired information 10 times and output the 
	 * most common result
	 * @param data
	 * @return mostCommonOutput
	 */
	private String cardDataCVV(CardData data) {
		HashMap<String, Integer> outputs = new HashMap<String, Integer>(); //records the number of times a certain output was given
		
		//get the output 10 times
		for(int i=0; i<=9; i++) {
			String currentOutput = data.getCVV();
			if(outputs.containsKey(currentOutput)) {
				outputs.put(currentOutput, outputs.get(currentOutput) + 1); //increment value for existing output
			}
			else {
				outputs.put(currentOutput, 1); //add new output to the HashMap
			}
		}
		
		//find the most common output
		String mostCommonOutput = "";
		int keyOccurence = 0;
		for(Map.Entry<String, Integer> current : outputs.entrySet()) {
			if(current.getValue() > keyOccurence) {
				mostCommonOutput = current.getKey();
				keyOccurence = current.getValue();
			}
		}
		return mostCommonOutput;
	}
	
	/**
	 * Swipes the card to transfer money. If the magnetic strip fails, it will try again until it works. Only causes a reduction in the
	 * amount of money in the cardholder's account, doesn't add that amount to the amount paid.
	 * @param amount
	 * @param reader
	 * @return Whether or not the transaction went through
	 * @throws IOException, everything should actually be caught
	 */
	public boolean swipe(BigDecimal amount, CardReaderBronze reader) throws IOException{
		CardData data;
		
		while(true) {
			try {
				data = reader.swipe(DebitCard, signature);
			}
			catch(MagneticStripeFailureException e) { //If the magnetic strip fails, try again
				continue;
			}
			catch(BlockedCardException e){
				return false;
			}
			
			int holdNumber = Bank.authorizeHold(data.getNumber(), amount);
			if(holdNumber >= 0) {
				Bank.postTransaction(data.getNumber(), holdNumber, amount);
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	public boolean swipe(BigDecimal amount, CardReaderSilver reader) throws IOException{
		CardData data;
		
		while(true) {
			try {
				data = reader.swipe(DebitCard, signature);
			}
			catch(MagneticStripeFailureException e) { //If the magnetic strip fails, try again
				continue;
			}
			catch(BlockedCardException e){
				return false;
			}
			
			int holdNumber = Bank.authorizeHold(data.getNumber(), amount);
			if(holdNumber >= 0) {
				Bank.postTransaction(data.getNumber(), holdNumber, amount);
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	public boolean swipe(BigDecimal amount, CardReaderGold reader) throws IOException{
		CardData data;
		
		while(true) {
			try {
				data = reader.swipe(DebitCard);
			}
			catch(MagneticStripeFailureException e) { //If the magnetic strip fails, try again
				continue;
			}
			catch(BlockedCardException e){
				return false;
			}
			
			int holdNumber = Bank.authorizeHold(data.getNumber(), amount);
			if(holdNumber >= 0) {
				Bank.postTransaction(data.getNumber(), holdNumber, amount);
				return true;
			}
			else {
				return false;
			}
		}
	}
	
}



