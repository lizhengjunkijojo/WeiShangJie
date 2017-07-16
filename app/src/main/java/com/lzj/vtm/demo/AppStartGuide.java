package com.lzj.vtm.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

/**
 * 引导页面
 */
public class AppStartGuide extends AppCompatActivity {

    private ViewPager mGuideViewPager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_start_guide);

        init();
    }

    //初始化页面数据
    @SuppressLint("InflateParams")
    private void init() {

        View view1 = getLayoutInflater().inflate(R.layout.app_start_guide_view1, null);
        View view2 = getLayoutInflater().inflate(R.layout.app_start_guide_view2, null);
        View view3 = getLayoutInflater().inflate(R.layout.app_start_guide_view3, null);
        View view4 = getLayoutInflater().inflate(R.layout.app_start_guide_view4, null);

        ArrayList<View> viewList = new ArrayList<View>();
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);

        mGuideViewPager = (ViewPager) findViewById(R.id.guideViewPager);
        mGuideViewPager.setAdapter(new ViewPageAdapter(viewList));
        mGuideViewPager.setOnPageChangeListener(new MyViewChangeListener());

        Button goButton = (Button) view4.findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intentMain = new Intent(AppStartGuide.this, MainActivity.class);
                startActivity(intentMain);
                finish();
            }
        });
    }

    //页面滑动
    private class MyViewChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {
            mGuideViewPager.setCurrentItem(index);
        }
    }

    //适配器
    private class ViewPageAdapter extends PagerAdapter {

        private ArrayList<View> mPageViews = null;

        public ViewPageAdapter(ArrayList<View> views) {
            mPageViews = views;
        }

        @Override
        public int getCount() {
            int size = 0;
            if (mPageViews != null && mPageViews.size() > 0) {
                size = mPageViews.size();
            }
            return size;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mPageViews.get(arg1));
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mPageViews.get(arg1));
            return mPageViews.get(arg1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
