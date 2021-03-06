package com.parse.starter.Profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.BaseActivity;
import com.parse.starter.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class EditProfileActivity extends BaseActivity {
    private static final String TAG = "EditProfileActivity";
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    //firebase
    private static final int REQUEST_PERMISSION_SETTING = 101;
    Button man, woman, update;
    ImageButton back;
    TextView man_text, women_text;
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView;
    Bitmap myBitmap;
    Uri picUri;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Context mContext = EditProfileActivity.this;
    private ImageView mProfileImage;
    private String userId, profileImageUri;
    private Uri resultUri;
    private String userSex;
    private EditText phoneNumber, aboutMe, job, company, school, age, interest;
    private CheckBox sportsCheckBox, travelCheckBox, musicCheckBox, fishingCheckBox;
    private boolean isSportsClicked = false;
    private boolean isTravelClicked = false;
    private boolean isFishingClicked = false;
    private boolean isMusicClicked = false;
    private RadioGroup userSexSelection;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;

    //parse
    private ParseUser currentuser = ParseUser.getCurrentUser();

    private ParseObject flingcard = new ParseObject("Card");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        hidekeyboard();
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        requestMultiplePermissions();
        imageView1 = findViewById(R.id.image_view_1);
        imageView2 = findViewById(R.id.image_view_2);
        imageView3 = findViewById(R.id.image_view_3);
        imageView4 = findViewById(R.id.image_view_4);
        imageView5 = findViewById(R.id.image_view_5);
        imageView6 = findViewById(R.id.image_view_6);
        man = findViewById(R.id.man_button);
        woman = findViewById(R.id.woman_button);
        man_text = findViewById(R.id.man_text);
        women_text = findViewById(R.id.woman_text);
        back = findViewById(R.id.back);
        update = findViewById(R.id.update_details);
        aboutMe = findViewById(R.id.user_bio);
        job = findViewById(R.id.user_job);
        company = findViewById(R.id.user_company);
        school = findViewById(R.id.user_school);
        age = findViewById(R.id.user_age);
        interest = findViewById(R.id.user_interest);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        woman.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                women_text.setTextColor(R.color.colorAccent);
                woman.setBackgroundResource(R.drawable.ic_check_select);
                man_text.setTextColor(R.color.black);
                man.setBackgroundResource(R.drawable.ic_check_unselect);
            }
        });

        man.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                man_text.setTextColor(R.color.colorAccent);
                man.setBackgroundResource(R.drawable.ic_check_select);
                women_text.setTextColor(R.color.black);
                woman.setBackgroundResource(R.drawable.ic_check_unselect);
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView1;
                proceedAfterPermission();

            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView2;
                proceedAfterPermission();

            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView3;
                proceedAfterPermission();

            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView4;
                proceedAfterPermission();

            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView5;
                proceedAfterPermission();

            }
        });

        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView = imageView6;
                proceedAfterPermission();

            }
        });

        fill_userdetails();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO code
                updatedetails();

            }
        });


    }

    private void fill_userdetails() {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Card");
        query.fromLocalDatastore();
        query.fromPin("local");

        query.whereEqualTo("userobject_id_fk", currentuser.getObjectId());


        try {
            if(query.getFirst().isDataAvailable()) {
                flingcard = query.getFirst();
                Log.i("@@update_status:","old_user");
                aboutMe.setText(flingcard.get("bio").toString());
                job.setText(flingcard.get("job").toString());
                company.setText(flingcard.get("company").toString());
                school.setText(flingcard.get("school").toString());
                school.setText(flingcard.get("age").toString());
                school.setText(flingcard.get("interest").toString());
            }
            else
            {
                Log.i("@@update_status:","new_user");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void updatedetails() {

        if(!aboutMe.getText().toString().equals("") && !job.getText().toString().equals("") && !company.getText().toString().equals("") && !school.getText().toString().equals(""))
        {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Card");
            query.whereEqualTo("userobject_id_fk", currentuser.getObjectId());
            try {
               if(query.getFirst().isDataAvailable()) {
                   flingcard = query.getFirst();
                   Log.i("@@update_status:","old_user");
               }
               else
               {
                   Log.i("@@update_status:","new_user");
               }
            } catch (ParseException e) {
                e.printStackTrace();
            }



            flingcard.put("userobject_id_fk",currentuser.getObjectId());
            flingcard.put("bio",aboutMe.getText().toString());
            flingcard.put("company",company.getText().toString());
            flingcard.put("school",school.getText().toString());
            flingcard.put("job",job.getText().toString());
            flingcard.put("cardname",currentuser.getUsername());
            flingcard.put("age",age.getText().toString());
            flingcard.put("interest",interest.getText().toString());
            flingcard.pinInBackground();

            try {
                flingcard.pin("local");
            }catch (Exception e)
            {
                Log.i("@@",e.getMessage());
            }


            flingcard.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null)
                    {
                        Log.i("@@update_status:","success");
                        onBackPressed();
                    }
                    else
                    {
                        Log.i("@@update_status:",e.getMessage());
                    }


                }
            });

        }
        else
        {
            Toast.makeText(this,"Fill all your details! Help us get you a better match",Toast.LENGTH_SHORT).show();
        }


    }


    private void requestMultiplePermissions() {
        if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(EditProfileActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(EditProfileActivity.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, permissionsRequired[2])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(EditProfileActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(EditProfileActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            // txtPermissions.setText("Permissions Required");

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            //proceedAfterPermission();
        }
    }

    private void proceedAfterPermission() {


        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};


        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);

        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {

                    cameraIntent();

                } else if (options[item].equals("Choose from Gallery"))

                {

                    galleryIntent();


                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] byteArray = bytes.toByteArray();
        ParseFile img_file = new ParseFile("dp.png", byteArray);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Card");
        query.whereEqualTo("userobject_id_fk", currentuser.getObjectId());
        try {
            if(query.getFirst().isDataAvailable()) {
                flingcard = query.getFirst();
                flingcard.put("profile_picture",img_file);
                flingcard.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if(e == null)
                        {
                            Log.i("@@Pic update_status:","success");
                        }
                        else
                        {
                            Log.i("@@Pic update_status:","failure:"+e.getMessage());                        }
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


        imageView.setImageBitmap(thumbnail);


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        byte[] byteArray = bytes.toByteArray();
        ParseFile img_file = new ParseFile("dp.png", byteArray);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Card");
        query.whereEqualTo("userobject_id_fk", currentuser.getObjectId());
        try {
            if(query.getFirst().isDataAvailable()) {
                flingcard = query.getFirst();
                flingcard.put("profile_picture",img_file);
                flingcard.saveInBackground();
                Log.i("@@Pic update_status:","success");
            }
            else
            {
                Log.i("@@Pic update_status:","no_user");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(bm);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }
}
