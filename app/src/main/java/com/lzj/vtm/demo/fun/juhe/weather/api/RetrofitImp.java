package com.lzj.vtm.demo.fun.juhe.weather.api;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit网络处理类
 */
public class RetrofitImp {

    private static Retrofit retrofit;
    private static WeatherApi weatherApi;

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    /**
     * 1.Retrofit创建
     */
    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://op.juhe.cn/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
        }
        return retrofit;
    }

    /**
     * 3.动态代理对接处理
     */
    public static WeatherApi getWeatherApi() {
        if (weatherApi == null) {
            weatherApi = getRetrofit().create(WeatherApi.class);
        }
        return weatherApi;
    }
}
