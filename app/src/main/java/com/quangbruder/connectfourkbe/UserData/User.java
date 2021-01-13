package com.quangbruder.connectfourkbe.UserData;

import android.content.Context;
import android.content.SharedPreferences;

public interface User {

    /**
     * @return the name of the user.
     */
    String getName(Context context);
    /**
     * set the name for a user.
     * @param name
     */
    void setName(Context context, String name);

    /**
     * @return the number of victories.
     */
    int getWon(Context context);

    /**
     * plus one win
     * @param context
     */
    void addWon(Context context);

    /**
     * @return the number of times lost
     */
    int getLost(Context context);
    
    /**
     * plus one loss.
     * @param context
     */
    void addLost(Context context);
}