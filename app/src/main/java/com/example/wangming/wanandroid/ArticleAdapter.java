package com.example.wangming.wanandroid;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangming on 2018/6/6.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
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
        public ViewHolder(View view){
            super(view);
            listTitle = view.findViewById(R.id.list_title);
            listAuthor = view.findViewById(R.id.list_author);
            listDate = view.findViewById(R.id.list_date);
            listImage = view.findViewById(R.id.collection);
        }
    }
    public void setMdata(List<Article.DataBean.DatasBean> data){
        articleData.addAll(data);
    }

    public ArticleAdapter(Context context,String name){
        this.context = context;
        this.name = name;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
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
                if (cursor.moveToFirst()){
                    boo = 0;
                    do {
                        String name1 = cursor.getString(cursor.getColumnIndex("username"));
                        int id1 = cursor.getInt(cursor.getColumnIndex("id"));
                        if (id == id1 && name.equals(name1)){
                            db.delete("Article","id=? and username=?",new String[] {String.valueOf(id),name});
                            holder.listImage.setImageResource(R.mipmap.collection);
                            Toast.makeText(context,"取消收藏",Toast.LENGTH_SHORT).show();
                            break;
                        }
                        boo++;
                    }while (cursor.moveToNext());
                }
                if (boo == cursor.getCount()){
                    ContentValues values = new ContentValues();
                    values.put("username",name);
                    values.put("id",article.getId());
                    values.put("author",article.getAuthor());
                    values.put("date",article.getNiceDate());
                    values.put("link",article.getLink());
                    values.put("title",article.getTitle());
                    db.insert("Article",null,values);
                    holder.listImage.setImageResource(R.mipmap.be_collectioned);
                    Toast.makeText(context,"收藏成功",Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Article.DataBean.DatasBean article = articleData.get(position);
        holder.listAuthor.setText(article.getAuthor());
        holder.listDate.setText(article.getNiceDate());
        holder.listTitle.setText(article.getTitle());
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
