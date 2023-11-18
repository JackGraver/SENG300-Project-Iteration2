package observers;

import com.jjjwelectronics.IDevice;
import com.jjjwelectronics.IDeviceListener;
import com.jjjwelectronics.card.Card.CardData;
import com.jjjwelectronics.card.CardReaderListener;

public interface CreditCardObserver extends CardReaderListener {

	@Override
	public void aDeviceHasBeenEnabled(IDevice<? extends IDeviceListener> device);
	
	@Override
	public void aDeviceHasBeenDisabled(IDevice<? extends IDeviceListener> device);

	@Override
	public void aDeviceHasBeenTurnedOn(IDevice<? extends IDeviceListener> device);
	
	@Override
	public void aDeviceHasBeenTurnedOff(IDevice<? extends IDeviceListener> device);

	@Override
	public void aCardHasBeenSwiped();

	@Override
	public void theDataFromACardHasBeenRead(CardData data);
	
}