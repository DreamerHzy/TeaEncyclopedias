package com.ckw.teaencyclopedias.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ckw on 2016/11/14.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public MySQLiteOpenHelper(Context context){
        super(context, "data.db", null, 1);
    }

    /**
     *
     contentTitle = intent.getStringExtra("title");
     imgPath = intent.getStringExtra("imgPath");
     contentSource = intent.getStringExtra("source");
     nickName = intent.getStringExtra("nickName");
     createTime = intent.getStringExtra("createTime");
     description = intent.getStringExtra("description");
     * @param db
     */

    //建了一个名为 history的表，有六个主要的属性
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table history (_id integer primary key autoincrement," +
                "title varchar,imgPath varchar,source varchar,nickName varchar," +
                "createTime varchar,description varchar,keyId varchar)");

        db.execSQL("create table collection (_id integer primary key autoincrement," +
                "title varchar,imgPath varchar,source varchar,nickName varchar," +
                "createTime varchar,description varchar,keyId varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
