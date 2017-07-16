package com.lzj.vtm.demo.fun.juhe.pcd.api;

import com.lzj.vtm.demo.fun.juhe.pcd.model.PCDBase;

import rx.Observable;

/**
 * Observable的使用   被观察者
 */
public class ObservableImp<T> extends Observable<T> {

    protected ObservableImp(OnSubscribe<T> f) {
        super(f);
    }

    public static Observable<PCDBase> getObservablePCD(String appKey) {
        return RetrofitImp.getPCDApi().getCPD(appKey);
    }
}
