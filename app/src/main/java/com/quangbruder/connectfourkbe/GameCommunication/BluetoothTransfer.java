package com.quangbruder.connectfourkbe.GameCommunication;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Handler;

import static com.quangbruder.connectfourkbe.GameCommunication.Helper.STATE_MSG_RECEIVED;

public class BluetoothTransfer extends Thread  {
    private final OutputStream outputStream;
    private final InputStream inputStream;
    private final BluetoothSocket bluetoothSocket;

    Handler handler;

    public BluetoothTransfer(BluetoothSocket btsocket, Handler mHandler) {
        handler = mHandler;
        bluetoothSocket = btsocket;
        InputStream tempInputStream = null;
        OutputStream tempOutputStream = null;
        try {
            tempOutputStream = bluetoothSocket.getOutputStream();
            tempInputStream = bluetoothSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inputStream = tempInputStream;
        outputStream = tempOutputStream;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (true) {
            try {
                bytes = inputStream.read(buffer);
                handler.obtainMessage(STATE_MSG_RECEIVED, bytes, -1, buffer).sendToTarget();
                byte[] readBuffer = (byte[]) buffer;
                String tempMsg = new String(readBuffer, 0, bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            System.out.println("Write function: "+bytes.toString());
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}