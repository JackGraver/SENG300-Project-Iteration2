package com.thelocalmarketplace.software.AddItem;

import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;

import powerutility.PowerGrid;
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
public class StartSession {

    public SelfCheckoutStationGold selfCheckoutStationGold;
    public SelfCheckoutStationSilver selfCheckoutStationSilver;
    public SelfCheckoutStationBronze selfCheckoutStationBronze;

    // Precondition: System is not in a session
    private static boolean inSession = false;

    public StartSession(SelfCheckoutStationGold station) {
        this.selfCheckoutStationGold = station;
    }

    public StartSession(SelfCheckoutStationSilver station) {
        this.selfCheckoutStationSilver = station;
    }

    public StartSession(SelfCheckoutStationBronze station) {
        this.selfCheckoutStationBronze = station;
    }

    /**
     * Starts session
     *
     * @param powerGrid
     * @return void
     */
    public void startSession(PowerGrid powerGrid) {
        if (this.selfCheckoutStationGold != null) {
            this.selfCheckoutStationGold.plugIn(powerGrid);
            selfCheckoutStationGold.turnOn();
        } else if (selfCheckoutStationSilver != null) {
            this.selfCheckoutStationSilver.plugIn(powerGrid);
            selfCheckoutStationSilver.turnOn();
        } else if (selfCheckoutStationBronze != null) {
            this.selfCheckoutStationBronze.plugIn(powerGrid);
            selfCheckoutStationBronze.turnOn();
        }

        inSession = true;
    }

    public static boolean isInSession() {
        return inSession;
    }

    public static void endSession() {
        inSession = false;
    }
}
