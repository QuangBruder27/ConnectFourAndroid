package com.quangbruder.connectfourkbe.UserData;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserImpl implements User {

    public UserImpl(){
    }

    SharedPreferences preferencesName = null;
    SharedPreferences preferencesWon = null;
    SharedPreferences preferencesLost = null;

    @Override
    public String getName(Context context) {
        if (preferencesName == null){
            preferencesName =  PreferenceManager.getDefaultSharedPreferences(context);
        }
        String name = preferencesName.getString("NameofUser", "");
        if(!name.equals("")){
            return name;
        }
        return "Noname";
    }

    @Override
    public void setName(Context context, String newName) {
        if (preferencesName == null){
                preferencesName =  PreferenceManager.getDefaultSharedPreferences(context);
            }
        preferencesName.edit().putString("NameofUser", newName).apply();
    }

    @Override
    public int getWon(Context context) {
        if (preferencesWon == null){
            preferencesWon =  PreferenceManager.getDefaultSharedPreferences(context);
        }
        int result = preferencesWon.getInt("Won", 0);
        return result;
    }

    @Override
    public void addWon(Context context) {
        if (preferencesWon == null){
            preferencesWon =  PreferenceManager.getDefaultSharedPreferences(context);
        }
        preferencesWon.edit().putInt("Won", getWon(context)+1).apply();
    }

    @Override
    public int getLost(Context context) {
        if (preferencesLost == null){
            preferencesLost =  PreferenceManager.getDefaultSharedPreferences(context);
        }
        int result = preferencesLost.getInt("Lost", 0);
        return result;
    }

    @Override
    public void addLost(Context context) {
        if (preferencesLost == null){
            preferencesLost =  PreferenceManager.getDefaultSharedPreferences(context);
        }
        preferencesLost.edit().putInt("Lost", getLost(context)+1).apply();
    }
}
