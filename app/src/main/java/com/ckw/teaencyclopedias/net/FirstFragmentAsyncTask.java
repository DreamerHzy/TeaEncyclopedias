package com.ckw.teaencyclopedias.net;

import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.ckw.teaencyclopedias.beans.HotData;
import com.ckw.teaencyclopedias.interfaces.HotDataCallback;
import com.ckw.teaencyclopedias.utils.HttpUtils;

/**
 * Created by ckw on 2016/11/11.
 */

public class FirstFragmentAsyncTask extends AsyncTask<String,Void,HotData> {
    private HotDataCallback mCallback;

    public FirstFragmentAsyncTask(HotDataCallback callback) {
        mCallback = callback;
    }

    @Override
    protected HotData doInBackground(String... params) {

        byte[] bytes = HttpUtils.downloadData(params[0]);

        HotData hotData = JSON.parseObject(new String(bytes), HotData.class);

        return hotData;
    }

    @Override
    protected void onPostExecute(HotData data) {
        super.onPostExecute(data);

        mCallback.callbackHotData(data);
    }
}
