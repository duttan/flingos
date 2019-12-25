package com.parse.starter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.parse.ParseUser;

import java.util.HashMap;

public class UserSession {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    public static final String PREFER_NAME = "Login_ref";

    // All Shared Preferences Keys
    public static final String IS_USER_LOGIN = "IsUserLoggedIn";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "Email";

    // password
    public static final String KEY_PASSWORD = "Password";

    //session token
    public static final String USER_SESSION_TOKEN = "SessionToken";

    public UserSession(Context context)
    {
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();

    }
    public void createUserLoginSession(String uEmail, String uPassword){

        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_EMAIL, uEmail);
        editor.putString(KEY_PASSWORD, uPassword);
        editor.putString(USER_SESSION_TOKEN, ParseUser.getCurrentUser().getSessionToken());
        editor.commit();
    }

    public boolean checkLogin(){
        // Check login status
        if(this.isUserLoggedIn() && this.Check_sessiontoken()){

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, com.parse.starter.Main.MainActivity.class);

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);

            return true;
            }
        return false;
    }


    public HashMap<String, String> getUserDetails(){

        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user name
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // user name
        user.put(USER_SESSION_TOKEN, pref.getString(USER_SESSION_TOKEN, null));

        // return user
        return user;
    }

    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to MainActivity
        Intent i = new Intent(context, MainActivity.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }



    public boolean isUserLoggedIn(){
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public boolean Check_sessiontoken(){
        if(pref.getString(USER_SESSION_TOKEN,"0").equals(ParseUser.getCurrentUser().getSessionToken()))
        {
            return true;
        }

        return false;
    }
}
