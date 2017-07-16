package com.lzj.vtm.demo.base;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import com.lzj.vtm.demo.AppConfig;
import com.lzj.vtm.demo.AppContext;
import com.lzj.vtm.demo.banner.Banner;
import com.lzj.vtm.demo.details.BannerDetailFragment;
import com.lzj.vtm.demo.details.BrowserFragment;
import com.lzj.vtm.demo.details.NewsDetailFragment;
import com.lzj.vtm.demo.download.DownloadService;
import com.lzj.vtm.demo.download.ICallbackResult;
import com.lzj.vtm.demo.fun.recycleswpie.swipe.SwipeActivity;
import com.lzj.vtm.demo.home.model.News;
import com.lzj.vtm.demo.utils.TDevice;
import com.lzj.vtm.demo.utils.URLsUtils;


/**
 * 界面帮助类
 */
public class UIHelper {

    /**
     *1.普通开启
     */
    public static void showSwipeActivity(Context context) {
        Intent intent = new Intent(context, SwipeActivity.class);
        context.startActivity(intent);
    }

    /**
     * 2.开启详情 : 列表
     * @param context
     * @param news
     * @param type
     */
    public static void showNewsRedirect(Context context, News news, int type) {
        switch (type) {
            case News.NEWSTYPE_NEWS_FREE:
                showNewsDetail(context, news);
                break;
            case News.NEWSTYPE_NEWS_HTML_1:
                openSysBrowser(context, news.html);
                break;
            case News.NEWSTYPE_NEWS_HTML_2:
                openBrowser(context, news.html);
                break;
            default:
                break;
        }
    }

    /**
     * 2.开启详情 : 列表
     */
    public static void showBannerRedirect(Context context, Banner banner, int type) {
        switch (type) {
            case Banner.NEWSTYPE_BANNER_FREE:
                showBannerDetail(context,banner);
                break;
            case Banner.NEWSTYPE_BANNER_HTML_1:
                openSysBrowser(context, banner.html);
                break;
            case Banner.NEWSTYPE_BANNER_HTML_2:
                openBrowser(context, banner.html);
                break;
            default:
                break;
        }
    }


    public static void showNewsDetail(Context context, News news) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(NewsDetailFragment.BUNDLE_KEY_TYPE, news);
        intent.putExtra(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE, DetailActivity.DISPLAY_NEWS_FREE);
        context.startActivity(intent);
    }

    /**
     * 2.开启详情 : 轮播图
     * @param context
     * @param banner
     */
    public static void showBannerDetail(Context context, Banner banner) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(BannerDetailFragment.BUNDLE_KEY_TYPE, banner);
        intent.putExtra(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE, DetailActivity.DISPLAY_BANNER);
        context.startActivity(intent);
    }

    /**
     * 3.开启Fragment页面
     * @param context
     * @param page
     */
    public static void showSimpleBack(Context context, SimpleBackPage page) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent);
    }

    public static void showSimpleBack(Context context, SimpleBackPage page, Bundle args) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent);
    }

    //
    public static void openDownLoadService(Context context, String downurl, String tilte) {
        final ICallbackResult callback = new ICallbackResult() {

            @Override
            public void OnBackResult(Object s) {
            }
        };
        ServiceConnection conn = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DownloadService.DownloadBinder binder = (DownloadService.DownloadBinder) service;
                binder.addCallback(callback);
                binder.start();

            }
        };
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(DownloadService.BUNDLE_KEY_DOWNLOAD_URL, downurl);
        intent.putExtra(DownloadService.BUNDLE_KEY_TITLE, tilte);
        context.startService(intent);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }
    /**
     * 4.webView初始化
     * @param webView
     */
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public static void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setDefaultFontSize(15);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        int sysVersion = Build.VERSION.SDK_INT;
        if (sysVersion >= 11) {
            settings.setDisplayZoomControls(false);
        } else {
            ZoomButtonsController zbc = new ZoomButtonsController(webView);
            zbc.getZoomControls().setVisibility(View.GONE);
        }
        webView.setWebViewClient(UIHelper.getWebViewClient());
    }

    public static WebViewClient getWebViewClient() {

        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                showUrlRedirect(view.getContext(), url);
                return true;
            }
        };
    }


    public static void showUrlRedirect(Context context, String url) {
        if (url == null)
            return;

        URLsUtils urls = URLsUtils.parseURL(url);
        if (urls != null) {
            showLinkRedirect(context, urls.getObjType(), urls.getObjId(), urls.getObjKey());
        } else {
            openBrowser(context, url);
        }
    }

    public static void showLinkRedirect(Context context, int objType, int objId, String objKey) {
        switch (objType) {
            case URLsUtils.URL_OBJ_TYPE_NEWS:
                showNewsDetail(context, objId, -1);
                break;
            case URLsUtils.URL_OBJ_TYPE_OTHER:
                openBrowser(context, objKey);
                break;
            case URLsUtils.URL_OBJ_TYPE_GIT:
                openSysBrowser(context, objKey);
                break;
        }
    }

    //开启详情
    public static void showNewsDetail(Context context, int newsId, int commentCount) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("id", newsId);
        intent.putExtra("comment_count", commentCount);
        intent.putExtra(DetailActivity.BUNDLE_KEY_DISPLAY_TYPE, DetailActivity.DISPLAY_NEWS_FREE);
        context.startActivity(intent);
    }

    //开启自定义浏览器
    public static void openBrowser(Context context, String url) {

        try {
            Bundle bundle = new Bundle();
            bundle.putString(BrowserFragment.BROWSER_KEY, url);
            showSimpleBack(context, SimpleBackPage.BROWSER, bundle);
        } catch (Exception e) {
            e.printStackTrace();
            AppContext.showToastShort("无法浏览此网页");
        }
    }

    //开启系统浏览器
    public static void openSysBrowser(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            AppContext.showToastShort("无法浏览此网页");
        }
    }

    public static String setHtmlCotentSupportImagePreview(String body) {
        // 读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
        if (AppContext.get(AppConfig.KEY_LOAD_IMAGE, true)
                || TDevice.isWifiOpen()) {
            // 过滤掉 img标签的width,height属性
            body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
            body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
            // 添加点击图片放大支持
            body = body.replaceAll("(<img[^>]+src=\")(\\S+)\"",
                    "$1$2\" onClick=\"showImagePreview('$2')\"");
        } else {
            // 过滤掉 img标签
            body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>", "");
        }
        return body;
    }

    public final static String linkCss = "<script type=\"text/javascript\" src=\"file:///android_asset/shCore.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/brush.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/client.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/detail_page.js\"></script>"
            + "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>"
            + "<script type=\"text/javascript\">function showImagePreview(var url){window.location.url= url;}</script>"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shThemeDefault.css\">"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shCore.css\">"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/common.css\">";

    public final static String WEB_STYLE = linkCss;

    public static final String WEB_LOAD_IMAGES = "<script type=\"text/javascript\"> var allImgUrls = getAllImgSrc(document.body.innerHTML);</script>";

    public static final String BODY = "</body>";

    private static final String SHOWIMAGE = "ima-api:action=showImage&data=";

    public static final String WebViewBodyString = "<body ><div class='contentstyle' id='article_body'>";
}
