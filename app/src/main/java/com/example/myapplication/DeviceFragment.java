package com.example.myapplication;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class DeviceFragment extends Fragment implements ServiceAppHandler {

    private  final  String TAG = "DeviceFragment";
    ViewPager wp;
    TabLayout tabLayout;
    BleDeviceInfo dev_info;
    private DeviceAppFragmentAdapter devWpAdapter;
    Fragment wifiAppFrag;
    String   wifiAppName;
    BleServicesFragment serviceFragment;
    SensorDeviceGattCbk deviceGattCbk;
    ArrayList<BleServiceUuid> serviceList;
    Context mContex;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public DeviceFragment(Context mContex, BleDeviceInfo dev_info) {
        super();
        this.dev_info = dev_info;
        wifiAppName = "Wifi App";
        this.mContex = mContex;
        serviceList = new ArrayList<>();
        serviceFragment = new BleServicesFragment(this,serviceList);
        deviceGattCbk = new SensorDeviceGattCbk(serviceFragment);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.device_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        devWpAdapter = new DeviceAppFragmentAdapter(getChildFragmentManager(), 0);
        devWpAdapter.addFragment(serviceFragment, "Services");
        dev_info.getScanRes().getDevice().connectGatt(mContex,false,deviceGattCbk);
        wp = view.findViewById(R.id.device_ble_wp);
        tabLayout = view.findViewById(R.id.dev_tab_layout);
        tabLayout.setupWithViewPager(wp);
        wp.setAdapter(devWpAdapter);
        Log.d("DevFragment", "Create device fragment");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void connectDevice()
    {
        dev_info.getScanRes().getDevice().connectGatt(mContex,false,deviceGattCbk);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void disconnectDevice()
    {
        deviceGattCbk.gatt.disconnect();
        this.serviceList.clear();
        serviceFragment.updateUI();
        this.handleCloseWifiApp();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void handleOpenWifiApp() {
        if(wifiAppFrag == null)
        {
            wifiAppFrag = new WifiConnectionFragment(deviceGattCbk);
        }
        else
        {
            WifiConnectionFragment frag = (WifiConnectionFragment)wifiAppFrag;
            frag.startScanBt();
        }
        devWpAdapter.addFragment(wifiAppFrag, wifiAppName);
        devWpAdapter.notifyDataSetChanged();
    }

    public BleDeviceInfo getDeviceInfo()
    {
        return this.dev_info;
    }


    @Override
    public void handleCloseWifiApp() {
        if(wifiAppFrag != null)
        {
            ((WifiConnectionFragment) wifiAppFrag).clearAll();
            devWpAdapter.deleteFragment(wifiAppFrag, wifiAppName);
            devWpAdapter.notifyDataSetChanged();
        }
    }

}