package com.lzj.vtm.demo.fun.juhe.wnl.api;

import com.lzj.vtm.demo.fun.juhe.wnl.model.WnlBase;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Retrofit接口处理方法
 */
public interface WnlApi {

    /**
     * 2.图片请求接口
     */
    @GET("calendar/day?")
    Observable<WnlBase> getWnl(@Query("date") String date, @Query("key") String key);
}
