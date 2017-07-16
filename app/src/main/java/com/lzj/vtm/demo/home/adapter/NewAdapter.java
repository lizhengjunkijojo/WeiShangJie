package com.lzj.vtm.demo.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.home.model.News;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewAdapter extends BaseAdapter{

    public Context context;
    public ArrayList<News> arrayList;
    private LayoutInflater inflater;

    public NewAdapter(Context context,ArrayList<News> arrayList){
        this.context =context;
        this.inflater=LayoutInflater.from(context);
        this.arrayList = arrayList;
    }

    public void setImages(ArrayList<News> arrayList) {
        this.arrayList = arrayList;
    }

    public void setChanged() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public News getItem(int position) {
        if (position >= 0 && position < arrayList.size())
            return arrayList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;

        if (convertView == null) {
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.home_item_layout,null);

            vh.item_img = (ImageView) convertView.findViewById(R.id.item_img);
            vh.item_tv = (TextView) convertView.findViewById(R.id.item_tv);
            vh.item_tv_name = (TextView) convertView.findViewById(R.id.item_tv_name);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if(arrayList!=null && position<arrayList.size()){
            News news = arrayList.get(position);
            Glide.with(context)
                    .load(news.pic)
                    .centerCrop()
                    .placeholder(R.color.cardview_light_background)
                    .crossFade()
                    .into(vh.item_img);

            vh.item_tv_name.setText(news.title);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            vh.item_tv.setText(df.format(new Date(news.addtime)));
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView item_img;
        TextView item_tv;
        TextView item_tv_name;
    }
}
