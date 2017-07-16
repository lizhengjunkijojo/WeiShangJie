package com.lzj.vtm.demo.fun.rxjava.use1.api;

import com.lzj.vtm.demo.fun.rxjava.model.ZhuangbiImage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Retrofit接口处理方法
 */
public interface ZhuangbiApi {

    /**
     * 图片请求接口
     */
    @GET("search")
    Observable<List<ZhuangbiImage>> search(@Query("q") String query);

    /**
     * 示例
     */
    @FormUrlEncoded
    @POST("quan.log.LogCountManage/saveLogEquipment?")
    Call saveLogEquipmentPost(@Field("installId") long installId,
                              @Field("os") String os,
                              @Field("phoneType") String phoneType,
                              @Field("resolvingPower") String resolvingPower,
                              @Field("edition") String edition);
    @GET("quan.log.LogCountManage/saveLogEquipment?")
    Call saveLogEquipmentGet(@Query("installId") long installId,
                             @Query("os") String os,
                             @Query("phoneType") String phoneType,
                             @Query("resolvingPower") String resolvingPower,
                             @Query("edition") String edition);

}
