package com.parse.starter;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.parse.ParseUser;


public class LaunchActivity extends AppCompatActivity
{
    private int SPLASH_TIME = 5000;
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
                        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                    }
                    else
                    {
                        startActivity(new Intent(LaunchActivity.this, com.parse.starter.Main.MainActivity.class));
                    }


                }
            }

            };
        timer.start();

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isUserLoggedIn(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "User Login Details: " + session.getUserDetails(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "User Session token: " + ParseUser.getCurrentUser().getSessionToken(), Toast.LENGTH_LONG).show();










    }
}
