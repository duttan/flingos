package com.parse.starter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public class LaunchActivity extends AppCompatActivity
{
    private int SPLASH_TIME = 1000;
    UserSession session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_splash);
        session = new UserSession(this);


        Thread timer = new Thread()
        {
            public void run()
            {
                try {
                    sleep(SPLASH_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    Boolean check = session.checkLogin();

                    if(!check)
                    {
                        startActivity(new Intent(LaunchActivity.this,MainActivity.class));
                    }


                }
            }

            };
        timer.start();

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isUserLoggedIn(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.getUserDetails(), Toast.LENGTH_LONG).show();










    }
}
