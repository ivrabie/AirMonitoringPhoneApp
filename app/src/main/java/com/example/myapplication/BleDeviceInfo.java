package com.example.myapplication;

import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;


public class BleDeviceInfo implements Parcelable {
    ScanResult  scanRes;
    public BleDeviceInfo(ScanResult  scanRes)
    {
        this.scanRes = scanRes;
    }

    public BleDeviceInfo(Parcel  scanRes)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.scanRes = scanRes.readParcelable(ScanResult.class.getClassLoader());
        }
    }

    public ScanResult getScanRes() {
        return scanRes;
    }

    public String getConnectionStatus()
    {
        return "Not available";
    }

    public void setScanRes(ScanResult scanRes) {
        this.scanRes = scanRes;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dest.writeParcelable(scanRes,flags);
        }
    }

    public static final Creator<BleDeviceInfo> CREATOR = new Creator<BleDeviceInfo>() {
        public BleDeviceInfo createFromParcel(Parcel source) {
            BleDeviceInfo target = new BleDeviceInfo(source);
            return target;
        }

        public BleDeviceInfo[] newArray(int size) {
            return new BleDeviceInfo[size];
        }
    };
}
