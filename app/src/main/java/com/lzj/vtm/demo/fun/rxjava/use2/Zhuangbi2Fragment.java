package com.lzj.vtm.demo.fun.rxjava.use2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.fun.rxjava.adapter.ZhuangbiListAdapter;
import com.lzj.vtm.demo.fun.rxjava.use2.repository.NewsRepo;
import com.lzj.vtm.demo.fun.rxjava.use2.retrofit.base.ObserverImp;
import com.lzj.vtm.demo.fun.rxjava.base.ZbBaseFragment;
import com.lzj.vtm.demo.fun.rxjava.model.ZhuangbiImage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

/**
 * RecyclerView的使用
 *
 * SwipeRefreshLayout的使用
 *
 * repository+retrofit使用
 */
public class Zhuangbi2Fragment extends ZbBaseFragment{

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.gridRv)
    RecyclerView gridRv;

    ZhuangbiListAdapter adapter = new ZhuangbiListAdapter();
    ObserverImp<List<ZhuangbiImage>> observer = new ObserverImp<List<ZhuangbiImage>>(){
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<ZhuangbiImage> zhuangbiImages) {
            super.onNext(zhuangbiImages);
            swipeRefreshLayout.setRefreshing(false);
            adapter.setImages(zhuangbiImages);
        }
    };

    @OnCheckedChanged({R.id.searchRb1, R.id.searchRb2, R.id.searchRb3, R.id.searchRb4})
    void onTagChecked(RadioButton searchRb, boolean checked) {
        if (checked) {
            unsubscribe();
            adapter.setImages(null);
            swipeRefreshLayout.setRefreshing(true);
            search(searchRb.getText().toString());
        }
    }

    private void search(String key) {
        /**
         * 新闻调用
         */
        subscription = NewsRepo.getIns().getNewsList(key).subscribe(observer);
        /**
         * 视频调用
         */
        //subscription = VideoRepo.getIns().getVideoList().subscribe(observer);
        /**
         * 图片调用
         */
        //subscription = ImageRepo.getIns().getImageList().subscribe(observer);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.retrofit_rejava_fragment, container, false);
        ButterKnife.bind(this, view);

        gridRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);

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
}
