package com.lzj.vtm.demo.fun.juhe.pcd.api;

import com.lzj.vtm.demo.fun.juhe.pcd.model.PCDBase;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Retrofit接口处理方法
 */
public interface PCDApi {

    /**
     * 2.图片请求接口
     */
    @GET("postcode/pcd?")
    Observable<PCDBase> getCPD (@Query("key") String key);
}
