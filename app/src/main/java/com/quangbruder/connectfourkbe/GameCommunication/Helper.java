package com.quangbruder.connectfourkbe.GameCommunication;

import android.bluetooth.BluetoothDevice;
import android.widget.TextView;

import java.util.UUID;

public class Helper {


    public static final String APP_NAME = "BTChat";
    public static final UUID MY_UUID = UUID.fromString("03fc21a4-27cb-11eb-adc1-0242ac120002");

    public static final int STATE_DEFAULT = 0;
    public static final int STATE_LISTENING = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED_SERVER = 3;
    public static final int STATE_CONNECTED_CLIENT = 4;
    public static final int STATE_CONNECTION_FAILED = 5;
    public static final int STATE_MSG_RECEIVED = 6;

    public static final int SERVER_SIDE = 1;
    public static final int CLIENT_SIDE = 2;

    public static BluetoothTransfer bluetoothTransfer;

    public static final String REPLAY_REQUEST = "REPLAY";
    public static final String REPLAY_AGREE = "REPLAY_AGREE";
    public static final String REPLAY_DISAGREE = "REPLAY_DISAGREE";
    public static String REPLAY_REQUEST_STATUS = "";

    public static void addMessage(String str, TextView tv){
        String text = (String) tv.getText();
        tv.setText(text+str);
    }

    public static String printDevice(BluetoothDevice device){
        if (null == device.getName() || device.getName().isEmpty()){
            return device.getAddress();
        }
        return device.getName();
    }



}
