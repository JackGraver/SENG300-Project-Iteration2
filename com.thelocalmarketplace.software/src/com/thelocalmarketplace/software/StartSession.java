package com.thelocalmarketplace.software;

import com.thelocalmarketplace.hardware.SelfCheckoutStationBronze;
import com.thelocalmarketplace.hardware.SelfCheckoutStationGold;
import com.thelocalmarketplace.hardware.SelfCheckoutStationSilver;

import powerutility.PowerGrid;

public class StartSession {

    public SelfCheckoutStationGold selfCheckoutStationGold;
    public SelfCheckoutStationSilver selfCheckoutStationSilver;
    public SelfCheckoutStationBronze selfCheckoutStationBronze;

    // Precondition: System is not in a session
    private boolean inSession = false;

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

    public boolean isInSession() {
        return inSession;
    }
}