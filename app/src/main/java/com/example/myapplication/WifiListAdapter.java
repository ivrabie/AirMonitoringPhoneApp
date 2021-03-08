package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class  WifiListAdapter extends  RecyclerView.Adapter<WifiListAdapter.WifiNetworkHolder> implements WifiUpdateConnection{

    Context mContext;
    ArrayList<WifiNetwork> networks;
    SensorDeviceGattCbk sensDevCbk;
    WifiUpdateConnection wifiConnectionUpdater;
    private final LayoutInflater inflater;
    int  currentConnectionPosition;
    private  final  byte wifi_idle   = 0;
    private  final  byte in_progress = 1;
    private  final  byte connected   = 2;
    public WifiListAdapter(Context mContext, ArrayList<WifiNetwork> list_members,SensorDeviceGattCbk sensDevCbk) {
        super();
        this.mContext = mContext;
        this.networks = list_members;
        this.sensDevCbk = sensDevCbk;
        wifiConnectionUpdater = this;
        inflater= LayoutInflater.from(mContext);
    }


    @NonNull
    @Override
    public WifiNetworkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.wifi_network, parent, false);
        WifiNetworkHolder wifiNetHolder = new WifiListAdapter.WifiNetworkHolder(view);
        return wifiNetHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull WifiNetworkHolder holder, int position) {
        WifiNetwork wifiNet = networks.get(position);
        holder.twWifiName.setText(wifiNet.getWifiNetwork());
        holder.twWifiStatus.setText(wifiNet.getWifiStatus());

        holder.itemView.setOnClickListener(holder);
        Button conn = holder.itemView.findViewById(R.id.wifi_connect);
        Button autoBtn = holder.itemView.findViewById(R.id.wifi_auto_connect);
        if(wifiNet.connectionStatus == connected)
        {
            conn.setText(holder.itemView.getResources().getString(R.string.btnDisconnect));
            autoBtn.setEnabled(false);
        }
        else
        {
            conn.setText(holder.itemView.getResources().getString(R.string.btnConnect));
            autoBtn.setEnabled(wifiNet.isWifiKnown());
        }
        conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Button connect" + conn.getText(), Toast.LENGTH_SHORT).show();
                if(conn.getText().equals(v.getResources().getString(R.string.btnConnect))) {
                    Dialog passwordDialog = new Dialog(mContext);
                    passwordDialog.setContentView(R.layout.wifi_password);
                    Button okayBtn = (Button) passwordDialog.findViewById(R.id.passwordOkayBtn);
                    TextView pass = (TextView) passwordDialog.findViewById(R.id.wifiPassword);

                    okayBtn.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                        public void onClick(View v) {
                            String password = pass.getText().toString();
                            sensDevCbk.connectWifiSsidPassword(wifiNet.wifiNetwork, password, wifiConnectionUpdater);
                            passwordDialog.dismiss();
                            currentConnectionPosition = position;
                        }
                    });
                    passwordDialog.show();
                }
                else if(conn.getText().equals(v.getResources().getString(R.string.btnDisconnect)))
                {
                    sensDevCbk.disconectWifi(wifiNet.wifiNetwork, wifiConnectionUpdater);
                    currentConnectionPosition = position;
                }
            }
        });
        autoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Button auto connect", Toast.LENGTH_SHORT).show();
                sensDevCbk.connectWifiSsid(wifiNet.wifiNetwork, wifiConnectionUpdater);
                currentConnectionPosition = position;
            }
        });

    }

    @Override
    public int getItemCount() {
        return networks.size();
    }

    @Override
    public void update(int state) {
        Activity act = (Activity)this.mContext;
        switch (state)
        {
            case wifi_idle:
                this.networks.get(currentConnectionPosition).wifiStatus = "Not connected";

                break;
            case in_progress:
                this.networks.get(currentConnectionPosition).wifiStatus = "Connecting...";
                break;
            case connected:
                this.networks.get(currentConnectionPosition).wifiStatus = "Connected";
                break;
        }
        networks.get(currentConnectionPosition).connectionStatus = state;
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
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
