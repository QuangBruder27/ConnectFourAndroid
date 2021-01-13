package com.quangbruder.connectfourkbe.GameCommunication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ArrayAdapter;

import com.quangbruder.connectfourkbe.GUI.MainActivity;

import java.lang.reflect.Method;

public interface BluetoothConnector {

    /**
     *
     * @return true if bluetooth is enabled
     */
    boolean isEnabled();

    /**
     * request user to turn on bluetooth
     * @param activity
     */
     void requestEnableBluetooth(Activity activity);

    /**
     * set the bluetooth broadcast receiver
     * @param activity
     */
     void setBTReceiver(MainActivity activity);

    /**
     * wait for the connection invitaion
     * @param activity
     */
     void waiting(MainActivity activity);

    /**
     * scan available bluetooth devices
     */
    void scanning();

    /**
     * accept the connection invitation
     * @param activity
     * @param position
     */
     void acceptDevice(MainActivity activity, int position);

    /**
     * make the device as discoverable
     */
    void makeDiscoverable();
}
