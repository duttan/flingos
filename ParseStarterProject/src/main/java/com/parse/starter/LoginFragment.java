package com.parse.starter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.starter.Main.MainActivity;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

public class LoginFragment extends BaseFragment{

    private View view;
    private Animation shakeAnimation;
    private Boolean signin_flag;




    @BindView(R.id.login_emailid)
    EditText emailid;
    @BindView(R.id.login_password)
    EditText password;
    @BindView(R.id.loginBtn)
    Button loginButton;
    @BindView(R.id.forgot_password)
    TextView forgotPassword;
    @BindView(R.id.createAccount)
    TextView signUp;
    @BindView(R.id.show_hide_password)
    CheckBox show_hide_password;
    @BindView(R.id.login_layout)
    LinearLayout loginLayout;
    Pattern p_mail = Pattern.compile(Utils.regExemail);
    Pattern p_pass = Pattern.compile(Utils.regExpass);
    Matcher m_mail,m1_pass;
    UserSession session;


    @OnClick({R.id.loginBtn,R.id.forgot_password,R.id.createAccount,R.id.show_hide_password})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.loginBtn:

                if(signin_flag)
                {
                    do_login();
                }

                break;

            case R.id.forgot_password:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .addToBackStack(null)
                        .add(R.id.frameContainer, new ForgotPassword_Fragment(), Utils.ForgotPassword_Fragment).commit();
                break;


            case R.id.createAccount:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .addToBackStack(null)
                        .add(R.id.frameContainer, new SignUp_Fragment(), Utils.SignUp_Fragment).commit();
                break;


            case R.id.show_hide_password:
                show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        // If it is checked then show password else hide
                        // password
                        if (b){
                            show_hide_password.setText(R.string.hide_pwd);// change // checkbox // text
                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());       // show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change // checkbox // text
                            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());         // hide password

                        }

                    }
                });
        }
    }

    public LoginFragment() {

    }

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        hidekeyboard();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_layout, container, false);
        ButterKnife.bind(this,view);
        initViews();
        if(!checkconnection())
        {
            AlertDialog alert = build_Network_Error_Dialog(getContext()).create();
            alert.show();
        }

        return view;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        session = new UserSession(getContext());

        }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Observable<CharSequence> emailobservable = RxTextView.textChanges(emailid);
        Observable<CharSequence> passobservable = RxTextView.textChanges(password);


        Subscription emailsubscription = emailobservable
                .doOnNext(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence)
                    { }
                })
                .debounce(500,TimeUnit.MICROSECONDS)
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        return !TextUtils.isEmpty(charSequence);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        }

                    @Override
                    public void onNext(CharSequence charSequence) {

                        Boolean isEmailValid = validateEmail(charSequence.toString());
                        if (!isEmailValid) {
                            showmailtoast();

                        }

                    }
                });
    compositeSubscription.add(emailsubscription);

        Subscription passsubscription = passobservable
                .doOnNext(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {

                    }
                })
                .debounce(500,TimeUnit.MICROSECONDS)
                .filter(new Func1<CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence) {
                        return !TextUtils.isEmpty(charSequence);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() { }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        }

                    @Override
                    public void onNext(CharSequence charSequence) {

                        Boolean isPasswordValid = validatePass(charSequence.toString());
                        if (!isPasswordValid) {
                            showpasstoast();
                        }

                    }
                });

        compositeSubscription.add(passsubscription);

        Subscription SignInFieldsSubscription = Observable.combineLatest(emailobservable, passobservable, new Func2<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence email, CharSequence pass) {
                boolean isEmailValid = validateEmail(email);
                boolean isPasswordValid = validatePass(pass);

                return isEmailValid && isPasswordValid;
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted()
                            { }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(Boolean valid_fields) {
                        if(!valid_fields) {
                            disableSignIn();
                        }
                        else
                        {
                            enableSignIn();
                            signin_flag = true;

                        }

                    }
                });

        compositeSubscription.add(SignInFieldsSubscription);

    }

        //Region Helper Methods

    private void initViews() {
        // Load ShakeAnimation
         shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        // Setting text selector over textviews
        @SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);
            forgotPassword.setTextColor(csl);
            show_hide_password.setTextColor(csl);
            signUp.setTextColor(csl);
        } catch (Exception e) {
        }

    }


    private boolean validateEmail(CharSequence email)
    {
        if (TextUtils.isEmpty(email))
            return false;
        m_mail = p_mail.matcher(email);
        return m_mail.matches();
    }

    private boolean validatePass(CharSequence pass)
    {
        if (TextUtils.isEmpty(pass))
            return false;
        m1_pass = p_pass.matcher(pass);
        return m1_pass.matches();
    }

    private void showmailtoast()
    {
        CustomToast.Show_Toast(getActivity(), view, getString(R.string.mail_error_toast));
    }

    private void showpasstoast()
    {
        CustomToast.Show_Toast(getActivity(), view, getString(R.string.pass_error_toast));
    }

    private void enableSignIn(){

        loginButton.setEnabled(true);
        loginButton.setTextColor(ContextCompat.getColor(getContext(), R.color.background_color));
    }

    private void disableSignIn(){

        loginButton.setEnabled(false);
        loginButton.setTextColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
    }

    public void do_login()
    {


        showProgressDialog();
        ParseUser.logInInBackground(emailid.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                hideProgressDialog();
                if(user != null)
                {
                    session.createUserLoginSession(emailid.getText().toString(),password.getText().toString());
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

}