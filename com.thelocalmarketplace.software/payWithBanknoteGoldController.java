import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.thelocalmarketplace.hardware.*;
import com.tdc.banknote.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;

public class payWithBanknoteGoldController implements BanknoteInsertionSlotObserver, BanknoteValidatorObserver, BanknoteStorageUnitObserver, BanknoteDispensationSlotObserver{
    private final SelfCheckoutStationGold goldStation;
    public BigDecimal remainingAmount;
    public Boolean banknoteInserted = false;
    public int banknoteInsertedNum;
    public Boolean banknoteEjected;
    public Boolean banknoteRemoved;
    public Boolean banknoteValidity;
    public int goodBanknoteNum;
    public int badBanknoteNum;
    public Boolean banknoteAdded;
    public int banknoteAddedNum;
    public Boolean banknotesFull;
    public Boolean banknotesDispensed;
    public List<Banknote> dispensedBanknotes;
    public Boolean banknotesRemoved;
    private Boolean payingCompleted;
    public Boolean canChange;
    public Boolean enabled;
    public Boolean turnedOn;
    private ReceiptPrinterController printer;
    private BigDecimal totalPrice;


    public payWithBanknoteGoldController(SelfCheckoutStationGold station){
        goldStation = station;
        goldStation.banknoteValidator.attach(this);
        goldStation.banknoteStorage.attach(this);
        goldStation.banknoteInput.attach(this);
        goldStation.banknoteOutput.attach(this);
        printer = new ReceiptPrinterController(station);
        banknoteInsertedNum = 0;
        goodBanknoteNum = 0;
        badBanknoteNum = 0;
        banknoteAddedNum = 0;
        banknotesFull = false;
        payingCompleted = false;
        canChange = true;
        enabled = false;
    }

    @Override
    public void enabled(IComponent<? extends IComponentObserver> component) {
    }

    @Override
    public void disabled(IComponent<? extends IComponentObserver> component) {
    }

    @Override
    public void turnedOn(IComponent<? extends IComponentObserver> component) {
    }

    @Override
    public void turnedOff(IComponent<? extends IComponentObserver> component) {
    }

    @Override
    public void banknoteInserted(BanknoteInsertionSlot slot) {
        banknoteInserted = true;
        banknoteEjected = false;
        banknoteRemoved = true;
        banknoteInsertedNum ++;
        System.out.println("Banknote is inserted to the insertion slot.");
    }

    @Override
    public void banknoteEjected(BanknoteInsertionSlot slot) {
        banknoteEjected = true;
        banknoteRemoved = false;
        System.out.println("Banknote is rejected from the insertion slot.");
    }

    @Override
    public void banknoteRemoved(BanknoteInsertionSlot slot) {
        banknoteRemoved = true;
        System.out.println("Banknote is removed from the insertion slot.");
    }

    @Override
    public void banknoteDispensed(BanknoteDispensationSlot slot, List<Banknote> banknotes) {
        banknotesDispensed = true;
        banknotesRemoved = false;
        dispensedBanknotes = banknotes;
        System.out.println("Banknote is dispensed from the dispensation slot.");
    }

    @Override
    public void banknotesRemoved(BanknoteDispensationSlot slot) {
        banknotesRemoved = true;
        System.out.println("Banknote is removed from the dispensation slot.");
    }

    @Override
    public void banknotesFull(BanknoteStorageUnit unit) {
        banknotesFull = true;
        //banknoteAdded = false;
        System.out.println("The storage unit is full. No more banknote can be added to storage unit.");
    }

    @Override
    public void banknoteAdded(BanknoteStorageUnit unit) {
        banknoteAdded = true;
        banknoteAddedNum++;
        System.out.println("Banknote is added to the storage unit.");
    }

    @Override
    public void banknotesLoaded(BanknoteStorageUnit unit) {
        banknoteAdded = true;
        System.out.println("Banknote is loaded in the storage unit.");
    }

    @Override
    public void banknotesUnloaded(BanknoteStorageUnit unit) {
        banknotesFull = false;
        System.out.println("Banknote is not loaded in the storage unit.");
    }

    @Override
    public void goodBanknote(BanknoteValidator validator, Currency currency, BigDecimal denomination) {
        banknoteValidity = true;
        goodBanknoteNum++;
        System.out.println("This " + denomination + currency + " is good.");
    }

    @Override
    public void badBanknote(BanknoteValidator validator) {
        banknoteValidity = false;
        badBanknoteNum++;
        System.out.println("This Banknote is verified to be false.");
    }

    public void setTotalPrice(BigDecimal price){
        totalPrice = price;
        remainingAmount = totalPrice;
    }

    public void payWithBanknote(Banknote... banknotes) throws DisabledException, CashOverloadException {
        int i =0;
        int n = banknotes.length;
        payingCompleted = false;
        while(!payingCompleted) {
            if (banknotesFull) {
                goldStation.banknoteInput.receive(banknotes[i]);
                System.out.println("it is full, cannot insert any banknote.");
            }
            goldStation.banknoteInput.receive(banknotes[i]);
            if (banknoteInserted) {
                //bronzeStation.banknoteValidator.receive(banknotes[i]);
                if (banknoteValidity) {
                    //bronzeStation.banknoteStorage.receive(banknotes[i]);
                    if (banknoteAdded) {
                        remainingAmount = remainingAmount.subtract(banknotes[i].getDenomination());
                        if (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
                            System.out.println("the remaining amount is " + remainingAmount);
                            System.out.println();
                        }
                        else if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
                            System.out.println("the remaining amount is zero.");
                            printer.printReceipt("Receipt\n" + "Total $" + totalPrice.intValue() + "\n" + "By Banknote");
                            payingCompleted = true;
                        }
                        else {
                            System.out.println("the remaining amount is less than zero.");
                            if (!makeChange(remainingAmount.negate())){
                                canChange = false;
                                //signalAttendant();
                                //suspendStation();
                            }
                            printer.printReceipt("Receipt\n" + "Total $" + totalPrice.intValue() + "\n" + "By Banknote");
                            payingCompleted = true;
                        }
                    }
                    else {
                        System.out.println("the banknote will be returned.");
                        //bronzeStation.banknoteOutput.receive(banknotes[i]);
                        //bronzeStation.banknoteOutput.dispense();
                    }
                }
                else {
                    System.out.println("the banknote will be returned.");
                    if (goldStation.banknoteInput.hasDanglingBanknotes()) {
                        payingCompleted = true;
                    }
                }
            }
            else {
                System.out.println("the banknote will be returned.");
                //bronzeStation.banknoteInput.emit(banknotes[i]);
                //bronzeStation.banknoteInput.removeDanglingBanknote();
            }
            i++;
            if (i == n && !payingCompleted) {
                //printReceipt();
                payingCompleted = true;
            }
        }
    }

    public SelfCheckoutStationGold getStation() {
        return goldStation;
    }

    public List<Banknote> findChange(Banknote[] storedBanknotes, BigDecimal changeAmount) {
        // The value of banknoteSet[i][j] will be true if there is a banknoteSet of
        // set[0..j-1] with sum equal to i
        int n = storedBanknotes.length;
        boolean[][] banknoteSet = new boolean[n + 1][changeAmount.intValue() + 1];

        // If sum is 0, then answer is true
        for (int i = 0; i <= n; i++)
            banknoteSet[i][0] = true;

        // If sum is not 0 and set is empty, then answer is false
        for (int i = 1; i <= changeAmount.intValue(); i++)
            banknoteSet[0][i] = false;

        // Fill the banknoteSet table in bottom-up manner
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= changeAmount.intValue(); j++) {
                if (j < storedBanknotes[i - 1].getDenomination().intValue())
                    banknoteSet[i][j] = banknoteSet[i - 1][j];
                else
                    banknoteSet[i][j] = banknoteSet[i - 1][j] || banknoteSet[i - 1][j - storedBanknotes[i - 1].getDenomination().intValue()];
            }
        }

        // Print the True-False table
        /*for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= changeAmount; j++) {
                System.out.print(banknoteSet[i][j] + "  ");
            }
            System.out.println();
        }*/

        if (banknoteSet[n][changeAmount.intValue()]) {
            System.out.println("Found a banknoteSet with the given sum");
            ArrayList<Banknote> sol = new ArrayList<>();
            // Using backtracking to find the solution
            int i = n;
            while (i >= 0) {
                if (banknoteSet[i][changeAmount.intValue()] && !banknoteSet[i - 1][changeAmount.intValue()]) {
                    sol.add(storedBanknotes[i - 1]);
                    changeAmount = changeAmount.subtract(storedBanknotes[i - 1].getDenomination());
                }
                if (changeAmount.intValue() == 0) {
                    break;
                }
                i--;
            }
            return sol;
        } else {
            return null;
        }
    }

    Boolean makeChange(BigDecimal expectedChange) throws DisabledException, CashOverloadException {
        List<Banknote> storedBanknotes = goldStation.banknoteStorage.unload();
        ArrayList<Banknote> allBanknotes = new ArrayList<>(storedBanknotes);
        Iterator<Banknote> iterator = allBanknotes.iterator();
        while (iterator.hasNext()) {
            Banknote element = iterator.next();
            if (element == null) {
                iterator.remove();
            }
        }
        if (findChange(allBanknotes.toArray(new Banknote[0]), remainingAmount.negate()) == null) {
            goldStation.banknoteStorage.load(allBanknotes.toArray(new Banknote[0]));
            return false;
        }
        else {
            ArrayList<Banknote> banknoteAsChange = new ArrayList<>(findChange(allBanknotes.toArray(new Banknote[0]), remainingAmount.negate()));
            Iterator<Banknote> iterator1 = allBanknotes.iterator();
            for (Banknote element2 : banknoteAsChange) {
                while (iterator1.hasNext()) {
                    Banknote element1 = iterator1.next();
                    if (element2 == element1) {
                        iterator1.remove();
                        break;
                    }
                }
            }
            goldStation.banknoteStorage.load(allBanknotes.toArray(new Banknote[0]));
            dispensedBanknotes = banknoteAsChange;
            for (Banknote banknote: banknoteAsChange) {
                goldStation.banknoteOutput.receive(banknote);
            }
            goldStation.banknoteOutput.dispense();
            return true;
        }
    }
}