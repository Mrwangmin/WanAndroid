package com.example.wangming.wanandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wangming on 2018/5/1.
 */

public class sqllite extends SQLiteOpenHelper {
    public static final String USER = "create table Book("
            +"id integer"
            +"key text"
            +"name text";
    private Context mcontext;
    public sqllite(Context context, String name,
                   SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mcontext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(USER);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }

}
