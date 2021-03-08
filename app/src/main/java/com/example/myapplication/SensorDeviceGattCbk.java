package com.example.myapplication;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class SensorDeviceGattCbk extends BluetoothGattCallback {
    private  final  String TAG = "SensorDeviceGattCbk";
    private  final  int  mtu_size    = 512;
    private  final  byte scan_req_on = 1;

    List<BluetoothGattService> deviceServices;
    WifiAppServiceDescription  wifiAppServDesc;
    ArrayList<String>         wifiAvbList;
    ArrayList<String>         wifiSavedList;
    BleServiceUpdateUI uiUpdate;
    BluetoothGatt gatt;
    WifiAppUpdater wifiAppIf;
    WifiUpdateConnection wifiConnection;
    public SensorDeviceGattCbk(BleServiceUpdateUI uiUpdate) {
        super();
        this.wifiAppServDesc = new WifiAppServiceDescription();
        this.uiUpdate = uiUpdate;
        this.wifiAvbList = new ArrayList<>();
        this.wifiSavedList = new ArrayList<>();
    }

    public void setWifiAppUpdater(WifiAppUpdater wifiAppIf)
    {
        this.wifiAppIf = wifiAppIf;
    }

    @Override
    public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        super.onPhyUpdate(gatt, txPhy, rxPhy, status);
    }

    @Override
    public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        super.onPhyRead(gatt, txPhy, rxPhy, status);
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        Log.d(TAG, "onConnectionStateChange");
        if(newState == BluetoothGatt.STATE_CONNECTED)
        {
            this.gatt = gatt;
            boolean discovered = gatt.discoverServices();
            if(discovered == true)
            {
                Log.d(TAG, "onConnectionStateChange start discovering");
            }
        }
        else
        {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        Log.d(TAG, "onServicesDiscovered");
        deviceServices = gatt.getServices();

        if(deviceServices != null)
        {
            for(BluetoothGattService gatt_serv : deviceServices)
            {
                if(gatt_serv.getUuid().equals(this.wifiAppServDesc.serviceUuid))
                {
                    this.uiUpdate.addService(new BleServiceUuid("Wifi Connection", gatt_serv.getUuid().toString()));
                    gatt.requestMtu(mtu_size);
                }
            }
            uiUpdate.updateUI();
        }

    }


    public void startWifiScan()
    {
        BluetoothGattService wifiServ = this.gatt.getService(this.wifiAppServDesc.serviceUuid);
        BluetoothGattCharacteristic wifiScanChr = wifiServ.getCharacteristic(this.wifiAppServDesc.scanChrUuid);
        byte []scan_req = {scan_req_on};
        wifiScanChr.setValue(scan_req);
        this.gatt.writeCharacteristic(wifiScanChr);
    }

    public void connectWifiSsidPassword(String ssid, String password, WifiUpdateConnection conn)
    {
        BluetoothGattService wifiServ = this.gatt.getService(this.wifiAppServDesc.serviceUuid);
        BluetoothGattCharacteristic wifiSsidPassChr = wifiServ.getCharacteristic(this.wifiAppServDesc.ssidPassChrUuid);
        StringBuilder ssidPass = new StringBuilder();
        ssidPass.append(ssid);
        ssidPass.append("#");
        ssidPass.append(password);
        this.wifiConnection = conn;
        wifiSsidPassChr.setValue(ssidPass.toString());
        this.gatt.writeCharacteristic(wifiSsidPassChr);

    }

    public void connectWifiSsid(String ssid, WifiUpdateConnection conn)
    {
        BluetoothGattService wifiServ = this.gatt.getService(this.wifiAppServDesc.serviceUuid);
        BluetoothGattCharacteristic wifiSavedChr = wifiServ.getCharacteristic(this.wifiAppServDesc.wifiSavedChrUuid);

        this.wifiConnection = conn;
        wifiSavedChr.setValue(ssid);
        this.gatt.writeCharacteristic(wifiSavedChr);

    }

    public void disconectWifi(String ssid, WifiUpdateConnection conn)
    {
        BluetoothGattService wifiServ = this.gatt.getService(this.wifiAppServDesc.serviceUuid);
        BluetoothGattCharacteristic wifiAvb = wifiServ.getCharacteristic(this.wifiAppServDesc.wifiAvbChrUuid);

        this.wifiConnection = conn;
        wifiAvb.setValue(ssid);
        this.gatt.writeCharacteristic(wifiAvb);
    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
        Log.d(TAG, "onCharacteristicRead");
        byte []response  = characteristic.getValue();
        if(status == BluetoothGatt.GATT_SUCCESS)
        {
            if(characteristic.getUuid().equals(this.wifiAppServDesc.wifiAvbChrUuid)) {
                String wifiAvbStr = new String(response, StandardCharsets.US_ASCII);
                Log.d(TAG,wifiAvbStr);
                String[] wifiList = wifiAvbStr.split(",");
                this.wifiAvbList.clear();
                for (String s : wifiList)
                {
                    if(!s.isEmpty() && !s.trim().isEmpty())
                    {
                        this.wifiAvbList.add(s);
                    }
                }
                BluetoothGattCharacteristic wifiSavedChr = gatt.getService(this.wifiAppServDesc.serviceUuid).getCharacteristic(this.wifiAppServDesc.wifiSavedChrUuid);
                gatt.readCharacteristic(wifiSavedChr);
            }

            if(characteristic.getUuid().equals(this.wifiAppServDesc.wifiSavedChrUuid))
            {
                String wifiSavedStr = new String(response, StandardCharsets.US_ASCII);
                Log.d(TAG, "Available:" + wifiSavedStr);
                String[] wifiList = wifiSavedStr.split(",");
                this.wifiSavedList.clear();
                for (String s : wifiList)
                {
                    if(!s.isEmpty() && !s.trim().isEmpty())
                    {
                        this.wifiSavedList.add(s);
                    }
                }
                if(this.wifiAppIf != null)
                {
                    this.wifiAppIf.updateWifiList(wifiAvbList, wifiSavedList);
                }
            }

            if(characteristic.getUuid().equals(this.wifiAppServDesc.ssidPassChrUuid))
            {
                if(response.length == 1)
                {
                    if(wifiConnection != null) {
                        wifiConnection.update(response[0]);
                        if (response[0] == 1)
                        {
                            BluetoothGattService wifiServ = this.gatt.getService(this.wifiAppServDesc.serviceUuid);
                            BluetoothGattCharacteristic wifiSsidPassChr = wifiServ.getCharacteristic(this.wifiAppServDesc.ssidPassChrUuid);
                            gatt.readCharacteristic(wifiSsidPassChr);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
        Log.d(TAG, "onCharacteristicWrite " + String.valueOf(status));

        byte[] response = characteristic.getValue();
        if (status == BluetoothGatt.GATT_SUCCESS)
        {
            if (characteristic.getUuid().equals(this.wifiAppServDesc.scanChrUuid))
            {
                BluetoothGattCharacteristic wifiAvbChr = gatt.getService(this.wifiAppServDesc.serviceUuid).getCharacteristic(this.wifiAppServDesc.wifiAvbChrUuid);
                gatt.readCharacteristic(wifiAvbChr);
            }

            if (characteristic.getUuid().equals(this.wifiAppServDesc.ssidPassChrUuid) ||
                characteristic.getUuid().equals(this.wifiAppServDesc.wifiSavedChrUuid) ||
                characteristic.getUuid().equals(this.wifiAppServDesc.wifiAvbChrUuid))
            {
                BluetoothGattService wifiServ = this.gatt.getService(this.wifiAppServDesc.serviceUuid);
                BluetoothGattCharacteristic wifiSsidPassChr = wifiServ.getCharacteristic(this.wifiAppServDesc.ssidPassChrUuid);
                gatt.readCharacteristic(wifiSsidPassChr);
            }
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        Log.d(TAG, "onCharacteristicChanged");
        byte []response  = characteristic.getValue();
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorRead(gatt, descriptor, status);
        Log.d(TAG, "onDescriptorRead");
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
        Log.d(TAG, "onDescriptorWrite");
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        super.onReliableWriteCompleted(gatt, status);
        Log.d(TAG, "onReliableWriteCompleted");
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        super.onReadRemoteRssi(gatt, rssi, status);
        Log.d(TAG, "onReadRemoteRssi");
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        super.onMtuChanged(gatt, mtu, status);
        Log.d(TAG, "onMtuChanged");
    }

}
