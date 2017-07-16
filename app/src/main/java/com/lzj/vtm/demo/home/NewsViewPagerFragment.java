package com.lzj.vtm.demo.home;

import android.view.View;

import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.base.BaseViewPagerFragment;
import com.lzj.vtm.demo.base.ViewPageFragmentAdapter;
import com.lzj.vtm.demo.fun.juhe.news.NewsFragment;
import com.lzj.vtm.demo.fun.juhe.pcd.PFragment;
import com.lzj.vtm.demo.fun.rxjava.use1.Zhuangbi1Fragment;

/**
 * 信息中心
 */
public class NewsViewPagerFragment extends BaseViewPagerFragment{

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {

        String[] title = getResources().getStringArray(
                R.array.news_viewpage_arrays);

        // 免费信息
        adapter.addTab(title[0], "news_free", Home2Fragment.class,null);

        // 项目信息
        adapter.addTab(title[1], "news_project",NewsFragment.class,null);

        // 项目信息
        adapter.addTab(title[2], "news_first", HomeFragment.class,null);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void setScreenPageLimit() {
        mViewPager.setOffscreenPageLimit(3);
    }

}
