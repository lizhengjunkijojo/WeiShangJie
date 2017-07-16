package com.lzj.vtm.demo.fun.juhe.news.api;

import com.lzj.vtm.demo.fun.juhe.news.model.RetBase;

import rx.Observable;

/**
 * Observable的使用   被观察者
 */
public class ObservableImp<T> extends Observable<T> {

    protected ObservableImp(OnSubscribe<T> f) {
        super(f);
    }

    public static Observable<RetBase> getObservable(String type, String appKey) {
        return RetrofitImp.getNewsApi().topNews(type,appKey);
    }
}
