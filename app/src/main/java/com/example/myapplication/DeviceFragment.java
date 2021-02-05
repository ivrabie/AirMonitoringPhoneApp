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
    BleServicesFragment serviceFragment;
    public DeviceFragment(BleDeviceInfo dev_info) {
        super();
        this.dev_info = dev_info;
        devWpAdapter = null;


    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.device_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(devWpAdapter == null)
        {
            devWpAdapter = new DeviceAppFragmentAdapter(getChildFragmentManager(), 0);
            serviceFragment = new BleServicesFragment(dev_info,this);
            devWpAdapter.addFragment(serviceFragment, "Services");
        }
        wp = view.findViewById(R.id.device_ble_wp);
        tabLayout = view.findViewById(R.id.dev_tab_layout);
        tabLayout.setupWithViewPager(wp);
        wp.setAdapter(devWpAdapter);

//        wp.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });

        Log.d("DevFragment", "Create device fragment");

    }


    @Override
    public void hadleOpenWifiApp() {
        Fragment frag = new WifiConnectionFragment(serviceFragment.getSensorDeviceCbk());
        devWpAdapter.addFragment(frag, "Wifi app");
        devWpAdapter.notifyDataSetChanged();
    }
}