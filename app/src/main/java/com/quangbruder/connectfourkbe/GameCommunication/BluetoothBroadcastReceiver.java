package com.quangbruder.connectfourkbe.GameCommunication;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.quangbruder.connectfourkbe.GUI.MainActivity;

import static com.quangbruder.connectfourkbe.GameCommunication.Helper.printDevice;

public class BluetoothBroadcastReceiver  extends BroadcastReceiver {

    MainActivity activity;

    public BluetoothBroadcastReceiver(MainActivity mainActivity){
        activity = mainActivity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)){
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (!activity.listOfDevice.contains(device)) {
                activity.stringArrayList.add(printDevice(device));
                System.out.println("Add device: " + printDevice(device));
                activity.arrayAdapter.notifyDataSetChanged();
                activity.listOfDevice.add(device);
            }
        } else {
            System.out.println("Nothing!!!");
        }
    }
}
