package com.lzj.vtm.demo.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lzj.vtm.demo.R;

/**
 * 登录用户中心页面：个人中心
 */
public class ExploreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        initWidget(view);

        return view;
    }

    private void initWidget(View view) {


    }

}
