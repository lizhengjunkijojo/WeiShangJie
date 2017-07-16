package com.lzj.vtm.demo.fun.rxjava.use;

import com.lzj.vtm.demo.fun.rxjava.model.ZhuangbiImage;

import java.util.List;

import rx.Observable;

/**
 * Observable的使用   被观察者
 */
public class ObservableImp<T> extends Observable<T> {

    protected ObservableImp(OnSubscribe<T> f) {
        super(f);
    }

    public static Observable<List<ZhuangbiImage>> getObservable(String key) {
        return RetrofitImp.getZhuangbiApi().search(key);
    }
}
