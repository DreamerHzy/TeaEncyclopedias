package com.ckw.teaencyclopedias.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.ckw.teaencyclopedias.interfaces.ImageCallback;
import com.ckw.teaencyclopedias.utils.HttpUtils;

/**
 * Created by ckw on 2016/11/11.
 */

public class ImageAsyncTask extends AsyncTask<String,Void,Bitmap> {

    private ImageCallback mCallback;

    public ImageAsyncTask(ImageCallback callback) {
        mCallback = callback;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        byte[] data = HttpUtils.downloadData(params[0]);

       // Log.d("flag", "----------------->doInBackground: " +data);
        Bitmap bitmap = null;
        if(data!=null){

             bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
        }

        return bitmap;

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mCallback.callbackImage(bitmap);
    }
}
