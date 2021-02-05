package com.example.myapplication;

public class WifiNetwork {
    String wifiNetwork;
    String wifiStatus;
    boolean isWifiKnown;

    public WifiNetwork(String wifiNetwork, String wifiStatus)
    {
        this.wifiNetwork = wifiNetwork;
        this.wifiStatus  = wifiStatus;
        this.isWifiKnown = false;
    }

    public void setWifiNetwork(String wifiNetwork) {
        this.wifiNetwork = wifiNetwork;
    }

    public void setWifiStatus(String wifiStatus) {
        this.wifiStatus = wifiStatus;
    }

    public void setWifiKnown(boolean wifiKnown) {
        isWifiKnown = wifiKnown;
    }

    public String getWifiNetwork() {
        return wifiNetwork;
    }

    public String getWifiStatus() {
        return wifiStatus;
    }

    public boolean isWifiKnown() {
        return isWifiKnown;
    }
}
