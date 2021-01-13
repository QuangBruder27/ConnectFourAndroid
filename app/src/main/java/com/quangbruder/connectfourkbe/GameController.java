package com.quangbruder.connectfourkbe;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.quangbruder.connectfourkbe.GUI.BoardColumn.BoardColumnsAdapter;
import com.quangbruder.connectfourkbe.GUI.GameActivity;
import com.quangbruder.connectfourkbe.UserData.User;
import com.quangbruder.connectfourkbe.UserData.UserImpl;


import java.io.IOException;

import GameData.Board;
import GameData.BoardImpl;
import GameLogic.Logic;
import GameLogic.LogicImpl;

import static com.quangbruder.connectfourkbe.GameCommunication.Helper.CLIENT_SIDE;
import static com.quangbruder.connectfourkbe.GameCommunication.Helper.SERVER_SIDE;
import static com.quangbruder.connectfourkbe.GameCommunication.Helper.bluetoothTransfer;

public class GameController {

    static GameActivity activity;
    public static Board gameboard;
    public static Logic gameLogic = new LogicImpl();
    public static Board.Player device_player;
    public int device_role;

    public GameController(GameActivity gameActivity, int deviceRole){
        activity = gameActivity;
        device_role = deviceRole;
        // Create board game
        gameboard = new BoardImpl();
        gameboard.cleanBoard();
    }

    /**
     * movement of the opponent
     * @param position
     * @throws IOException
     */
    public static void replyMove(int position) throws IOException {
        boardClick(gameboard, Board.Player.NONE,
                (BoardColumnsAdapter.ViewHolder) activity.recyclerView.findViewHolderForAdapterPosition(position),position);
    }

    /**
     * click event handler
     * @param gameboard
     * @param player
     * @param holder
     * @param col_nr
     * @throws IOException
     */
    public static void boardClick(Board gameboard, Board.Player player, BoardColumnsAdapter.ViewHolder holder, int col_nr) throws IOException {
        if (gameLogic.isMovable(col_nr, gameboard)  && !gameLogic.isFinished(gameboard)){
            int row = gameLogic.makeMove(col_nr, gameboard);
            System.out.println("BoardClick----------------------------------");
            if (device_player == player)
                System.out.println("BoardClick: OK");
                bluetoothTransfer.write((""+col_nr).getBytes());
            drawCircle(gameboard.getCurrentPlayer(), holder ,row);
            statusNoti(gameboard, holder);
        } else {
            System.out.println("BoardClick: Not movable");
            Toast.makeText(holder.itemView.getContext(), "Not movable", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * draw the new circle
     * @param player
     * @param holder
     * @param row
     */
    public static void drawCircle(Board.Player player, BoardColumnsAdapter.ViewHolder holder, int row){
        if(player == Board.Player.ONE) {
            holder.imgViewList[row].setImageResource(R.drawable.yellow);
        } else {
            holder.imgViewList[row].setImageResource(R.drawable.red);
        }
    }

    /**
     * notify user, if the game finished
     * @param gameboard
     * @param holder
     */
    public static void statusNoti(Board gameboard, BoardColumnsAdapter.ViewHolder holder){
        User resultUser = new UserImpl();
        Context context = holder.itemView.getContext();
        if(gameLogic.isVictory(gameboard)){
            if (device_player != gameboard.getCurrentPlayer()) {
                Toast.makeText(context, "You are Victory", Toast.LENGTH_SHORT).show();
                resultUser.addWon(context);
            } else {
                Toast.makeText(context, "You are lost", Toast.LENGTH_SHORT).show();
                resultUser.addLost(context);
            }
            Toast.makeText(holder.itemView.getContext(), "Won: "+resultUser.getWon(context)+"\nLost: "
                    +resultUser.getLost(context), Toast.LENGTH_LONG).show();
            activity.btn_replay.setVisibility(View.VISIBLE);
        }
        if (gameLogic.isDrawn(gameboard)){
            Toast.makeText(context, "Is Drawn", Toast.LENGTH_SHORT).show();
            activity.btn_replay.setVisibility(View.VISIBLE);
        }
    }

    /**
     * create user object to get the name of the player
     */
    public void createUser(){
        // Create User
        User user  = new UserImpl();
        activity.tv_nameOfPlayer.setText("Hey "+user.getName(activity));
    }

    /**
     * set the  graphical user interface for the player
     */
    public void setUIforPlayer(){
        if(device_role == SERVER_SIDE){
            System.out.println("Start server");
            //Toast.makeText(activity, "Start server", Toast.LENGTH_SHORT).show();
            device_player = Board.Player.ONE;
            activity.imgView_player.setImageResource(R.drawable.red);
            activity.tv_player.setText("You are server");
        }
        if(device_role == CLIENT_SIDE){
            System.out.println("Start client");
            //Toast.makeText(activity, "Start Client", Toast.LENGTH_SHORT).show();
            device_player = Board.Player.TWO;
            activity.imgView_player.setImageResource(R.drawable.yellow);
            activity.tv_player.setText("You are client");
        }
    }

    /**
     * set the RecyclerView for the game board
     */
    public void setRecyclerView(){
        activity.recyclerView.setNestedScrollingEnabled(false);
        // Create adapter passing in the sample user data
        BoardColumnsAdapter adapter = new BoardColumnsAdapter(this);
        // Attach the adapter to the recyclerview to populate items
        activity.recyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        activity.recyclerView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false));

    }



}
