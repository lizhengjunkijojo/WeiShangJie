package com.lzj.vtm.demo.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.base.BaseFragment;
import com.lzj.vtm.demo.utils.TDevice;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * 系统设置界面
 */
public class SettingsFragment extends BaseFragment {

    @Bind(R.id.tv_versionCode)
    TextView mTvVersion;
    @Bind(R.id.tv_cache_size)
    TextView mTvCacheSize;
    @Bind(R.id.setting_logout)
    TextView mTvExit;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container,false);
        ButterKnife.bind(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {

        view.findViewById(R.id.rl_loading_img).setOnClickListener(this);
        view.findViewById(R.id.rl_clean_cache).setOnClickListener(this);
        view.findViewById(R.id.rl_double_click_exit).setOnClickListener(this);
        view.findViewById(R.id.rl_about).setOnClickListener(this);
        view.findViewById(R.id.rl_exit).setOnClickListener(this);

        mTvVersion.setText(TDevice.getVersionName());
    }

    @Override
    public void initData() {
        caculateCacheSize();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.rl_clean_cache:
                onClickCleanCache();//清楚缓存
                break;
            case R.id.rl_about:
                onClickUpdate();//版本更新
                break;
            case R.id.rl_exit:
                onClickExit();//退出
                break;
            default:
                break;
        }
    }

    private void onClickUpdate() {
        //new UpdateManager(getActivity(), true).checkUpdate();
    }

    private void onClickCleanCache() {

    }

    /**
     * 退出
     */
    private void onClickExit() {

    }

    /**
     * 计算缓存的大小
     */
    private void caculateCacheSize() {

    }

}
