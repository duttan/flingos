
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
              .addToBackStack(null)
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

  public void replaceLoginFragment() {
    fragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
            .replace(R.id.frameContainer, loginFragment, Utils.Login_Fragment).commit();
  }


  @Override
  public void onBackPressed() {
    // Find the tag of signup and forgot password fragment
    android.support.v4.app.Fragment SignUp_Fragment = fragmentManager
            .findFragmentByTag(Utils.SignUp_Fragment);
    android.support.v4.app.Fragment ForgotPassword_Fragment = fragmentManager
            .findFragmentByTag(Utils.ForgotPassword_Fragment);

    // Check if both are null or not
    // If both are not null then replace login fragment else do backpressed
    // task

    if (SignUp_Fragment != null)
      replaceLoginFragment();
    else if (ForgotPassword_Fragment != null)
      replaceLoginFragment();
    else
      super.onBackPressed();
  }
}
