package com.ckw.teaencyclopedias.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ckw.teaencyclopedias.R;
import com.ckw.teaencyclopedias.adapters.FirstFragmentBaseAdapter;
import com.ckw.teaencyclopedias.beans.HotData;
import com.ckw.teaencyclopedias.database.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private MySQLiteOpenHelper mSQLiteOpenHelper;
    private SQLiteDatabase mDatabase;

    private ListView mListView;
    private BaseAdapter mAdapter;
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
        setContentView(R.layout.activity_history);

        mSQLiteOpenHelper = new MySQLiteOpenHelper(this);

        mDatabase = mSQLiteOpenHelper.getReadableDatabase();

        initView();

        initListView();

        initData();



    }


    private void initListView() {

        mAdapter = new FirstFragmentBaseAdapter(this,mList);

        mListView.setAdapter(mAdapter);

        //监听事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent jumpToDetails = new Intent();

                jumpToDetails.putExtra("id",mId);
                jumpToDetails.putExtra("title",mTitle);
                jumpToDetails.putExtra("imgPath",mImgPath);
                jumpToDetails.putExtra("source",mSource);
                jumpToDetails.putExtra("nickName",mNickName);
                jumpToDetails.putExtra("createTime",mCreateTime);
                jumpToDetails.putExtra("description",mDescription);

                jumpToDetails.setClass(HistoryActivity.this,DetailsItemActivity.class);

                HistoryActivity.this.startActivity(jumpToDetails);
            }
        });

        //长按删除
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);

                LayoutInflater inflater = LayoutInflater.from(HistoryActivity.this);
                View layout = inflater.inflate(R.layout.dialog_view,null);

                TextView delete = (TextView) layout.findViewById(R.id.delete_dialog);
                TextView negative = (TextView) layout.findViewById(R.id.delete_negative);
                TextView title = (TextView) layout.findViewById(R.id.delete_title);
                title.setText(mTitle);

                builder.setView(layout);

                final AlertDialog dialog = builder.create();

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();//让前一个dialog消失

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(HistoryActivity.this);

                        View titleView = LayoutInflater.from(HistoryActivity.this)
                                .inflate(R.layout.custom_title,null);
                        builder1.setCustomTitle(titleView);

                        builder1.setMessage("确定要删除吗？");

                        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int delete = mDatabase.delete("history", "title = ? ", new String[]{mTitle});
                                if(delete>0){
                                    Toast.makeText(HistoryActivity.this, "删除成功：", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(HistoryActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                                }

                                mList.remove(position);

                                mAdapter.notifyDataSetChanged();

                            }
                        });
                        builder1.setNegativeButton("取消",null);

                        builder1.create().show();
                    }
                });

                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();


                return true;
            }
        });
    }

    private List<HotData.DataBean> mList = new ArrayList<>();

    /**
     * title varchar,imgPath varchar,source varchar,nickName varchar" +
     "createTime varchar,description varchar)
     */
    private void initData() {
        //从数据库从拿数据
        Cursor cursor = mDatabase.query(
                "history",
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
            dataBean.setId(mId);

            //目的：添加到数据源中
            mList.add(dataBean);

        }

        //通知适配器发生了改变
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.history_listView);
    }
}
