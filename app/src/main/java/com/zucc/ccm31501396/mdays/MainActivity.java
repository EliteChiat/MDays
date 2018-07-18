package com.zucc.ccm31501396.mdays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zucc.ccm31501396.mdays.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SharedPreferences login_sp;
    private SharedPreferences remember_sp;
    private String in_username;
    private String in_password;
    private String username;
    private String password;
    private  final String URL="http://10.0.2.2:3000/users/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText edt_Username = (EditText) findViewById(R.id.username);
        final EditText edt_Password = (EditText) findViewById(R.id.password);
        Button but_to_register = (Button) findViewById(R.id.button_to_register);
        Button but_login = (Button) findViewById(R.id.button_login);
        final CheckBox re_password = (CheckBox) findViewById(R.id.re_password);
        login_sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        remember_sp = getSharedPreferences("remember",MODE_PRIVATE);

        final boolean isRemenber = remember_sp.getBoolean("remember_password",false);

        //勾选过记住密码的账号密码补全
        if(isRemenber){
            username = remember_sp.getString("username","");
            password = remember_sp.getString("password","");
            edt_Username.setText(username);
            edt_Password.setText(password);
            re_password.setChecked(true);
        }

        re_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(MainActivity.this, "已勾选", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "未勾选", Toast.LENGTH_SHORT).show();
                    re_password.setChecked(false);
                }
            }
        });

        but_login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                in_username = edt_Username.getText().toString();
                in_password = edt_Password.getText().toString();

                //判断输入是否为空
                if(in_username.trim().equals("")||in_password.trim().equals("")){
                    Toast.makeText(MainActivity.this,"用户名密码不能为空",Toast.LENGTH_LONG).show();
                    return;
                }

                RequestBody requestBody = new FormBody.Builder()
                        .add("userName",in_username)
                        .add("password",in_password)
                        .build();
                HttpUtil.postOKHttpRequest(URL,requestBody, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "服务器未响应");
                                Toast.makeText(MainActivity.this,"服务器未响应",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final SharedPreferences.Editor edit = login_sp.edit();
                        final SharedPreferences.Editor edit2 = remember_sp.edit();
                        String responseData = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            final String status = jsonObject.getString("status");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(status.equals("1")){
                                        Log.d(TAG, "login success");
                                        if(re_password.isChecked()){
                                            Log.d(TAG, in_username);
                                            Log.d(TAG,in_password);
                                            edit.putString("username",in_username);
                                            edit.putString("password",in_password);
                                            edit.commit();
                                            edit2.putString("username",in_username);
                                            edit2.putString("password",in_password);
                                            edit2.putBoolean("remember_password",true);
                                            edit2.commit();
                                        }else{
                                            edit.putString("username",in_username);
                                            edit.putString("password",in_password);
                                            edit.commit();
                                            edit2.putBoolean("remember_password",false);
                                            edit2.commit();
                                        }
                                        Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this,UseAcitvity.class);
                                        startActivity(intent);
                                        finish();
                                    }else if(status.equals("1001")){
                                        Log.d(TAG, "login fail");
                                        Toast.makeText(MainActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        but_to_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });

    }
}
