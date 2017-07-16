package com.lzj.vtm.demo.fun.rxjava.use2.repository.base;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * rst线程转化
 */
public abstract class BaseRepo {

    //对rst进行判断再做处理
    protected static Observable transform(Observable observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
