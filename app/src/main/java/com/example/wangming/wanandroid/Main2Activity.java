package com.example.wangming.wanandroid;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_OK = 1;
    private static final int REQUEST_ERROR = -1;
    public ArrayList<Banner.DataBean> banner_json = new ArrayList<>();
    public ArrayList<Lists.DataBean.DatasBean> list_json = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        new MyAsyncTask().execute();

    }

    @Override
    public void onClick(View v) {
    }

    private void makeBanner() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://www.wanandroid.com/banner/json")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    bannerJson(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void bannerJson(String jsonData) {
        try {
//            JSONObject object = new JSONObject(jsonData);
//            JSONArray jsonArray = object.getJSONArray("data");
            Banner banner = new Gson().fromJson(jsonData, new TypeToken<Banner>() {
            }.getType());
            for (Banner.DataBean dataBeans : banner.getData()) {
                int Id = dataBeans.getId();
                String Desc = dataBeans.getDesc();
                String ImagePath = dataBeans.getImagePath();
                String Title = dataBeans.getTitle();
                String Url = dataBeans.getUrl();
                int IsVisible = dataBeans.getIsVisible();
                int Order = dataBeans.getOrder();
                int Type = dataBeans.getType();
                banner_json.add(new Banner.DataBean(Desc, Id, ImagePath, IsVisible, Order, Title, Type, Url));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Message message = new Message();
            message.what = REQUEST_OK;
            handler.sendMessage(message);
        }
    }
    private void makeList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://www.wanandroid.com/article/list/1/json")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    listJson(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void listJson(String jsonData) {
        Message message = new Message();
        try {
            Lists lists = new Gson().fromJson(jsonData, new TypeToken<Lists>(){
            }.getType());
            /*for (Lists.DataBean.DatasBean datasBean : lists.getData().getDatas()) {
                list_json.add(datasBean);
            }*/
            for (int i=0; i<lists.getData().getDatas().size(); i++){
                list_json.set(i,lists.getData().getDatas().get(i));
            }
            //list_json.addAll(lists.getData().getDatas());
            Log.e("AAA", "listJson: "+list_json.size() );
            message.what = REQUEST_OK;
        } catch (Exception e) {
            message.what = REQUEST_ERROR;
            e.printStackTrace();
        } finally {
            handler.sendMessage(message);
        }
    }

    //
    class MyAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            makeList();
            makeBanner();
            return null;
        }

        //
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
//    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_OK:
                    Banner.DataBean bean = banner_json.get(1);
                    Log.e("BBB", "handleMessage: "+list_json.size());
                    Lists.DataBean.DatasBean datasBean = list_json.get(1);
                    Log.e(datasBean.getLink(),datasBean.getTitle());
                    RecyclerView recyclerView = findViewById(R.id.recycler_view);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(Main2Activity.this);
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(layoutManager);
                    BannerAdapter adapter = new BannerAdapter(banner_json, Main2Activity.this);
                    recyclerView.setAdapter(adapter);
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    Handler handlers = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case REQUEST_OK:
//                    Banner.DataBean bean = banner_json.get(1);
//                    Log.e(bean.getImagePath(),bean.getTitle());
                    Lists.DataBean.DatasBean datasBean = list_json.get(1);
                    Log.e(datasBean.getLink(),datasBean.getTitle());
                    break;
                default:
                    break;
            }
            return false;
        }
    });
}