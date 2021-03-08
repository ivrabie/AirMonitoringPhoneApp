package com.example.myapplication;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

public class BleServiceRecycleListAdapter extends RecyclerView.Adapter<BleServiceRecycleListAdapter.ServiceHolder> {

    private ArrayList<BleServiceUuid> services;
    ServiceHolder serviceHolder;
    private final LayoutInflater inflater;
    private Context mContext;
    ServiceAppHandler app_hnd;
    public BleServiceRecycleListAdapter(Context mContext, ArrayList<BleServiceUuid> list_members,
                                        ServiceAppHandler app_hnd) {
        super();
        this.mContext = mContext;
        this.services = list_members;
        inflater=LayoutInflater.from(mContext);
        this.app_hnd = app_hnd;
    }

    @NonNull
    @Override
    public ServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.ble_service_layout, parent, false);
        serviceHolder = new ServiceHolder(view);
        return serviceHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final  BleServiceRecycleListAdapter.ServiceHolder holder, int position) {
        BleServiceUuid serv_uuid = services.get(position);
        holder.tw_app_name.setText(serv_uuid.app_name);
        holder.tw_uuid.setText(serv_uuid.uuid);

        holder.itemView.setOnClickListener(holder);
        Button btn = holder.itemView.findViewById(R.id.open_app);
        if( services.size() > 0)
        {
            BleServiceUuid serviceApp = services.get(position);
            if(serviceApp.isOpen == true)
            {
                btn.setText(holder.itemView.getResources().getString(R.string.btnClose));
            }
            else
            {
                btn.setText(holder.itemView.getResources().getString(R.string.btnOpen));
            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(services.get(position).isOpen == false)
                {
                    services.add(services.get(position));
                    app_hnd.handleOpenWifiApp();
                    btn.setText(v.getResources().getString(R.string.btnClose));
                    services.get(position).isOpen = true;

                }
                else
                {
                    services.remove(services.get(position));
                    app_hnd.handleCloseWifiApp();
                    btn.setText(v.getResources().getString(R.string.btnOpen));
                    services.get(position).isOpen = false;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }


    class ServiceHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tw_app_name, tw_uuid;
        public ServiceHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tw_app_name = (TextView)itemView.findViewById(R.id.ble_app_serv);
            tw_uuid = (TextView)itemView.findViewById(R.id.ble_uuid);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(view.getContext(),"Works blyaty", Toast.LENGTH_SHORT).show();
        }
    }


}
