package com.lzj.vtm.demo.details;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.banner.Banner;
import com.lzj.vtm.demo.base.BaseFragment;
import com.lzj.vtm.demo.base.UIHelper;
import com.lzj.vtm.demo.cache.CacheManager;
import com.lzj.vtm.demo.empty.EmptyLayout;
import com.lzj.vtm.demo.utils.DialogHelp;
import com.lzj.vtm.demo.utils.FontSizeUtils;
import com.lzj.vtm.demo.utils.HTMLUtil;
import com.lzj.vtm.demo.utils.TDevice;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 信息中心
 */
public class BannerDetailFragment extends BaseFragment {

    public static final String BUNDLE_KEY_TYPE = "BUNDLE_KEY_TYPE_Banner";

    @Bind(R.id.ll_header)
    protected LinearLayout ll_header;

    @Bind(R.id.tv_title)
    protected TextView tv_title;
    @Bind(R.id.tv_source)
    protected TextView tv_source;
    @Bind(R.id.tv_time)
    protected TextView tv_time;

    protected EmptyLayout mEmptyLayout;

    protected WebView mWebView;

    protected Banner mDetail;

    private AsyncTask<String, Void, Banner> mCacheTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);

        mDetail = (Banner) getActivity().getIntent().getSerializableExtra(BUNDLE_KEY_TYPE);

        ButterKnife.bind(this, view);
        initView(view);
        initData();
        requestData(mDetail);
        return view;
    }

    @Override
    public void initView(View view) {
        mEmptyLayout = (EmptyLayout) view.findViewById(R.id.error_layout);
        mWebView = (WebView) view.findViewById(R.id.webview);
        UIHelper.initWebView(mWebView);
    }

    private void requestData(Banner detail) {
        String key = getCacheKey();
        if (TDevice.hasInternet() && (!CacheManager.isExistDataCache(getActivity(), key))) {
            if (detail != null) {
                mEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                executeOnLoadDataSuccess(detail);
                saveCache(detail);
            } else {
                executeOnLoadDataError();
            }
        } else {
            readCacheData(key);
        }
    }

    @Override
    public void onDestroyView() {
        recycleWebView();
        super.onDestroyView();
    }

    private void recycleWebView() {
        if (mWebView != null) {
            mWebView.setVisibility(View.GONE);
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }

    private void readCacheData(String cacheKey) {
        cancelReadCache();
        mCacheTask = new CacheTask(getActivity()).execute(cacheKey);
    }

    private void cancelReadCache() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }

    private class CacheTask extends AsyncTask<String, Void, Banner> {
        private final WeakReference<Context> mContext;

        private CacheTask(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        protected Banner doInBackground(String... params) {
            if (mContext.get() != null) {
                Serializable seri = CacheManager.readObject(mContext.get(), params[0]);
                if (seri == null) {
                    return null;
                } else {
                    return (Banner)seri;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Banner detail) {
            super.onPostExecute(detail);
            mEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            if (detail != null) {
                executeOnLoadDataSuccess(detail);
            } else {
                executeOnLoadDataError();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        }
    }

    //执行加载成功的数据显示
    protected void executeOnLoadDataSuccess(Banner detail) {
        if (detail == null || TextUtils.isEmpty(this.getWebViewBody(detail))) {
            executeOnLoadDataError();
            return;
        }

        mWebView.loadDataWithBaseURL("", this.getWebViewBody(detail), "text/html", "UTF-8", "");//加载WebView的封装数据入口方法
        // 显示存储的字体大小
        mWebView.loadUrl(FontSizeUtils.getSaveFontSize());

        this.getHeadViewBody(detail);
    }

    //执行加载失败的数据显示
    protected void executeOnLoadDataError() {
        mEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        mEmptyLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mState = STATE_REFRESH;
                mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                requestData(mDetail);
            }
        });
    }

    protected void saveCache(Banner detail) {
        new SaveCacheTask(getActivity(), detail, getCacheKey()).execute();
    }

    private class SaveCacheTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<Context> mContext;
        private final Serializable seri;
        private final String key;

        private SaveCacheTask(Context context, Serializable seri, String key) {
            mContext = new WeakReference<Context>(context);
            this.seri = seri;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            CacheManager.saveObject(mContext.get(), seri, key);
            return null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.font_size:
                showChangeFontSize();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    AlertDialog fontSizeChange;
    private void showChangeFontSize() {

        final String[] items = getResources().getStringArray(R.array.font_size);
        fontSizeChange = DialogHelp.getSingleChoiceDialog(getActivity(), items, FontSizeUtils.getSaveFontSizeIndex(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 更改字体大小
                FontSizeUtils.saveFontSize(i);
                mWebView.loadUrl(FontSizeUtils.getFontSize(i));
                fontSizeChange.dismiss();
            }
        }).show();
    }


    protected String getFilterHtmlBody(String body) {
        if (body == null) return "";
        return HTMLUtil.delHTMLTag(body.trim());
    }

    protected String getCacheKey() {
        return "bannerDetail_" + mDetail.title;
    }

    /**
     * 摘要
     */
    protected void getHeadViewBody(Banner detail) {
        ll_header.setVisibility(View.GONE);

        tv_title.setText(detail.title);
        tv_source.setText(detail.title);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        tv_time.setText(df.format(new Date(detail.addtime)));
    }

    /**
     * 详情内容
     */
    protected String getWebViewBody(Banner detail) {

        StringBuffer body = new StringBuffer();
//        body.append(UIHelper.WEB_STYLE).append(UIHelper.WEB_LOAD_IMAGES);
//        body.append(UIHelper.WebViewBodyString);
        byte b[]= Base64.decode(detail.bannerContent,Base64.DEFAULT);
//        body.append(UIHelper.setHtmlCotentSupportImagePreview(new String(b)));
//        body.append(UIHelper.BODY);
        body.append(new String(b));
        return  body.toString();
    }

}
