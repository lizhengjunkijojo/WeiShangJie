package com.lzj.vtm.demo.fun.juhe.weather.model;

/**
 * RxJava模型类
 */
public class WeatherBase {

    public String reason;
    public Result result;
    public int error_code;

    public class Result{
        public Weather data;
    }
}
