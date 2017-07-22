package com.lzj.vtm.demo.home.video;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.RestVO;
import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.blvs.PolyvOnlineListViewAdapter;
import com.lzj.vtm.demo.blvs.PolyvPlayerActivity;
import com.lzj.vtm.demo.widget.SuperRefreshLayout;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频页面
 */
public class PolyvVideoFragment extends Fragment implements AdapterView.OnItemClickListener,SuperRefreshLayout.SuperRefreshLayoutListener{

    protected SuperRefreshLayout mRefreshLayout;
    private ListView lv_online;
    private PolyvOnlineListViewAdapter lv_online_adapter;
    private List<RestVO> arrayList;

    private int pages= 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.polyv_activity_online_video, container, false);

        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {

        mRefreshLayout = (SuperRefreshLayout) view.findViewById(R.id.superRefreshLayout);
        mRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        mRefreshLayout.setSuperRefreshLayoutListener(this);

        lv_online = (ListView) view.findViewById(R.id.lv_online);
        arrayList = new ArrayList<RestVO>();
        lv_online_adapter = new PolyvOnlineListViewAdapter(getActivity() , arrayList);
        lv_online.setAdapter(lv_online_adapter);

        lv_online.setOnItemClickListener(this);
    }

    private void initData() {
        new LoadVideoList(pages).execute();
    }

    @Override
    public void onRefreshing() {
        pages = 1;
        mRefreshLayout.setRefreshing(true);
        new LoadVideoList(pages).execute();
        mRefreshLayout.setNoMoreData();
    }

    @Override
    public void onLoadMore() {
        pages++;
        mRefreshLayout.setIsOnLoading(true);
        new LoadVideoList(pages).execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RestVO restVO = arrayList.get(position);
        Intent intent = PolyvPlayerActivity.newIntent(getActivity(), PolyvPlayerActivity.PlayMode.portrait, restVO.getVid());
        getActivity().startActivity(intent);
    }

    class LoadVideoList extends AsyncTask<String, String, List<RestVO>> {

        int page = 1;

        public LoadVideoList(int page){
            this.page =  page;
        }

        @Override
        protected List<RestVO> doInBackground(String... arg0) {
            try {
                return PolyvSDKClient.getInstance().getVideoList(page, 20);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<RestVO> result) {
            super.onPostExecute(result);
            if (result == null) return;

            if(mRefreshLayout.isRefreshing()){
                arrayList.clear();
            }

            arrayList.addAll(result);
            lv_online_adapter.setVideos(arrayList);
            lv_online_adapter.notifyDataSetChanged();

            mRefreshLayout.onLoadComplete();
            mRefreshLayout.setCanLoadMore();
            mRefreshLayout.setRefreshing(false);
        }
    }

}
