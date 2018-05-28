package com.example.wangming.wanandroid;

import android.app.Application;
import android.content.Context;

/**
 * Created by wangming on 2018/5/26.
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
