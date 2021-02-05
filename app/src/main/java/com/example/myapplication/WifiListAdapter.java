package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class  WifiListAdapter extends  RecyclerView.Adapter<WifiListAdapter.WifiNetworkHolder>{

    Context mContext;
    ArrayList<WifiNetwork> networks;
    private final LayoutInflater inflater;
    public WifiListAdapter(Context mContext, ArrayList<WifiNetwork> list_members) {
        super();
        this.mContext = mContext;
        this.networks = list_members;
        inflater= LayoutInflater.from(mContext);
    }


    @NonNull
    @Override
    public WifiNetworkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.wifi_network, parent, false);
        WifiNetworkHolder wifiNetHolder = new WifiListAdapter.WifiNetworkHolder(view);
        return wifiNetHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WifiNetworkHolder holder, int position) {
        WifiNetwork wifiNet = networks.get(position);
        holder.twWifiName.setText(wifiNet.getWifiNetwork());
        holder.twWifiStatus.setText(wifiNet.getWifiStatus());

        holder.itemView.setOnClickListener(holder);
        Button btnConnect = holder.itemView.findViewById(R.id.wifi_connect);
        Button btnAutoConnect = holder.itemView.findViewById(R.id.wifi_auto_connect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Button connect", Toast.LENGTH_SHORT).show();
            }
        });
        btnAutoConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Button auto connect", Toast.LENGTH_SHORT).show();
            }
        });


        btnAutoConnect.setEnabled(wifiNet.isWifiKnown());


    }

    @Override
    public int getItemCount() {
        return networks.size();
    }

    class WifiNetworkHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView twWifiName;
        public TextView twWifiStatus;


        WifiNetworkHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            twWifiName   = (TextView)itemView.findViewById(R.id.wifi_name);
            twWifiStatus = (TextView)itemView.findViewById(R.id.wifi_status);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
