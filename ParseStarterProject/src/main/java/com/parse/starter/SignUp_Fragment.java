package com.parse.starter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.parse.starter.Main.MainActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUp_Fragment extends BaseFragment {

    private  View view;
    @BindView(R.id.fullName)
    EditText fullName;
    @BindView(R.id.userEmailId)
    EditText emailId;
    @BindView(R.id.mobileNumber)
    EditText mobileNumber;
    @BindView(R.id.location)
    EditText location;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.confirmPassword)
    EditText confirmPassword;
    @BindView(R.id.already_user)
    TextView login;
    @BindView(R.id.signUpBtn)
    Button signUpButton;
    @BindView(R.id.terms_conditions)
    CheckBox terms_conditions;
    private Boolean signup_flag;
    static int accountno;

    UserSession session;


    @OnClick({R.id.signUpBtn,R.id.already_user})
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.signUpBtn:
                checkValidation();
                if(signup_flag)
                {
                    do_regrisration();
                }

                break;

            case R.id.already_user:
                getFragmentManager().popBackStack();
                break;
        }
    }

    public SignUp_Fragment()
    { }
    public static SignUp_Fragment newInstance() {

        Bundle args = new Bundle();

        SignUp_Fragment fragment = new SignUp_Fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_layout, container, false);
        ButterKnife.bind(this,view);
        initViews();

        if(!checkconnection())
        {
            AlertDialog alert = build_Network_Error_Dialog(getContext()).create();
            alert.show();
        }
        return view;
    }



    private void initViews() {
        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);
            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        session = new UserSession(getContext());

    }


    private void checkValidation() {
        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getLocation = location.getText().toString();
        String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regExemail);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("") || getLocation.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0)

            new CustomToast().Show_Toast(getActivity(), getView(), "All fields are required.");

        // Check if email id valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view, "Your Email Id is Invalid.");

            // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword))
            new CustomToast().Show_Toast(getActivity(), view, "Both password doesn't match.");

            // Make sure user should check Terms and Conditions checkbox
        else if (!terms_conditions.isChecked())
            new CustomToast().Show_Toast(getActivity(), view, "Please select Terms and Conditions.");

            // Else do signup or do your stuff
        else
            Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT).show();
            signup_flag = true;

    }

    private void do_instant_login()
    {
        showProgressDialog();
        ParseUser.logInInBackground(emailId.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                hideProgressDialog();
                if(user != null)
                {
                    session.createUserLoginSession(emailId.getText().toString(),password.getText().toString());
                    Toast.makeText(getActivity(), "Hi"+user.getUsername(), Toast.LENGTH_SHORT).show();
                    startActivity( new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }
                else
                {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    private void do_regrisration()
    {
        accountno++;
        ParseUser user = new ParseUser();
        user.setUsername(fullName.getText().toString());
        user.setPassword(confirmPassword.getText().toString());
        user.setEmail(emailId.getText().toString());
        user.put("user_id",accountno);
        user.put("phone_num",mobileNumber.getText().toString());
        user.put("location",location.getText().toString());

        showProgressDialog();
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                hideProgressDialog();
                if(e == null)
                {
                    Toast.makeText(getActivity(), "Signup successfull", Toast.LENGTH_SHORT).show();
                    do_instant_login();

                }
                else
                {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });







    }


}
