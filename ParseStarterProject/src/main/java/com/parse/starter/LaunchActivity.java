package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.Main.Cards;


public class LaunchActivity extends AppCompatActivity
{
    private int SPLASH_TIME = 5000;
    UserSession session;
    ParseUser currentuser = ParseUser.getCurrentUser();
    ParseObject flingcard = new ParseObject("Card");
    ParseFile img_file;
    Bitmap parcelbitmap;
    Bundle args = new Bundle();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_splash);
        session = new UserSession(this);

        prepare_model();

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

                        startActivity(new Intent(LaunchActivity.this, com.parse.starter.Main.MainActivity.class).putExtra("finalbundle",args));
                    }


                }
            }

            };
        timer.start();

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isUserLoggedIn(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "User Login Details: " + session.getUserDetails(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "User Session token: " + ParseUser.getCurrentUser().getSessionToken(), Toast.LENGTH_LONG).show();


    }

    private void prepare_model() {

        if(currentuser!= null) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Card");
            query.fromLocalDatastore();
            query.whereEqualTo("userobject_id_fk", currentuser.getObjectId());
            try {
                if (query.getFirst().isDataAvailable()) {
                    flingcard = query.getFirst();
                    flingcard.pinInBackground();
                    if (flingcard.get("profile_picture") != null) {
                        img_file = (ParseFile) flingcard.get("profile_picture");
                        img_file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null && data != null) {
                                    parcelbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    args.putParcelable("argus",parcelbitmap);
                                } else {
                                    Log.i("@@Error-msg", e.getMessage());
                                }
                            }
                        });
                    } else {
                        //do nothing
                    }

                } else {
                    Log.i("@@Pic update_status:", "no_user");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
