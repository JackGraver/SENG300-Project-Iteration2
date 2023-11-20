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

import powerutility.PowerGrid;



public class DebitCardController implements CardReaderListener {
	
	public SelfCheckoutStationGold selfCheckoutStationGold;
	public SelfCheckoutStationBronze selfCheckoutStationBronze;
	public SelfCheckoutStationSilver selfCheckoutStationSilver;
	public PowerGrid powerGrid; 
	public CardIssuer bank;
	public double amountDue;
	public CardData cardData; 

	public DebitCardController (SelfCheckoutStationGold ssg) {
		this.selfCheckoutStationGold = ssg;
        this.selfCheckoutStationGold.cardReader.register(this);;
		
	}
	
	public DebitCardController (SelfCheckoutStationSilver ssv) {
		this.selfCheckoutStationSilver = ssv;
		this.selfCheckoutStationSilver.cardReader.register(this);
		
		
	}
	
	public DebitCardController (SelfCheckoutStationBronze ssb) {
		this.selfCheckoutStationBronze = ssb;
		this.selfCheckoutStationBronze.cardReader.register(this);
		
	}
	

	@Override
	public void aCardHasBeenSwiped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void theDataFromACardHasBeenRead(CardData data) {
		cardData = data;
		if (data.getType() != "Debit")
			System.out.println("Wrong type of card");
		else {
			//Check Identity
			
			long holdNumber = bank.authorizeHold(data.getNumber(), amountDue);
			if (holdNumber == -1){
				System.out.println("Hold failed");
			}
			else {
				boolean transactionResult = bank.postTransaction(data.getNumber(), holdNumber, amountDue);

				 if (transactionResult) {
					 this.setAmountDue(0.0);
		             updateAmountDueDisplay();
		         } else {
		             System.out.println("Transaction posting failed");
		         }
				
			}		
			
		}		
		
	}
	
	
	private void updateAmountDueDisplay() {
		System.out.println("Here is updated total: ");
		System.out.println("$" + this.getAmountDue());
		
		
	}
	
	public void setAmountDue(double amount){
		amountDue = amount;
    }
	
	public double getAmountDue(){
		return amountDue;
    }
	public void setBank(CardIssuer cardIssuer){
		bank = cardIssuer;
    }
	
	public CardIssuer getBank(){
		return bank;
    }


	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		// TODO Auto-generated method stub
		
	}
	
	

}
