package com.parse.starter.Utilss;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.Main.Cards;
import com.parse.starter.Main.MainActivity;
import com.parse.starter.Matched.Matched_Activity;
import com.parse.starter.Profile.Profile_Activity;
import com.parse.starter.R;

import java.util.ArrayList;
import java.util.List;


public class TopNavigationViewHelper {

    private static final String TAG = "TopNavigationViewHelper";

    public static void setupTopNavigationView(BottomNavigationViewEx tv) {
        Log.d(TAG, "setupTopNavigationView: setting up navigationview");


    }



    public static void enableNavigation(final Context context, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_profile:
                        Intent intent2 = new Intent(context, Profile_Activity.class);
                        context.startActivity(intent2);
                        break;

                    case R.id.ic_main:
                        Intent intent1 = new Intent(context, MainActivity.class);
                        context.startActivity(intent1);
                        break;

                    case R.id.ic_matched:
                        Intent intent3 = new Intent(context, Matched_Activity.class);
                        context.startActivity(intent3);

                        break;
                }

                return false;
            }
        });
    }
}
