package com.example.wangming.wanandroid;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText userName;
    EditText userKey;
    Button SignIn;
    Button SingUp;
    SharedPreferences sp;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = findViewById(R.id.user_name);
        userKey = findViewById(R.id.user_key);
        SignIn = findViewById(R.id.sign_in);
        SingUp = findViewById(R.id.sign_up);
        checkBox = findViewById(R.id.checkbox);
        sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        init();
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    String name = userName.getText().toString();
                    String key = userKey.getText().toString();
                    String[] user = new String[2];
                    try {
                        user = getNameKey(name);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(name.equals(user[0])&&key.equals(user[1])){
                        Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                        intent.putExtra("extra_data",name);
                        startActivity(intent);
                    }else {
                        Toast.makeText(MainActivity.this,"账户密码错误",Toast.LENGTH_SHORT).show();
                    }

                    if (checkBox.isChecked()) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("username", name);
                        editor.putString("userkey", key);
                        editor.putBoolean("checkboxBoolean", true);
                        editor.commit();
                    }
                    else {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("username ", null);
                        editor.putString("userkey", null);
                        editor.putBoolean("checkboxBoolean", false);
                        editor.commit();
                    }
                }
            }
        });
        SingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Sign_up.class);
                startActivity(intent);
            }
        });
    }
    private void init(){
        if (sp.getBoolean("checkboxBoolean", false))
        {
            userName.setText(sp.getString("username", null));
            userKey.setText(sp.getString("userkey", null));
            checkBox.setChecked(true);
        }
    }

    @Override
    public void onClick(View view){

    }
    //对用户输入的用户名密码进行校验
    private boolean validate(){
        String name = userName.getText().toString().trim();
        String key = userKey.getText().toString().trim();
        if(name.equals("")){
            Toast.makeText(MainActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(key.equals("")){
            Toast.makeText(MainActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public String[] getNameKey(String name) throws FileNotFoundException {
        String[] users = new String[2];
        File file = new File("data/data/com.example.wangming.wanandroid/user.txt");
        Scanner input = new Scanner(file);
        while (input.hasNext()){
            users[0] = input.next();
            users[1] = input.next();
            if(users[0].equals(name)){
                return users;
            }
        }
        Toast.makeText(MainActivity.this,"用户名不存在",Toast.LENGTH_LONG).show();
        return users;
    }

}
