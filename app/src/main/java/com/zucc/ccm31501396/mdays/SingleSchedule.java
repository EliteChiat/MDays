package com.zucc.ccm31501396.mdays;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.zucc.ccm31501396.mdays.Util.HttpUtil;
import com.zucc.ccm31501396.mdays.Util.JsonUtil;
import com.zucc.ccm31501396.mdays.data.Schedule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SingleSchedule extends AppCompatActivity {
    public static final String INTENT_ALARM_LOG = "intent_alarm_log";
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
    private Spinner sStatus_spinner;
    private String[] sStatus_list = {"未完成","已完成"};
    private ArrayAdapter<String> sStatus_adapter;
    private String sStatus;
    private Button update_button;
    private Button delete_button;
    private Calendar c;
    private Switch tixing_switch;
    private Schedule mSchedule;
    private int timeYear;
    private int timeMonth;
    private int timeDay;
    private int timeHour;
    private int timeMin;
    AlarmManager am;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_schedule);
        Intent intent = getIntent();
        final String sId = intent.getStringExtra("sId");
        title_edit = (EditText) findViewById(R.id.title_edit);
        date_text = (EditText) findViewById(R.id.date_edit);
        type_spinner = (Spinner) findViewById(R.id.stype_spinner);
        startTime_text = (EditText) findViewById(R.id.startTime_text);
        endTime_text = (EditText) findViewById(R.id.endTime_text);
        priority_spinner = (Spinner) findViewById(R.id.priority_spinner);
        place_edit = (EditText) findViewById(R.id.place_edit);
        note_edit = (EditText) findViewById(R.id.note_edit);
        update_button = (Button) findViewById(R.id.update_button);
        delete_button = (Button) findViewById(R.id.delete_button);
        sStatus_spinner = (Spinner) findViewById(R.id.status_spinner);
        type_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,type_list);
        type_spinner.setAdapter(type_adapter);
        type = (String)type_spinner.getItemAtPosition(0);
        priority_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,priority_list);
        priority_spinner.setAdapter(priority_adapter);
        priority_spinner.setSelection(0,true);
        priority = (String)priority_spinner.getItemAtPosition(0);
        sStatus_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sStatus_list);
        sStatus_spinner.setAdapter(sStatus_adapter);
        sStatus_spinner.setSelection(0,true);
        sStatus = (String)sStatus_spinner.getItemAtPosition(0);
        tixing_switch = (Switch)findViewById(R.id.tixing_switch);
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        RequestBody requestBody = new FormBody.Builder()
                .add("sId",sId)
                .build();
        HttpUtil.postOKHttpRequest("http://10.0.2.2:3000/schedule/singleschedule",requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SingleSchedule.this,"服务器无响应",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseDate = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseDate);
                    final String status = jsonObject.getString("status");
                    if(status.equals("1")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final Schedule result = JsonUtil.DealSingleScheduleResponce(responseDate);
                                mSchedule = result;
                                title_edit.setText(result.getSTitle());
                                date_text.setText(result.getSDate());
                                timeYear = result.getYear();
                                timeMonth = result.getMonth();
                                timeDay = result.getDay();
                                timeHour = result.getHour();
                                timeMin = result.getMin();
                                setTypeSpinner(result.getSType());
                                startTime_text.setText(result.getStartTime());
                                endTime_text.setText(result.getEndTime());
                                setPrioritySpinner(result.getPriority());
                                place_edit.setText(result.getPlace());
                                note_edit.setText(result.getSNote());
                                setSStatusSpinner(result.getSStatus());
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //将选择的类型放入type变量
                type=(String)type_spinner.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        priority_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //将选择的类型放入type变量
                priority=(String)priority_spinner.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sStatus_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //将选择的类型放入type变量
                sStatus=(String)sStatus_spinner.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        date_text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                new DatePickerDialog(SingleSchedule.this, new DatePickerDialog.OnDateSetListener() {
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
                new TimePickerDialog(SingleSchedule.this,new TimePickerDialog.OnTimeSetListener() {

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
                new TimePickerDialog(SingleSchedule.this,new TimePickerDialog.OnTimeSetListener() {

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

        tixing_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setAlarm();
                }
            }
        });

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title_edit.getText().equals("")||date_text.getText().equals("")||startTime_text.getText().equals("")||endTime_text.getText().equals("")||place_edit.getText().equals("")){
                    Toast.makeText(SingleSchedule.this,"请将信息填写完整",Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestBody updateBody = new FormBody.Builder()
                        .add("sId",sId)
                        .add("sTitle",title_edit.getText().toString())
                        .add("sType",type)
                        .add("sDate",date_text.getText().toString())
                        .add("startTime",startTime_text.getText().toString())
                        .add("endTime",endTime_text.getText().toString())
                        .add("priority",priority)
                        .add("place",place_edit.getText().toString())
                        .add("sStatus",sStatus)
                        .build();
                HttpUtil.postOKHttpRequest("http://10.0.2.2:3000/schedule/updateschedule",updateBody, new okhttp3.Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseData= response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            final String status = jsonObject.getString("status");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(status.equals("1")){
                                        Toast.makeText(SingleSchedule.this,"修改成功",Toast.LENGTH_SHORT).show();
                                        Intent intent =new Intent(SingleSchedule.this,UseAcitvity.class);
                                        startActivity(intent);
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
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody deleteRequest = new FormBody.Builder()
                        .add("sId",sId)
                        .build();
                HttpUtil.postOKHttpRequest("http://10.0.2.2:3000/schedule/updateschedule",deleteRequest, new okhttp3.Callback(){

                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseData= response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            String status = jsonObject.getString("status");
                            if(status.equals("1")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SingleSchedule.this,"删除成功",Toast.LENGTH_SHORT).show();
                                        Intent intent =new Intent(SingleSchedule.this,UseAcitvity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void setTypeSpinner(String type){
        if(type.equals("工作")){
            type_spinner.setSelection(0,true);
        }else if(type.equals("娱乐")){
            type_spinner.setSelection(1,true);
        }else if(type.equals("学习")){
            type_spinner.setSelection(2,true);
        }else if(type.equals("日常")){
            type_spinner.setSelection(3,true);
        }
    }
    private void setPrioritySpinner(String priority){
        if(priority.equals("1")){
            priority_spinner.setSelection(0,true);
        }else if(priority.equals("2")){
            priority_spinner.setSelection(1,true);
        }else if(priority.equals("3")){
            priority_spinner.setSelection(2,true);
        }else if(priority.equals("4")){
            priority_spinner.setSelection(3,true);
        }else if(priority.equals("5")){
            priority_spinner.setSelection(4,true);
        }
    }
    private void setSStatusSpinner(String sStatus){
        if(sStatus.equals("未完成")){
            sStatus_spinner.setSelection(0,true);
        }else{
            sStatus_spinner.setSelection(1,true);
        }
    }

    public void setAlarm(){
        timeHour=getHour(startTime_text.getText().toString());
        timeMin=getMin(startTime_text.getText().toString());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,timeHour);
        calendar.set(Calendar.MINUTE,timeMin);
        calendar.set(Calendar.SECOND,0);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        Intent intent = new Intent(INTENT_ALARM_LOG);
        intent.putExtra("Clock",mSchedule.getClockId().toString());

        PendingIntent pi = PendingIntent.getBroadcast(SingleSchedule.this, mSchedule.getClockId(), intent, 0);

        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);

    }

    public int getHour(String time){
        if(time.length()==4){
            return Integer.parseInt(time.substring(0,1));
        }else{
            return Integer.parseInt(time.substring(0,2));
        }
    }
    public int getMin(String time){
        if(time.length()==4){
            return Integer.parseInt(time.substring(2,4));
        }else{
            return Integer.parseInt(time.substring(3,5));
        }
    }

}
