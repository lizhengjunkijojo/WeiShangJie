package com.lzj.vtm.demo.fun.juhe.weather.api;

import com.lzj.vtm.demo.fun.juhe.weather.model.WeatherBase;

import rx.Observable;

/**
 * Observable的使用   被观察者
 */
public class ObservableImp<T> extends Observable<T> {

    protected ObservableImp(OnSubscribe<T> f) {
        super(f);
    }

    public static Observable<WeatherBase> getObservable(String cityName, String appKey) {
        return RetrofitImp.getWeatherApi().getWeather(cityName,appKey);
    }
}
