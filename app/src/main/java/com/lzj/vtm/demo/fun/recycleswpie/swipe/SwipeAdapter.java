package com.lzj.vtm.demo.fun.recycleswpie.swipe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzj.vtm.demo.R;

import java.util.ArrayList;

/**
 * 适配器
 */
public class SwipeAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    /**
     * 初始化定义
     */
    private Context context;
    private ArrayList<DataInfo.Info> list;

    public SwipeAdapter(Context context, ArrayList<DataInfo.Info> list) {
        this.context = context;
        this.list = list;
    }

    /**
     * 点击回调
     */
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, DataInfo.Info data);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (DataInfo.Info) v.getTag());
        }
    }

    /**
     * 创建绑定
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_swipe_item_layout, null);
        ViewHodler vh = new ViewHodler(view);
        LinearLayout.LayoutParams lp = new LinearLayout.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewHodler mHodler = (ViewHodler) holder;

        mHodler.textview.setVisibility(View.GONE);
        Glide.with(context)
                .load(list.get(position).url)
                .centerCrop()
                .placeholder(R.color.cardview_light_background)
                .crossFade()
                .into(mHodler.imageView);

        mHodler.itemView.setTag(list.get(position));
    }

    /**
     * 复写方法
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHodler extends RecyclerView.ViewHolder{

        private TextView textview;
        private ImageView imageView;
        public ViewHodler(View itemView) {
            super(itemView);
            textview= (TextView) itemView.findViewById(R.id.textview);
            imageView= (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
