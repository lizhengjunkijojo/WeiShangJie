package com.lzj.vtm.demo.fun.juhe.news;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.cache.CacheManager;
import com.lzj.vtm.demo.fun.juhe.news.adapter.NewsListAdapter;
import com.lzj.vtm.demo.fun.juhe.news.api.ObservableImp;
import com.lzj.vtm.demo.fun.juhe.news.api.ObserverImp;
import com.lzj.vtm.demo.fun.juhe.news.model.News;
import com.lzj.vtm.demo.fun.juhe.news.model.RetBase;
import com.lzj.vtm.demo.fun.recycleswpie.base.RefreshFootAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.lzj.vtm.demo.app.AppOperator;

/**
 *
 */
public class NewsFragment extends Fragment{

    private int pages=1;
    private final int TOP_REFRESH = 1;
    private final int BOTTOM_REFRESH = 2;

    private String def = "top";
    private int lastVisibleItem;
    protected Subscription subscription;
    private ArrayList<News> arrayList;

    public String fileCacheName = getClass().getName();

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.gridRv)
    RecyclerView gridRv;
    @Bind(R.id.jh_news)
    LinearLayout jh_news;
    private NewsListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @OnCheckedChanged({R.id.searchRb1, R.id.searchRb2, R.id.searchRb3, R.id.searchRb4})
    void onTagChecked(RadioButton searchRb, boolean checked) {
        if (checked) {
            unsubscribe();
            arrayList.clear();
            adapter.setImages(null);
            swipeRefreshLayout.setRefreshing(true);
            def = searchRb.getText().toString().trim();
            search(0);
        }
    }

    /**
     * 1.初始化观察者
     */
    ObserverImp<RetBase> observer = new ObserverImp<RetBase>(){
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCompleted() {
            swipeRefreshLayout.setRefreshing(false);
            adapter.changeMoreStatus(RefreshFootAdapter.PULLUP_LOAD_MORE);
        }

        @Override
        public void onNext(RetBase retBase) {
            super.onNext(retBase);
            arrayList.addAll(retBase.result.data);
            adapter.setImages(arrayList);
            //保存缓存
            AppOperator.runOnThread(new Runnable() {
                @Override
                public void run() {
                    CacheManager.saveObject(getActivity(), arrayList, fileCacheName);
                }
            });
        }
    };

    private void search(int page) {

        /**
         * 2.初始化被观察着
         */
        Observable<RetBase> observable = ObservableImp.getObservable(def,"1f10c76363a964ed0826726fab95d3dd");

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.juhe_news_fragment, container, false);
        ButterKnife.bind(this, view);

        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        gridRv.setLayoutManager(linearLayoutManager);

        arrayList=new ArrayList<News>();
        adapter = new NewsListAdapter();
        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(adapter);
        slideAdapter.setFirstOnly(true);
        slideAdapter.setDuration(500);
        slideAdapter.setInterpolator(new AnticipateInterpolator(1f));
        gridRv.setAdapter(slideAdapter);

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
            }
        });


        dataOption(TOP_REFRESH);

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
        gridRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount() && lastVisibleItem != 0) {
                    adapter.changeMoreStatus(RefreshFootAdapter.LOADING_MORE);
                    dataOption(BOTTOM_REFRESH);
                }
            }
        });


        //TODO 测试
        jh_news.setVisibility(View.GONE);
        return view;
    }


    private void dataOption(int option){
        switch (option) {
            case TOP_REFRESH:
                //下拉刷新
                arrayList.clear();
                search(1);
                break;
            case BOTTOM_REFRESH:
                //上拉加载更多
                pages++;
                search(pages);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
        gridRv.clearOnScrollListeners();
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
