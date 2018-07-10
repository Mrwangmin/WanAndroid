//package com.example.wangming.wanandroid;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.os.Parcelable;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.widget.RecyclerView;
//import android.util.DisplayMetrics;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.xyliwp.news.R;
//import com.xyliwp.news.bean.TuiJianMessage;
//import com.xyliwp.news.view.myview.MyViewPager;
//import com.xyliwp.news.view.viewpageranim.LRZheDie;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by wenpengli on 2017/3/12.
// */
//public class TuiJianRecyclerViewAdapter extends RecyclerView.Adapter{
//
//    private ArrayList<TuiJianMessage> addPinDaos;
//    private Context context;
//    private ViewHolderOne viewHolderOne;
//
//    private int currentItem = 0; // 当前图片的索引号
//    private List<View> views; // 滑动原点的view
//    // 切换当前图片
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            // 显示当前图片
//            viewHolderOne.myViewPager.setCurrentItem(currentItem);
//        }
//    };
//
//    public TuiJianRecyclerViewAdapter(Context context,ArrayList<TuiJianMessage> addPinDaos){
//        super();
//        this.addPinDaos = addPinDaos;
//        this.context = context;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = null;
//        RecyclerView.ViewHolder viewHolder = null;
//        switch (viewType){
//            case 0:
//                view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_tuijian_tou
//                        ,parent,false);
//                viewHolder = new ViewHolderOne(view);
//                break;
//            case 1:
//                view = LayoutInflater.from(context).inflate(R.layout.item_recyclerview_tuijian
//                        ,parent,false);
//                viewHolder = new ViewHolderMy(view);
//                break;
//        }
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        TuiJianMessage addPinDao = addPinDaos.get(position);
//        switch (getItemViewType(position)){
//            //viewpager
//            case 0:
//                viewHolderOne = (ViewHolderOne)holder;
//                viewHolderOne.textview_Viewpager.setText(addPinDaos.get(0).getTitle());
//
//                viewpagerecommendAdapter adapter = new viewpagerecommendAdapter(context);
//
//                viewHolderOne.myViewPager.setPageTransformer(true,new LRZheDie());
//                viewHolderOne.myViewPager.setAdapter(adapter);
//                viewHolderOne.myViewPager.setOnPageChangeListener(new viewpagerRecommendPageChangeListener());
//
//                ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//                scheduledExecutorService.scheduleAtFixedRate(new YuanDianRun(), 3, 6,
//                        TimeUnit.SECONDS);
//                break;
//            case 1:
//                final ViewHolderMy viewHolderMy = (ViewHolderMy)holder;
//                Glide.with(context).load(addPinDao.getPicUrl()).
//                        override(100, 60).centerCrop().into(viewHolderMy.imageView_my);
//                viewHolderMy.textView_my.setText(addPinDao.getTitle());
//                break;
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return addPinDaos.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if(position == 0){
//            return 0;
//        }else{
//            return 1;
//        }
//    }
//
//    class ViewHolderMy extends RecyclerView.ViewHolder{
//        private TextView textView_my;
//        private ImageView imageView_my;
//        public ViewHolderMy(View itemView) {
//            super(itemView);
////            imageView_my = (ImageView)itemView.findViewById(R.id.android_image);
////            textView_my = (TextView)itemView.findViewById(R.id.android_version);
//        }
//    }
//    class ViewHolderOne extends RecyclerView.ViewHolder{
////        private MyViewPager myViewPager;
//        private TextView textview_Viewpager;
//        private View textview_tuijian_tou1;
//        private View textview_tuijian_tou2;
//        private View textview_tuijian_tou3;
//        private View textview_tuijian_tou4;
//        private View textview_tuijian_tou5;
//        public ViewHolderOne(View itemView) {
//            super(itemView);
////            myViewPager = (MyViewPager) itemView.findViewById(R.id.myViewPage_tuijian_tou);
////            textview_Viewpager = (TextView)itemView.findViewById(R.id.textview_Viewpager);
////            textview_tuijian_tou1 = (View)itemView.findViewById(R.id.textview_tuijian_tou1);
////            textview_tuijian_tou2 = (View)itemView.findViewById(R.id.textview_tuijian_tou2);
////            textview_tuijian_tou3 = (View)itemView.findViewById(R.id.textview_tuijian_tou3);
////            textview_tuijian_tou4 = (View)itemView.findViewById(R.id.textview_tuijian_tou4);
////            textview_tuijian_tou5 = (View)itemView.findViewById(R.id.textview_tuijian_tou5);
//            //初始化原点
//            views = new ArrayList<View>();
//            views.add(textview_tuijian_tou1);
//            views.add(textview_tuijian_tou2);
//            views.add(textview_tuijian_tou3);
//            views.add(textview_tuijian_tou4);
//            views.add(textview_tuijian_tou5);
//        }
//    }
//
//    public int getWidth(){
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;// 屏幕宽度（像素）
//        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
//        //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
//        return  (int) (width/density);//屏幕宽度(dp)
//    }
//
//    //viewpagger的PageChangeListener
//    private class viewpagerRecommendPageChangeListener implements ViewPager.OnPageChangeListener {
//
//        private int oldposition = 0;
//
//        @Override
//        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//        }
//        @Override
//        public void onPageSelected(int position) {
//
//            currentItem = position;
////            viewHolderOne.textview_Viewpager.setText(addPinDaos.get(position).getTitle());
////            views.get(oldposition).setBackgroundResource(R.drawable.dot_normal);
////            views.get(position).setBackgroundResource(R.drawable.dot_focused);
//            oldposition = position;
//
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int state) {
//
//        }
//    }
//    //viewpager的aderpter
//
//    private class viewpagerecommendAdapter extends PagerAdapter {
//
//        private Context mContext;
//
//        public viewpagerecommendAdapter(Context mContext) {
//            this.mContext = mContext;
//        }
//
//        @Override
//        public int getCount() {
//            return 5;
//        }
//
//        @Override
//        public Object instantiateItem(View container, int position) {
//            ImageView imageview = new ImageView(context);
//            Glide.with(mContext).load(addPinDaos.get(position).getPicUrl()).
//                    override(getWidth(), 150).centerCrop().into(imageview);
//            ((ViewPager) container).addView(imageview);
//            return imageview;
//        }
//
//        @Override
//        public void destroyItem(View container, int position, Object object) {
//            ((ViewPager) container).removeView((View) object);
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//
//        @Override
//        public void restoreState(Parcelable arg0, ClassLoader arg1) {
//
//        }
//
//        @Override
//        public Parcelable saveState() {
//            return null;
//        }
//
//        @Override
//        public void startUpdate(View arg0) {
//
//        }
//
//        @Override
//        public void finishUpdate(View arg0) {
//
//        }
//
//    }
//
//    private class YuanDianRun implements Runnable {
//
//        @Override
//        public void run() {
//            synchronized (viewHolderOne.myViewPager) {
//                currentItem = (currentItem + 1) % 5;
//                // handler切换图片
//                handler.obtainMessage().sendToTarget();
//            }
//        }
//    }
//}