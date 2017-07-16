package com.lzj.vtm.demo.fun.rxjava.use2.retrofit.base;

import rx.Subscriber;

/**
 *
 */
public abstract class DefaultSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T entity) {
        _onNext(entity);
    }


    public abstract void _onNext(T entity);
}
