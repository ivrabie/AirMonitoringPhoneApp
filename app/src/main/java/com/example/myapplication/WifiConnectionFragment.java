package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;


public class WifiConnectionFragment extends Fragment implements WifiAppUpdater{


    ArrayList<WifiNetwork> netDisc;
    WifiListAdapter recycleAdapter;
    SensorDeviceGattCbk devGattCbk;
    public WifiConnectionFragment(SensorDeviceGattCbk devGattCbk) {
        super();
        netDisc = new ArrayList<>();
        this.devGattCbk = devGattCbk;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.wifi_connection_fragment, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView mRecycleView = (RecyclerView) view.findViewById(R.id.ListWifi);
        mRecycleView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycleAdapter = new WifiListAdapter(view.getContext(), this.netDisc);
        mRecycleView.setAdapter(recycleAdapter);
        this.devGattCbk.setWifiAppUpdater(this);
        this.devGattCbk.startWifiScan();
    }

    @Override
    public void updateWifiList(ArrayList<String> wifiAvb, ArrayList<String> savedWifi) {
        for(String s : wifiAvb)
        {
            WifiNetwork wifiNet = new WifiNetwork(s, "Not connected");
            for(String known : savedWifi)
            {
                if(s.equals(known))
                {
                    wifiNet.setWifiKnown(true);
                }
            }
            netDisc.add(wifiNet);
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recycleAdapter.notifyDataSetChanged();
            }
        });
    }
}