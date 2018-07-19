package com.example.wangming.wanandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by wangming on 2018/6/10
 */


public class BannerAdapter extends PagerAdapter {
    private List<ImageView> list = new ArrayList<>();
    private List<Banner.DataBean> banner_list = new ArrayList<>();
    private Context context;

    public BannerAdapter(List<ImageView> lists, List<Banner.DataBean> banner_Lists,
                         Context context) {
//        list.clear();
//        banner_list.clear();
        this.list.addAll(lists);
        this.context = context;
        this.banner_list.addAll(banner_Lists);
    }

    public BannerAdapter(List<ImageView> list,Context context) {
        this.list.addAll(list);
        this.context = context;
    }

   public void setBanaer(List<Banner.DataBean> banner){
        banner_list.clear();
        banner.addAll(banner);
   }
    @Override
    public int getCount() {
           return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

//    @Override
//    public CharSequence getPageTitle(int position){
//        return banner_list.get((position - 1 + banner_list.size())%banner_list.size()).getTitle();
//    }

    //滑动一次调用一次
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

//        Log.e(TAG, "instantiateItem: 中position" +  position  );

        final int realPosition = (position - 1 + banner_list.size())%banner_list.size();

//        Log.e(TAG, "instantiateItem: 中realPosition" + realPosition );

        ImageView imageView = list.get(realPosition);//使view不超出界限 实现无限循环
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Banner.DataBean banner = banner_list.get(realPosition);
        Glide.with(context)
                .load(banner.getImagePath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Banner.DataBean banner = banner_list.get(realPosition);
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("extra_data", banner.getUrl());
                context.startActivity(intent);
                Log.e(TAG, "当前的  : 中realposition" +  realPosition  );
                Log.e(TAG, "当前的  : 中position" +  position  );
                Log.e(TAG,"中"+list.size());
            }
        });
        if(imageView.getParent()!=null){
            ((ViewPager)imageView.getParent()).removeView(imageView);
        }
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //container.removeView((View) object);
    }


}

