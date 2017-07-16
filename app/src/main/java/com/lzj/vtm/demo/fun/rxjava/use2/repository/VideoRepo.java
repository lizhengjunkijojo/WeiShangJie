package com.lzj.vtm.demo.fun.rxjava.use2.repository;

import com.lzj.vtm.demo.fun.rxjava.use2.repository.base.BaseRepo;
import com.lzj.vtm.demo.fun.rxjava.use2.retrofit.base.RetrofitClient;

import rx.Observable;

/**
 *
 */
public class VideoRepo extends BaseRepo {

    private static volatile VideoRepo instance;

    private VideoRepo() {
    }

    public static VideoRepo getIns() {
        if (instance == null) {
            synchronized (VideoRepo.class) {
                if (instance == null) {
                    instance = new VideoRepo();
                }
            }
        }
        return instance;
    }

    public Observable getVideoList() {
        return transform(RetrofitClient.getVideoIns().VIDEO().getList());
    }
}
