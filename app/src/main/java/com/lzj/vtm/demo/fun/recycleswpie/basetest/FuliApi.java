package com.lzj.vtm.demo.fun.recycleswpie.basetest;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Retrofit接口处理方法
 */
public interface FuliApi {

    //http://gank.io/api/data/福利/5/1
    @GET("api/data/福利/{pageCount}/{pageIndex}")
    Observable<FuLiInfo> getData(@Path("pageCount") int pageCount,
                 @Path("pageIndex") int pageIndex);
}
