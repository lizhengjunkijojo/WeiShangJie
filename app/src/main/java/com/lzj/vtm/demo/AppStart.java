package com.lzj.vtm.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.lzj.vtm.demo.banner.Banner;
import com.lzj.vtm.demo.banner.RetBaseBanner;
import com.lzj.vtm.demo.api.ObservableImp;
import com.lzj.vtm.demo.api.ObserverImp;
import com.lzj.vtm.demo.utils.TDevice;

import org.kymjs.kjframe.utils.SystemTool;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 应用启动界面--设置广告
 */
public class AppStart extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layout = null;//启动布局
    LinearLayout rl_show_ad = null;//广告页
    ImageView iv_ad_img = null;//广告图片
    Button ll_ad_skip_btn = null;//广告按钮

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity aty = AppManager.getActivity(MainActivity.class);//防止第三方跳转时出现双实例
        if (aty != null && !aty.isFinishing()) {
            finish();
        }

        SystemTool.gc(this); //针对性能好的手机使用，加快应用响应速度

        final View view = View.inflate(this, R.layout.app_start, null);
        setContentView(view);

        layout = (LinearLayout) view.findViewById(R.id.action);
        rl_show_ad = (LinearLayout) view.findViewById(R.id.rl_show_ad);
        iv_ad_img = (ImageView) view.findViewById(R.id.iv_ad_img);
        ll_ad_skip_btn = (Button) view.findViewById(R.id.ll_ad_skip_btn);

        /**
         * 设置启动3秒动画
         */
        startAnim(layout);
    }

    /**
     * 动画设置
     */
    private void startAnim(View view) {

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setDuration(2000);
        view.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new AnimationListener() {

            //结束动画
            @Override
            public void onAnimationEnd(Animation arg0) {
                init();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            //开启动画
            @Override
            public void onAnimationStart(Animation animation) {
                banner();
            }
        });
    }

    /**
     * 首页广告位---得到应该的广告信息
     */
    private Banner banner = null;
    private void banner() {
        ObservableImp.getStartImage().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(observer1);
    }
    ObserverImp<RetBaseBanner> observer1 = new ObserverImp<RetBaseBanner>(){
        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onCompleted() {
        }

        @Override
        public void onNext(RetBaseBanner retBase) {
            super.onNext(retBase);

            ArrayList<Banner> news = retBase.result.banner;
            if(retBase.result.banner!=null&&retBase.result.banner.size()>0){
                banner = news.get(0);
            }
        }
    };

    //3秒结束
    private void init() {
        if (TDevice.hasInternet() &&  banner!=null && !TextUtils.isEmpty(banner.pic)) {
            rl_show_ad.setVisibility(View.VISIBLE);
            startImage();
            start3ad();
        } else {
            rl_show_ad.setVisibility(View.GONE);
            startMain();
        }
    }

    //设置图片,设置点击事件
    private void startImage(){
        Glide.with(AppStart.this)
                .load(banner.pic)
                .centerCrop()
                .placeholder(R.color.cardview_light_background)
                .crossFade()
                .into(iv_ad_img);
        rl_show_ad.setOnClickListener(this);
        ll_ad_skip_btn.setOnClickListener(this);
    }

    //3秒自动调整主页定时器
    Timer timer = null;
    private void start3ad(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startMain();
            }
        }, 3000);
    }

    //点击图片进入广告页面,点击跳过进入主页
    @Override
    public void onClick(View v) {
        timer.cancel();
        switch (v.getId()){
            case  R.id.rl_show_ad:
                //点击广告处理
                startMainAD();
                break;
            case R.id.ll_ad_skip_btn:
                //点击跳过处理
                startMain();
                break;
        }
    }

    //开启主页--直接调整
    private void startMain(){
        Intent intent = new Intent();
        intent.setClass(AppStart.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //开启主页--开启广告页面
    private void startMainAD(){
        Intent intent = new Intent();
        intent.setClass(AppStart.this, MainActivity.class);
        intent.putExtra("banner",banner);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
