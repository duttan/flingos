package com.parse.starter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPassword_Fragment extends BaseFragment {

    private View view;
    @BindView(R.id.registered_emailid) EditText emailId;
    @BindView(R.id.backToLoginBtn) TextView submit;
    @BindView(R.id.forgot_button) TextView back;

    @OnClick({R.id.backToLoginBtn,R.id.forgot_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backToLoginBtn:
                getFragmentManager().popBackStack();
                break;

            case R.id.forgot_button:
                submitButtonTask();
                break;

        }

    }

    public ForgotPassword_Fragment()
    {}

    public static ForgotPassword_Fragment newInstance() {

        Bundle args = new Bundle();

        ForgotPassword_Fragment fragment = new ForgotPassword_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.forgotpassword_layout, container,
                false);
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
            ColorStateList csl = ColorStateList.createFromXml(getResources(),xrp);
            back.setTextColor(csl);
            submit.setTextColor(csl);

        } catch (Exception e) {
        }


    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private void submitButtonTask() {
        String getEmailId = emailId.getText().toString();
        // Pattern for email id validation
        Pattern p = Pattern.compile(Utils.regExemail);
        // Match the pattern
        Matcher m = p.matcher(getEmailId);
        // First check if email id is not null else show error toast
        if (getEmailId.equals("") || getEmailId.length() == 0)

            new CustomToast().Show_Toast(getActivity(), view,
                    "Please enter your Email Id.");

            // Check if email id is valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");

            // Else submit email id and fetch passwod or do your stuff
        else
            Toast.makeText(getActivity(), "Get Forgot Password.",
                    Toast.LENGTH_SHORT).show();
    }
    }

