package com.lzj.vtm.demo.fun.juhe.pcd.api;

import rx.Observer;

/**
 * Observer的使用   观察者
 */
public class ObserverImp<T> implements Observer<T>{

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onNext(T t) {
    }
}