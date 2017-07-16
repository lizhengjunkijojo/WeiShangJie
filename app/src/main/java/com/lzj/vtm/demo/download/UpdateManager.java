package com.lzj.vtm.demo.download;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.lzj.vtm.demo.AppConfig;
import com.lzj.vtm.demo.AppContext;
import com.lzj.vtm.demo.base.UIHelper;
import com.lzj.vtm.demo.api.ObservableImp;
import com.lzj.vtm.demo.api.ObserverImp;
import com.lzj.vtm.demo.utils.DialogHelp;
import com.lzj.vtm.demo.utils.TDevice;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 更新管理类
 */
public class UpdateManager {

    private Update mUpdate;

    private Context mContext;

    private boolean isShow = false;

    private ProgressDialog _waitDialog;

    public UpdateManager(Context context, boolean isShow) {
        this.mContext = context;
        this.isShow = isShow;
    }

    /**
     * 轮播图
     */
    private void getApp() {

        /**
         * 2.初始化被观察着
         */
        Observable<Update> observable = ObservableImp.getApp(AppConfig.APP_PACKAGE_DEVTYPE);
        /**
         * 3.线程交换
         */
        observable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        /**
         * 4.订阅
         */
        observable.subscribe(observer);
    }
    ObserverImp<Update> observer = new ObserverImp<Update>(){

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            hideCheckDialog();
            if (isShow) {
                showFaileDialog();
            }
        }

        @Override
        public void onCompleted() {
            if(mUpdate!=null){
                if(mUpdate.code == 200){
                    onFinshCheck();
                }else{
                    if (isShow) {
                        DialogHelp.getMessageDialog(mContext, mUpdate.msg).show();
                    }
                }
            }
        }

        @Override
        public void onNext(Update update) {
            super.onNext(update);
            hideCheckDialog();
            mUpdate = update;
        }
    };


    public void checkUpdate() {
        if (isShow) {
            showCheckDialog();
        }
        //TODO 访问接口
        getApp();
    }

    private void onFinshCheck() {
        if (haveNew()) {
            showUpdateInfo();
        } else {
            if (isShow) {
                showLatestDialog();
            }
        }
    }

    public boolean haveNew() {
        if (this.mUpdate == null) {
            return false;
        }
        boolean haveNew = false;
        int curVersionCode = TDevice.getVersionCode(AppContext.getInstance().getPackageName());
        if (curVersionCode < mUpdate.result.latestAppInfo.versionCode) {
            haveNew = true;
        }
        return haveNew;
    }

    private void showUpdateInfo() {
        if (mUpdate == null) {
            return;
        }
        AlertDialog.Builder dialog = DialogHelp.getConfirmDialog(mContext, mUpdate.result.latestAppInfo.updateLog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {//开启DownloadService下载服务
                UIHelper.openDownLoadService(mContext, mUpdate.result.latestAppInfo.downloadUrl, mUpdate.result.latestAppInfo.versionName);
            }
        });
        dialog.setTitle("发现新版本");
        dialog.show();
    }

    private void showCheckDialog() {
        if (_waitDialog == null) {
            _waitDialog = DialogHelp.getWaitDialog((Activity) mContext, "正在获取新版本信息...");
        }
        _waitDialog.show();
    }

    private void hideCheckDialog() {
        if (_waitDialog != null) {
            _waitDialog.dismiss();
        }
    }

    private void showLatestDialog() {
        DialogHelp.getMessageDialog(mContext, "已经是新版本了").show();
    }

    private void showFaileDialog() {
        DialogHelp.getMessageDialog(mContext, "网络异常，无法获取新版本信息").show();
    }
}
