package com.example.wangming.wanandroid;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by wangming on 2018/5/28.
 */

public class ListAdapter extends ArrayAdapter<Lists.DataBean>{
    private int resourceId;
    public ListAdapter(Context context,int textViewResourceId,
                       List<Lists.DataBean> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }
}
