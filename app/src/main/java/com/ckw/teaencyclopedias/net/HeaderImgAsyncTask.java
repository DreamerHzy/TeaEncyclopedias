package com.ckw.teaencyclopedias.net;

import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ckw.teaencyclopedias.beans.HeaderImg;
import com.ckw.teaencyclopedias.interfaces.HeaderImgCallback;
import com.ckw.teaencyclopedias.utils.HttpUtils;

/**
 * Created by ckw on 2016/11/14.
 */

public class HeaderImgAsyncTask extends AsyncTask<String,Void,HeaderImg> {

    private HeaderImgCallback mHeaderImgCallback;

    public HeaderImgAsyncTask(HeaderImgCallback headerImgCallback) {
        mHeaderImgCallback = headerImgCallback;
    }

    @Override
    protected HeaderImg doInBackground(String... params) {

        byte[] bytes = HttpUtils.downloadData(params[0]);

        //Log.d("flag", "----------------->doInBackground: 数据的长度=" +bytes.length);

        HeaderImg headerImg = JSON.parseObject(bytes,HeaderImg.class);

        if(headerImg==null){
            Log.d("flag", "----------------->doInBackground: 对象为空");
        }

        return headerImg;
    }

    @Override
    protected void onPostExecute(HeaderImg headerImg) {
        super.onPostExecute(headerImg);
        Log.d("flag", "----------------->onPostExecute: 对象中数据的长度：=" +headerImg.getData().size());

        mHeaderImgCallback.callbackHeaderImg(headerImg);
    }
}
