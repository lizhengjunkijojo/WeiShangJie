package com.lzj.vtm.demo.home;

import android.view.View;

import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.base.BaseViewPagerFragment;
import com.lzj.vtm.demo.base.ViewPageFragmentAdapter;
import com.lzj.vtm.demo.fun.juhe.news.NewsFragment;
import com.lzj.vtm.demo.home.episode.EpisodeFragment;
import com.lzj.vtm.demo.home.news.Home2Fragment;
import com.lzj.vtm.demo.home.news.HomeFragment;
import com.lzj.vtm.demo.home.video.PolyvVideoFragment;

/**
 * 信息中心
 */
public class NewsVPFragment extends BaseViewPagerFragment{

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {

        String[] title = getResources().getStringArray(
                R.array.news_viewpage_arrays);

        // 免费信息
        adapter.addTab(title[0], "news_free", Home2Fragment.class,null);

        // 项目信息
        adapter.addTab(title[1], "news_project",PolyvVideoFragment.class,null);

        // 项目信息
        adapter.addTab(title[2], "news_first", EpisodeFragment.class,null);

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
