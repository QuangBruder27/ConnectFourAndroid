package com.quangbruder.connectfourkbe.GameCommunication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;

import com.quangbruder.connectfourkbe.GUI.MainActivity;

import java.lang.reflect.Method;

public class BluetoothConnectorImpl implements BluetoothConnector {

    BluetoothAdapter bluetoothAdapter;
    Server serverClass;
    Client clientClass;

    public BluetoothConnectorImpl(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public boolean isEnabled(){
        return bluetoothAdapter.isEnabled();
    }

    @Override
    public void requestEnableBluetooth(Activity activity){
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableIntent, 1);
    }

    @Override
    public void setBTReceiver(MainActivity activity){
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        BluetoothBroadcastReceiver myReceiver = new BluetoothBroadcastReceiver(activity);
        activity.registerReceiver(myReceiver, intentFilter);

    }

    @Override
    public void waiting(MainActivity activity){
        serverClass = new Server(bluetoothAdapter, activity.handler, activity);
        serverClass.start();
        // discoverable
        /*
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        activity.startActivity(intent);
         */
        makeDiscoverable();
    }

    @Override
    public void scanning(){
        bluetoothAdapter.startDiscovery();
    }

    @Override
    public void acceptDevice(MainActivity activity, int position){
        clientClass = new Client(activity.listOfDevice.get(position), activity.handler, activity);
        clientClass.start();
    }

    @Override
    public void makeDiscoverable(){
        Method method;
        try {
            method = bluetoothAdapter.getClass().getMethod("setScanMode", int.class, int.class);
            method.invoke(bluetoothAdapter,BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE,300);
            //Log.e("invoke","method invoke successfully");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



}
