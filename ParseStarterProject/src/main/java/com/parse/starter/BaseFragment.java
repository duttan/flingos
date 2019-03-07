package com.parse.starter;


import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

import rx.subscriptions.CompositeSubscription;

public abstract class BaseFragment extends Fragment{

    protected CompositeSubscription compositeSubscription = new CompositeSubscription();
    public ProgressDialog progressDialog;

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }

    protected void hidekeyboard()
    {
        if(getActivity()!= null && getActivity().getCurrentFocus()!= null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }

    public void showProgressDialog()
    {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Just 2 mins");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public void hideProgressDialog()
    {
        progressDialog.dismiss();
    }


}
