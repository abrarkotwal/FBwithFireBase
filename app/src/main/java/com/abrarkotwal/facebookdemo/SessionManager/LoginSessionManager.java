package com.abrarkotwal.facebookdemo.SessionManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.abrarkotwal.facebookdemo.Activity.LoginActivity;

import java.util.HashMap;


public class LoginSessionManager {
    SharedPreferences pref;
    Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME       = "UserLogin";
    private static final String IS_LOGIN        = "IsLoggedIn";
    public static final String KEY_EMAIL        = "email";


    // Constructor
    public LoginSessionManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String email){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public void checkUserLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }


    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_EMAIL,       pref.getString(KEY_EMAIL, ""));
        return user;
    }

    public void userLogout(){
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}