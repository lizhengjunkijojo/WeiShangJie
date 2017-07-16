package com.lzj.vtm.demo.fun.juhe.weather.api;

import com.lzj.vtm.demo.fun.juhe.weather.model.WeatherBase;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Retrofit接口处理方法
 */
public interface WeatherApi {

    /**
     *
     */
    @GET("onebox/weather/query?")
    Observable<WeatherBase> getWeather(@Query("cityname") String cityname, @Query("key") String key);
}
