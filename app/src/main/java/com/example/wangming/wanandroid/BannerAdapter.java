package com.example.wangming.wanandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wangming on 2018/6/10
 */


public class BannerAdapter extends PagerAdapter {
    private List<ImageView> list;
    private List<Banner.DataBean> banner_list;
    private Context context;

    public BannerAdapter(List<ImageView> list, List<Banner.DataBean> banner_List,
                     Context context) {
        this.list = list;
        this.context = context;
        this.banner_list = banner_List;
   }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public CharSequence getPageTitle(int positon){
        return banner_list.get(positon % banner_list.size()).getTitle();
    }
    //滑动一次调用一次
    @Override
    public Object instantiateItem(ViewGroup container, final int positon) {
        final int repositon = positon % banner_list.size();
        ImageView imageView = list.get(repositon);//使view不超出界限 实现无限循环
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Banner.DataBean banner = banner_list.get(repositon);
        Glide.with(context)
                .load(banner.getImagePath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Banner.DataBean banner = banner_list.get(repositon);
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("extra_data", banner.getUrl());
                context.startActivity(intent);
            }
        });
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

