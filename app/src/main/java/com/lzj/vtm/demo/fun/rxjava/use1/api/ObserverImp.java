package com.lzj.vtm.demo.fun.rxjava.use1.api;

import rx.Observer;

/**
 * Observer的使用
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
