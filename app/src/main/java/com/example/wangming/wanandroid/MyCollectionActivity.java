package com.example.wangming.wanandroid;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyCollectionActivity extends AppCompatActivity {
    CollectionArticleData dbHelper;
    List<Article.DataBean.DatasBean> articleData = new ArrayList<>();
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        Intent intent = getIntent();
        name = intent.getStringExtra("extra_data");
        RecyclerView recyclerView = findViewById(R.id.collection);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        dbHelper = new CollectionArticleData(this,"Article",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Article",null,"username=?",new String[]{name},null,null,null);
        if (cursor.moveToFirst()){
            do {
                Article.DataBean.DatasBean article = new Article.DataBean.DatasBean();
                article.setId(cursor.getInt(cursor.getColumnIndex("id")));
                article.setAuthor(cursor.getString(cursor.getColumnIndex("author")));
                article.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                article.setLink(cursor.getString(cursor.getColumnIndex("link")));
                article.setNiceDate(cursor.getString(cursor.getColumnIndex("date")));
                article.setCollectiondate(cursor.getString(cursor.getColumnIndex("collectiondate")));
                articleData.add(article);
            }while (cursor.moveToNext());
        }
        cursor.close();
        CollectionAdapter adapter = new CollectionAdapter(MyCollectionActivity.this,name,articleData);
        recyclerView.setAdapter(adapter);
    }
}
