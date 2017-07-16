package com.lzj.vtm.demo.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.banner.Banner;
import com.lzj.vtm.demo.banner.ViewHeader;
import com.lzj.vtm.demo.banner.ViewHeader2;
import com.lzj.vtm.demo.base.UIHelper;
import com.lzj.vtm.demo.home.model.News;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class NewsListAdapter extends RecyclerView.Adapter {

    private MyItemClickListener mListener;

    private boolean load_more_msg = true;

    private int load_more_status = 0;//上拉加载更多状态-默认为0

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载更多数据
    public static final int LOADING_NO_MORE = 2;//没有数据

    private static final int TYPE_NULL = -1;  //普通Item View
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_BANNER = 2;  //轮播图
    private static final int TYPE_FOOTER = 1;  //顶部FootView

    private ArrayList<News> images;
    private ArrayList<Banner> banners;

    private Context context = null;
    private RequestManager requestManager;

    public NewsListAdapter(Context context, RequestManager requestManager) {
        this.context = context;
        this.requestManager = requestManager;
        this.images = new ArrayList<News>();
        this.banners = new ArrayList<Banner>();
    }

    public void setBanners(ArrayList<Banner> banners) {
        this.banners = banners;
        notifyDataSetChanged();
    }

    public void setImages(ArrayList<News> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    /**
     *
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View

        if (viewType == TYPE_NULL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_null_layout, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            NullViewHolder nullViewHolder = new NullViewHolder(view);
            return nullViewHolder;
        }else if (viewType == TYPE_BANNER) {
            ViewHeader2 mHeaderView = new ViewHeader2(context);
            mHeaderView.setRefreshLayout(parent);
            //这边可以做一些属性设置，甚至事件监听绑定
            BannerViewHolder bannerViewHolder = new BannerViewHolder(mHeaderView);
            return bannerViewHolder;
        }else if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_layout2, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View foot_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_more_layout, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;

    }

    //动画设置
    private void startAnim(View view) {

        Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.anim_tip);
        operatingAnim.setInterpolator(new LinearInterpolator());
        view.startAnimation(operatingAnim);//开始动画

        //view.clearAnimation();//清空动画
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            if(images!=null&&position<images.size()) {
                News news = images.get(position);
                Glide.with(holder.itemView.getContext())
                        .load(news.pic)
                        .centerCrop()
                        .placeholder(R.color.cardview_light_background)
                        .crossFade()
                        .into(((ItemViewHolder) holder).item_img);
                ((ItemViewHolder) holder).item_tv_name.setText(news.title);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                ((ItemViewHolder) holder).item_tv.setText(df.format(new Date(news.addtime)));
            }
            holder.itemView.setTag(position);
        } else if (holder instanceof BannerViewHolder) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            bannerViewHolder.viewHeader.initData(requestManager, banners);
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.foot_view_item_pb.setVisibility(View.VISIBLE);
                    footViewHolder.foot_view_item_tv.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footViewHolder.foot_view_item_pb.setVisibility(View.VISIBLE);
                    footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
                    break;
                case LOADING_NO_MORE:
                    footViewHolder.foot_view_item_pb.setVisibility(View.GONE);
                    footViewHolder.foot_view_item_tv.setText("没有数据");
                    break;
            }
        }else if (holder instanceof NullViewHolder) {
            NullViewHolder nullViewHolder = (NullViewHolder) holder;
            startAnim(nullViewHolder.img);
        }
    }


    /**
     * 进行判断是普通Item视图还是FootView视图
     */
    @Override
    public int getItemViewType(int position) {
        /**
         * 第一个item设置为提示页面
         *
         * 最后一个item设置为footerView
         *
         * 其他为正常的item显示
         */
        if (1 == getItemCount()) {
            return TYPE_NULL;
        }else if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        }else if (position  == 0) {
            return TYPE_BANNER;
        }else {
            return TYPE_ITEM;
        }

    }

    @Override
    public int getItemCount() {
        return images == null ? 1 : images.size() + 1;
    }

    /**
     * 空数据时,显示提示页面
     */
    public static class NullViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        public NullViewHolder(View view) {
            super(view);

            img = (ImageView) view.findViewById(R.id.null_img);
        }
    }

    /**
     * 空数据时,显示提示页面
     */
    public static class BannerViewHolder extends RecyclerView.ViewHolder {

        public ViewHeader2 viewHeader;

        public BannerViewHolder(View view) {
            super(view);
            this.viewHeader = (ViewHeader2) view;
        }
    }

    /**
     * 自定义的ViewHolder，持有每个Item的的所有界面元素
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item_tv_name;
        public TextView item_tv;
        public ImageView item_img;

        public ItemViewHolder(View view) {
            super(view);
            item_tv_name = (TextView) view.findViewById(R.id.item_tv_name);
            item_tv = (TextView) view.findViewById(R.id.item_tv);
            item_img = (ImageView) view.findViewById(R.id.item_img);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null && getAdapterPosition()!=0){
                mListener.onItemClick(v,getAdapterPosition());
            }
        }
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView foot_view_item_tv;
        private ProgressBar foot_view_item_pb;

        public FootViewHolder(View view) {
            super(view);
            foot_view_item_pb = (ProgressBar) view.findViewById(R.id.progressData);
            foot_view_item_tv = (TextView) view.findViewById(R.id.foot_view_item_tv);
        }
    }

    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
    }

    /**
     * 设置多次加载显示提示信息重复出现问题
     * @param load_more_msg
     */
    public void setload_more_msg(boolean load_more_msg) {
        this.load_more_msg = load_more_msg;
    }

    public boolean isLoad_more_msg() {
        return load_more_msg;
    }

    /**
     * 设置条目点击事件
     */
    public interface MyItemClickListener {
        public void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mListener = listener;
    }
}
