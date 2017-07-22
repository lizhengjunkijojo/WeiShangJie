package com.lzj.vtm.demo.banner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.RequestManager;
import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.base.UIHelper;
import com.lzj.vtm.demo.home.news.model.News;

/**
 * ViewBanner控件
 */
public class ViewBanner extends RelativeLayout implements View.OnClickListener {

    private Banner banner;
    private ImageView iv_banner;

    private Context context;

    public ViewBanner(Context context) {
        super(context, null);
        init(context);
    }

    /**
     * 初始化控件
     */
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_banner, this, true);
        iv_banner = (ImageView) findViewById(R.id.iv_banner);
        setOnClickListener(this);
        this.context = context;
    }

    /**
     * 初始加载图片
     */
    public void initData(RequestManager manager, Banner banner) {
        this.banner = banner;
        manager.load(banner.pic).into(iv_banner);
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (banner != null) {
            /**
             *  TODO 开启详情页
             */
            if(banner.type == 0){
                UIHelper.showBannerRedirect(v.getContext(),banner, Banner.NEWSTYPE_BANNER_FREE);
            }else if(banner.type == 1) {
                UIHelper.showBannerRedirect(v.getContext(), banner, Banner.NEWSTYPE_BANNER_HTML_1);
            }else if(banner.type == 2){
                UIHelper.showBannerRedirect(v.getContext(),banner,Banner.NEWSTYPE_BANNER_HTML_2);
            }
        }
    }

    /**
     * 标题
     * @return
     */
    public String getTitle() {
        return banner.title;
    }
}
