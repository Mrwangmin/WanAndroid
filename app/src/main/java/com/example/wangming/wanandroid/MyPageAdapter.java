package com.example.wangming.wanandroid;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangming on 2018/6/7.
 */

class MyPagerAdapter extends PagerAdapter {
    private List<Banner.DataBean> mBannerList;
    private Context context;

    public MyPagerAdapter(Context context,List<Banner.DataBean> mBannerList) {
        this.mBannerList = mBannerList;
        this.context = context;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new  ImageView(context);
//        View view = container;
////        ViewHolder holder = new ViewHolder();
        Banner.DataBean banner = mBannerList.get(position);
//        ImageView bannerImage = view.findViewById(R.id.banner_image);
//        TextView bannerText = view.findViewById(R.id.banner_name);
        /****设置图片*/
        Glide.with(context)
                .load(banner.getImagePath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
        container.addView(imageView);
                return true;
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
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
