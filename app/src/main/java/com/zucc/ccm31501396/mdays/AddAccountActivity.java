package com.zucc.ccm31501396.mdays;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zucc.ccm31501396.mdays.Util.HttpUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddAccountActivity extends AppCompatActivity {
    private SharedPreferences logininfo_sp;
    private TextInputEditText price_edit;
    private TextInputEditText note_edit;
    private TextInputEditText date_edit;
    private Spinner type_spinner;
    private String[] type_list ={"工资收入","食品支出","交通支出","娱乐支出","医药支出","其他支出"};
    private ArrayAdapter<String> type_adapter;
    private AppCompatButton up_button;
    private String userName;
    private float price;
    private String typeName;
    private String date;
    private String note;
    private Calendar c;
    private TextView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_add);
        price_edit = (TextInputEditText) findViewById(R.id.ed_price);
        date_edit = (TextInputEditText) findViewById(R.id.ed_date);
        note_edit = (TextInputEditText) findViewById(R.id.ed_note);
        type_spinner = (Spinner) findViewById(R.id.type_spinner);
        up_button = (AppCompatButton) findViewById(R.id.but_up);
        back_button = (TextView) findViewById(R.id.back);
        type_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,type_list);
        type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        logininfo_sp = getSharedPreferences("loginInfo",MODE_PRIVATE);

        //初始化下拉菜单并且选择第一个
        type_spinner.setAdapter(type_adapter);
        type_spinner.setSelection(0,true);
        typeName=(String)type_spinner.getItemAtPosition(0);

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //将选择的类型放入type变量
                typeName=(String)type_spinner.getItemAtPosition(position);
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

        up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(date_edit.getText().toString().equals("")||price_edit.getText().toString().equals("")||note_edit.getText().toString().equals("")||typeName.equals("")){
                    Toast.makeText(AddAccountActivity.this,"请将信息输入完整",Toast.LENGTH_SHORT).show();
                    return;
                }
                userName = logininfo_sp.getString("username","");
                price = Float.parseFloat(price_edit.getText().toString());
                note = note_edit.getText().toString();
                date = date_edit.getText().toString();
                RequestBody requestBody = new FormBody.Builder()
                        .add("userName",userName)
                        .add("typeName",typeName)
                        .add("price", String.valueOf(price))
                        .add("date",date)
                        .add("note",note)
                        .build();
                HttpUtil.postOKHttpRequest("http://10.0.2.2:3000/price/addaccount",requestBody, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(AddAccountActivity.this,"服务器无响应",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
//                        Toast.makeText(AddAccountActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddAccountActivity.this, UseAcitvity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddAccountActivity.this, UseAcitvity.class);
                startActivity(intent);
                finish();
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
        }, c.get(Calendar.YEAR), // 传入年份
                c.get(Calendar.MONTH), // 传入月份
                c.get(Calendar.DAY_OF_MONTH) // 传入天数
        );

        return dialog;
    }
}
