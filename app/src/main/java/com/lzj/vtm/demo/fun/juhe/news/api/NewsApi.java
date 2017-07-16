package com.lzj.vtm.demo.fun.juhe.news.api;

import com.lzj.vtm.demo.fun.juhe.news.model.RetBase;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Retrofit接口处理方法
 */
public interface NewsApi {

    /**
     * 2.图片请求接口
     */
    @GET("toutiao/index?")
    Observable<RetBase> topNews(@Query("type") String type, @Query("key") String key);
}
