package com.aman.electroworks.constants;

import android.content.Context;
import android.content.SharedPreferences;

import java.security.Key;

public class SessionManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences("ElecroWorks",0);
        editor =sharedPreferences.edit();
        editor.apply();
    }


    String KEY_IS_LOGIN="is_logged_in";



    public void createUser(){
        editor.putBoolean(KEY_IS_LOGIN,true);
        editor.commit();
    }
    public void removeUser(){
        editor.putBoolean(KEY_IS_LOGIN,false);
        editor.commit();
    }
    public Boolean isLoggedIn(){
        return sharedPreferences.getBoolean(KEY_IS_LOGIN,false);
    }
}
