package com.zucc.ccm31501396.mdays;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zucc.ccm31501396.mdays.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {
    private SharedPreferences register_sp;
    private String username;
    private String password;
    private String conf_password;
    private String URL = "http://10.0.2.2:3000/users/register";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText edt_Username = (EditText) findViewById(R.id.username);
        final EditText edt_Password = (EditText) findViewById(R.id.password);
        final EditText edt_Conf_password = (EditText) findViewById(R.id.conf_password);
        Button but_Register = (Button) findViewById(R.id.button_register);
        Button but_To_Login = (Button) findViewById(R.id.button_to_login);

        but_Register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                username = edt_Username.getText().toString();
                password = edt_Password.getText().toString();
                conf_password = edt_Conf_password.getText().toString();
                //判断输入是否为空，再将数据存存入SharedPreferences
                if (username.trim().equals("")||password.trim().equals("")||conf_password.trim().equals("")){
                    Toast.makeText(Register.this,"用户名密码不能为空",Toast.LENGTH_LONG).show();
                    return;
                }else if(!(password.trim().equals(conf_password.trim()))){
                    Toast.makeText(Register.this,"密码确认错误",Toast.LENGTH_LONG).show();
                    return;
                }
                //封装数据
                RequestBody requestBody = new FormBody.Builder()
                        .add("userName",username)
                        .add("password",password)
                        .build();
                //向后台发送http请求并传入数据
                HttpUtil.postOKHttpRequest(URL,requestBody, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            final String status = jsonObject.getString("status");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(status.equals("1")){
                                        Toast.makeText(Register.this,"注册成功",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Register.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else if (status.equals("1002")){
                                        Toast.makeText(Register.this,"帐号已经被注册",Toast.LENGTH_LONG).show();
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

        but_To_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
