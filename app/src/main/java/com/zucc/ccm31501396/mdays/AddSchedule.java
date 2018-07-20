package com.zucc.ccm31501396.mdays;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.zucc.ccm31501396.mdays.Util.HttpUtil;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddSchedule extends AppCompatActivity {
    private SharedPreferences user_sp;
    private EditText title_edit;
    private EditText place_edit;
    private EditText note_edit;
    private EditText date_text;
    private EditText startTime_text;
    private EditText endTime_text;
    private Spinner type_spinner;
    private String[] type_list ={"工作","娱乐","学习","日常"};
    private String type;
    private ArrayAdapter<String> type_adapter;
    private Spinner priority_spinner;
    private String[] priority_list = {"1","2","3","4","5"};
    private String priority;
    private ArrayAdapter<String> priority_adapter;
    private Button up_button;
    private Calendar c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        user_sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        final String username = user_sp.getString("username","");
        title_edit = (EditText) findViewById(R.id.title_edit);
        date_text = (EditText) findViewById(R.id.date_edit);
        type_spinner = (Spinner) findViewById(R.id.stype_spinner);
        startTime_text = (EditText) findViewById(R.id.startTime_text);
        endTime_text = (EditText) findViewById(R.id.endTime_text);
        priority_spinner = (Spinner) findViewById(R.id.priority_spinner);
        place_edit = (EditText) findViewById(R.id.place_edit);
        note_edit = (EditText) findViewById(R.id.note_edit);
        up_button = (Button) findViewById(R.id.up_button);

        type_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,type_list);
        type_spinner.setAdapter(type_adapter);
        type_spinner.setSelection(0,true);
        type = (String)type_spinner.getItemAtPosition(0);
        priority_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,priority_list);
        priority_spinner.setAdapter(priority_adapter);
        priority_spinner.setSelection(0,true);
        priority = (String)priority_spinner.getItemAtPosition(0);


        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = (String)type_spinner.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        priority_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priority = (String)priority_spinner.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        date_text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                new DatePickerDialog(AddSchedule.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if(dayOfMonth<10){
                            Log.d("", String.valueOf(dayOfMonth));
                            date_text.setText(year + "-" + (month + 1) + "-" + dayOfMonth );

                        }else{
                            date_text.setText(year + "-" + (month + 1) + "-" +dayOfMonth );
                        }
                    }
                },c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        startTime_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddSchedule.this,new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (minute < 10){
                            startTime_text.setText(hourOfDay+":"+"0"+minute);
                        }else {
                            startTime_text.setText(hourOfDay+":"+minute);
                        }
                    }
                }, 0, 0, true).show();
            }
        });
        endTime_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AddSchedule.this,new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (minute < 10){
                            endTime_text.setText(hourOfDay+":"+"0"+minute);
                        }else {
                            endTime_text.setText(hourOfDay+":"+minute);
                        }
                    }
                }, 0, 0, true).show();
            }
        });
        up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title_edit.getText().toString().equals("")||date_text.getText().toString().equals("")||startTime_text.getText().toString().equals("")||endTime_text.getText().toString().equals("")||place_edit.getText().toString().equals("")){
                    Toast.makeText(AddSchedule.this,"请将信息填写完整",Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestBody requestBody = new FormBody.Builder()
                        .add("userName",username)
                        .add("sTitle",title_edit.getText().toString())
                        .add("sDate",date_text.getText().toString() )
                        .add("sType",type)
                        .add("startTime",startTime_text.getText().toString())
                        .add("endTime",endTime_text.getText().toString())
                        .add("priority",priority)
                        .add("place",place_edit.getText().toString())
                        .add("sNote",note_edit.getText().toString())
                        .add("clock","0")
                        .add("sStatus","未完成")
                        .build();
                HttpUtil.postOKHttpRequest("http://10.0.2.2:3000/schedule/addschedule",requestBody, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(AddSchedule.this,"服务器无响应",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddSchedule.this,"上传成功",Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent(AddSchedule.this, UseAcitvity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

    }
}
