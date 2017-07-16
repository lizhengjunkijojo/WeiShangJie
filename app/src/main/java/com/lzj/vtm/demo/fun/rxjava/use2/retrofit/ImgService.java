package com.lzj.vtm.demo.fun.rxjava.use2.retrofit;

import com.lzj.vtm.demo.fun.rxjava.use2.models.BaseEntity;

import retrofit2.http.GET;
import rx.Observable;

/**
 *
 */
public interface ImgService {

    @GET("data/{page}")
    Observable<BaseEntity> imgList();
}
