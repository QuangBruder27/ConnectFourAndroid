package com.quangbruder.connectfourkbe.GameCommunication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import com.quangbruder.connectfourkbe.GUI.MainActivity;

import java.io.IOException;

import static com.quangbruder.connectfourkbe.GameCommunication.Helper.*;

public class Server extends Thread {

    BluetoothAdapter bluetoothAdapter;
    private BluetoothServerSocket serverSocket;

    BluetoothTransfer sendReceive;
    Handler handler;
    MainActivity btActivity;

    public Server(BluetoothAdapter btAdapter, Handler mHandler, MainActivity activity){
        btActivity = activity;
        handler = mHandler;
        bluetoothAdapter = btAdapter;
        try {
            serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME, MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        bluetoothAdapter.cancelDiscovery();
        BluetoothSocket socket = null;
        while (socket == null) {
            try {
                Message message = Message.obtain();
                message.what = STATE_LISTENING;
                handler.sendMessage(message);
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                System.out.println("Connection failed by Server");
                handler.sendMessage(message);
            }

            if (socket != null) {
                Message message = Message.obtain();
                message.what = STATE_CONNECTED_SERVER;
                handler.sendMessage(message);
                // write code for send and receive
                sendReceive = new BluetoothTransfer(socket, handler);
                sendReceive.start();
                System.out.println("Conected. Server---------------------------------");
                btActivity.changeActivity(SERVER_SIDE, sendReceive);

                break;
            }
        }
    }
}