package com.example.wangming.wanandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
    private CollectionArticleData dbHelper;
    public List<Banner.DataBean> banner_json = new ArrayList<>();
    public List<Article.DataBean.DatasBean> list_json = new ArrayList<>();
    private DrawerLayout mDrawerLayout;
    private RecyclerView articleRecyclerView;
    private LinearLayoutManager articleLayoutManager;
    private int page = 0;
    private MyAdapter articleAdapter;
    private ViewPager viewPager;
    private List<ImageView> list;
    private String name;
    private String pictureUri,autograph1;
    private SwipeRefreshLayout swipeRefreshLayout;


    private static final String TAG = "Main2Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        name = intent.getStringExtra("extra_data");
        articleRecyclerView = findViewById(R.id.list_view);
        articleLayoutManager = new LinearLayoutManager(this);
        articleRecyclerView.setLayoutManager(articleLayoutManager);
        new MyAsyncTaskBanner().execute();
        setnavigation();//侧滑
        setSwipeRefreshLayout();
    }
    private void setSwipeRefreshLayout(){
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                new MyAsyncTaskBanner().execute();
                articleAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    public void setnavigation(){
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        View headview = navView.inflateHeaderView(R.layout.nav_header);
        setNavHead(headview);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        }
        navView.setCheckedItem(R.id.mycollection);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.mycollection:
                        Intent intent = new Intent(Main2Activity.this,MyCollectionActivity.class);
                        intent.putExtra("extra_data",name);
                        startActivity(intent);
                        break;
                    case R.id.exit:
                        Intent intent1 = new Intent(Main2Activity.this,MainActivity.class);
                        startActivity(intent1);
                    default:
                        mDrawerLayout.closeDrawers();
                }
                return true;
            }
        });
    }
    private void setNavHead(View headview){
        SharedPreferences preferences = getSharedPreferences("uerifo",MODE_PRIVATE);
        pictureUri = preferences.getString("pictureuri","");
        autograph1 = preferences.getString("autograph","");
        ImageView userhead = headview.findViewById(R.id.icon_image);
        TextView autograph = headview.findViewById(R.id.autograph);
        if (pictureUri.equals("")){
            Resources res = Main2Activity.this.getResources();
            Bitmap bitmap = BitmapFactory.decodeResource(res,R.mipmap.nav_icon);
            userhead.setImageBitmap(bitmap);
        }else {
            Bitmap bitmap = BitmapFactory.decodeFile(pictureUri);
            userhead.setImageBitmap(bitmap);
        }
        if (autograph1.equals("")){
            autograph.setText("设置你的个性签名吧");
        }else {
            autograph.setText(autograph1);
        }
        userhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,UserActivity.class);
                intent.putExtra("extra_data",name);
                startActivity(intent);
            }
        });
        autograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,UserActivity.class);
                intent.putExtra("extra_data",name);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }
    private void bannerJson(String jsonData) {
        try {
            Banner banner = new Gson().fromJson(jsonData, new TypeToken<Banner>() {
            }.getType());
            banner_json.addAll(banner.getData());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void listJson(String jsonData) {
        try {
            Article article = new Gson().fromJson(jsonData, new TypeToken<Article>(){
            }.getType());
            list_json.clear();
            list_json.addAll(article.getData().getDatas());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class MyAsyncTaskBanner extends AsyncTask<Void,Integer, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://www.wanandroid.com/banner/json")
                    .build();
            String responseData = null;
            Response response = null;
            try {
                response = client.newCall(request).execute();
                responseData = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            bannerJson(responseData);
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result){
            viewPager = findViewById(R.id.viewPager);
            list = new ArrayList<>();
            for (int i = 0; i < banner_json.size(); i++) {
                ImageView imageView = new ImageView(Main2Activity.this);
                list.add(imageView);
            }
//            BannerAdapter pagerAdapter = new BannerAdapter(list,banner_json,Main2Activity.this);
//            viewPager.setAdapter(pagerAdapter);
            new MyAsyncTaskArticle().execute();
        }
    }

    class MyAsyncTaskArticle extends AsyncTask<Void,Integer, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params){
            OkHttpClient client = new OkHttpClient();
            Response response;
            Request request = new Request.Builder()
                    .url("http://www.wanandroid.com/article/list/"+String.valueOf(page)+"/json")
                    .build();
            Log.d("aaa","http://www.wanandroid.com/article/list/"+String.valueOf(page)+"/json");
            String responseData;
            try {
                response = client.newCall(request).execute();
                responseData = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            page++;
            listJson(responseData);
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result){
            if (page == 1) {
                articleAdapter = new MyAdapter(list, banner_json, name, Main2Activity.this, list_json);
                articleRecyclerView.addOnScrollListener(scrollListener);
                articleRecyclerView.setAdapter(articleAdapter);
            }else if (page <= 3){
                articleAdapter.setMdata(list_json);
                articleRecyclerView.getAdapter().notifyDataSetChanged();
            }else {
                Toast.makeText(getApplicationContext(),"哼 不准往下滑了！！",Toast.LENGTH_SHORT).show();
            }
        }
    }
    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (newState == RecyclerView.SCROLL_STATE_IDLE){
                int lastVisiblePosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisiblePosition+1==recyclerView.getAdapter().getItemCount()){
                    new MyAsyncTaskArticle().execute();
                }
            }
        }
    };
}