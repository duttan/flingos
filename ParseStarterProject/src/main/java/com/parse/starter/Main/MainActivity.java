package com.parse.starter.Main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.BaseActivity;
import com.parse.starter.LaunchActivity;
import com.parse.starter.R;
import com.parse.starter.Utilss.GPS;
import com.parse.starter.Utilss.PulsatorLayout;
import com.parse.starter.Utilss.TopNavigationViewHelper;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private static final int ACTIVITY_NUM = 1;
    final private int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    ListView listView;
    List<Cards> rowItems = new ArrayList<Cards>();
    List<Cards> temp_items = new ArrayList<Cards>();
    List<Cards> final_stack = new ArrayList<Cards>();
    List<String> likes = new ArrayList<String>();
    List<String> dislikes = new ArrayList<String>();
    FrameLayout cardFrame, moreFrame;
    CircleImageView dpimageView;
    private Context mContext = MainActivity.this;
    private NotificationHelper mNotificationHelper;
    private Cards cards_data[];
    private PhotoAdapter arrayAdapter;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private GPS gps;
    private Location current_loc;
    ParseObject match;
    private ParseGeoPoint currentUserLocationParse;
    private ParseUser currentUser = ParseUser.getCurrentUser();
    private List<String> potential_matches = new ArrayList<String>();
    Bitmap bt = null;
    LocationManager mLocationManager;
    String mProvider = LocationManager.GPS_PROVIDER;
    LocationListener mLocationListener;
    SwipeFlingAdapterView flingContainer;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_flingos);
        Bundle bu = getIntent().getBundleExtra("finalbundle");

        cardFrame = findViewById(R.id.card_frame);
        moreFrame = findViewById(R.id.more_frame);
        dpimageView = findViewById(R.id.post);

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        // start pulsator
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                current_loc = location;
                updateLocation();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
        else {
            final_stack = updateDeck();
             current_loc = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (current_loc == null) {
                mLocationManager.requestLocationUpdates(mProvider, 0, 0, mLocationListener);

            }
        }


        //current_loc = gps.getLocation();
        PulsatorLayout mPulsator = findViewById(R.id.pulsator);
        mPulsator.start();



        if(bu!=null)
        {
            bt = bu.getParcelable("finalbundle");
            dpimageView.setImageBitmap(bt);
        }
        else {
            dpimageView.setImageResource(R.drawable.circle_background);
        }



        arrayAdapter = new PhotoAdapter(mContext, R.layout.item, rowItems);
        arrayAdapter.notifyDataSetChanged();
//        Thread timer = new Thread()
//        {
//            public void run()
//            {
//                try {
//                    sleep(5000);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//
//                    arrayAdapter = new PhotoAdapter(mContext, R.layout.item, rowItems);
//                    arrayAdapter.notifyDataSetChanged();
//                }
//            }
//
//        };
//        timer.start();

        mNotificationHelper = new NotificationHelper(this);


        setupTopNavigationView();

        checkRowItem();



        //https://im.idiva.com/author/2018/Jul/shivani_chhabra-_author_s_profile.jpg


//        Cards cards = new Cards("1", "Tom Chris", "21", "https://coverfiles.alphacoders.com/848/84877.jpg", "Simple and beautiful Girl", "Acting", 200);
//        final_stack.add((Cards) rowItems);
//        cards = new Cards("2", "Ananaya Panday", 20, "https://i0.wp.com/profilepicturesdp.com/wp-content/uploads/2018/06/beautiful-indian-girl-image-for-profile-picture-8.jpg", "cool Minded Girl", "Dancing", 800);
//        rowItems.add(cards);
//        cards = new Cards("3", "Anjali Kasyap", 22, "https://pbs.twimg.com/profile_images/967542394898952192/_M_eHegh_400x400.jpg", "Simple and beautiful Girl", "Singing", 400);
//        rowItems.add(cards);
//        cards = new Cards("4", "Preety Deshmukh", 19, "http://profilepicturesdp.com/wp-content/uploads/2018/07/fb-real-girls-dp-3.jpg", "dashing girl", "swiming", 1308);
//        rowItems.add(cards);
//        cards = new Cards("5", "Srutimayee Sen", 20, "https://dp.profilepics.in/profile_pictures/selfie-girls-profile-pics-dp/selfie-pics-dp-for-whatsapp-facebook-profile-25.jpg", "chulbuli nautankibaj ", "Drawing", 1200);
//        rowItems.add(cards);
//        cards = new Cards("6", "Dikshya Agarawal", 21, "https://pbs.twimg.com/profile_images/485824669732200448/Wy__CJwU.jpeg", "Simple and beautiful Girl", "Sleeping", 700);
//        rowItems.add(cards);
//        cards = new Cards("7", "Sudeshna Roy", 19, "https://talenthouse-res.cloudinary.com/image/upload/c_fill,f_auto,h_640,w_640/v1411380245/user-415406/submissions/hhb27pgtlp9akxjqlr5w.jpg", "Papa's Pari", "Art", 5000);
//        rowItems.add(cards);


    }

    public Location getLocation() {
        return current_loc;
    }

    private List<Cards> updateDeck() {


        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNear("currentloc",currentUser.getParseGeoPoint("currentloc") );

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if(e == null)
                {
                    for (ParseUser user: objects)
                    {
                        if(user.getObjectId() != currentUser.getObjectId()){
                        potential_matches.add(user.getObjectId());}

                    }
                    temp_items = prepare_stack(potential_matches);
                    final_stack = temp_items;

                }
                else {
                    Log.i("@@Error:",e.getMessage());
                }
            }
        });
            return temp_items;
    }


    private List<Cards> prepare_stack(List<String> matches) {

        if(matches.size() > 0)
        {
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Card");

            query.whereContainedIn("userobject_id_fk", matches);


            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e == null)
                    {
                        int i = 0;
                        for(ParseObject card: objects)
                        {

                            Cards newcard = new Cards(card.getObjectId()+"",card.get("cardname").toString(),card.get("age").toString(),card.getParseFile("profile_picture").getUrl(),card.get("bio").toString(),card.get("interest").toString(),50);
                            rowItems.add(newcard);
                            Log.i("@@card"+ card.getObjectId(),"added");
                            i++;
                        }

                    }
                    else
                    {
                        Log.i("@@Error:",e.getMessage());

                        rowItems.add(new Cards("1", "Tom Chris", "21", "https://coverfiles.alphacoders.com/848/84877.jpg", "Simple and beautiful Girl", "Acting", 200));
                    }

                    arrayAdapter = new PhotoAdapter(mContext, R.layout.item, rowItems);
                    arrayAdapter.notifyDataSetChanged();
                    checkRowItem();
                    updateSwipeCard();
                    final_stack = rowItems;


                }
            });


        }
        else
        {
         Log.i("@@","No one is available rightnow");
        }


        return rowItems;
    }

    private void card_init() {

        if(final_stack.size() > 0) {
            for (Cards card : final_stack) {
                match = new ParseObject("Match");
                match.put("card_id_fk", card.getUserId());
                match.saveInBackground();
            }
        }
    }

    private void checkRowItem() {
        if (rowItems.isEmpty()) {
            moreFrame.setVisibility(View.VISIBLE);
            cardFrame.setVisibility(View.GONE);
        }
        else
        {
            moreFrame.setVisibility(View.GONE);
            cardFrame.setVisibility(View.VISIBLE);
        }
    }

    private void updateLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION); }
        else {
            mLocationManager.requestLocationUpdates(mProvider, 0, 0, mLocationListener);
            current_loc = getLocation();
            if(current_loc!=null) {
                currentUserLocationParse = new ParseGeoPoint(current_loc.getLatitude(), current_loc.getLongitude());

                if (currentUser != null) {
                    currentUser.put("currentloc", currentUserLocationParse);
                    currentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.i("@@userloc", current_loc.toString());
                            } else {
                                Log.i("@@userloc", e.getMessage());
                            }

                        }
                    });
                } else {

                    Toast.makeText(mContext, "Please login to proceed", Toast.LENGTH_SHORT).show();
                }
            }

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mLocationManager.requestLocationUpdates(mProvider, 0, 0, mLocationListener);
                        //updateLocation();
                        final_stack = updateDeck();

                    } else {
                        Toast.makeText(MainActivity.this, "Location Permission Denied. You have to give permission inorder to know the user range ", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Location Permission Denied. You have to give permission inorder to know the user range ", Toast.LENGTH_SHORT).show();
                    cardFrame.setVisibility(View.GONE);
                }

            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void updateSwipeCard() {

        final SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("@@LIST", "removed object!");

            }

            @Override
            public void onLeftCardExit(Object dataObject)  {
                Cards obj = (Cards) dataObject;
                dislikedmatch(obj);
                final_stack.remove(0);
                arrayAdapter.notifyDataSetChanged();
                checkRowItem();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Cards obj = (Cards) dataObject;
                likedmatch(obj);

                //check matches
                final_stack.remove(0);
                arrayAdapter.notifyDataSetChanged();
                checkRowItem();

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here


            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        //Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void dislikedmatch(final Cards current_card)  {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");

        try {
        query.whereEqualTo("card_id_fk",current_card.getUserId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size() > 0 && e == null) {
                    match = objects.get(0);
                    if (match.getList("dislike") == null) {
                        dislikes.add(currentUser.getObjectId());
                        match.put("dislike", dislikes);
                    } else if(!dislikes.contains(currentUser.getObjectId()))
                    {
                        dislikes = match.getList("dislike");

                        if(!dislikes.contains(currentUser.getObjectId())){
                        dislikes.add(currentUser.getObjectId());
                        match.put("dislike", dislikes);}
                    }
                    else {}

                    save_eventually();
                }
                else {

                    match = new ParseObject("Match");
                    match.put("card_id_fk", current_card.getUserId());
                    dislikes.add(currentUser.getObjectId());
                    match.put("dislike", dislikes);
                    match.saveEventually();

                }

            }
        });
        }catch (Exception e)
        {
            Log.i("@@EXP",e.getMessage());
        }

    }

    private void save_eventually() {
        match.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null)
                {
                    Log.i("@@Dislike",dislikes.toString());
                }
                else {
                    Log.i("@@Error:Dislike",e.getMessage());
                }
            }
        });
    }

    private void likedmatch(final Cards current_card) {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");

        try {
            query.whereEqualTo("card_id_fk",current_card.getUserId());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(objects.size() > 0 && e == null) {
                        match = objects.get(0);
                        if (match.getList("like") == null) {
                            likes.add(currentUser.getObjectId());
                            match.put("like", likes);
                        } else if(!likes.contains(currentUser.getObjectId()))
                        {
                            likes = match.getList("like");
                            if(!likes.contains(currentUser.getObjectId())){
                            likes.add(currentUser.getObjectId());
                            match.put("like", likes);}
                        }
                        else {}

                        //save_eventually();
                        match.saveInBackground();
                    }
                    else {

                        match = new ParseObject("Match");
                        match.put("card_id_fk", current_card.getUserId());
                        likes.add(currentUser.getObjectId());
                        match.put("like", likes);
                        //match.saveEventually();
                        match.saveInBackground();

                    }

                }
            });
        }catch (Exception e)
        {
            Log.i("@@EXP",e.getMessage());
        }


    }


    public void sendNotification() {
        NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification(mContext.getString(R.string.app_name), mContext.getString(R.string.match_notification));
        mNotificationHelper.getManager().notify(1, nb.build());

    }


    public void DislikeBtn(View v) {
        if (rowItems.size() != 0) {
            Cards card_item = rowItems.get(0);

            String userId = card_item.getUserId();



            flingContainer.getTopCardListener().selectLeft();
          //  rowItems.remove(0);
            arrayAdapter.notifyDataSetChanged();
            dislikedmatch(card_item);


            Toast.makeText(MainActivity.this, "Dislike", Toast.LENGTH_SHORT).show();
           // Intent btnClick = new Intent(mContext, BtnDislikeActivity.class);
           // btnClick.putExtra("url", card_item.getProfileImageUrl());
           // startActivity(btnClick);
        }
    }

    public void LikeBtn(View v) {
        if (rowItems.size() != 0) {
            Cards card_item = rowItems.get(0);

            String userId = card_item.getUserId();

            //check matches

          //  rowItems.remove(0);


            flingContainer.getTopCardListener().selectRight();
            arrayAdapter.notifyDataSetChanged();
            likedmatch(card_item);


            Toast.makeText(MainActivity.this, "Like", Toast.LENGTH_SHORT).show();
//            Intent btnClick = new Intent(mContext, BtnLikeActivity.class);
//            btnClick.putExtra("url", card_item.getProfileImageUrl());
//            startActivity(btnClick);
        }
    }


    /**
     * setup top tool bar
     */
    private void setupTopNavigationView() {
        Log.d(TAG, "setupTopNavigationView: setting up TopNavigationView");
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationViewHelper.setupTopNavigationView(tvEx);
        TopNavigationViewHelper.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


    @Override
    public void onBackPressed() {

    }


}
