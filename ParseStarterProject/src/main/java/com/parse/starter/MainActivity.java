
package com.parse.starter;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.parse.ParseAnalytics;


public class MainActivity extends BaseActivity {

  private static FragmentManager fragmentManager;
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

//    try {
//      //SHAExample.encrypt();
//    } catch (NoSuchAlgorithmException e) {
//      e.printStackTrace();
//    }
  }


}
