package com.quangbruder.connectfourkbe.GUI.BoardColumn;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quangbruder.connectfourkbe.GameController;
import com.quangbruder.connectfourkbe.R;

import java.io.IOException;
import java.util.List;

import static com.quangbruder.connectfourkbe.GUI.BoardColumn.Column.createBoardColumnList;
import static com.quangbruder.connectfourkbe.GameController.device_player;
import static com.quangbruder.connectfourkbe.GameController.gameboard;

public class BoardColumnsAdapter extends RecyclerView.Adapter<BoardColumnsAdapter.ViewHolder> {

    List<Column> columnList = createBoardColumnList();
    GameController gameController;
    public BoardColumnsAdapter(GameController controller){
        gameController = controller;
    }

    /**
     *  a static class to hold the recycler view
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgView;
        public ImageView imgView1;
        public ImageView imgView2;
        public ImageView imgView3;
        public ImageView imgView4;
        public ImageView imgView5;
        public ImageView imgView6;
        public ImageView imgView7;
        public ImageView[] imgViewList;

        public ViewHolder(View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imgView);
            imgView1 = itemView.findViewById(R.id.imgView1);
            imgView2 = itemView.findViewById(R.id.imgView2);
            imgView3 = itemView.findViewById(R.id.imgView3);
            imgView4 = itemView.findViewById(R.id.imgView4);
            imgView5 = itemView.findViewById(R.id.imgView5);
            imgViewList = new ImageView[]{imgView5,imgView4 ,imgView3,imgView2,imgView1,imgView};
        }
    }


    /**
     * create a view holder.
     * @param parent
     * @param viewType
     * @return a view holder.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_boardcolumn, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        //--------------- Size for imageView
        int displayWidth = context.getResources().getDisplayMetrics().widthPixels;

        for (int i=0; i< 6;i++){
            viewHolder.imgViewList[i].setLayoutParams(new LinearLayout.LayoutParams(displayWidth/7,
                    displayWidth/7));
        }

        return viewHolder;
    }

    /**
     * set OnClick-Event for the view
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Column column = columnList.get(position);
        for (int i=0; i< 6;i++){
            holder.imgViewList[i].setContentDescription("C"+column.getNumber());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    System.out.println("currentPlayer = " + gameboard.getCurrentPlayer());
                    if(gameboard.getCurrentPlayer() == device_player)
                        gameController.boardClick(gameboard, device_player, holder, position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("holder.itemview setted: "+holder.itemView.toString());
    }

    /**
     * @return length of the list of object.
     */
    @Override
    public int getItemCount() {
        return columnList.size();
    }


}
