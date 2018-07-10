package com.example.wangming.wanandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wangming on 2018/7/6.
 */

public class CollectionArticleData  extends SQLiteOpenHelper{
    public static final String CREATE_ARTICLE = "create table Article("
            +"id integer,"
            +"username text,"
            +"author text,"
            +"date text,"
            +"link text,"
            +"title text)";
    private Context mContext;
    public CollectionArticleData (Context context, String name,
                                  SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_ARTICLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}
