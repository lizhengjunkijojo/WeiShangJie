package com.lzj.vtm.demo.blvs;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.RestVO;
import com.lzj.vtm.demo.R;

import org.json.JSONException;

import java.util.List;

public class PolyvOnlineVideoActivity extends Activity {

    private static final String TAG = "VideoList";
    private ListView lv_online;
    private PolyvOnlineListViewAdapter lv_online_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polyv_activity_online_video);
        findIdAndNew();
        initView();
    }

    private void findIdAndNew() {
        lv_online = (ListView) findViewById(R.id.lv_online);
    }

    private void initView() {
        new LoadVideoList().execute();
    }

    class LoadVideoList extends AsyncTask<String, String, List<RestVO>> {

        @Override
        protected List<RestVO> doInBackground(String... arg0) {
            try {
                return PolyvSDKClient.getInstance().getVideoList(1, 10);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<RestVO> result) {
            super.onPostExecute(result);
            if (result == null) return;
            lv_online_adapter = new PolyvOnlineListViewAdapter(PolyvOnlineVideoActivity.this, result);
            lv_online.setAdapter(lv_online_adapter);
            String a = result.toString();
            Log.i(TAG, a);
        }
    }

}
