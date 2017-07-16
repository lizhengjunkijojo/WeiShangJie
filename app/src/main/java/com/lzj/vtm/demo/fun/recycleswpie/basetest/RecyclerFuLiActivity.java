package com.lzj.vtm.demo.fun.recycleswpie.basetest;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.animation.AnticipateInterpolator;

import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.fun.recycleswpie.base.RecycleViewDivider;
import com.lzj.vtm.demo.fun.recycleswpie.base.RefreshFootAdapter;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * SwipeRefreshLayout+RecyclerView实现下拉刷新/上拉加载
 */
public class RecyclerFuLiActivity extends AppCompatActivity {

    private int pages=1;

    private final int TOP_REFRESH = 1;
    private final int BOTTOM_REFRESH = 2;

    private ArrayList<FuLiInfo.Info> arrayList;

    private int lastVisibleItem;
    private SwipeRefreshLayout swiperefreshlayout;
    private RecyclerView recycler;
    private RefreshFuLiAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycler_footview_layout);
        swiperefreshlayout = (SwipeRefreshLayout) this.findViewById(R.id.demo_swiperefreshlayout);
        recycler = (RecyclerView) this.findViewById(R.id.demo_recycler);

        //设置刷新时动画的颜色，可以设置4个
        swiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swiperefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycler.setLayoutManager(linearLayoutManager);

        //添加分隔线
        recycler.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));
        adapter = new RefreshFuLiAdapter(this);

        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(adapter);
        slideAdapter.setFirstOnly(true);
        slideAdapter.setDuration(500);
        slideAdapter.setInterpolator(new AnticipateInterpolator(1f));

        recycler.setAdapter(slideAdapter);

        arrayList=new ArrayList();
        dataOption(TOP_REFRESH);

        /**
         * 下拉刷新处理
         * swiperefreshlayout刷新监听
         */
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataOption(TOP_REFRESH);
            }
        });

        /**
         * 上拉加载处理
         * RecyclerView滑动监听
         */
        recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
    }

    private void initData(int page) {
        FuNetwork.getFuliApi()
                .getData(20,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    Observer<FuLiInfo> observer = new Observer<FuLiInfo>(){

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(FuLiInfo images) {
            adapter.addItem(images.results);
        }

        @Override
        public void onCompleted() {
            swiperefreshlayout.setRefreshing(false);
            adapter.changeMoreStatus(RefreshFootAdapter.PULLUP_LOAD_MORE);
        }
    };

    private void dataOption(int option){
        switch (option) {
            case TOP_REFRESH:
                //下拉刷新
                arrayList.clear();
                initData(1);
                break;
            case BOTTOM_REFRESH:
                //上拉加载更多
                pages++;
                initData(pages);
                break;
        }
    }
}
