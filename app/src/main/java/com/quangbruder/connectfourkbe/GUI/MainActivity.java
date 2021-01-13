package com.quangbruder.connectfourkbe.GUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.quangbruder.connectfourkbe.GameCommunication.BluetoothConnector;
import com.quangbruder.connectfourkbe.GameCommunication.BluetoothConnectorImpl;
import com.quangbruder.connectfourkbe.GameCommunication.Helper;
import com.quangbruder.connectfourkbe.GameCommunication.BluetoothTransfer;
import com.quangbruder.connectfourkbe.UserData.User;
import com.quangbruder.connectfourkbe.UserData.UserImpl;
import com.quangbruder.connectfourkbe.R;

import java.io.IOException;
import java.util.ArrayList;

import static com.quangbruder.connectfourkbe.GameCommunication.Helper.CLIENT_SIDE;
import static com.quangbruder.connectfourkbe.GameCommunication.Helper.REPLAY_AGREE;
import static com.quangbruder.connectfourkbe.GameCommunication.Helper.REPLAY_DISAGREE;
import static com.quangbruder.connectfourkbe.GameCommunication.Helper.REPLAY_REQUEST;
import static com.quangbruder.connectfourkbe.GameCommunication.Helper.REPLAY_REQUEST_STATUS;
import static com.quangbruder.connectfourkbe.GameCommunication.Helper.STATE_CONNECTED_CLIENT;
import static com.quangbruder.connectfourkbe.GameCommunication.Helper.STATE_CONNECTED_SERVER;
import static com.quangbruder.connectfourkbe.GameCommunication.Helper.STATE_CONNECTING;
import static com.quangbruder.connectfourkbe.GameCommunication.Helper.STATE_CONNECTION_FAILED;
import static com.quangbruder.connectfourkbe.GameCommunication.Helper.STATE_DEFAULT;
import static com.quangbruder.connectfourkbe.GameCommunication.Helper.STATE_LISTENING;
import static com.quangbruder.connectfourkbe.GameCommunication.Helper.STATE_MSG_RECEIVED;
import static com.quangbruder.connectfourkbe.GameCommunication.Helper.printDevice;
import static com.quangbruder.connectfourkbe.GUI.GameActivity.replayAlert;
import static com.quangbruder.connectfourkbe.GameController.replyMove;

public class MainActivity extends AppCompatActivity {

    Button btnListen, btnScan;
    TextView  tv_stt;
    public ListView listViewOfDevices;

    public ArrayList<BluetoothDevice> listOfDevice = new ArrayList<BluetoothDevice>();
    public ArrayList<String> stringArrayList = new ArrayList<String>();
    public ArrayAdapter<String> arrayAdapter;
    BluetoothConnector btConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        // Create User
        User user = new UserImpl();

        //----------- GET Name of User --------------------------------
        if (user.getName(this).equals("Noname")) {

            final View view = getLayoutInflater().inflate(R.layout.dialog_name, null);
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("What is your name?");
            alertDialog.setCancelable(false);
            final EditText editText = (EditText) view.findViewById(R.id.editText_name);

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String answer = editText.getText().toString();
                    if (!answer.isEmpty()) {
                        user.setName(MainActivity.this, answer);
                    }
                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.setView(view);
            alertDialog.show();
        }

        //---------------------------------------------------------------
        // Find view elements by id
        findViewById();
        // Request Location permissions
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
        implementListener();
        btConnector= new BluetoothConnectorImpl();

        btConnector.setBTReceiver(this);
        // Show found devices in Listview
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringArrayList);
        listViewOfDevices.setAdapter(arrayAdapter);
    }

    public void changeActivity(int device_role, BluetoothTransfer msendAndReceive){
        System.out.println("Change activity function");
        Intent myIntent = new Intent( this, GameActivity.class);
        Helper.bluetoothTransfer = msendAndReceive;
        myIntent.putExtra("type",device_role);
        this.startActivity(myIntent);
    }

    public void changeActivityForClient(int device_role){
        System.out.println("Change activity function");
        Intent myIntent = new Intent( this, GameActivity.class);
        //Helper.sendAndReceive = msendAndReceive;
        myIntent.putExtra("type",device_role);
        this.startActivity(myIntent);
    }

    private void implementListener() {
        btnListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start to wait request
               btConnector.waiting(MainActivity.this);
                tv_stt.setText("Connecting: Listening");
                Toast.makeText(MainActivity.this, "Listening", Toast.LENGTH_SHORT).show();
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btConnector.isEnabled()){
                    btConnector.requestEnableBluetooth(MainActivity.this);
                }
                btConnector.scanning();
                tv_stt.setText("Connecting: Scanning");
            }
        });

        listViewOfDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btConnector.acceptDevice(MainActivity.this, position);
                Toast.makeText(MainActivity.this, "You choose:"+ printDevice(listOfDevice.get(position)), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void findViewById() {
        listViewOfDevices = findViewById(R.id.list_devices);
        btnScan = findViewById(R.id.btn_scan);
        btnListen = (Button) findViewById(R.id.btn_listen);
        tv_stt = (TextView) findViewById(R.id.tv_status);
    }

    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case STATE_DEFAULT:
                    tv_stt.setText("");
                case STATE_LISTENING:
                    tv_stt.setText("Connecting: Listening");
                    break;
                case STATE_CONNECTING:
                    tv_stt.setText("Connecting");
                    break;
                case STATE_CONNECTED_SERVER:
                    tv_stt.setText("Connected - Server");
                    break;
                case STATE_CONNECTED_CLIENT:
                    //SendAndReceive sendReceive = (SendAndReceive) msg.obj;
                    changeActivityForClient(CLIENT_SIDE);
                    tv_stt.setText("Connected - Client");
                    break;
                case STATE_CONNECTION_FAILED:
                    tv_stt.setText("Connection failed");
                    break;
                case STATE_MSG_RECEIVED:
                    byte[] readBuffer = (byte[]) msg.obj;
                    String tempMsg = new String(readBuffer, 0, msg.arg1);
                    //tv_message.setText(tempMsg);

                    switch (tempMsg){
                        case REPLAY_REQUEST: replayAlert.show();
                            System.out.println("Send request to play again!!!"); break;
                        case REPLAY_AGREE:
                            System.out.println("Replay request is agreed");
                                REPLAY_REQUEST_STATUS = REPLAY_AGREE;break;

                        case REPLAY_DISAGREE: REPLAY_REQUEST_STATUS = REPLAY_DISAGREE;break;
                        default:
                            try {
                                System.out.println("Reply move");
                            replyMove(Integer.valueOf(tempMsg));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                    break;
            }
            return false;
        }
    });






}