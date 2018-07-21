package com.zucc.ccm31501396.mdays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zucc.ccm31501396.mdays.Adapter.ScheduleAdapter;
import com.zucc.ccm31501396.mdays.Util.HttpUtil;
import com.zucc.ccm31501396.mdays.Util.JsonUtil;
import com.zucc.ccm31501396.mdays.data.Schedule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchSchedule extends AppCompatActivity {
    private SharedPreferences user_sp;
    private TextView back_button;
    private TextView serach_button;
    private Spinner search_spinner;
    private String[] search_list={"优先级为1","优先级为2","优先级为3","优先级为4","优先级为5","类型为‘工作’","类型为‘娱乐’","类型为‘学习’","类型为‘日常’"};
    private ArrayAdapter<String> search_adapter;
    private ScheduleAdapter adapter;
    private RecyclerView mRecyclerView;
    private String URL;
    private RequestBody requestBody;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_schedule);
        back_button = (TextView)findViewById(R.id.back);
        serach_button = (TextView)findViewById(R.id.search);
        serach_button.setVisibility(View.GONE);
        search_spinner = (Spinner)findViewById(R.id.search_spinner);
        mRecyclerView = (RecyclerView)findViewById(R.id.list_recyclerView);
        search_spinner = (Spinner) findViewById(R.id.search_spinner);
        search_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,search_list);
        search_spinner.setAdapter(search_adapter);
        user_sp= getSharedPreferences("loginInfo",MODE_PRIVATE);
        userName = user_sp.getString("username","");

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchSchedule.this,UseAcitvity.class);
                startActivity(intent);
                finish();
            }
        });

        search_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                URL = BuildSearchURL(search_spinner.getItemAtPosition(position).toString());
                requestBody = BuildSearchBody(search_spinner.getItemAtPosition(position).toString());
                reflashSchedule(requestBody,URL);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void reflashSchedule(RequestBody requestBody,String URL){
        HttpUtil.postOKHttpRequest(URL,requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseDate = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseDate);
                    final String status = jsonObject.getString("status");
                    if(status.equals("1")){
                        final ArrayList<Schedule> result = JsonUtil.DealScheduleResponse(responseDate);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new ScheduleAdapter(result);
                                mRecyclerView.setAdapter(adapter);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(SearchSchedule.this);
                                mRecyclerView.setLayoutManager(layoutManager);
                            }
                        });
                    }else if(status.equals("1002")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SearchSchedule.this,"没有此类型日程",Toast.LENGTH_SHORT).show();
                                LinearLayoutManager layoutManager = new LinearLayoutManager(SearchSchedule.this);
                                mRecyclerView.setLayoutManager(layoutManager);
                                mRecyclerView.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private RequestBody BuildSearchBody(String typeString){
        String searchBody=null;
        String searchInfo=null;
//{"优先级为1","优先级为2","优先级为3","优先级为4","优先级为5","类型为‘工作’","类型为‘娱乐’","类型为‘学习’","类型为‘日常’"};
        if(typeString.equals("优先级为1")){
            searchBody="priority";
            searchInfo="1";
        }else if(typeString.equals("优先级为2")){
            searchBody="priority";
            searchInfo="2";
        }else if(typeString.equals("优先级为3")) {
            searchBody = "priority";
            searchInfo = "3";
        }else if(typeString.equals("优先级为4")) {
            searchBody = "priority";
            searchInfo = "4";
        }else if(typeString.equals("优先级为5")) {
            searchBody = "priority";
            searchInfo = "5";
        }else if(typeString.equals("类型为‘工作’")) {
            searchBody = "sType";
            searchInfo = "工作";
        }else if(typeString.equals("类型为‘娱乐’")) {
            searchBody = "sType";
            searchInfo = "娱乐";
        }else if(typeString.equals("类型为‘学习’")) {
            searchBody = "sType";
            searchInfo = "学习";
        }else if(typeString.equals("类型为‘日常’")) {
            searchBody = "sType";
            searchInfo = "日常";
        }

        RequestBody requestBody = new FormBody.Builder()
                .add(searchBody,searchInfo)
                .add("userName",userName)
                .build();

        return requestBody;
    }

    private String BuildSearchURL(String typeString){
        String result = null;
        if(typeString.equals("优先级为1")||typeString.equals("优先级为2")||typeString.equals("优先级为3")||typeString.equals("优先级为4")||typeString.equals("优先级为5")){
            result = "http://10.0.2.2:3000/schedule/priporityschedule";
        }else if(typeString.equals("类型为‘工作’")||typeString.equals("类型为‘娱乐’")||typeString.equals("类型为‘学习’")||typeString.equals("类型为‘日常’")) {
            result = "http://10.0.2.2:3000/schedule/typeschedule";
        }
        return result;
    }

}
