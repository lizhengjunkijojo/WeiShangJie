package com.lzj.vtm.demo.blvs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.lzj.vtm.demo.R;

public class PolyvMainActivity extends Activity implements OnClickListener {

    // 在线视频按钮,上传按钮,缓存按钮
    private TextView iv_online;
    private PolyvPermission polyvPermission = null;
    private static final int SETTING = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polyv_activity_main);

        iv_online = (TextView) findViewById(R.id.iv_online);
        iv_online.setOnClickListener(this);

        polyvPermission = new PolyvPermission();
        polyvPermission.setResponseCallback(new PolyvPermission.ResponseCallback() {
            @Override
            public void callback(@NonNull PolyvPermission.OperationType type) {
                requestPermissionWriteSettings(type.getNum());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_online:
                polyvPermission.applyPermission(this, PolyvPermission.OperationType.play);
                break;
        }
    }

    private void gotoActivity(int type) {
        PolyvPermission.OperationType OperationType = PolyvPermission.OperationType.getOperationType(type);
        switch (OperationType) {
            case play:
                startActivity(new Intent(PolyvMainActivity.this, PolyvOnlineVideoActivity.class));
                break;
        }
    }

    private int actionType;

    /**
     * 请求写入设置的权限
     */
    @SuppressLint("InlinedApi")
    private void requestPermissionWriteSettings(int type) {
        if (!PolyvPermission.canMakeSmores()) {
            gotoActivity(type);
        } else if (Settings.System.canWrite(this)) {
            gotoActivity(type);
        } else {
            actionType = type;
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + this.getPackageName()));
            startActivityForResult(intent, SETTING);
        }
    }

    @Override
    @SuppressLint("InlinedApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTING) {
            if (Settings.System.canWrite(this)) {
                gotoActivity(actionType);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("showPermissionInternet")
                        .setMessage(Settings.ACTION_MANAGE_WRITE_SETTINGS + " not granted")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (polyvPermission.operationHasPermission(requestCode)) {
            requestPermissionWriteSettings(requestCode);
        } else {
            polyvPermission.makePostRequestSnack();
        }
    }
}
