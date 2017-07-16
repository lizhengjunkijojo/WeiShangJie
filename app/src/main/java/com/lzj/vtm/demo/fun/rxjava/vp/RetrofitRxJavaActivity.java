package com.lzj.vtm.demo.fun.rxjava.vp;

import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.fun.rxjava.use.ZhuangbiFragment;
import com.lzj.vtm.demo.fun.rxjava.use2.Zhuangbi2Fragment;
import com.lzj.vtm.demo.fun.rxjava.use1.Zhuangbi1Fragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * RetrofitRxJava主页面
 *
 * AppCompatActivity中viewpager的使用
 */
public class RetrofitRxJavaActivity extends AppCompatActivity {

    @Bind(android.R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.toolBar)
    Toolbar toolBar;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrofit_rxjava_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolBar);

        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {

            @Override
            public int getCount() {
                return 6;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new ZhuangbiFragment();
                    case 1:
                        return new Zhuangbi1Fragment();
                    case 2:
                        return new Zhuangbi2Fragment();
                    case 3:
                        return new ZhuangbiFragment();
                    case 4:
                        return new Zhuangbi1Fragment();
                    case 5:
                        return new Zhuangbi2Fragment();
                    default:
                        return new ZhuangbiFragment();
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.title_elementary);
                    case 1:
                        return getString(R.string.title_map);
                    case 2:
                        return getString(R.string.title_zip);
                    case 3:
                        return getString(R.string.title_token);
                    case 4:
                        return getString(R.string.title_token_advanced);
                    case 5:
                        return getString(R.string.title_cache);
                    default:
                        return getString(R.string.title_elementary);
                }
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }
}
