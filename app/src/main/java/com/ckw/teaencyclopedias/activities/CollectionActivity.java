package com.ckw.teaencyclopedias.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.ckw.teaencyclopedias.R;
import com.ckw.teaencyclopedias.adapters.FirstFragmentBaseAdapter;
import com.ckw.teaencyclopedias.beans.HotData;
import com.ckw.teaencyclopedias.database.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {

    private ListView mListView;
    private BaseAdapter mAdapter;
    private List<HotData.DataBean> mList = new ArrayList<>();

    private MySQLiteOpenHelper mSQLiteOpenHelper;
    private SQLiteDatabase mDatabase;
    private String mTitle;
    private String mImgPath;
    private String mSource;
    private String mNickName;
    private String mCreateTime;
    private String mDescription;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        mSQLiteOpenHelper = new MySQLiteOpenHelper(this);

        mDatabase = mSQLiteOpenHelper.getReadableDatabase();

        initListView();

        initData();
    }

    private void initData() {
        Cursor cursor = mDatabase.query(
                "collection",
                new String[]{"title","imgPath","source","nickName","createTime","description","keyId"},
                null,null,null,null,null);

        while (cursor.moveToNext()){
            HotData.DataBean dataBean = new HotData.DataBean();

            mTitle = cursor.getString(cursor.getColumnIndex("title"));
            mImgPath = cursor.getString(cursor.getColumnIndex("imgPath"));
            mSource = cursor.getString(cursor.getColumnIndex("source"));
            mNickName = cursor.getString(cursor.getColumnIndex("nickName"));
            mCreateTime = cursor.getString(cursor.getColumnIndex("createTime"));
            mDescription = cursor.getString(cursor.getColumnIndex("description"));
            mId = cursor.getString(cursor.getColumnIndex("keyId"));

            dataBean.setTitle(mTitle);
            dataBean.setWap_thumb(mImgPath);
            dataBean.setSource(mSource);
            dataBean.setNickname(mNickName);
            dataBean.setCreate_time(mCreateTime);
            dataBean.setDescription(mDescription);
            dataBean.setId(mId);//用于返回页面使用

            //目的：添加到数据源中
            mList.add(dataBean);

            //关闭数据库
            mDatabase.close();
        }

        //通知适配器发生了改变
        mAdapter.notifyDataSetChanged();
    }

    private void initListView() {
        mListView = (ListView) findViewById(R.id.collectionListView);

        mAdapter = new FirstFragmentBaseAdapter(this,mList);

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进行跳转,回到DetailsItemActivity
                Intent jumpToDetails = new Intent();

                jumpToDetails.putExtra("id",mId);
                jumpToDetails.putExtra("title",mTitle);
                jumpToDetails.putExtra("imgPath",mImgPath);
                jumpToDetails.putExtra("source",mSource);
                jumpToDetails.putExtra("nickName",mNickName);
                jumpToDetails.putExtra("createTime",mCreateTime);
                jumpToDetails.putExtra("description",mDescription);

                jumpToDetails.setClass(CollectionActivity.this,DetailsItemActivity.class);

                CollectionActivity.this.startActivity(jumpToDetails);
            }
        });

    }
}
