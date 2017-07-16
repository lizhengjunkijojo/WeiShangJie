package com.lzj.vtm.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.lzj.vtm.demo.banner.Banner;
import com.lzj.vtm.demo.base.SimpleBackPage;
import com.lzj.vtm.demo.base.UIHelper;
import com.lzj.vtm.demo.download.UpdateManager;
import com.lzj.vtm.demo.home.NewsViewPagerFragment;
import com.lzj.vtm.demo.home.Home2Fragment;
import com.lzj.vtm.demo.setting.AboutFragment;
import com.lzj.vtm.demo.setting.FeedBackFragment;
import com.lzj.vtm.demo.setting.SettingsFragment;
import com.lzj.vtm.demo.widget.DoubleClickExitHelper;

/**
 * 主页面
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DoubleClickExitHelper mDoubleClickExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * TODO 标题栏
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * TODO 底部悬浮按钮
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showSimpleBack(MainActivity.this, SimpleBackPage.FEEDBACK);
                /**Snackbar信息提示
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                 */
            }
        });

        /**
         * TODO 顶部侧滑与标题栏关联
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /**
         * TODO 侧滑菜单
         */
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**
         * TODO 有无Tab栏的布局,同时需要修改布局
         */
        init();  //有Tab选择,activity_main_content
        //init1();   //无Tab选择,activity_main_content1

        /**
         * TODO 页面栈管理
         */
        AppManager.getAppManager().addActivity(this);//Activity页面管理器

        /**
         * TODO 检查版本更新
         */
        checkUpdate();
        if (AppContext.isAppUpgrade()) {
            AppContext.setAppUpgrade(false);
        }

        /**
         * TODO 双击退出
         */
        mDoubleClickExit = new DoubleClickExitHelper(this);


        /**
         * TODO 接收广告,开启广告页面
         */
        adInfo();
    }

    /**
     * 开启广告fragment页面
     */
    private void adInfo() {

        Banner banner = (Banner) getIntent().getSerializableExtra("banner");
        if(banner!=null){
            UIHelper.openBrowser(MainActivity.this, banner.html);
        }
    }

    /**
     * 初始化主页面单页面
     */
    private void init1() {
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.real_tab_content, new Home2Fragment());
        trans.commitAllowingStateLoss();
    }

    /**
     * 初始化主页面Tab栏信息
     */
    private final String mTabSpec[] = {"head","heart","user"};
    private final String mIndicator[] = {"首页", "喜欢", "我的"};
    private final Class mFragmentsClass[] = {
            NewsViewPagerFragment.class,
            FeedBackFragment.class,
            SettingsFragment.class};
    private final int mTabImage[] = {
            R.drawable.tab_home_item,
            R.drawable.tab_heart_item,
            R.drawable.tab_user_item};
    private  FragmentTabHost tabHost;
    private void init() {
        tabHost = (FragmentTabHost) findViewById(R.id.tab_host);
        if (tabHost == null) {
            return;
        }
        tabHost.setup(this, getSupportFragmentManager(), R.id.real_tab_content);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            tabHost.getTabWidget().setShowDividers(0);
        }
        for (int i = 0; i < mTabSpec.length; i++) {
            TabHost.TabSpec tab = tabHost.newTabSpec(mTabSpec[i]).setIndicator(getTabView(i));
            tab.setContent(new TabHost.TabContentFactory() {

                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });//设置tab内容
            tabHost.addTab(tab,mFragmentsClass[i], null);
        }
    }
    private View getTabView(int index) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_main_content_tab_item, null);
        ImageView image = (ImageView) view.findViewById(R.id.tab_image);
        TextView text = (TextView) view.findViewById(R.id.tab_title);
        image.setImageResource(mTabImage[index]);
        text.setText(mIndicator[index]);
        return view;
    }

    /**
     * 返回按钮,侧滑控制
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 双击退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                return mDoubleClickExit.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 菜单栏设置与点击事件处理
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            UIHelper.showSimpleBack(this, SimpleBackPage.SETTING);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 侧滑菜单点击事件处理
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        UIHelper.showSimpleBack(this, SimpleBackPage.ABOUT);

        int id = item.getItemId();

        /**
        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, RecyclerFootActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, RecyclerFuLiActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this, SwipeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        /**
         * 侧滑关闭
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * 5. 软件更新
     */
    private void checkUpdate() {
        if (!AppContext.get(AppConfig.KEY_CHECK_UPDATE, true)) {
            return;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                new UpdateManager(MainActivity.this, false).checkUpdate();
            }
        }, 2000);
    }
}
