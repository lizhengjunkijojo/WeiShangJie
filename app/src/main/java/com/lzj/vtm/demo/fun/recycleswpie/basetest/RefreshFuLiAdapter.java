package com.lzj.vtm.demo.fun.recycleswpie.basetest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzj.vtm.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * SwipeRefreshLayout+RecyclerView实现下拉刷新/上拉加载
 */
public class RefreshFuLiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int load_more_status = 0;//上拉加载更多状态-默认为0

    public static final int PULLUP_LOAD_MORE = 0;//上拉加载更多
    public static final int LOADING_MORE = 1;//正在加载中

    private static final int TYPE_NULL = -1;  //普通Item View
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView

    private LayoutInflater mInflater;
    private List<FuLiInfo.Info> mTitles = null;
    private Context context;

    public RefreshFuLiAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mTitles = new ArrayList<FuLiInfo.Info>();
    }

    /**
     * item显示类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_NULL) {
            View view = mInflater.inflate(R.layout.recycler_item_null_layout, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            NullViewHolder nullViewHolder = new NullViewHolder(view);
            return nullViewHolder;
        }else if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.recycler_item_layout_test, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View foot_view = mInflater.inflate(R.layout.recycler_item_more_layout, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Glide.with(context)
                    .load(mTitles.get(position).url)
                    .centerCrop()
                    .placeholder(R.color.cardview_light_background)
                    .crossFade()
                    .into(((ItemViewHolder) holder).item_img);

            ((ItemViewHolder) holder).item_tv_name.setText("李正君");
            ((ItemViewHolder) holder).item_tv.setText(mTitles.get(position).url);
            holder.itemView.setTag(position);
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.foot_view_item_tv.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
                    break;
            }
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
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mTitles.size() + 1;
    }

    /**
     * 空数据时,显示提示页面
     */
    public static class NullViewHolder extends RecyclerView.ViewHolder {

        public NullViewHolder(View view) {
            super(view);
        }
    }

    /**
     * 自定义的ViewHolder，持有每个Item的的所有界面元素
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView item_tv_name;
        public TextView item_tv;
        public ImageView item_img;

        public ItemViewHolder(View view) {
            super(view);
            item_tv_name = (TextView) view.findViewById(R.id.item_tv_name);
            item_tv = (TextView) view.findViewById(R.id.item_tv);
            item_img = (ImageView) view.findViewById(R.id.item_img);
        }
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView foot_view_item_tv;

        public FootViewHolder(View view) {
            super(view);
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

    //添加数据  重复保存
    public void addItem(List<FuLiInfo.Info> newDatas) {
        newDatas.addAll(mTitles);
        mTitles.removeAll(mTitles);
        mTitles.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<FuLiInfo.Info> newDatas) {
        mTitles.addAll(newDatas);
        notifyDataSetChanged();
    }
}
