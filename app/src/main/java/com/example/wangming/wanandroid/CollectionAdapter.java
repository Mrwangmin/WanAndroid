package com.example.wangming.wanandroid;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by wangming on 2018/7/7.
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {
    public List<Article.DataBean.DatasBean> articleData = new ArrayList<>();
    private CollectionArticleData dbHelper;
    private Context context;
    private String name;
    private int boo;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView listTitle;
        TextView listAuthor;
        TextView listDate;
        ImageView listImage;
        ViewHolder(View view){
            super(view);
            listTitle = view.findViewById(R.id.list_title);
            listAuthor = view.findViewById(R.id.list_author);
            listDate = view.findViewById(R.id.list_date);
            listImage = view.findViewById(R.id.collection);
        }
    }

    CollectionAdapter(Context context,String name,List<Article.DataBean.DatasBean> data){
        this.context = context;
        this.name = name;
        this.articleData = data;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.listTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Article.DataBean.DatasBean article = articleData.get(position);
                Intent intent = new Intent(context,WebActivity.class);
                intent.putExtra("extra_data",article.getLink());
                context.startActivity(intent);
            }
        });
        holder.listImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Article.DataBean.DatasBean article = articleData.get(position);
                int id = article.getId();
                dbHelper = new CollectionArticleData(context,"Article",null,1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("Article",null,null,null,null,null,null);
//                if (cursor.moveToFirst()){
//                    boo = 0;
//                    do {
//                        String name1 = cursor.getString(cursor.getColumnIndex("username"));
//                        int id1 = cursor.getInt(cursor.getColumnIndex("id"));
//                        if (id == id1 && name.equals(name1)){
//                            db.delete("Article","id=? and username=?",new String[] {String.valueOf(id),name});
//                            holder.listImage.setImageResource(R.mipmap.collection);
//                            Toast.makeText(context,"取消收藏",Toast.LENGTH_SHORT).show();
//                            remove(position);
//                            break;
//                        }
//                        boo++;
//                    }while (cursor.moveToNext());
//                }
                db.delete("Article","id=? and username=?",new String[] {String.valueOf(id),name});
                holder.listImage.setImageResource(R.mipmap.collection);
                Toast.makeText(context,"取消收藏",Toast.LENGTH_SHORT).show();
                remove(position);
                cursor.close();
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Article.DataBean.DatasBean article = articleData.get(position);
        int id = article.getId();
        int a = 1;
        dbHelper = new CollectionArticleData(context,"Article",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Article", null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String name1 = cursor.getString(cursor.getColumnIndex("username"));
                int id1 = cursor.getInt(cursor.getColumnIndex("id"));
                if (id == id1 && name.equals(name1)){
                    holder.listImage.setImageResource(R.mipmap.be_collectioned);
                    a = 0;
                    break;
                }
            }while (cursor.moveToNext());
        }
        if (a == 1){
            holder.listImage.setImageResource(R.mipmap.collection);
        }
        holder.listAuthor.setText(article.getAuthor());
        holder.listDate.setText(article.getCollectiondate());
        holder.listTitle.setText(article.getTitle());
    }

    public void remove(int position){
        articleData.remove(position);
        notifyItemRemoved(position); // 提醒item删除指定数据，这里有RecyclerView的动画效果
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount(){
        return articleData.size();
    }
}
