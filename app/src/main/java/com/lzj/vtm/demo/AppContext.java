package com.lzj.vtm.demo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.PolyvDevMountInfo;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.PolyvSDKUtil;
import com.google.gson.Gson;
import com.lzj.vtm.demo.base.BaseApplication;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;
import java.util.Set;

public class AppContext extends BaseApplication {


    public static final String TAG = AppContext.class.getSimpleName();

    private static String PREF_NAME = "wsj.pref";

    public static final int PAGE_SIZE = 20;// 默认分页大小

    private static AppContext instance;

    private static boolean sIsAtLeastGB;

    private static String lastToast = "";//toast显示文字
    private static long lastToastTime;//toast最后显示时间点

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sIsAtLeastGB = true;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // 创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);

        initPolyvCilent();

        init();
        initLogin();
    }

    private void init() {

    }

    private void initLogin() {

    }

    /**
     * 获得当前app运行的AppContext
     */
    public static AppContext getInstance() {
        return instance;
    }


    /**
     * Gson
     * @return
     */
    public static Gson createGson() {
        com.google.gson.GsonBuilder gsonBuilder = new com.google.gson.GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        return gsonBuilder.create();
    }


    /**
     *
     */
    public static void set(String key, boolean value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        apply(editor);
    }

    public static void set(String key, int value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt(key, value);
        apply(editor);
    }

    public static void set(String key, long value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putLong(key, value);
        apply(editor);
    }

    public static void set(String key, String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        apply(editor);
    }

    public static void set(String key, Set<String> value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.remove(key);
        editor.putStringSet(key, value);
        apply(editor);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void apply(SharedPreferences.Editor editor) {
        if (sIsAtLeastGB) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences() {
        return getPreferences(PREF_NAME);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static SharedPreferences getPreferences(String prefName) {
        return context().getSharedPreferences(prefName, Context.MODE_MULTI_PROCESS);
    }

    public static boolean get(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public static int get(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    public static long get(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    public static String get(String key, String defValue) {
        return getPreferences().getString(key, defValue);
    }

    public static float get(String key, float defValue) {
        return getPreferences().getFloat(key, defValue);
    }

    public static Set<String> get(String key, Set<String> defValue) {
        return getPreferences().getStringSet(key, defValue);
    }


    /**
     *
     */
    public static void showToast(int message) {
        showToast(message, Toast.LENGTH_LONG, 0);
    }

    public static void showToast(String message) {
        showToast(message, Toast.LENGTH_LONG, 0, Gravity.BOTTOM);
    }

    public static void showToast(int message, int icon) {
        showToast(message, Toast.LENGTH_LONG, icon);
    }

    public static void showToast(String message, int icon) {
        showToast(message, Toast.LENGTH_LONG, icon, Gravity.BOTTOM);
    }

    public static void showToastShort(int message) {
        showToast(message, Toast.LENGTH_SHORT, 0);
    }

    public static void showToastShort(String message) {
        showToast(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM);
    }

    public static void showToastShort(int message, Object... args) {
        showToast(message, Toast.LENGTH_SHORT, 0, Gravity.BOTTOM, args);
    }

    public static void showToast(int message, int duration, int icon) {
        showToast(message, duration, icon, Gravity.BOTTOM);
    }

    public static void showToast(int message, int duration, int icon, int gravity) {
        showToast(context().getString(message), duration, icon, gravity);
    }

    public static void showToast(int message, int duration, int icon, int gravity, Object... args) {
        showToast(context().getString(message, args), duration, icon, gravity);
    }

    public static void showToast(String message, int duration, int icon, int gravity) {
        if (message != null && !message.equalsIgnoreCase("")) {
            long time = System.currentTimeMillis();
            if (!message.equalsIgnoreCase(lastToast) || Math.abs(time - lastToastTime) > 2000) {
                View view = LayoutInflater.from(context()).inflate(R.layout.view_toast, null);
                ((TextView) view.findViewById(R.id.title_tv)).setText(message);
                if (icon != 0) {
                    ((ImageView) view.findViewById(R.id.icon_iv))
                            .setImageResource(icon);
                    ((ImageView) view.findViewById(R.id.icon_iv))
                            .setVisibility(View.VISIBLE);
                }
                Toast toast = new Toast(context());
                toast.setView(view);
                if (gravity == Gravity.CENTER) {
                    toast.setGravity(gravity, 0, 0);
                } else {
                    toast.setGravity(gravity, 0, 35);
                }
                toast.setDuration(duration);
                toast.show();
                lastToast = message;
                lastToastTime = System.currentTimeMillis();
            }
        }
    }
    /**
     * 11.设置是否第一次启动
     */
    public static boolean isFristStart() {
        return getPreferences().getBoolean(AppConfig.KEY_FRITST_START, true);
    }

    public static void setFristStart(boolean frist) {
        set(AppConfig.KEY_FRITST_START, frist);
    }

    /**
     * 12.程序更新
     */
    public static boolean isAppUpgrade() {
        return getPreferences().getBoolean(AppConfig.KEY_UPGRADE_APP, true);
    }

    public static void setAppUpgrade(boolean upgrade) {
        set(AppConfig.KEY_UPGRADE_APP, upgrade);
    }


    /**
     * 保利威视
     */
    //加密秘钥和加密向量，在后台->设置->API接口中获取，用于解密SDK加密串
    //值修改请参考https://github.com/easefun/polyv-android-sdk-demo/wiki/10.%E5%85%B3%E4%BA%8E-SDK%E5%8A%A0%E5%AF%86%E4%B8%B2-%E4%B8%8E-%E7%94%A8%E6%88%B7%E9%85%8D%E7%BD%AE%E4%BF%A1%E6%81%AF%E5%8A%A0%E5%AF%86%E4%BC%A0%E8%BE%93
    /** 加密秘钥 */
    private String aeskey = "VXtlHmwfS2oYm0CZ";
    /** 加密向量 */
    private String iv = "2u9gDPKdX6GyQJKU";

    public void initPolyvCilent() {
        //网络方式取得SDK加密串，（推荐）
        //网络获取到的SDK加密串可以保存在本地SharedPreference中，下次先从本地获取
        //new LoadConfigTask().execute();
        PolyvSDKClient client = PolyvSDKClient.getInstance();
        //使用SDK加密串来配置
        client.setConfig("CMWht3MlpVkgpFzrLNAebYi4RdQDY/Nhvk3Kc+qWcck6chwHYKfl9o2aOVBvXVTRZD/14XFzVP7U5un43caq1FXwl0cYmTfimjTmNUYa1sZC1pkHE8gEsRpwpweQtEIiTGVEWrYVNo4/o5jI2/efzA==", aeskey, iv, getApplicationContext());
        //初始化SDK设置
        client.initSetting(getApplicationContext());
        //启动Bugly
        client.initCrashReport(getApplicationContext());
        //启动Bugly后，在学员登录时设置学员id
        //client.crashReportSetUserId(userId);
        //获取SD卡信息
        PolyvDevMountInfo.getInstance().init(this, new PolyvDevMountInfo.OnLoadCallback() {

            @Override
            public void callback() {
                //是否有可移除的存储介质（例如 SD 卡）或内部（不可移除）存储可供使用。
                if (!PolyvDevMountInfo.getInstance().isSDCardAvaiable()) {
                    // TODO 没有可用的存储设备,后续不能使用视频缓存功能
                    Log.e(TAG, "没有可用的存储设备,后续不能使用视频缓存功能");
                    return;
                }

                //可移除的存储介质（例如 SD 卡），需要写入特定目录/storage/sdcard1/Android/data/包名/。
                String externalSDCardPath = PolyvDevMountInfo.getInstance().getExternalSDCardPath();
                if (!TextUtils.isEmpty(externalSDCardPath)) {
                    StringBuilder dirPath = new StringBuilder();
                    dirPath.append(externalSDCardPath).append(File.separator).append("Android").append(File.separator).append("data")
                            .append(File.separator).append(getPackageName()).append(File.separator).append("polyvdownload");
                    File saveDir = new File(dirPath.toString());
                    if (!saveDir.exists()) {
                        getExternalFilesDir(null); // 生成包名目录
                        saveDir.mkdirs();//创建下载目录
                    }

                    //设置下载存储目录
                    PolyvSDKClient.getInstance().setDownloadDir(saveDir);
                    return;
                }

                //如果没有可移除的存储介质（例如 SD 卡），那么一定有内部（不可移除）存储介质可用，都不可用的情况在前面判断过了。
                File saveDir = new File(PolyvDevMountInfo.getInstance().getInternalSDCardPath() + File.separator + "polyvdownload");
                if (!saveDir.exists()) {
                    saveDir.mkdirs();//创建下载目录
                }

                //设置下载存储目录
                PolyvSDKClient.getInstance().setDownloadDir(saveDir);
            }
        });

        //显示默认加载图片
        //BaseImageDownloader.getStreamFromOtherSource();
    }

    private class LoadConfigTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String config = PolyvSDKUtil.getUrl2String("http://demo.polyv.net/demo/appkey.php");
            if (TextUtils.isEmpty(config)) {
                try {
                    throw new Exception("没有取到数据");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return config;
        }

        @Override
        protected void onPostExecute(String config) {
            PolyvSDKClient client = PolyvSDKClient.getInstance();
            client.setConfig(config, aeskey, iv);
        }
    }
}
