package com.lzj.vtm.demo.fun.rxjava.use2.repository;


import com.lzj.vtm.demo.fun.rxjava.use2.repository.base.BaseRepo;
import com.lzj.vtm.demo.fun.rxjava.use2.retrofit.base.RetrofitClient;

import rx.Observable;

/**
 *
 */
public class ImageRepo extends BaseRepo {

    private static volatile ImageRepo instance;

    private ImageRepo() {
    }

    public static ImageRepo getIns() {
        if (instance == null) {
            synchronized (ImageRepo.class) {
                if (instance == null) {
                    instance = new ImageRepo();
                }
            }
        }
        return instance;
    }

    /**
     *
     */
    public Observable getImageList() {
        return transform(RetrofitClient.getImgIns().IMAGE().imgList());
    }
}
