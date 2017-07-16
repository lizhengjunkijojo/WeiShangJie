package com.lzj.vtm.demo.fun.home.api;

import com.lzj.vtm.demo.fun.home.model.RetBase;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Retrofit接口处理方法
 */
public interface NewsApi {

    /**
     * 2.图片请求接口
     * http://v.juhe.cn/toutiao/index?type=top&key=1f10c76363a964ed0826726fab95d3dd&pageIndex=2
     */
    @GET("toutiao/index?")
    Observable<RetBase> topNews(@Query("type") String type, @Query("key") String key);
}
