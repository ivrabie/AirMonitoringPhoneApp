package com.example.myapplication;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BleDeviceInfoAdapter extends RecyclerView.Adapter<BleDeviceInfoAdapter.MyViewHolder> {

    private static final String TAG = "PersonListAdapter";
    private ArrayList<BleDeviceInfo> list_members;
    private final LayoutInflater inflater;
    private Context mContext;

    MyViewHolder holder;
    public BleDeviceInfoAdapter(Context mContext,ArrayList<BleDeviceInfo> list_members)
    {
        this.mContext = mContext;
        this.list_members = list_members;
        inflater=LayoutInflater.from(mContext);
    }
    //This method inflates view present in the RecyclerView
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_view_layout, parent, false);
        holder=new MyViewHolder(view);
        return holder;
    }


    //Binding the data using get() method of POJO object
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        BleDeviceInfo list_items=list_members.get(position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.mac.setText(list_items.getScanRes().getDevice().getAddress());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.rssi.setText(String.valueOf(list_items.getScanRes().getRssi()));
        }
        holder.status.setText(list_items.getConnectionStatus());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.name.setText(list_items.getScanRes().getDevice().getName());
        }
        holder.itemView.setOnClickListener(holder);
        BleDeviceInfo dev = list_members.get(position);
        Button btn = holder.itemView.findViewById(R.id.Connect);

        if(dev.isConnected == false)
        {
            btn.setText(holder.itemView.getResources().getString(R.string.btnConnect));
        }
        else
        {
            btn.setText(holder.itemView.getResources().getString(R.string.btnDisconnect));
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemScanListHandler handler = (ItemScanListHandler)mContext;
                if (btn.getText().equals(v.getResources().getString(R.string.btnConnect)))
                {
                    btn.setText(v.getResources().getString(R.string.btnDisconnect));
                    handler.btnConnectDevice(list_members.get(position));
                    dev.isConnected = true;

                }
                else
                {
                    btn.setText(v.getResources().getString(R.string.btnConnect));
                    handler.btnDisconnectDevice(list_members.get(position));
                    dev.isConnected = false;
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list_members.size();
    }


    //View holder class, where all view components are defined
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,mac,status,rssi;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name=(TextView)itemView.findViewById(R.id.twDevName);
            mac=(TextView)itemView.findViewById(R.id.twMac);
            status=(TextView)itemView.findViewById(R.id.twConnStatus);
            rssi=(TextView)itemView.findViewById(R.id.twRssi);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(view.getContext(),"Works blyaty", Toast.LENGTH_SHORT).show();
        }
    }
    public void removeAt(int position) {
        list_members.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(0, list_members.size());
    }

}
