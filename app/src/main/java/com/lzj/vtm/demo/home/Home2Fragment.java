package com.lzj.vtm.demo.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.banner.Banner;
import com.lzj.vtm.demo.banner.RetBaseBanner;
import com.lzj.vtm.demo.base.UIHelper;
import com.lzj.vtm.demo.home.adapter.NewsListAdapter;
import com.lzj.vtm.demo.api.ObservableImp;
import com.lzj.vtm.demo.api.ObserverImp;
import com.lzj.vtm.demo.home.model.News;
import com.lzj.vtm.demo.home.model.RetBaseNews;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 首页
 *
 * 缓存数据与加载数据的处理
 *      数据缓存,清除后,点击后有时间响应间隔
 *      数据缓存,不清除,数据一直增加
 *      出现数据重复的现象
 * adapter第一个数据位置,轮播图,少一条数据的解决
 */
public class Home2Fragment extends Fragment implements AdapterView.OnItemClickListener, NewsListAdapter.MyItemClickListener{

    private int pages=0;
    private int total = 0;
    private final int TOP_REFRESH = 1;
    private final int BOTTOM_REFRESH = 2;

    private int lastVisibleItem;
    protected Subscription subscription;
    private ArrayList<News> arrayList;
    private ArrayList<Banner> banners;

    public String fileCacheName = getClass().getName();
    public String bannerCacheName = fileCacheName+"banner";

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.gridRv)
    RecyclerView recyclerView;
    private NewsListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;


    private RequestManager mImgLoader;


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
            //保存缓存
//            AppOperator.runOnThread(new Runnable() {
//                @Override
//                public void run() {
//                    CacheManager.saveObject(getActivity(), banners, bannerCacheName);
//                }
//            });
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

            adapter.setBanners(banners);
        }
    };

    /**
     * 1.初始化观察者
     */
    ObserverImp<RetBaseNews> observer = new ObserverImp<RetBaseNews>(){
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            adapter.setload_more_msg(true);
            swipeRefreshLayout.setRefreshing(false);

            Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCompleted() {

            adapter.setload_more_msg(true);

            swipeRefreshLayout.setRefreshing(false);
            adapter.changeMoreStatus(NewsListAdapter.PULLUP_LOAD_MORE);

            //保存缓存
//            AppOperator.runOnThread(new Runnable() {
//                @Override
//                public void run() {
//                    CacheManager.saveObject(getActivity(), arrayList, fileCacheName);
//                }
//            });
        }

        @Override
        public void onNext(RetBaseNews retBase) {
            super.onNext(retBase);
            if(swipeRefreshLayout.isRefreshing()){
                arrayList.clear();
            }

            /**
             * TODO 轮播图,第一条数据占位处理
             */
            //if(pages == 0){
            //    arrayList.add(new News());
            //}

            pages = retBase.currentPage;
            total = retBase.totalPage;
            arrayList.addAll(retBase.result.kxnr);
            adapter.setImages(arrayList);
        }
    };

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home2, container, false);

        initView(view);
        initData();

        return view;
    }

    public void initView(View view) {

        ButterKnife.bind(this, view);

        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList=new ArrayList<News>();
        banners=new ArrayList<Banner>();

        adapter = new NewsListAdapter(getActivity(),getImgLoader());
        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(adapter);
        slideAdapter.setFirstOnly(true);
        slideAdapter.setDuration(500);
        slideAdapter.setInterpolator(new AnticipateInterpolator(1f));
        recyclerView.setAdapter(slideAdapter);

        adapter.setOnItemClickListener(this);
    }

    protected void initData() {
        /**
         * 获取缓存
         */
//        AppOperator.runOnThread(new Runnable() {
//            @Override
//            public void run() {
//                final ArrayList<Banner> banners = (ArrayList<Banner>) CacheManager.readObject(getActivity(), bannerCacheName);
//                if(banners!=null){
//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run()
//                        {
//                            adapter.setBanners(banners);
//                            adapter.notifyDataSetChanged();
//                        }
//
//                    });
//                }
//            }
//        });
//        AppOperator.runOnThread(new Runnable() {
//            @Override
//            public void run() {
//                final ArrayList<News> arrayList = (ArrayList<News>) CacheManager.readObject(getActivity(), fileCacheName);
//                if(arrayList!=null){
//                    getActivity().runOnUiThread(new Runnable() {
//                        public void run()
//                        {
//                            /**
//                             * TODO 数据缓存问题,点击时间间隔
//                             */
//                            //Home2Fragment.this.arrayList = arrayList;
//                            adapter.setImages(arrayList);
//                            adapter.notifyDataSetChanged();
//                        }
//
//                    });
//                }
//            }
//        });


        /**
         * 请求
         */
        banner();
        search(pages);

        /**
         * 下拉刷新处理
         * swiperefreshlayout刷新监听
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataOption(TOP_REFRESH);
            }
        });

        /**
         * 上拉加载处理
         * RecyclerView滑动监听
         */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount() && lastVisibleItem != 0) {
                    adapter.changeMoreStatus(NewsListAdapter.LOADING_MORE);
                    dataOption(BOTTOM_REFRESH);
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemClick(View view, int position) {

        if(arrayList.size()>0){
            //第一个位置为轮播图  position-1
            News news = arrayList.get(position);

            if(news.type == 0){
                UIHelper.showNewsRedirect(view.getContext(),news,News.NEWSTYPE_NEWS_FREE);
            }else if(news.type == 1){

                UIHelper.showNewsRedirect(view.getContext(),news,News.NEWSTYPE_NEWS_HTML_1);
            }else if(news.type == 2){

                UIHelper.showNewsRedirect(view.getContext(),news,News.NEWSTYPE_NEWS_HTML_2);
            }
        }
    }

    private void dataOption(int option){
        switch (option) {
            case TOP_REFRESH:
                //下拉刷新
                search(0);
                break;
            case BOTTOM_REFRESH:
                //上拉加载更多
                if(adapter.isLoad_more_msg()){
                    adapter.setload_more_msg(false);
                    pages++;
                    if(pages<total) {
                        search(pages);
                    }else{
                        adapter.changeMoreStatus(NewsListAdapter.LOADING_NO_MORE);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
        recyclerView.clearOnScrollListeners();
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
