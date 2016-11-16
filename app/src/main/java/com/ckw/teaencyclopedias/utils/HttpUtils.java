package com.ckw.teaencyclopedias.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ckw on 2016/11/11.
 */
public class HttpUtils {
    public static byte[] downloadData(String path) {
       // Log.d("flag", "----------------->downloadData: " +path);

        byte[] data = null;


        //1.创建URL对象
        InputStream is = null;
        ByteArrayOutputStream bs = null;
        URL url = null;
        try {
            url = new URL(path);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setConnectTimeout(3000);
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            //Log.d("flag", "----------------->downloadData: "+path);
            //Log.d("flag", "----------------->downloadData: "+responseCode);
            if(responseCode==200){
                //Log.d("flag", "----->downloadByte连网成功 " );
                is = con.getInputStream();
                bs = new ByteArrayOutputStream();

                byte[] buf = new byte[1024*8];
                int len = 0;

                while((len = is.read(buf))!=-1){
                    bs.write(buf,0,len);
                    bs.flush();
                }

                data = bs.toByteArray();
                //Log.d("flag", "----------------->downloadData: "+(data));
                return  data;

            }
        } catch (MalformedURLException e) {
            Log.d("flag", "----------------->downloadData: ");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("flag", "----------------->downloadData: ");
            e.printStackTrace();
        }


        return null;


        /*OkHttpClient client = new OkHttpClient();

        Request.Builder builder = new Request.Builder();

        builder.url(path);

        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {

                byte[] bytes = response.body().bytes();

                return bytes;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;*/
    }
}
