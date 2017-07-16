package com.lzj.vtm.demo.fun.rxjava.use2.retrofit;

import com.lzj.vtm.demo.fun.rxjava.model.ZhuangbiImage;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 *
 */
public interface NewsService {

    @GET("search")
    Observable<List<ZhuangbiImage>> search(@Query("q") String query);
}
