package com.lzj.vtm.demo.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.indicator.CirclePagerIndicator;
import com.lzj.vtm.demo.widget.SuperRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *轮播图
 *
 使用:
 private ViewHeader mHeaderView;

 mHeaderView = new ViewHeader(getActivity());

 mHeaderView.initData(getImgLoader(), resultBean.getResult().getItems());

 mHeaderView.setRefreshLayout(mRefreshLayout);

 mListView.addHeaderView(mHeaderView);
 *
 */
public class ViewHeader2 extends RelativeLayout implements ViewPager.OnPageChangeListener {
    private ViewPager vp_news;
    private List<ViewBanner> banners = new ArrayList<>();
    private NewsPagerAdapter adapter;
    private View refreshLayout; //父控件,设置焦点
    private CirclePagerIndicator indicator;
    private TextView tv_news_title;
    private ScheduledExecutorService mSchedule;
    private int mCurrentItem = 0;
    private Handler handler;
    private boolean isMoving = false;
    private boolean isScroll = false;


    public ViewHeader2(Context context) {
        super(context);
        init(context);
    }

    public ViewHeader2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setRefreshLayout(View refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_banner_viewpager, this, true);
        vp_news = (ViewPager) findViewById(R.id.vp_news);
        indicator = (CirclePagerIndicator) findViewById(R.id.indicator);
        tv_news_title = (TextView) findViewById(R.id.tv_title);
        adapter = new NewsPagerAdapter();
        vp_news.setAdapter(adapter);
        indicator.bindViewPager(vp_news);
        new SmoothScroller(getContext()).bingViewPager(vp_news);
        vp_news.addOnPageChangeListener(this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                vp_news.setCurrentItem(mCurrentItem);
            }
        };
        mSchedule = Executors.newSingleThreadScheduledExecutor();
        mSchedule.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                if (!isMoving && !isScroll) {
                    mCurrentItem = (mCurrentItem + 1) % banners.size();
                    handler.obtainMessage().sendToTarget();
                }
            }
        }, 2, 4, TimeUnit.SECONDS);

        vp_news.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        isMoving = false;
                        refreshLayout.setEnabled(true);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        isMoving = false;
                        refreshLayout.setEnabled(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        refreshLayout.setEnabled(false);
                        isMoving = true;
                        break;
                }
                return false;
            }
        });
    }

    public void initData(RequestManager manager, List<Banner> banners) {
        this.banners.clear();
        for (Banner banner : banners) {
            ViewBanner newsBanner = new ViewBanner(getContext());
            newsBanner.initData(manager, banner);
            this.banners.add(newsBanner);
        }
        adapter.notifyDataSetChanged();
        indicator.notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        isMoving = mCurrentItem != position;
    }

    @Override
    public void onPageSelected(int position) {
        isMoving = false;
        mCurrentItem = position;
        refreshLayout.setEnabled(true);
        isScroll = false;
        tv_news_title.setText(banners.get(position).getTitle());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        isMoving = state != ViewPager.SCROLL_STATE_IDLE;
        isScroll = state != ViewPager.SCROLL_STATE_IDLE;
        refreshLayout.setEnabled(state == ViewPager.SCROLL_STATE_IDLE);
    }


    private class NewsPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return banners.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(banners.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(banners.get(position));
            return banners.get(position);
        }
    }
}
