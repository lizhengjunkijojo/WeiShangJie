package com.lzj.vtm.demo.fun.recycleswpie.swipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SwipeApi {

    //http://gank.io/api/data/福利/5/1
    @GET("api/data/福利/{pageCount}/{pageIndex}")
    Call<DataInfo> getData(@Path("pageCount") int pageCount,
                           @Path("pageIndex") int pageIndex);
}
