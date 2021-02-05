package com.example.myapplication;

import java.util.UUID;

public class WifiAppServiceDescription {
    public UUID serviceUuid;
    public UUID scanChrUuid;
    public UUID wifiAvbChrUuid;
    public UUID ssidPassChrUuid;
    public UUID wifiSavedChrUuid;



    public WifiAppServiceDescription()
    {
        serviceUuid      = UUID.fromString("2e8a111e-5ebd-ab93-ac4a-da19b6f91fcd");
        scanChrUuid      = UUID.fromString("71256ef8-17bd-4c5f-a566-3afa5f4a7850");
        wifiAvbChrUuid   = UUID.fromString("a3a2b88d-c3ff-4d9e-a98f-33e74f18bec0");
        ssidPassChrUuid  = UUID.fromString("7ac76fb2-ee63-41ee-89f6-0bcd8d33e7dc");
        wifiSavedChrUuid = UUID.fromString("e5cdb810-0e3c-436c-942e-c21de434a515");
    }

}
