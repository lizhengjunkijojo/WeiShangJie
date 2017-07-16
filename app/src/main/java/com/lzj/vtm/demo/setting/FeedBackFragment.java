package com.lzj.vtm.demo.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.lzj.vtm.demo.AppContext;
import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.base.BaseFragment;
import com.lzj.vtm.demo.utils.TDevice;


import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * 用户意见反馈
 */
public class FeedBackFragment extends BaseFragment{

    private String Content;

    @Bind(R.id.et_feedback)
    EditText mEtContent;
    @Bind(R.id.btn_send_feedback)
    Button mBtnSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_feedback, null);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(View view) {
        mBtnSend.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_feedback:
                sendFeedbcak();
                break;
            default:
                break;
        }
    }

    private void sendFeedbcak() {

        Content = mEtContent.getText().toString();

        if (!TDevice.hasInternet()) {
            AppContext.showToastShort("当前没有可用的网络链接");
            return;
        }
        if (Content.length() == 0) {
            AppContext.showToastShort("请输入反馈内容");
            return;
        }

        AppContext.showToastShort("尽情期待");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
