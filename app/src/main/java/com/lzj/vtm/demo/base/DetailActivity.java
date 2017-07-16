package com.lzj.vtm.demo.base;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.details.BannerDetailFragment;
import com.lzj.vtm.demo.details.NewsDetailFragment;

/**
 * 详情Fragment显示页
 */
public class DetailActivity extends BaseActivity {

    /**
     * 不同详情页面参数
     */
    public static final int DISPLAY_NEWS_FREE = 0;
    public static final int DISPLAY_BANNER = 1;

    /**
     * key
     */
    public static final String BUNDLE_KEY_DISPLAY_TYPE = "BUNDLE_KEY_DISPLAY_TYPE";

    /**
     * 父类复写
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.actionbar_title_detail;
    }

    /**
     * 不同fragment操作
     * @param savedInstanceState
     */
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        int displayType = getIntent().getIntExtra(BUNDLE_KEY_DISPLAY_TYPE, DISPLAY_NEWS_FREE);
        BaseFragment fragment = null;
        int actionBarTitle = 0;
        switch (displayType) {
            case DISPLAY_NEWS_FREE:
                actionBarTitle = R.string.actionbar_title_news;
                fragment = new NewsDetailFragment();
                break;
            case DISPLAY_BANNER:
                actionBarTitle = R.string.actionbar_title_news;
                fragment = new BannerDetailFragment();
                break;
            default:
                break;
        }
        setActionBarTitle(actionBarTitle);
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.container, fragment);
        trans.commitAllowingStateLoss();
    }

    /**
     *
     */
    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
