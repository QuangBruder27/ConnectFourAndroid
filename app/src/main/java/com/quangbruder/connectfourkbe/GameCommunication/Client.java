package com.quangbruder.connectfourkbe.GameCommunication;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import com.quangbruder.connectfourkbe.GUI.MainActivity;

import java.io.IOException;

import static com.quangbruder.connectfourkbe.GameCommunication.Helper.MY_UUID;
import static com.quangbruder.connectfourkbe.GameCommunication.Helper.STATE_CONNECTED_CLIENT;
import static com.quangbruder.connectfourkbe.GameCommunication.Helper.STATE_CONNECTION_FAILED;


public class Client extends Thread {
    private BluetoothDevice device;
    private BluetoothSocket socket;

    Handler handler;
    BluetoothTransfer sendReceive;
    MainActivity btActivity;

    public Client(BluetoothDevice btdevice, Handler mHandler, MainActivity activity) {
        btActivity = activity;
        handler = mHandler;
        device = btdevice;
        try {
            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            socket.connect();
            Message msg = Message.obtain();
            msg.what = STATE_CONNECTED_CLIENT;
            msg.obj = sendReceive;

            //-----Start BT Transfer -----------
            sendReceive = new BluetoothTransfer(socket, handler);
            sendReceive.start();
            Helper.bluetoothTransfer = sendReceive;
            handler.sendMessage(msg);

        } catch (IOException e) {
            e.printStackTrace();
            Message msg = Message.obtain();
            msg.what = STATE_CONNECTION_FAILED;
            System.out.println("Connection failed by Client");
            handler.sendMessage(msg);
        }
    }

}