package com.example.wangming.wanandroid;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by wangming on 2018/7/7.
 */

public class MyAdapter extends RecyclerView.Adapter{
    public List<Article.DataBean.DatasBean> articleData = new ArrayList<>();
    private CollectionArticleData dbHelper;
    private Context context;
    private String name;
    private int boo;
    private List<ImageView> list;
    private List<Banner.DataBean> banner_list = new ArrayList<>();

    public MyAdapter(List<ImageView> list, List<Banner.DataBean> banner_List,String name,
                     Context context,List<Article.DataBean.DatasBean> articleData ){
        this.list = list;
        this.context = context;
        this.banner_list = banner_List;
        this.name = name;
        this.articleData.addAll(articleData);
    }
    public void setMdata(List<Article.DataBean.DatasBean> data){
        articleData.addAll(data);
    }
    class BannerViewHolder extends RecyclerView.ViewHolder{
        private ViewPager viewPager;
        public BannerViewHolder(View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.viewPager);
        }
    }
    static class ArticleViewHolder extends RecyclerView.ViewHolder{
        TextView listTitle;
        TextView listAuthor;
        TextView listDate;
        ImageView listImage;
        ArticleViewHolder(View view){
            super(view);
            listTitle = view.findViewById(R.id.list_title);
            listAuthor = view.findViewById(R.id.list_author);
            listDate = view.findViewById(R.id.list_date);
            listImage = view.findViewById(R.id.collection);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.banner
                        ,parent,false);
                viewHolder = new BannerViewHolder(view);
                break;
            default:
                view = LayoutInflater.from(context).inflate(R.layout.article_item
                        ,parent,false);
                viewHolder = new ArticleViewHolder(view);
                ((ArticleViewHolder) viewHolder).listTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = viewType;
                        Article.DataBean.DatasBean article = articleData.get(position-1);
                        Intent intent = new Intent(context,WebActivity.class);
                        intent.putExtra("extra_data",article.getLink());
                        context.startActivity(intent);
                    }
                });
                final RecyclerView.ViewHolder finalViewHolder = viewHolder;
                ((ArticleViewHolder)viewHolder).listImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = viewType;
                        Article.DataBean.DatasBean article = articleData.get(position-1);
                        int id = article.getId();
                        dbHelper = new CollectionArticleData(context,"Article",null,1);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        Cursor cursor = db.query("Article",null,null,null,
                                null,null,null);
                        if (cursor.moveToFirst()){
                            boo = 0;
                            do {
                                String name1 = cursor.getString(cursor.getColumnIndex("username"));
                                int id1 = cursor.getInt(cursor.getColumnIndex("id"));
                                if (id == id1 && name.equals(name1)){
                                    db.delete("Article","id=? and username=?",new String[] {String.valueOf(id),name});
                                    ((ArticleViewHolder) finalViewHolder).listImage.setImageResource(R.mipmap.collection);
                                    Toast.makeText(context,"取消收藏",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                boo++;
                            }while (cursor.moveToNext());
                        }
                        Date date = new Date();
                        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
                        if (boo == cursor.getCount()){
                            ContentValues values = new ContentValues();
                            values.put("username",name);
                            values.put("id",article.getId());
                            values.put("author",article.getAuthor());
                            values.put("date",article.getNiceDate());
                            values.put("collectiondate",dateFormat.format(date));
                            values.put("link",article.getLink());
                            values.put("title",article.getTitle());
                            db.insert("Article",null,values);
                            ((ArticleViewHolder) finalViewHolder).listImage.setImageResource(R.mipmap.be_collectioned);
                            Toast.makeText(context,"收藏成功",Toast.LENGTH_SHORT).show();
                        }
                        cursor.close();
                    }
                });
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case 0:
                BannerViewHolder holder1 = (BannerViewHolder)holder;
                list = new ArrayList<>();
                for (int i = 0; i < banner_list.size(); i++) {
                    ImageView imageView = new ImageView(context);
                    list.add(imageView);
                }
                BannerAdapter pagerAdapter = new BannerAdapter(list,banner_list,context);
                holder1.viewPager.setAdapter(pagerAdapter);
                break;
            default:
                final ArticleViewHolder holder2 = (ArticleViewHolder) holder;
                Article.DataBean.DatasBean article = articleData.get(position-1);
                holder2.listAuthor.setText(article.getAuthor());
                holder2.listDate.setText(article.getNiceDate());
                holder2.listTitle.setText(article.getTitle());
                int id = article.getId();
                int a = 1;
                dbHelper = new CollectionArticleData(context,"Article",null,1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("Article", null,"username=?",new String[]{name},
                        null,null,null);
                if (cursor.moveToFirst()){
                    do {
                        int id1 = cursor.getInt(cursor.getColumnIndex("id"));
                        if (id == id1){
                            holder2.listImage.setImageResource(R.mipmap.be_collectioned);
                            a = 0;
                            break;
                        }
                    }while (cursor.moveToNext());
                }
                if (a == 1){
                    holder2.listImage.setImageResource(R.mipmap.collection);
                }
        }
    }
    @Override public int getItemViewType(int position) {
        return position;
    }
    @Override public int getItemCount() {
        return articleData.size()+1;
    }

}




