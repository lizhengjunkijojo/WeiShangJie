package com.lzj.vtm.demo.fun.juhe.weather;


import android.view.View;
import android.widget.TextView;

import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.fun.juhe.weather.api.ObservableImp;
import com.lzj.vtm.demo.fun.juhe.weather.api.ObserverImp;
import com.lzj.vtm.demo.fun.juhe.weather.model.Weather;
import com.lzj.vtm.demo.fun.juhe.weather.model.WeatherBase;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class WeatherFragment extends CommonFragment<Weather> {

    @Bind(R.id.realtime_day_info)
    protected TextView realtime_day_info;
    @Bind(R.id.realtime_weather)
    protected TextView realtime_weather;
    @Bind(R.id.realtime_wind)
    protected TextView realtime_wind;

    @Bind(R.id.life_chuanyi)
    protected TextView life_chuanyi;
    @Bind(R.id.life_ganmao)
    protected TextView life_ganmao;
    @Bind(R.id.life_kongtiao)
    protected TextView life_kongtiao;
    @Bind(R.id.life_wuran)
    protected TextView life_wuran;
    @Bind(R.id.life_xiche)
    protected TextView life_xiche;
    @Bind(R.id.life_yundong)
    protected TextView life_yundong;
    @Bind(R.id.life_ziwaixian)
    protected TextView life_ziwaixian;

    @Bind(R.id.pm25_info)
    protected TextView pm25_info;
    @Bind(R.id.pm25_pm25)
    protected TextView pm25_pm25;


    protected int getLayoutId() {
        return R.layout.juhe_weather_fragment;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void requestData() {

        Observable<WeatherBase> observable = ObservableImp.getObservable("北京","ecc1a01502b3dd002611e625b4a7834f");

        observable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        subscription = observable.subscribe(observer);
    }

    ObserverImp<WeatherBase> observer = new ObserverImp<WeatherBase>(){

        @Override
        public void onNext(WeatherBase retBase) {
            super.onNext(retBase);
            dataViewBody(retBase.result.data);
        }
    };

    @Override
    protected void dataViewBody(Weather detail) {

        realtimeData(detail.realtime);
        lifeData(detail.life);
        pm25Data(detail.pm25);
    }

    private void realtimeData(Weather.Realtime realtime) {
        realtime_day_info.setText(realtime.city_name+"  "+realtime.date+"  "+realtime.time +"  "+realtime.moon);

        StringBuffer body1 = new StringBuffer();
        body1.append("天气:"+realtime.weather.info);
        body1.append("\n");
        body1.append("湿度:"+realtime.weather.temperature);
        body1.append("\n");
        body1.append("湿度:"+realtime.weather.humidity);
        realtime_weather.setText(body1);

        StringBuffer body2 = new StringBuffer();
        body2.append("风向:"+realtime.wind.direct);
        body2.append("\n");
        body2.append("级数:"+realtime.wind.power);
        if(realtime.wind.windspeed!=null){
            body2.append("\n");
            body2.append("风速:"+realtime.wind.windspeed);
        }
        if(realtime.wind.offset!=null){
            body2.append("\n");
            body2.append("偏移:"+realtime.wind.offset);
        }
        realtime_wind.setText(body2);
    }

    private void pm25Data(Weather.Pm25 pm25) {
        pm25_info.setText(pm25.cityName+"  "+pm25.dateTime);

        StringBuffer body = new StringBuffer();
        body.append("pm25指数:"+pm25.pm25.pm25);
        body.append("\n");
        body.append("pm10指数:"+pm25.pm25.pm10);
        body.append("\n");
        body.append("级别:"+pm25.pm25.level);
        body.append("\n");
        body.append("质量:"+pm25.pm25.quality);
        body.append("\n");
        body.append("描述:"+pm25.pm25.des);
        pm25_pm25.setText(body);
    }

    private void lifeData(Weather.Life life) {
        life_chuanyi.setText("穿衣指数:"+life.info.chuanyi[0]+"\n"+life.info.chuanyi[1]);
        life_ganmao.setText("感冒指数:"+life.info.ganmao[0]+"\n"+life.info.ganmao[1]);
        life_kongtiao.setText("空调指数:"+life.info.kongtiao[0]+"\n"+life.info.kongtiao[1]);
        life_wuran.setText("污染指数:"+life.info.wuran[0]+"\n"+life.info.wuran[1]);
        life_xiche.setText("洗车指数:"+life.info.xiche[0]+"\n"+life.info.xiche[1]);
        life_yundong.setText("运动指数:"+life.info.yundong[0]+"\n"+life.info.yundong[1]);
        life_ziwaixian.setText("紫外线:"+life.info.ziwaixian[0]+"\n"+life.info.ziwaixian[1]);
    }
}
