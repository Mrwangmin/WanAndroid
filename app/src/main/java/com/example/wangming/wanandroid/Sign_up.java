package com.example.wangming.wanandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class Sign_up extends AppCompatActivity {
    private EditText et_userName,et_userKey,et_userReKey;
    private Button et_signUp;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        et_userName = findViewById(R.id.et_user_name);
        et_userKey = findViewById(R.id.et_user_key);
        et_userReKey = findViewById(R.id.et_user_rekey);
        et_signUp = findViewById(R.id.et_sign_up);
        et_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = et_userName.getText().toString();
                String userkey = et_userKey.getText().toString();
                saveUserInfo(username,userkey);
//                SharedPreferences.Editor editor = getSharedPreferences("user",MODE_PRIVATE).edit();
//                editor.putString("name",username);
//                editor.putString("key",userkey);
//                editor.apply();
                Intent intent = new Intent(Sign_up.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public boolean saveUserInfo(String username, String password) {
        try {
            // 使用当前项目的绝对路径
            File file = new File("data/data/com.example.wangming.wanandroid/user.txt");
            // 创建输出流对象
            FileOutputStream fos = new FileOutputStream(file,true);
            // 向文件中写入信息
            fos.write((username + " " + password+" ").getBytes());
            // 关闭输出流对象
            fos.close();
            return true;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
