package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class BleServicesFragment extends Fragment implements  BleServiceUpdateUI{


    BleServiceRecycleListAdapter recycleAdapter;
    BleDeviceInfo dev_info;
    public ArrayList<BleServiceUuid> serviceList;
    SensorDeviceGattCbk deviceGattCbk;
    ServiceAppHandler app_hnd;
    public BleServicesFragment(BleDeviceInfo dev_info, ServiceAppHandler app_hnd) {
        this.dev_info = dev_info;
        serviceList = new ArrayList<>();
        this.app_hnd = app_hnd;
    }

    public BleServicesFragment() {
        serviceList = new ArrayList<>();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.ble_services_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView mRecycleView = (RecyclerView) view.findViewById(R.id.serv_recycle_view);
        mRecycleView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycleAdapter = new BleServiceRecycleListAdapter(view.getContext(), this.serviceList,app_hnd);
        mRecycleView.setAdapter(recycleAdapter);
        deviceGattCbk = new SensorDeviceGattCbk(this.dev_info, serviceList, recycleAdapter, this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(dev_info != null)
            {
                this.dev_info.getScanRes().getDevice().connectGatt(view.getContext(), false, deviceGattCbk);
            }

        }
    }

    public SensorDeviceGattCbk getSensorDeviceCbk()
    {
        return this.deviceGattCbk;
    }

    @Override
    public void updateUI() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recycleAdapter.notifyDataSetChanged();
            }
        });

    }
}