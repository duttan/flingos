
package com.parse.starter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.parse.ParseAnalytics;


public class MainActivity extends AppCompatActivity {

  private static android.support.v4.app.FragmentManager fragmentManager;
  LoginFragment loginFragment = LoginFragment.newInstance();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    fragmentManager = getSupportFragmentManager();


    if (savedInstanceState == null) {
      fragmentManager
              .beginTransaction()
              .replace(R.id.frameContainer, loginFragment, Utils.Login_Fragment)
              .commit();
    }


    findViewById(R.id.close_activity).setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View arg0) {
                finish();
                }
            });


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }


}
