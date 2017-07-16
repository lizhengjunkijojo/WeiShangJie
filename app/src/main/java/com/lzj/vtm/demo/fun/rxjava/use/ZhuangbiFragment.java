package com.lzj.vtm.demo.fun.rxjava.use;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.fun.rxjava.adapter.ZhuangbiListAdapter;
import com.lzj.vtm.demo.fun.rxjava.base.ZbBaseFragment;
import com.lzj.vtm.demo.fun.rxjava.model.ZhuangbiImage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * RecyclerView的使用
 *
 * SwipeRefreshLayout的使用
 *
 * repository+retrofit使用
 */
public class ZhuangbiFragment extends ZbBaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.gridRv)
    RecyclerView gridRv;

    /**
     * 1.初始化观察者
     */
    ZhuangbiListAdapter adapter = new ZhuangbiListAdapter();
    ObserverImp<List<ZhuangbiImage>> observer = new ObserverImp<List<ZhuangbiImage>>(){
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<ZhuangbiImage> images) {
            super.onNext(images);
            swipeRefreshLayout.setRefreshing(false);
            adapter.setImages(images);
        }
    };

    @OnCheckedChanged({R.id.searchRb1, R.id.searchRb2, R.id.searchRb3, R.id.searchRb4})
    void onTagChecked(RadioButton searchRb, boolean checked) {
        if (checked) {
            unsubscribe();
            adapter.setImages(null);
            swipeRefreshLayout.setRefreshing(true);
            search(searchRb.getText().toString(),0);
        }
    }

    private void search(String key,int page) {

        /**
         * 2.初始化被观察着
         */
        Observable<List<ZhuangbiImage>> observable = ObservableImp.getObservable(key);

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
        View view = inflater.inflate(R.layout.retrofit_rejava_fragment, container, false);
        ButterKnife.bind(this, view);

        gridRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(true);

        SlideInBottomAnimationAdapter slideAdapter = new SlideInBottomAnimationAdapter(adapter);
        slideAdapter.setFirstOnly(true);
        slideAdapter.setDuration(500);
        slideAdapter.setInterpolator(new AnticipateInterpolator(1f));

        gridRv.setAdapter(slideAdapter);

        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.retrofit_rejava_dialog_zhuangbi;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_elementary;
    }

    @Override
    public void onRefresh() {

    }
}
