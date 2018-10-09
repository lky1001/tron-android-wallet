package com.devband.tronwalletforandroid.common;

public class SecurityUpdater {

    private CustomPreference customPreference;

    public SecurityUpdater(CustomPreference customPreference) {

        this.customPreference = customPreference;
    }

    public boolean canUpdate() {
        return true;
    }

    public boolean doUpdate() {
        return true;
    }
}
