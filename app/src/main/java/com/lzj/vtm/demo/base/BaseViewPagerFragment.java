package com.lzj.vtm.demo.base;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.indicator.PagerSlidingTabStrip;


/**
 * 带有导航条的基类
 */
public abstract class BaseViewPagerFragment extends BaseFragment {

    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_viewpage_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.pager_tabstrip);

        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(), mTabStrip, mViewPager);
        setScreenPageLimit();
        onSetupTabAdapter(mTabsAdapter);
    }
    
    protected void setScreenPageLimit() {}

    protected abstract void onSetupTabAdapter(ViewPageFragmentAdapter adapter);
}