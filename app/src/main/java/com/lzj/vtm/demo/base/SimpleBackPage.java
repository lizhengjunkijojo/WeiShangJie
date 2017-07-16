package com.lzj.vtm.demo.base;


import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.details.BrowserFragment;
import com.lzj.vtm.demo.setting.AboutFragment;
import com.lzj.vtm.demo.setting.FeedBackFragment;
import com.lzj.vtm.demo.setting.SettingsFragment;

/**
 * 页面控制
 */
public enum SimpleBackPage {

    BROWSER(1, R.string.app_name, BrowserFragment.class),

    ABOUT(2, R.string.actionbar_title_about, AboutFragment.class),

    FEEDBACK(3, R.string.actionbar_title_feedback, FeedBackFragment.class),

    SETTING(4, R.string.actionbar_title_setting, SettingsFragment.class);

    private int title;
    private Class<?> clz;
    private int value;

    private SimpleBackPage(int value, int title, Class<?> clz) {
        this.value = value;
        this.title = title;
        this.clz = clz;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static SimpleBackPage getPageByValue(int val) {
        for (SimpleBackPage p : values()) {
            if (p.getValue() == val)
                return p;
        }
        return null;
    }
}
