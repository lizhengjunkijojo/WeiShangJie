package com.lzj.vtm.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 */
public class AppConfig {


    public static final String APP_PACKAGE_NAME = "WsjApp";

    public static final int APP_PACKAGE_DEVTYPE = 1;

    public static final String KEY_LOAD_IMAGE = "KEY_LOAD_IMAGE";

    public static final String KEY_FRITST_START = "KEY_FRIST_START";//第一次启动标示
    public static final String KEY_UPGRADE_APP = "KEY_UPGRADE_APP";//程序更新标示
    public static final String KEY_CHECK_UPDATE = "KEY_CHECK_UPDATE";//检查程序版本

    // 默认存放文件下载的路径
    public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + AppConfig.APP_PACKAGE_NAME
            + File.separator
            + "download"
            + File.separator;
}
