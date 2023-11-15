package observers;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;

public class CreditCardObserver implements IDeviceListener {
	public String notified = "";
	public String enabledOrDisabled = "";
	public String bankNotifier = "";

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device) {
		this.enabledOrDisabled = "chip reader enabled";
	}
	
	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device) {
		this.enabledOrDisabled = "chip reader disabed";
		
	}

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device) {
		this.enabledOrDisabled = "enabled";
	}

	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device) {
		this.enabledOrDisabled = "disabled";
	}

	public void creditCardHasBeenInserted(IDevice<? extends IDeviceListener> device) {
		this.notified = "credit card inserted";
	}

	
	public void signalToTheBankTheAmountCharged(IDevice<? extends IDeviceListener> device) {
		bankNotifier = "The amount charged is sent to the Bank";
	}

	// 4. Bank: Signals to the System the hold number against the account of the
	// credit card.
	public void signalHoldFromTheBank(IDevice<? extends IDeviceListener> device) {
		bankNotifier = "bank signals a hold number";
	}
	
	// 5. System: Signals to the Bank that the transaction identified with the hold
	// number should be posted, reducing the amount of credit available.
	public void signalRequestForHoldNumberFromTheBank(IDevice<? extends IDeviceListener> device) {
		bankNotifier = "system requests a hold number";
	}
	
	// 6. Bank: Signals to the System that the transaction was successful.
	public void signalFromTheBankTransactionIsSuccessfull(IDevice<? extends IDeviceListener> device) {
		bankNotifier = "transaction successful";
	}
}