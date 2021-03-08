package com.example.myapplication;

public class WifiNetwork {
    public String wifiNetwork;
    public String wifiStatus;
    boolean isWifiKnown;
    int  connectionStatus;

    public WifiNetwork(String wifiNetwork, String wifiStatus)
    {
        this.wifiNetwork = wifiNetwork;
        this.wifiStatus  = wifiStatus;
        this.isWifiKnown = false;
        this.connectionStatus = 0;
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
