package com.thelocalmarketplace.software;

import com.tdc.CashOverloadException;
import com.tdc.DisabledException;
import com.tdc.IComponent;
import com.tdc.IComponentObserver;
import com.thelocalmarketplace.hardware.*;
import com.thelocalmarketplace.software.printing.ReceiptPrinterController;
import com.tdc.banknote.*;

import java.math.BigDecimal;
import java.util.*;

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
public class payWithBanknoteBronzeController implements BanknoteInsertionSlotObserver, BanknoteValidatorObserver, BanknoteStorageUnitObserver, BanknoteDispensationSlotObserver{
    private final SelfCheckoutStationBronze bronzeStation;
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
    private List<Banknote> change;
    Cart cart;

    public payWithBanknoteBronzeController (SelfCheckoutStationBronze station) {
        bronzeStation = station;
        bronzeStation.banknoteValidator.attach(this);
        bronzeStation.banknoteStorage.attach(this);
        bronzeStation.banknoteInput.attach(this);
        bronzeStation.banknoteOutput.attach(this);
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

    // set the price of the bill, should be done after choosing paying by banknotes
    private void setTotalPrice(BigDecimal price) {
        totalPrice = price;
        remainingAmount = totalPrice;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
        setTotalPrice(new BigDecimal(cart.totalCost));
    }

    // inputting one or more banknotes, and then it will complete paying process automatically.
    public void payWithBanknote(Banknote... banknotes) throws DisabledException, CashOverloadException {
        int i =0;
        int n = banknotes.length;
        payingCompleted = false; //the flag of paying process, only paying is completed, it will change to be true
        while(!payingCompleted) {

            //firstly check whether storage unit is full
            //If it is full, the checkout station cannot receive any banknote.
            if (banknotesFull) {
                bronzeStation.banknoteInput.receive(banknotes[i]);
                System.out.println("it is full, cannot insert any banknote.");
            }

            //inserting the banknote
            bronzeStation.banknoteInput.receive(banknotes[i]);

            //Only if the banknote is inserted successfully, it will be verified automatically
            if (banknoteInserted) {
                //bronzeStation.banknoteValidator.receive(banknotes[i]);

                //Only if the banknote is inserted and verified to be good, it will be added to storage unit.
                if (banknoteValidity) {
                    //bronzeStation.banknoteStorage.receive(banknotes[i]);

                    //Only if the banknote is inserted, verified to be good, and added to storage unit successfully,
                    //the remaining amount will be change
                    if (banknoteAdded) {
                        remainingAmount = remainingAmount.subtract(banknotes[i].getDenomination());
                        if (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
                            //When remaining amount is still greater than 0, more banknotes should be inserted.
                            System.out.println("the remaining amount is " + remainingAmount);
                        }
                        else if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
                            //When remaining amount is 0, the banknotes paid are enough, and do not need to change
                            System.out.println("the remaining amount is zero.");
                            printBill();
                            payingCompleted = true;
                        }
                        else {
                            ////When remaining amount less than 0, which means customers pay more money than they should,
                            // then change should be made
                            System.out.println("the remaining amount is less than zero.");
                            if (!makeChange(remainingAmount.negate())){
                                canChange = false;
                                //signalAttendant();
                                //suspendStation();
                            }
                            else {
                                change = bronzeStation.banknoteOutput.removeDanglingBanknotes();
                                showChange();
                            }
                            printBill();
                            payingCompleted = true;
                        }
                    }
                    else {
                        //When the banknote is not added to storage unit successfully,
                        //it will be returned automatically by hardware
                        System.out.println("the banknote will be returned.");
                        //bronzeStation.banknoteOutput.receive(banknotes[i]);
                        //bronzeStation.banknoteOutput.dispense();
                    }
                }
                else {
                    //When the banknote is not verified to be bad
                    //it will be returned automatically by hardware
                    System.out.println("the banknote will be returned.");
                    if (bronzeStation.banknoteInput.hasDanglingBanknotes()) {
                        payingCompleted = true;
                    }
                }
            }
            else {
                //When the banknote is not inserted to slot successfully,
                //it will be returned automatically by hardware
                System.out.println("the banknote will be returned.");
                //bronzeStation.banknoteInput.emit(banknotes[i]);
                //bronzeStation.banknoteInput.removeDanglingBanknote();
            }
            i++; // continuing inserting more banknotes
            if (i == n && !payingCompleted) {
                payingCompleted = true;
            }
        }
    }

    public SelfCheckoutStationBronze getStation() {
        return bronzeStation;
    }

    //To calculate whether the storage unit have the right combination of banknotes to change
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

    //Change for the payment
    Boolean makeChange(BigDecimal expectedChange) throws DisabledException, CashOverloadException {
        //getting all banknotes in the storage unit
        List<Banknote> storedBanknotes = bronzeStation.banknoteStorage.unload();
        ArrayList<Banknote> allBanknotes = new ArrayList<>(storedBanknotes);
        Iterator<Banknote> iterator = allBanknotes.iterator();
        while (iterator.hasNext()) {
            Banknote element = iterator.next();
            if (element == null) {
                iterator.remove();
            }
        }

        //implement findChange method to tell whether it is able to make change
        if (findChange(allBanknotes.toArray(new Banknote[0]), expectedChange) == null) {
            //When it cannot find a right combination of banknotes, return false
            bronzeStation.banknoteStorage.load(allBanknotes.toArray(new Banknote[0]));
            return false;
        }
        else {
            //When there is a right combination of banknotes

            //get a right combination of banknotes and remove them from the stored banknotes
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
            bronzeStation.banknoteStorage.load(allBanknotes.toArray(new Banknote[0]));

            //dispensing the right combination of change
            dispensedBanknotes = banknoteAsChange;
            for (Banknote banknote: banknoteAsChange) {
                bronzeStation.banknoteOutput.receive(banknote);
            }
            bronzeStation.banknoteOutput.dispense();
            return true;
        }
    }

    public Banknote[] getChange() {
        return change.toArray(new Banknote[0]);
    }

    private void showChange() {
        System.out.println("This is your change: ");
        for (int i = 0; i < change.size(); i++) {
            System.out.print(change.get(i).getDenomination() + " " +
                    change.get(i).getCurrency());
            if (i < change.size() - 1) {
                System.out.print(", ");
            }
            else {
                System.out.print(".");
            }
        }
    }

    private void printBill() {
        String receiptString = "  Receipt\n";
        receiptString += "Total Quantity: ";
        receiptString += cart.currentQuantity;
        receiptString += "\n";
        receiptString += "Total Cost: ";
        receiptString += cart.totalCost;
        printer.printReceipt(receiptString);
        System.out.println(printer.getPrintedReceipt());
    }
}
