package com.lzj.vtm.demo.fun.rxjava.use;

import com.lzj.vtm.demo.fun.rxjava.model.ZhuangbiImage;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Retrofit接口处理方法
 */
public interface ZhuangbiApi {

    /**
     * 2.图片请求接口
     */
    @GET("search")
    Observable<List<ZhuangbiImage>> search(@Query("q") String query);
}
