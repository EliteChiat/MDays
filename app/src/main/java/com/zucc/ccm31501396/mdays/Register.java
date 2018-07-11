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

public class Register extends AppCompatActivity {
    private SharedPreferences register_sp;
    private String username;
    private String password;
    private String conf_password;

    EditText edt_Username = (EditText) findViewById(R.id.username);
    EditText edt_Password = (EditText) findViewById(R.id.password);
    EditText edt_Conf_password = (EditText) findViewById(R.id.conf_password);
    Button but_Register = (Button) findViewById(R.id.button_register);
    Button but_To_Login = (Button) findViewById(R.id.button_to_login);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        but_Register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                username = edt_Username.getText().toString();
                password = edt_Password.getText().toString();
                conf_password = edt_Conf_password.getText().toString();
                //判断输入是否为空，再将数据存纯如SharedPreferences
                if (username.trim().equals("")||password.trim().equals("")||conf_password.trim().equals("")){
                    Toast.makeText(Register.this,"用户名密码不能为空",Toast.LENGTH_LONG).show();
                    return;
                }else if(!(password.trim().equals(conf_password.trim()))){
                    Toast.makeText(Register.this,"密码确认错误",Toast.LENGTH_LONG).show();
                    return;
                }

                register_sp = getSharedPreferences("userInfo",MODE_PRIVATE);
                SharedPreferences.Editor edit =register_sp.edit();
                edit.putString("username",username);
                edit.putString("password",password);
                edit.commit();
                Toast.makeText(Register.this,"注册成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
                finish();
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
