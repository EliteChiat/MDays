package com.zucc.ccm31501396.mdays;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zucc.ccm31501396.mdays.Util.HttpUtil;
import com.zucc.ccm31501396.mdays.Util.JsonUtil;
import com.zucc.ccm31501396.mdays.data.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SingleAccount extends AppCompatActivity {
    private EditText price_edit;
    private Spinner type_spinner;
    private String[] type_list = {"工资收入","食品支出","交通支出","娱乐支出","医药支出","其他支出"};
    private ArrayAdapter<String> type_adapter;
    private EditText note_edit;
    private TextView date_edit;
    private AppCompatButton change_button;
    private AppCompatButton delect_button;
    private String accountId;
    private float price;
    private String date;
    private String note;
    private String typeName;
    private Calendar c;
    private Integer typeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_account);
        Intent intent = getIntent();
        accountId = intent.getStringExtra("result");
        price_edit = (EditText)findViewById(R.id.ed_price);
        date_edit = (TextView)findViewById(R.id.ed_date);
        note_edit = (EditText)findViewById(R.id.ed_note);
        type_spinner = (Spinner)findViewById(R.id.type_spinner);
        type_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,type_list);
        change_button = (AppCompatButton) findViewById(R.id.change_button);
        delect_button = (AppCompatButton) findViewById(R.id.delete_button);

        //发送Http请求来获取此Id的账单内容
        RequestBody requestBody = new FormBody.Builder()
                .add("accountId",accountId)
                .build();
        HttpUtil.postOKHttpRequest("http://10.0.2.2:3000/price/singaccount",requestBody, new okhttp3.Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(SingleAccount.this,"服务器无响应",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseData);
                    final String status = jsonObject.getString("status");
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if(status.equals("1")){
                                Toast.makeText(SingleAccount.this,"收到数据",Toast.LENGTH_SHORT).show();
                                final Account result = JsonUtil.DealSingleAccountResponce(responseData);
                                price_edit.setText(String.valueOf(result.getPrice()));
                                type_spinner.setAdapter(type_adapter);
                                type_spinner.setSelection(getTypePosition(result.getTypeName()),true);
                                date_edit.setText(result.getDate());
                                note_edit.setText(result.getNote());
                            }else{
                                Toast.makeText(SingleAccount.this,"错误",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //初始化下拉菜单并且选择显示数据的那个类型
//        type_spinner.setAdapter(type_adapter);
//        type_spinner.setSelection(0,true);
        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //将选择的类型放入type变量
                typeName=(String)type_spinner.getItemAtPosition(position);
                Log.d("typeName:", typeName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateDialog().show();
            }
        });

        delect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody requestBody = new FormBody.Builder()
                        .add("accountId",accountId)
                        .build();
                HttpUtil.postOKHttpRequest("http://10.0.2.2:3000/price/deleteaccount",requestBody, new okhttp3.Callback() {

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(SingleAccount.this,"服务器无响应",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseData = response.body().string();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(responseData);
                            final String status = jsonObject.getString("status");
                            if(status.equals("1")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SingleAccount.this,"删除成功",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Intent intent = new Intent(SingleAccount.this,UseAcitvity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                price = Float.parseFloat(price_edit.getText().toString());
                note = note_edit.getText().toString();
                date = date_edit.getText().toString();
                RequestBody requestBody =new FormBody.Builder()
                        .add("accountId",accountId)
                        .add("price", price_edit.getText().toString())
                        .add("typeName",typeName)
                        .add("date",date)
                        .add("note",note)
                        .build();
                HttpUtil.postOKHttpRequest("http://10.0.2.2:3000/price/updateaccount",requestBody, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SingleAccount.this,"服务器无响应",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseData = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            final String status = jsonObject.getString("status");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(status.equals("1")){
                                        Toast.makeText(SingleAccount.this,"修改成功",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SingleAccount.this,UseAcitvity.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(SingleAccount.this,"修改失败",Toast.LENGTH_SHORT).show();
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
    }
    protected Dialog onCreateDialog() {
        Dialog dialog = null;
        c = Calendar.getInstance();
        dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                if(dayOfMonth<10){
                    Log.d("", String.valueOf(dayOfMonth));
                    date_edit.setText(year + "-" + (month + 1) + "-" + "0" + dayOfMonth );

                }else{
                    date_edit.setText(year + "-" + (month + 1) + "-" +dayOfMonth );
                }
            }
        }, c.get(java.util.Calendar.YEAR), // 传入年份
                c.get(java.util.Calendar.MONTH), // 传入月份
                c.get(java.util.Calendar.DAY_OF_MONTH) // 传入天数
        );

        return dialog;
    }

    public Integer getTypePosition(String typeName){
        if(typeName.equals("工资收入")){
            return 0;
        }else if(typeName.equals("食品支出")){
            return 1;
        }else if(typeName.equals("交通支出")){
            return 2;
        }else if(typeName.equals("娱乐支出")){
            return 3;
        }else if(typeName.equals("医药支出")){
            return 4;
        }else if(typeName.equals("其他支出")){
            return 5;
        }
        return 0;
    }
}
