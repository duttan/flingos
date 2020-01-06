package com.parse.starter.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.R;
import com.parse.starter.Utilss.PulsatorLayout;
import com.parse.starter.Utilss.TopNavigationViewHelper;

public class Profile_Activity extends AppCompatActivity {
    private static final String TAG = "Profile_Activity";
    private static final int ACTIVITY_NUM = 0;
    static boolean active = false;

    private Context mContext = Profile_Activity.this;
    private ImageView imagePerson;
    private TextView name;

    private String userId;

    ParseUser currentuser = ParseUser.getCurrentUser();
    ParseObject flingcard = new ParseObject("Card");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: create the page");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        PulsatorLayout mPulsator = findViewById(R.id.pulsator);
        mPulsator.start();

        setupTopNavigationView();

        imagePerson = findViewById(R.id.circle_profile_image);
        name = findViewById(R.id.profile_name);

        ParseUser user = ParseUser.getCurrentUser();
        name.setText(user.getUsername());
        ImageButton edit_btn = findViewById(R.id.edit_profile);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        ImageButton settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile_Activity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: resume to the page");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Card");
        query.whereEqualTo("userobject_id_fk", currentuser.getObjectId());
        try {
            if(query.getFirst().isDataAvailable()) {
                flingcard = query.getFirst();
                ParseFile img_file = (ParseFile) flingcard.get("profile_picture");
                img_file.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        if(e == null && data!= null)
                        {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                            imagePerson.setImageBitmap(bitmap);
                        }
                    }
                });

            }
            else
            {
                Log.i("@@Pic update_status:","no_user");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    private void setupTopNavigationView() {
        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }



}
