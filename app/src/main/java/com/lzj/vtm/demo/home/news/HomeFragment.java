package com.lzj.vtm.demo.home.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.app.AppOperator;
import com.lzj.vtm.demo.banner.Banner;
import com.lzj.vtm.demo.banner.ViewHeader;
import com.lzj.vtm.demo.cache.CacheManager;
import com.lzj.vtm.demo.home.news.adapter.NewAdapter;
import com.lzj.vtm.demo.api.ObservableImp;
import com.lzj.vtm.demo.api.ObserverImp;
import com.lzj.vtm.demo.home.news.model.News;
import com.lzj.vtm.demo.home.news.model.RetBaseNews;
import com.lzj.vtm.demo.banner.RetBaseBanner;
import com.lzj.vtm.demo.base.UIHelper;
import com.lzj.vtm.demo.widget.SuperRefreshLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 首页
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener, SuperRefreshLayout.SuperRefreshLayoutListener {

    public String fileCacheName = getClass().getName();
    public String bannerCacheName = fileCacheName+"banner";

    @Bind(R.id.listView)
    protected ListView mListView;
    @Bind(R.id.superRefreshLayout)
    protected SuperRefreshLayout mRefreshLayout;
    private NewAdapter adapter;
    private ArrayList<News> arrayList;
    private ArrayList<Banner> banners;

    private int pages= 0;
    private int total = 0;
    protected Subscription subscription;

    private ViewHeader mHeaderView;
    private RequestManager mImgLoader;

    /**
     * 列表
     */
    private void search(int page) {

        /**
         * 2.初始化被观察着
         */
        Observable<RetBaseNews> observable = ObservableImp.getNews(page,20);

        /**
         * 3.线程交换
         */
        observable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        /**
         * 4.订阅
         */
        subscription = observable.subscribe(observer);
    }
    ObserverImp<RetBaseNews> observer = new ObserverImp<RetBaseNews>(){
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            mRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onCompleted() {
            mRefreshLayout.onLoadComplete();
            mRefreshLayout.setCanLoadMore();
            mRefreshLayout.setRefreshing(false);
            adapter.setChanged();
            //保存缓存
            AppOperator.runOnThread(new Runnable() {
                @Override
                public void run() {
                    CacheManager.saveObject(getActivity(), arrayList, fileCacheName);
                }
            });
        }

        @Override
        public void onNext(RetBaseNews retBase) {
            super.onNext(retBase);
            if(mRefreshLayout.isRefreshing()){
                arrayList.clear();
            }
            pages = retBase.currentPage;
            total = retBase.totalPage;
            arrayList.addAll(retBase.result.kxnr);
            adapter.setImages(arrayList);
        }
    };


    /**
     * 轮播图
     */
    private void banner() {

        /**
         * 2.初始化被观察着
         */
        Observable<RetBaseBanner> observable = ObservableImp.getBanner(0,5);

        /**
         * 3.线程交换
         */
        observable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        /**
         * 4.订阅
         */
        subscription = observable.subscribe(observer1);
    }
    ObserverImp<RetBaseBanner> observer1 = new ObserverImp<RetBaseBanner>(){

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onCompleted() {

            mHeaderView.initData(getImgLoader(), banners);
            //保存缓存
            AppOperator.runOnThread(new Runnable() {
                @Override
                public void run() {
                    CacheManager.saveObject(getActivity(), banners, bannerCacheName);
                }
            });
        }

        @Override
        public void onNext(RetBaseBanner retBase) {
            super.onNext(retBase);

            //模拟转换
            ArrayList<Banner> news = retBase.result.banner;
            int num = news.size()>=5 ? 5 :news.size();
            for(int i = 0;i<num; i++){
                banners.add(news.get(i));
            }
        }
    };

    /**
     * 初始化控件
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initWidget(view);
        initData();

        return view;
    }

    protected void initWidget(View view) {

        ButterKnife.bind(this, view);

        mRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        mRefreshLayout.setSuperRefreshLayoutListener(this);
        arrayList = new ArrayList<News>();
        adapter = new NewAdapter(getActivity(),arrayList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        banners = new ArrayList<>();
        mHeaderView = new ViewHeader(getActivity());
        mHeaderView.setRefreshLayout(mRefreshLayout);
        mListView.addHeaderView(mHeaderView);
    }

    protected void initData() {

        /**
         * 获取缓存
         */
        AppOperator.runOnThread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<News> arrayList = (ArrayList<News>) CacheManager.readObject(getActivity(), fileCacheName);
                if(arrayList!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        public void run()
                        {
                            adapter.setImages(arrayList);
                            adapter.notifyDataSetChanged();
                        }

                    });
                }
                final ArrayList<Banner> banners = (ArrayList<Banner>) CacheManager.readObject(getActivity(), bannerCacheName);
                if(banners!=null){
                    getActivity().runOnUiThread(new Runnable() {
                        public void run()
                        {
                            mHeaderView.initData(getImgLoader(), banners);
                        }

                    });
                }
            }
        });

        /**
         * 请求
         */
        search(pages);
        banner();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //第一个位置为轮播图  position-1
        News news = adapter.getItem(position-1);

        if(news.type == 0){
            UIHelper.showNewsRedirect(view.getContext(),news,News.NEWSTYPE_NEWS_FREE);
        }else if(news.type == 1){
            UIHelper.showNewsRedirect(view.getContext(),news,News.NEWSTYPE_NEWS_HTML_1);
        }else if(news.type == 2){
            UIHelper.showNewsRedirect(view.getContext(),news,News.NEWSTYPE_NEWS_HTML_2);
        }
    }

    @Override
    public void onRefreshing() {
        //下拉刷新
        mRefreshLayout.setRefreshing(true);
        search(0);
        mRefreshLayout.setNoMoreData();
    }

    @Override
    public void onLoadMore() {
        //上拉加载更多
        pages++;
        if(pages<total) {
            mRefreshLayout.setIsOnLoading(true);
            search(pages);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public synchronized RequestManager getImgLoader() {
        if (mImgLoader == null)
            mImgLoader = Glide.with(this);
        return mImgLoader;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestManager manager = mImgLoader;
        if (manager != null)
            manager.onDestroy();
    }
}
