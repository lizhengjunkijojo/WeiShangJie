package com.lzj.vtm.demo.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;


import com.lzj.vtm.demo.R;

import java.lang.ref.WeakReference;

/**
 * 不同Fragment显示页面
 */
public class SimpleBackActivity extends BaseActivity {

    protected int mPageValue = -1;

    /**
     * 页面
     */
    public final static String BUNDLE_KEY_PAGE = "BUNDLE_KEY_PAGE";
    /**
     * 参数
     */
    public final static String BUNDLE_KEY_ARGS = "BUNDLE_KEY_ARGS";
    /**
     * 页面Tag
     */
    private static final String TAG = "FLAG_TAG";


    protected WeakReference<Fragment> mFragment;

    /**
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        if (mPageValue == -1) {
            mPageValue = getIntent().getIntExtra(BUNDLE_KEY_PAGE, 0);
        }
        initFromIntent(mPageValue, getIntent());
    }

    protected void initFromIntent(int pageValue, Intent data) {
        if (data == null) {
            throw new RuntimeException(
                    "you must provide a page info to display");
        }
        SimpleBackPage page = SimpleBackPage.getPageByValue(pageValue);
        if (page == null) {
            throw new IllegalArgumentException("can not find page by value:" + pageValue);
        }

        setActionBarTitle(page.getTitle());

        try {
            Fragment fragment = (Fragment) page.getClz().newInstance();

            Bundle args = data.getBundleExtra(BUNDLE_KEY_ARGS);
            if (args != null) {
                fragment.setArguments(args);//传递参数
            }

            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.container, fragment, TAG);
            trans.commitAllowingStateLoss();

            mFragment = new WeakReference<Fragment>(fragment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    "generate fragment error. by value:" + pageValue);
        }
    }

    /**
     *
     */
    @Override
    public void onBackPressed() {
        if (mFragment != null && mFragment.get() != null
                && mFragment.get() instanceof BaseFragment) {
            BaseFragment bf = (BaseFragment) mFragment.get();
            if (!bf.onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.ACTION_DOWN
                && mFragment.get() instanceof BaseFragment) {
            ((BaseFragment) mFragment.get()).onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
    }

    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {}

    @Override
    public void initView() {}

    @Override
    public void initData() {}

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
