package com.lzj.vtm.demo.api;

import com.lzj.vtm.demo.download.Update;
import com.lzj.vtm.demo.home.model.RetBaseNews;
import com.lzj.vtm.demo.banner.RetBaseBanner;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Retrofit接口处理方法
 */
public interface WsjApi {

    /*生产地址:
     * http://123.56.221.86:8099/api/kxnrByPage    列表页面
     * http://123.56.221.86:8099/api/bannerByPage  轮播图页面
     * http://123.56.221.86:8099/api/latestAppInfo  更新下载
     *
     * 测试地址:
     * 47.94.108.82:8099
     */
    @GET("api/kxnrByPage?")
    Observable<RetBaseNews> topNews(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize);

    @GET("api/bannerByPage?")
    Observable<RetBaseBanner> topBanners(@Query("pageNo") int pageNo, @Query("pageSize") int pageSize);

    @GET("api/latestAppInfo?")
    Observable<Update> getApp(@Query("devType") int devType);

    @GET("api/startImage?")
    Observable<RetBaseBanner> getStartImage();
}
