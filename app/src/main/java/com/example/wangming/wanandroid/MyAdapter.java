package com.example.wangming.wanandroid;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private List<ImageView> dot_list = new ArrayList<>();
    private int k = 0;

    private boolean hasDrawDots = false;

    public MyAdapter(String name, Context context){
        this.context = context;
        this.name = name;

    }

    public void setMdata(List<Article.DataBean.DatasBean> data){
        articleData.addAll(data);
    }
    public void setreMdata(List<Article.DataBean.DatasBean> data){
        articleData.clear();
        articleData.addAll(data);
    }

    public void setBannerData(List<Banner.DataBean> banners){
        banner_list.clear();
        banner_list.addAll(banners);
    }

    class BannerViewHolder extends RecyclerView.ViewHolder{
        private ViewPager viewPager;
        private LinearLayout linear;
        public BannerViewHolder(View itemView) {
            super(itemView);
            viewPager = itemView.findViewById(R.id.viewPager);
            linear = itemView.findViewById(R.id.ll_dots);
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
                if (k == 0){
                view = LayoutInflater.from(context).inflate(R.layout.banner
                        ,parent,false);
                viewHolder = new BannerViewHolder(view);}
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
                if (k == 0){
                final BannerViewHolder holder1 = (BannerViewHolder)holder;
                list = new ArrayList<>();
                list.clear();
                for (int i = 0; i < banner_list.size() + 2; i++) {
                    ImageView imageView = new ImageView(context);
                    list.add(imageView);
                }
                final BannerAdapter pagerAdapter = new BannerAdapter(list,banner_list,context);
                holder1.viewPager.setAdapter(pagerAdapter);
                if (!hasDrawDots){
                    addLiner(holder1.linear);
                }
                final Handler handler = new Handler(){
                   @Override
                    public void handleMessage(Message msg) {
                        int curindex = (holder1.viewPager.getCurrentItem()+1)%(banner_list.size()+2);
                        holder1.viewPager.setCurrentItem(curindex,true);
                    }
                };

                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        handler.sendMessage(message);
                    }
                };
                holder1.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        if ( position == banner_list.size() && positionOffset > 0.99) {
                            //在position9左滑且左滑positionOffset百分比接近1时，偷偷替换为position1（原本会滑到position10）
                            holder1.viewPager.setCurrentItem(1, false);

                        } else if (position == 0 && positionOffset < 0.01) {
                            //在position1右滑且右滑百分比接近0时，偷偷替换为position9（原本会滑到position0）
                            holder1.viewPager.setCurrentItem(banner_list.size(), false);
                        }

                    }
                    @Override
                    public void onPageSelected(int position) {
//                       /* 当有手动操作时，remove掉之前auto的runnable。延迟将由手动的这次决定。
//                        总之，一个页面selected之后  最多只有一个runnable，要把多的remove掉
                        handler.removeCallbacks(runnable);
                        if (position != banner_list.size()+1 && position != 0){
                            handler.postDelayed(runnable,3000);
                        }
                        for(int i=0;i<dot_list.size();i++){
                            if(i==(position - 1 + banner_list.size())%banner_list.size()){
                                dot_list.get(i).setImageResource(R.drawable.dots_focuse);
                            }else{
                                dot_list.get(i).setImageResource(R.drawable.dots_normal);
                            }
                        }

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        switch (state) {
                            case 0://什么都没做  空闲状态
                                break;
                            case 1://正在滑动
                                break;
                            case 2://滑动完毕
                                break;
                        }
                    }
                });}
                k++;
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
        if (articleData.size()==0){
            return 0;
        }else {
            return articleData.size()+1;
        }

    }


    private void addLiner(LinearLayout linear) {
        dot_list.clear();
        linear.removeAllViews();
        for(int i=0;i<banner_list.size();i++){
            ImageView imageview=new ImageView(context);
            imageview.setImageResource(R.drawable.dots_normal);
            dot_list.add(imageview);
            LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(20, 20);
            params.setMargins(5, 0, 5, 0);
            linear.addView(imageview, params);
        }
        hasDrawDots = true;
    }

}




