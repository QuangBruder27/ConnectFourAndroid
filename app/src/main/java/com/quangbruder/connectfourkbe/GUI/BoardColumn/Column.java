package com.quangbruder.connectfourkbe.GUI.BoardColumn;

import java.util.ArrayList;

public class Column {
    private int no;

    public Column(int number){
        no = number;
    }

    /**
     * @return a number of column
     */
    public int getNumber(){
        return this.no;
    }

    /**
     * create a  arraylist containing all of the columns.
     * @return a arraylist containing all of the columns.
     */
    public static ArrayList<Column> createBoardColumnList(){
        ArrayList<Column> result = new ArrayList<Column>();
        for (int i=0;i < 7; i++) {
            result.add(new Column(i));
        }
        return result;
    }
}
