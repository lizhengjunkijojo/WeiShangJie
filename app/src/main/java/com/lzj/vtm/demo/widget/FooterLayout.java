package com.lzj.vtm.demo.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lzj.vtm.demo.R;

/**
 *FooterLayout列表底部加载器

 FooterLayout mFooterView = new FooterLayout();

 mListView.addFooterView(mFooterView);

 mFooterView.setFooterType(TYPE_LOADING);
 */
public class FooterLayout extends View{

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_LOADING = 1;
    public static final int TYPE_NO_MORE = 2;
    public static final int TYPE_ERROR = 3;
    public static final int TYPE_NET_ERROR = 4;

    private View mFooterView;
    private TextView mFooterText;
    private ProgressBar mFooterProgressBar;

    public FooterLayout(Context context) {
        super(context);

        mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.view_item_footer, null);
        mFooterText = (TextView) mFooterView.findViewById(R.id.tv_footer);
        mFooterProgressBar = (ProgressBar) mFooterView.findViewById(R.id.pb_footer);
    }

    /**
     *
     */
    public void setFooterType(int type) {
        switch (type) {
            case TYPE_NORMAL:
            case TYPE_LOADING:
                mFooterText.setText(getResources().getString(R.string.footer_type_loading));
                mFooterProgressBar.setVisibility(View.VISIBLE);
                break;
            case TYPE_NET_ERROR:
                mFooterText.setText(getResources().getString(R.string.footer_type_net_error));
                mFooterProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_ERROR:
                mFooterText.setText(getResources().getString(R.string.footer_type_error));
                mFooterProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_NO_MORE:
                mFooterText.setText(getResources().getString(R.string.footer_type_not_more));
                mFooterProgressBar.setVisibility(View.GONE);
                break;
        }
    }

}
