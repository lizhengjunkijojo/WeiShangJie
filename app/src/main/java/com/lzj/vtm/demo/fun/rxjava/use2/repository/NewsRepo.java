package com.lzj.vtm.demo.fun.rxjava.use2.repository;

import com.lzj.vtm.demo.fun.rxjava.use2.repository.base.BaseRepo;
import com.lzj.vtm.demo.fun.rxjava.use2.retrofit.base.RetrofitClient;
import com.lzj.vtm.demo.fun.rxjava.model.ZhuangbiImage;

import java.util.List;

import rx.Observable;

/**
 *
 */
public class NewsRepo extends BaseRepo {

    private static volatile NewsRepo instance;

    private NewsRepo() {
    }

    public static NewsRepo getIns() {
        if (instance == null) {
            synchronized (NewsRepo.class) {
                if (instance == null) {
                    instance = new NewsRepo();
                }
            }
        }
        return instance;
    }

    /**
     * 获取列表
     */
    public Observable<List<ZhuangbiImage>> getNewsList(String query) {
        return transform(RetrofitClient.getNewsIns().NEWS().search(query));
    }

}
