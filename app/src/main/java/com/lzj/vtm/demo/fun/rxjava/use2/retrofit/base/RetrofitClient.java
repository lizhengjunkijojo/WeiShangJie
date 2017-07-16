package com.lzj.vtm.demo.fun.rxjava.use2.retrofit.base;

import com.lzj.vtm.demo.fun.rxjava.use2.retrofit.ImgService;
import com.lzj.vtm.demo.fun.rxjava.use2.retrofit.NewsService;
import com.lzj.vtm.demo.fun.rxjava.use2.retrofit.VideoService;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * retrofit封装
 */
public class RetrofitClient {

    public static final String TAG = "lzj";
    public static final String URL_BASE_NEWS = "http://zhuangbi.info/";//新闻来源
    public static final String URL_BASE_VIDEO = "http://api.shenyou.tv/apiv1/";//视频来源
    public static final String URL_BASE_IMAGE = "http://gank.io/api/";//图片来源

    private static Retrofit mNewRetrofit;
    private static Retrofit mVideoRetrofit;
    private static Retrofit mImgRetrofit;
    private static volatile RetrofitClient sNewsClient;
    private static volatile RetrofitClient sVideoClient;
    private static volatile RetrofitClient sImgClient;

    /**
     *
     */
    private RetrofitClient(int type) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor(TAG))
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());

        if (type == 1) {
            Retrofit.Builder newsBuilder = builder;
            mNewRetrofit = newsBuilder.baseUrl(URL_BASE_NEWS).build();
        } else if (type == 2) {
            Retrofit.Builder newsBuilder = builder;
            mVideoRetrofit = newsBuilder.baseUrl(URL_BASE_VIDEO).build();
        } else if (type == 3) {
            Retrofit.Builder newsBuilder = builder;
            mImgRetrofit = newsBuilder.baseUrl(URL_BASE_IMAGE).build();
        }

    }

    /**
     * 新闻
     */
    public static RetrofitClient getNewsIns() {
        if (sNewsClient == null) {
            synchronized (RetrofitClient.class) {
                if (sNewsClient == null) {
                    sNewsClient = new RetrofitClient(1);
                }
            }
        }
        return sNewsClient;
    }

    /**
     * 视频
     */
    public static RetrofitClient getVideoIns() {
        if (sVideoClient == null) {
            synchronized (RetrofitClient.class) {
                if (sVideoClient == null) {
                    sVideoClient = new RetrofitClient(1);
                }
            }
        }
        return sVideoClient;
    }

    /**
     * 图片
     */
    public static RetrofitClient getImgIns() {
        if (sImgClient == null) {
            synchronized (RetrofitClient.class) {
                if (sImgClient == null) {
                    sImgClient = new RetrofitClient(3);
                }
            }
        }
        return sImgClient;
    }

    /**
     *
     */
    private static <T> T createNews(Class<T> apiService) {
        return sNewsClient.getNewsIns().mNewRetrofit.create(apiService);
    }


    private static <T> T createVideo(Class<T> apiService) {
        return sVideoClient.getVideoIns().mVideoRetrofit.create(apiService);
    }

    private static <T> T createImg(Class<T> apiService) {
        return sImgClient.getImgIns().mImgRetrofit.create(apiService);
    }

    /**
     *
     */
    public NewsService NEWS() {
        return createNews(NewsService.class);
    }

    public VideoService VIDEO() {
        return createVideo(VideoService.class);
    }

    public ImgService IMAGE() {
        return createImg(ImgService.class);
    }
}
