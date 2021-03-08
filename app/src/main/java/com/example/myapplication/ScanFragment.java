package com.example.myapplication;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScanFragment extends Fragment {

    private static final String TAG = "Fragment One";
    BleDeviceInfoAdapter adapter;
    ItemScanListHandler handler;
    ArrayList<BleDeviceInfo> deviceList = new ArrayList<>();
    public ScanFragment()
    {
        super();

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.scan_fragment, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.handler = (ItemScanListHandler)context;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "on create fragment");
        RecyclerView mRecycleView = (RecyclerView) view.findViewById(R.id.ble_list);
        mRecycleView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new BleDeviceInfoAdapter(view.getContext(), this.deviceList);
        mRecycleView.setAdapter(adapter);

    }

    public  void addResult(BleDeviceInfo dev)
    {
        boolean found = false;
        for(BleDeviceInfo dev1 : deviceList)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if(dev1.getScanRes().getDevice().getAddress().
                        equals(dev.getScanRes().getDevice().getAddress()))
                {
                    found = true;
                    break;
                }
            }
        }
        if(found == false)
        {
            this.deviceList.add(dev);
            this.adapter.notifyDataSetChanged();
        }
    }

    public void clearAllResults()
    {
        this.deviceList.clear();
        this.adapter.notifyDataSetChanged();
    }

    public ArrayList<BleDeviceInfo> getDevices()
    {
        return this.deviceList;
    }



}