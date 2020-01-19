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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.BaseActivity;
import com.parse.starter.LaunchActivity;
import com.parse.starter.Matched.Matched_Activity;
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
    private List<String> likedme_users = new ArrayList<String>();
    ParseUser userobj;
    Cards mycard;
    Bitmap bt = null;
    LocationManager mLocationManager;
    String mProvider = LocationManager.GPS_PROVIDER;
    LocationListener mLocationListener;
    SwipeFlingAdapterView flingContainer;
    ImageButton chat;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_flingos);
        Bundle bu = getIntent().getBundleExtra("finalbundle");

        cardFrame = findViewById(R.id.card_frame);
        moreFrame = findViewById(R.id.more_frame);
        dpimageView = findViewById(R.id.post);
        chat = findViewById(R.id.commentbtn);

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

            if (current_loc.equals(null)) {
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
          updateLocation();

            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent3 = new Intent(mContext, Matched_Activity.class);
                    mContext.startActivity(intent3);


                }
            });

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
                        potential_matches.add(user.getObjectId()); }
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


    @Override
    protected void onResume() {
        super.onResume();

       // updateLocation();
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
                            if(!((card.get("userobject_id_fk").toString()).equals(currentUser.getObjectId()))){

                            Cards newcard = new Cards(card.get("userobject_id_fk").toString(),card.getObjectId()+"",card.get("cardname").toString(),card.get("age").toString(),card.getParseFile("profile_picture").getUrl(),card.get("bio").toString(),card.get("interest").toString(),50,false);
                            rowItems.add(newcard);
                            Log.i("@@card"+ card.getObjectId(),"added");
                            i++;
                        }
                        else
                            {
                                mycard = new Cards(card.get("userobject_id_fk")+"",card.getObjectId()+"",card.get("cardname")+"",card.get("age").toString(),card.getParseFile("profile_picture").getUrl(),card.get("bio").toString(),card.get("interest").toString(),50,false);
                            }
                        }

                        updateMatches(rowItems);
                    }
                    else
                    {
                        Log.i("@@Error:",e.getMessage());

                        rowItems.add(new Cards("1", "Tom Chris","" ,"21", "https://coverfiles.alphacoders.com/848/84877.jpg", "Simple and beautiful Girl", "Acting", 200,false));
                    }

                    arrayAdapter = new PhotoAdapter(mContext, R.layout.item, rowItems);
                    arrayAdapter.notifyDataSetChanged();
                    checkRowItem();
                    updateSwipeCard();
                    final_stack = rowItems;




                }
            });

//            if(mycard != null) {
//                updateMatches(rowItems);
//            }
        }
        else
        {
         Log.i("@@","No one is available rightnow");
        }

            return rowItems;
       // return updateMatches(rowItems);
    }

    private List<Cards> updateMatches(final List<Cards> row_items) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");
        query.whereEqualTo("card_id_fk",mycard.getCardId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null && objects.size() > 0 ){
                likedme_users = objects.get(0).getList("like");}

                for(Cards card: row_items)
                {
                    if(likedme_users.contains(card.getUserId()))
                    {
                        card.setLikeStatus(true);
                    }

                }

                final_stack = row_items;
            }
        });


        return final_stack;


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
                if(obj.getLikeStatus())
                {
//                  Toast.makeText(mContext,"Its a Match",Toast.LENGTH_SHORT).show();
                    Intent btnClick = new Intent(mContext, BtnLikeActivity.class);
                    btnClick.putExtra("url", obj.getProfileImageUrl());
                    startActivity(btnClick);
                    chat.setVisibility(View.VISIBLE);
                    updateMatchList(obj.getUserId());
                    updatePartnerMatchList(obj.getUserId());
                }
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

    private void updateMatchList(String id) {

        List<String> like_eachother;

        like_eachother = currentUser.getList("matches");
        if(like_eachother == null)
        {
            like_eachother = new ArrayList<String>();
            like_eachother.add(id);
        }else {
            if (!like_eachother.contains(id)) {
                like_eachother.add(id);
            }
        }


        currentUser.put("matches",like_eachother);
        currentUser.saveInBackground();
    }

    private void updatePartnerMatchList(String id) {

        ParseQuery<ParseUser> query  = ParseUser.getQuery();
        userobj = new ParseUser();
        query.getInBackground(id, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                List<String> like_each;
                if( e == null)
                {
                    userobj = object;
                    like_each = userobj.getList("matches");

                    if(like_each == null)
                    {
                        like_each = new ArrayList<String>();
                        like_each.add(currentUser.getObjectId());
                        userobj.put("matches",like_each);
                    }else {
                        if (!like_each.contains(currentUser.getObjectId())) {
                            like_each.add(currentUser.getObjectId());
                            userobj.put("matches",like_each);
                        }
                    }

                    userobj.saveInBackground();

                }

            }
        });

//        query.whereEqualTo("objectId",id);
//
//        query.findInBackground(new FindCallback<ParseUser>() {
//            @Override
//            public void done(List<ParseUser> objects, ParseException e) {
//
//            }
//        });

    }

    private void dislikedmatch(final Cards current_card)  {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");
        match = new ParseObject("Match");

        try {
        query.whereEqualTo("card_id_fk",current_card.getCardId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                List<String> dislikes = new ArrayList<String>();
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
                    else {
                        Log.i("@@","im here");

                    }
                    Log.i("Dislike:Array:NEW",match.getList("dislike").toString());
                   // save_eventually();
                    match.saveInBackground();
                }
                else {

                    match = new ParseObject("Match");
                    match.put("card_id_fk", current_card.getCardId());
                    dislikes.add(currentUser.getObjectId());
                    match.put("dislike", dislikes);
                    match.saveInBackground();
                    //match.saveEventually();

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
                    //Log.i("@@Dislike",dislikes.toString());
                }
                else {
                   // Log.i("@@Error:Dislike",e.getMessage());
                }
            }
        });
    }

    private void likedmatch(final Cards current_card) {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");

        try {
            query.whereEqualTo("card_id_fk",current_card.getCardId());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    List<String> likes = new ArrayList<String>();
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
                        match.put("card_id_fk", current_card.getCardId());
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

//            if(card_item.getLikeStatus())
//            {
//                    Toast.makeText(mContext,"Its a Match",Toast.LENGTH_SHORT).show();
//                triggerMatch(card_item);
//            }

            Toast.makeText(MainActivity.this, "Like", Toast.LENGTH_SHORT).show();

        }
    }







    //   private void triggerMatch(Cards card_item)
    //   {
    //   chat.setVisibility(View.VISIBLE);
    //   }


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
