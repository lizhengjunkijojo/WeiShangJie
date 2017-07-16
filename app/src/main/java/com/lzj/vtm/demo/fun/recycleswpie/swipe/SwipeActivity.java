package com.lzj.vtm.demo.fun.recycleswpie.swipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;


import com.lzj.vtm.demo.R;
import com.lzj.vtm.demo.fun.recycleswpie.swipe.pull.SwipyRefreshLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SwipeActivity extends AppCompatActivity implements SwipeAdapter.OnRecyclerViewItemClickListener,SwipyRefreshLayout.OnRefreshListener {

    private int pages=1;

    private final int TOP_REFRESH = 1;
    private final int BOTTOM_REFRESH = 2;

    private ArrayList<DataInfo.Info> arrayList;

    private RecyclerView recyclerView;
    private SwipeAdapter adapter;
    private SwipyRefreshLayout refreshLayout;
    private LinearLayoutManager linearLayoutManager;

    /**
     * 初始化数据
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_swipe_activity_layout);

        recyclerView= (RecyclerView) findViewById(R.id.recyerview);
        refreshLayout= (SwipyRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);

        arrayList=new ArrayList();
        initData(1);

        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter=new SwipeAdapter(this,arrayList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 网络请求
     */
    private void initData(int pages) {
        //使用retrofit配置api
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://gank.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        SwipeApi api =retrofit.create(SwipeApi.class);
        Call<DataInfo> call=api.getData(20,pages);
        call.enqueue(new Callback<DataInfo>() {
            @Override
            public void onResponse(Call<DataInfo> call, Response<DataInfo> response) {

                arrayList.addAll(response.body().results);
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<DataInfo> call, Throwable t) {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 下拉刷新 上拉加载
     */
    @Override
    public void onRefresh(int index) {
        dataOption(TOP_REFRESH);
    }

    @Override
    public void onLoad(int index) {
        dataOption(BOTTOM_REFRESH);
    }

    private void dataOption(int option){
        switch (option) {
            case TOP_REFRESH:
                //下拉刷新
                arrayList.clear();
                initData(1);
                break;
            case BOTTOM_REFRESH:
                //上拉加载更多
                pages++;
                initData(pages);
                break;
        }
    }

    /**
     * Adapter的点击事件处理
     */
    @Override
    public void onItemClick(View view, DataInfo.Info data) {
        Toast.makeText(SwipeActivity.this, "click item " + data, Toast.LENGTH_SHORT).show();
    }
}
