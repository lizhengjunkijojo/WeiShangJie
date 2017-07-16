package com.lzj.vtm.demo.fun.juhe.wnl;


import android.view.View;
import android.widget.TextView;

import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.fun.juhe.wnl.api.ObservableImp;
import com.lzj.vtm.demo.fun.juhe.wnl.api.ObserverImp;
import com.lzj.vtm.demo.fun.juhe.wnl.model.WnlBase;
import com.lzj.vtm.demo.fun.juhe.wnl.model.Wnl;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class WnlFragment extends CommonFragment<Wnl> {

    private TextView time_day;
    private TextView description;

    protected int getLayoutId() {
        return R.layout.juhe_wnl_fragment;
    }

    @Override
    protected void initView(View view) {
        time_day = (TextView) view.findViewById(R.id.time_day);
        description = (TextView) view.findViewById(R.id.description);
    }

    @Override
    protected void requestData() {

        Observable<WnlBase> observable = ObservableImp.getObservable(getDataTime("yyyy-M-d"),"cd9629a2267e823e30eade7573ba2c6d");

        observable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        subscription = observable.subscribe(observer);
    }

    ObserverImp<WnlBase> observer = new ObserverImp<WnlBase>(){

        @Override
        public void onNext(WnlBase retBase) {
            super.onNext(retBase);
            dataViewBody(retBase.result.data);
        }
    };

    @Override
    protected void dataViewBody(Wnl detail) {

        time_day.setText(detail.date);

        StringBuffer body = new StringBuffer();
        body.append(detail.animalsYear+"  "+detail.lunarYear+"  "+detail.lunar +"  "+detail.weekday);
        body.append("\n");
        body.append("宜:"+detail.suit);
        body.append("\n");
        body.append("忌:"+detail.avoid);
        description.setText(body);
    }
}
