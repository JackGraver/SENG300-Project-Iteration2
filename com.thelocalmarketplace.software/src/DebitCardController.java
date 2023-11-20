import com.jjjwelectronics.*;
import com.jjjwelectronics.card.*;
import com.jjjwelectronics.card.Card.CardData;
import com.thelocalmarketplace.hardware.*;
import com.thelocalmarketplace.hardware.external.*;
import com.thelocalmarketplace.software.printing.ReceiptPrinterController;


import powerutility.PowerGrid;



public class DebitCardController implements CardReaderListener {
	
	public SelfCheckoutStationGold selfCheckoutStationGold;
	public SelfCheckoutStationBronze selfCheckoutStationBronze;
	public SelfCheckoutStationSilver selfCheckoutStationSilver;
	public PowerGrid powerGrid; 
	public CardIssuer bank;
	public double amountDue;
	public CardData cardData; 
	public ReceiptPrinterController printer;
	public double totalPrice;
	public boolean completePayment;
	public boolean isEnabled;
	public boolean isOn;
	

	public DebitCardController (SelfCheckoutStationGold ssg) {
		this.selfCheckoutStationGold = ssg;
        this.selfCheckoutStationGold.cardReader.register(this);
        printer = new ReceiptPrinterController(this.selfCheckoutStationGold);
        completePayment = false;
		
	}
	
	public DebitCardController (SelfCheckoutStationSilver ssv) {
		this.selfCheckoutStationSilver = ssv;
		this.selfCheckoutStationSilver.cardReader.register(this);
		printer = new ReceiptPrinterController(this.selfCheckoutStationSilver);
		completePayment = false;
		
		
	}
	
	public DebitCardController (SelfCheckoutStationBronze ssb) {
		this.selfCheckoutStationBronze = ssb;
		this.selfCheckoutStationBronze.cardReader.register(this);
		printer = new ReceiptPrinterController(this.selfCheckoutStationBronze);
		completePayment = false;
		
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
			
			boolean transactionResult = bank.postTransaction(data.getNumber(), holdNumber, amountDue);

			 if (transactionResult) {
	             updateAmountDueDisplay();
	             bank.releaseHold(data.getNumber(), holdNumber);
	         } else {
	             System.out.println("Transaction posting failed");
	         }
				
			}		
				
		
	}
	
	
	private void updateAmountDueDisplay() {
		printer.printReceipt("Receipt\n" + "Total $" + totalPrice + "\n" + "By Debit");
		this.setAmountDue(0.0);
		completePayment = true;
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
		isEnabled = true;
	}

	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		isEnabled = false;
		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		isOn = true;
		
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		isOn = false;
		
	}
	
	

}
