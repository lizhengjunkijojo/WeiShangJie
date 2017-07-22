package com.lzj.vtm.demo.fun.rxjava.vp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.home.news.Home2Fragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * RetrofitRxJava主页面
 *
 * AppCompatActivity中viewpager的使用
 */
public class RetrofitRxJavaFragment extends Fragment {

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.retrofit_rxjava_fragment, container, false);
        ButterKnife.bind(this, view);

        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new Home2Fragment();
                    case 1:
                        return new Home2Fragment();
                    case 2:
                        return new Home2Fragment();
                    default:
                        return new Home2Fragment();
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
                    default:
                        return getString(R.string.title_elementary);
                }
            }
        });

        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
