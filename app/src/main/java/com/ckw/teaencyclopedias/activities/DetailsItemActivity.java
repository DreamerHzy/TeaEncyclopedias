package com.ckw.teaencyclopedias.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.ckw.teaencyclopedias.MainActivity;
import com.ckw.teaencyclopedias.R;
import com.ckw.teaencyclopedias.beans.Detail;
import com.ckw.teaencyclopedias.database.MySQLiteOpenHelper;
import com.ckw.teaencyclopedias.interfaces.DetailCallback;
import com.ckw.teaencyclopedias.net.DetailAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class DetailsItemActivity extends AppCompatActivity implements DetailCallback {

    private String partPath = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2" +
            "afb754c33e6&format=json&method=news.getNewsContent&id=";

    private WebView mWebView;

    private TextView titleView,sourceView,timeView;

    private String id,contentTitle,imgPath,contentSource,nickName,createTime,description;

    private MySQLiteOpenHelper mSQLiteOpenHelper;
    private SQLiteDatabase mDatabase;
    /**
     * intent.putExtra("id",itemId);
     intent.putExtra("title",title);
     intent.putExtra("imgPath",path);
     intent.putExtra("source",source);
     intent.putExtra("nickName",nickName);
     intent.putExtra("createTime",createTime);
     intent.putExtra("description",description);
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_item);

        getSupportActionBar().hide();

        Intent intent = getIntent();//接收数据

        id = intent.getStringExtra("id");
        contentTitle = intent.getStringExtra("title");
        imgPath = intent.getStringExtra("imgPath");
        contentSource = intent.getStringExtra("source");
        nickName = intent.getStringExtra("nickName");
        createTime = intent.getStringExtra("createTime");
        description = intent.getStringExtra("description");


        partPath = partPath+id;

        initView();

        initWebView();

    }

    private void initView() {
        titleView = (TextView) findViewById(R.id.details_title);
        sourceView = (TextView) findViewById(R.id.details_source);
        timeView = (TextView) findViewById(R.id.details_time);
    }

    private void initWebView() {

        mWebView = (WebView) findViewById(R.id.webView_detail);
        //初始化操作
        //1.设置webView在本应用中加载数据
//        mWebView.setWebViewClient(new WebViewClient());
//        //2.设置webView实现一些js动画效果
//        mWebView.setWebChromeClient(new WebChromeClient());
//        //3.允许js交互
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        //4.允许缓存数据
//        mWebView.getSettings().setAppCacheEnabled(true);
//        //5.开启Dom storage API 功能
//        mWebView.getSettings().setDomStorageEnabled(true);
//        //6.开启 Database功能
//        mWebView.getSettings().setDatabaseEnabled(true);

        //加载数据
          new DetailAsyncTask(this).execute(partPath);


    }


    private String content;
    @Override
    public void callbackDetail(Detail detail) {

        content = detail.getData().getWap_content();

        timeView.setText(createTime);
        sourceView.setText(contentSource);
        titleView.setText(contentTitle);

        mWebView.loadDataWithBaseURL(null,content,"text/html","utf-8",null);

    }

    public void detailsClick(View view) {
        switch (view.getId()){
            case R.id.contextback://回到主界面

                Intent jump = new Intent(this, MainActivity.class);

                this.startActivity(jump);

                break;

            case R.id.collectcontent://收藏到数据库

                storeToCollectionDB();

                Toast.makeText(this,"收藏成功!",Toast.LENGTH_SHORT).show();

                break;
        }
    }

    private void storeToCollectionDB(){
        mSQLiteOpenHelper = new MySQLiteOpenHelper(this);
        mDatabase = mSQLiteOpenHelper.getReadableDatabase();

        //先对数据库进行查询，判断数据库中是否存在这条数据
        List<String> historyTitle = new ArrayList<String>();
        Cursor history = mDatabase.query("collection", new String[]{"title"}, null, null, null, null, null);

        while (history.moveToNext()){
            String str = history.getString(history.getColumnIndex("title"));
            historyTitle.add(str);
        }

        boolean isExist = false;
        if(historyTitle.size()>0){//如果数据库中有数据
            for (int i = 0; i < historyTitle.size(); i++) {
                if(contentTitle.equals(historyTitle.get(i))){//只要数据库中有，就表示为true
                    isExist = true;
                }
            }
        }

        if(!isExist){//表示数据库中不存在该条数据，需要向数据库中添加数据
            //这里需要传id
            ContentValues contentValues = new ContentValues();
            contentValues.put("createTime",createTime);
            contentValues.put("title",contentTitle);
            contentValues.put("source",contentSource);
            contentValues.put("nickName",nickName);
            contentValues.put("description",description);
            contentValues.put("imgPath",imgPath);
            contentValues.put("keyId",id);

            mDatabase.insert("collection",null,contentValues);
        }

    }
}
