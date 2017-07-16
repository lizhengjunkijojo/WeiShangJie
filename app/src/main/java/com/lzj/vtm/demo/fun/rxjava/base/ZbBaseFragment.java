package com.lzj.vtm.demo.fun.rxjava.base;

import android.app.AlertDialog;
import android.app.Fragment;

import com.lzj.vtm.demo.R;

import butterknife.OnClick;
import rx.Subscription;

public abstract class ZbBaseFragment extends Fragment {

    protected Subscription subscription;

    @OnClick(R.id.tipBt)
    void tip() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getTitleRes())
                .setView(getActivity().getLayoutInflater().inflate(getDialogRes(), null))
                .show();
    }

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

    protected abstract int getDialogRes();

    protected abstract int getTitleRes();
}
