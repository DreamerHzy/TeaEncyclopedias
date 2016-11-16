package com.ckw.teaencyclopedias.net;

import android.os.AsyncTask;

import com.alibaba.fastjson.JSON;
import com.ckw.teaencyclopedias.beans.Detail;
import com.ckw.teaencyclopedias.interfaces.DetailCallback;
import com.ckw.teaencyclopedias.utils.HttpUtils;


/**
 * Created by ckw on 2016/11/14.
 */

public class DetailAsyncTask extends AsyncTask<String,Void,Detail> {

    private DetailCallback mCallback;

    public DetailAsyncTask(DetailCallback callback) {
        mCallback = callback;
    }

    @Override
    protected Detail doInBackground(String... params) {

        byte[] bytes = HttpUtils.downloadData(params[0]);

        Detail ret = JSON.parseObject(new String(bytes),Detail.class);

        return ret;
    }

    @Override
    protected void onPostExecute(Detail detail) {
        super.onPostExecute(detail);
        mCallback.callbackDetail(detail);
    }
}
