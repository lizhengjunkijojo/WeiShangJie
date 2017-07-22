package com.lzj.vtm.demo.api;

import com.lzj.vtm.demo.download.Update;
import com.lzj.vtm.demo.home.episode.model.RetBaseEpisode;
import com.lzj.vtm.demo.home.news.model.RetBaseNews;
import com.lzj.vtm.demo.banner.RetBaseBanner;

import rx.Observable;

/**
 * Observable的使用   被观察者
 */
public class ObservableImp<T> extends Observable<T> {

    protected ObservableImp(OnSubscribe<T> f) {
        super(f);
    }

    public static Observable<RetBaseNews> getNews(int pageNo, int pageSize) {
        return RetrofitImp.getWsjApi().topNews(pageNo,pageSize);
    }

    public static Observable<RetBaseBanner> getBanner(int pageNo, int pageSize) {
        return RetrofitImp.getWsjApi().topBanners(pageNo,pageSize);
    }

    public static Observable<Update> getApp(int devType) {
        return RetrofitImp.getWsjApi().getApp(devType);
    }

    public static Observable<RetBaseBanner> getStartImage() {
        return RetrofitImp.getWsjApi().getStartImage();
    }

    public static Observable<RetBaseEpisode> getEpisode(int pageNo, int pageSize) {
        return RetrofitImp.getWsjApi().getEpisode(pageNo,pageSize);
    }
}
