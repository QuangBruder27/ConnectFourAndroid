package com.quangbruder.connectfourkbe.GUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quangbruder.connectfourkbe.GameController;
import com.quangbruder.connectfourkbe.R;

import static com.quangbruder.connectfourkbe.GameCommunication.Helper.*;

public class GameActivity extends AppCompatActivity {

    public static int device_role;


    public static RecyclerView recyclerView;
    public ImageView imgView_player;
    public TextView tv_player, tv_nameOfPlayer;
    public static Button btn_replay;
    public GameController gameController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        findViewByID();

        REPLAY_REQUEST_STATUS = "REPLAY";
        btn_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothTransfer.write(REPLAY_REQUEST.getBytes());

                Thread t1 = new Thread(new Runnable() {
                    public void run()
                    {
                        while (true){
                            if (REPLAY_REQUEST_STATUS.equals(REPLAY_AGREE)){
                                restartActivity(); break;
                            }
                            if (REPLAY_REQUEST_STATUS.equals(REPLAY_DISAGREE)){
                                break;
                            }
                        }
                    }});
                t1.start();
            }
        });
        createReplayDialog();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            device_role = extras.getInt("type");
        }

        gameController = new GameController(this, device_role);

        gameController.createUser();
        gameController.setUIforPlayer();

        // Lookup the recyclerview in activity layout
        recyclerView = (RecyclerView) findViewById(R.id.rvBoardColumn);
        gameController.setRecyclerView();

    }

    public static AlertDialog replayAlert;
    public void createReplayDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to play again");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        bluetoothTransfer.write(REPLAY_AGREE.getBytes());
                        restartActivity();
                    }
                });
        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        bluetoothTransfer.write(REPLAY_DISAGREE.getBytes());
                        dialog.cancel();
                    }
                });
        replayAlert = builder.create();
        //alert11.show();
    }

    public void restartActivity(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    void findViewByID(){
        imgView_player = (ImageView) findViewById(R.id.imgView_player);
        tv_player = (TextView) findViewById(R.id.tv_player);
        tv_nameOfPlayer = (TextView)findViewById(R.id.tv_nameOfPlayer);
        btn_replay = (Button) findViewById(R.id.btn_replay);
        //btn_replay.setVisibility(View.VISIBLE);
    }

}