
package com.parse.starter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.parse.ParseAnalytics;

import java.security.NoSuchAlgorithmException;


public class MainActivity extends BaseActivity {

  private static android.support.v4.app.FragmentManager fragmentManager;
  LoginFragment loginFragment = LoginFragment.newInstance();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    fragmentManager = getSupportFragmentManager();
      Log.i("@@class:  ","MainActivity");


    if (savedInstanceState == null) {
      fragmentManager
              .beginTransaction()
              .replace(R.id.frameContainer, loginFragment, Utils.Login_Fragment)
              .commit();
    }




    ParseAnalytics.trackAppOpenedInBackground(getIntent());

    try {
      SHAExample.encrypt();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }


}
