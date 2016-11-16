package com.ckw.teaencyclopedias.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ckw.teaencyclopedias.MainActivity;
import com.ckw.teaencyclopedias.R;
import com.ckw.teaencyclopedias.adapters.FirstFragmentBaseAdapter;
import com.ckw.teaencyclopedias.beans.HotData;
import com.ckw.teaencyclopedias.interfaces.HotDataCallback;
import com.ckw.teaencyclopedias.net.FirstFragmentAsyncTask;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements HotDataCallback {

    private String content;
    private String searchUrl = "http://sns.maimaicha.com/" +
            "api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.searcListByTitle&search=";

    private BaseAdapter mAdapter;
    private ListView mListView;

    private TextView search_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        content = intent.getStringExtra("content");

        searchUrl += content;

        initView();

        initListView();

        initData();


    }

    private void initView() {
        search_content = (TextView) findViewById(R.id.search_content);
        search_content.setText("检索关键字："+content);
    }

    private void initListView() {
        mListView = (ListView) findViewById(R.id.search_listView);
        mAdapter = new FirstFragmentBaseAdapter(this,mList);

        mListView.setAdapter(mAdapter);
    }

    private void initData() {
        new FirstFragmentAsyncTask(this).execute(searchUrl);
    }

    private List<HotData.DataBean> mList = new ArrayList<>();
    @Override
    public void callbackHotData(HotData data) {
        mList.addAll(data.getData());
        mAdapter.notifyDataSetChanged();
    }

    public void searchClick(View view) {

        switch (view.getId()){
            case R.id.search_backToMain:
                Intent jump = new Intent();
                jump.setClass(this, MainActivity.class);
                this.startActivity(jump);
                break;

            case R.id.search_backHome:
                Intent home = new Intent(Intent.ACTION_MAIN,null);
                home.addCategory(Intent.CATEGORY_HOME);
                this.startActivity(home);

                break;
        }


    }
}
