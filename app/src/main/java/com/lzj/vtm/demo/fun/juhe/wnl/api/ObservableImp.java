package com.lzj.vtm.demo.fun.juhe.wnl.api;

import com.lzj.vtm.demo.fun.juhe.wnl.model.WnlBase;

import rx.Observable;

/**
 * Observable的使用   被观察者
 */
public class ObservableImp<T> extends Observable<T> {

    protected ObservableImp(OnSubscribe<T> f) {
        super(f);
    }

    public static Observable<WnlBase> getObservable(String date, String appKey) {
        return RetrofitImp.getWnlApi().getWnl(date,appKey);
    }
}
