package com.example.myapplication;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.util.SparseArray;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ItemScanListHandler {

    final  static  String TAG = "Main activity";
    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    ArrayList<BleDeviceInfo> devicesConnected;
    int REQUEST_ENABLE_BT = 3;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean mScanning;
    private Handler handler;
    private ScanCallback leScanCallback ;
    private ViewPager viewPager;
    private TabLayout   tabLayout;
    private RecyclerView recyclerView;
    private Fragment scanFragment;
    private int      manufactureId = 0xAEEA;
    AppTabFragmentAdapter viewPagerAdapt;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        scanFragment = new ScanFragment();
        viewPagerAdapt = new AppTabFragmentAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapt.addFragment(scanFragment, "Scanner");
        viewPager.setAdapter(viewPagerAdapt);
        devicesConnected = new ArrayList<>();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }


        bluetoothLeScanner =
                BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();

        handler = new Handler();
        leScanCallback =
                new ScanCallback() {
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {

                        SparseArray<byte[]> manufactureData = result.getScanRecord().getManufacturerSpecificData();
                        if(manufactureData.size() > 0)
                        {
                            if(manufactureData.keyAt(0) == manufactureId) {
                                ScanFragment frag = (ScanFragment) scanFragment;
                                frag.addResult(new BleDeviceInfo(result));
                            }
                        }

                    }
                };
    }


    private void scanLeDevice() {
        if (!mScanning) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mScanning == true) {
                        mScanning = false;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            bluetoothLeScanner.stopScan(leScanCallback);

                        }
                    }
                }
            }, SCAN_PERIOD);

            mScanning = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bluetoothLeScanner.startScan(leScanCallback);
                Toast.makeText(this, R.string.scanning, Toast.LENGTH_SHORT).show();
            }
        } else {
            mScanning = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bluetoothLeScanner.stopScan(leScanCallback);
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.ble_scan) {
            this.scanLeDevice();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void btnHandler(BleDeviceInfo dev) {
        boolean found = this.devicesConnected.contains(dev);
        if(found == false)
        {
            Toast.makeText(getApplicationContext(), "Test activity hdl", Toast.LENGTH_SHORT);

            DeviceFragment frag = new DeviceFragment(dev);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewPagerAdapt.addFragment(frag, dev.getScanRes().getDevice().getName() + "\n" +
                        dev.getScanRes().getDevice().getAddress());
            }
            this.devicesConnected.add(dev);
            viewPagerAdapt.notifyDataSetChanged();
            viewPager.setCurrentItem(this.devicesConnected.size());
            viewPagerAdapt.notifyDataSetChanged();
        }

    }
}