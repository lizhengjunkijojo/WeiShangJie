package com.lzj.vtm.demo.fun.juhe.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzj.vtm.demo.R;

import java.io.Serializable;

import rx.Subscription;

/**
 *
 */
public abstract class CommonFragment<T extends Serializable> extends Fragment {

    protected T mDetail;

    protected int getLayoutId() {
        return R.layout.juhe_weather_fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view);
        requestData();
        return view;
    }

    protected abstract void initView(View view);
    protected abstract void requestData();
    protected abstract void dataViewBody(T detail);

    /**
     *
     */
    protected Subscription subscription;
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }
    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
