package com.example.wangming.wanandroid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangming on 2018/5/28.
 */

public class ListAdapter extends ArrayAdapter<Lists.DataBean.DatasBean> {
    private int resourceId;
    Context mcontext;
    public List<Lists.DataBean.DatasBean> list;

    public ListAdapter(Context context, int textViewResourceId,
                       List<Lists.DataBean.DatasBean> objects) {
        super(context, textViewResourceId, objects);
        mcontext = context;
        resourceId = textViewResourceId;
        this.list = objects;
    }
    //xin
    @Override
    public int getCount() {
        return list.size();
    }
//xin
    @Override
    public Lists.DataBean.DatasBean getItem(int position) {
        return list.get(position);
    }
//xin
    @Override
    public long getItemId(int position) {
        return position;
    }
    public void updateView(List<Lists.DataBean.DatasBean> nowList)
    {
        this.list = nowList;
        this.notifyDataSetChanged();//强制动态刷新数据进而调用getView方法
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Lists.DataBean.DatasBean bean = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent,
                    false);
            viewHolder = new ViewHolder();
            viewHolder.listTitle = view.findViewById(R.id.list_title);
            viewHolder.listAuthor = view.findViewById(R.id.list_author);
            viewHolder.listDate = view.findViewById(R.id.list_date);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.listTitle.setText(bean.getTitle());
        viewHolder.listAuthor.setText("作者：" + bean.getAuthor());
        viewHolder.listDate.setText(bean.getNiceDate());
        viewHolder.listTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,WebActivity.class);
                intent.putExtra("extra_data",bean.getLink());
                mcontext.startActivity(intent);
            }
        });
        return view;
    }

    class ViewHolder {
        TextView listTitle;
        TextView listAuthor;
        TextView listDate;
    }


}
