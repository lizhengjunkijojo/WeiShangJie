package com.lzj.vtm.demo.setting;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.lzj.vtm.demo.AppContext;
import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.base.BaseFragment;
import com.lzj.vtm.demo.utils.DialogHelp;
import com.lzj.vtm.demo.utils.TDevice;
import com.lzj.vtm.demo.base.UIHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 关于我们
 */
public class AboutFragment extends BaseFragment {


    @Bind(R.id.about_url)
    public TextView about_url;
    @Bind(R.id.about_phone_1)
    public TextView about_phone_1;
    @Bind(R.id.about_phone_2)
    public TextView about_phone_2;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {

        TextView tv = (TextView) view.findViewById(R.id.about_version);
        tv.setText("当前版本:" + TDevice.getVersionName());
    }

    @Override
    public void initData() {

        about_url.setOnClickListener(this);
        //about_phone_1.setOnClickListener(this);
        //about_phone_2.setOnClickListener(this);
    }

    private AlertDialog dialog;

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.about_phone_1:
                dialog = DialogHelp.getConfirmDialog(getActivity(),
                        getResources().getString(R.string.about_phone_1_hint),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Uri uri = Uri.parse("tel:13552756390");
                                Intent call = new Intent(Intent.ACTION_CALL, uri);
                                if (ActivityCompat.checkSelfPermission(AppContext.getInstance(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                AppContext.getInstance().startActivity(call);
                            dialog.dismiss();
                        }
                    }).show();
            break;
            case R.id.about_phone_2:
                dialog = DialogHelp.getConfirmDialog(getActivity(),
                        getResources().getString(R.string.about_phone_2_hint),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Uri uri = Uri.parse("tel:13552756390");
                                Intent call = new Intent(Intent.ACTION_CALL, uri);
                                if (ActivityCompat.checkSelfPermission(AppContext.getInstance(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                AppContext.getInstance().startActivity(call);
                                dialog.dismiss();
                            }
                        }).show();
                break;

            case R.id.about_url:
                UIHelper.openBrowser(getActivity(),getResources().getString(R.string.about_url));
                break;
            default:
                break;
        }
    }

}
