package com.example.myapplication;

public class BleServiceUuid {

    String app_name;
    String uuid;

    public BleServiceUuid(String app_name, String uuid)
    {
        this.app_name = app_name;
        this.uuid = uuid;
    }

    public String getApp_name() {
        return app_name;
    }

    public String getUuid() {
        return uuid;
    }
}
