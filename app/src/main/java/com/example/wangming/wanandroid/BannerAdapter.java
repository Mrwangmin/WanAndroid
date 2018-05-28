package com.example.wangming.wanandroid;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wangming on 2018/5/24.
 */

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder>{
    private ArrayList<Banner.DataBean> mBannerList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView bannerImage;
        TextView bannerText;
        View bannerView;
        public ViewHolder(View view){
            super(view);
            bannerImage = view.findViewById(R.id.banner_image);
            bannerText = view.findViewById(R.id.banner_name);
            bannerView = view;
        }
    }
    public BannerAdapter(ArrayList<Banner.DataBean> bannerList, Context context){
        mBannerList = bannerList;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent,int viewType){
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.banner,parent,false);
        final ViewHolder holder = new ViewHolder(view);
//        holder.bannerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = holder.getAdapterPosition();
//                Banner.DataBean banner = mBannerList.get(position);
//                Intent intent = new Intent(context,WebActivity.class);
//                Log.e("aa","********************");
//                intent.putExtra("extra_data",banner.getUrl());
//                Log.e("1","*********************");
//                context.startActivity(intent);
//                Log.e("2","*********************");
//            }
//        });
        holder.bannerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Banner.DataBean banner = mBannerList.get(position);
                Intent intent = new Intent(context,WebActivity.class);
                intent.putExtra("extra_data",banner.getUrl());
                context.startActivity(intent);
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Banner.DataBean banner = mBannerList.get(position);
        Glide.with(context)
                .load(banner.getImagePath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .into(holder.bannerImage);
        holder.bannerText.setText(banner.getTitle());
    }
    @Override
    public int getItemCount() {
        return mBannerList.size();
    }

}
