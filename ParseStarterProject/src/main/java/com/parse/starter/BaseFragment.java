package com.parse.starter;

import android.support.v4.app.Fragment;

import rx.subscriptions.CompositeSubscription;

public abstract class BaseFragment extends Fragment{

    protected CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}
