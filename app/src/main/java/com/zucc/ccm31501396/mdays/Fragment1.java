package com.zucc.ccm31501396.mdays;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
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
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mac on 2018/7/12.
 */

public class Fragment1 extends Fragment {
    private SharedPreferences logininfo_sp;
    private CalendarView mCalendarView;
    private RecyclerView mRecyclerView;
    private ScheduleAdapter adapter;
    private Calendar getTime = Calendar.getInstance();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_daily,container,false);
        logininfo_sp = getActivity().getSharedPreferences("loginInfo",MODE_PRIVATE);
        String username = logininfo_sp.getString("username","");
        mCalendarView = (CalendarView)view.findViewById(R.id.calendarView);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.schedulelist);
        String year = String.valueOf(getTime.get(Calendar.YEAR));
        String month = String.valueOf(getTime.get(Calendar.MONTH)+1);
        String date = String.valueOf(getTime.get(Calendar.DATE));
        String time =year+"-"+month+"-"+date;

        RequestBody requestBody = new FormBody.Builder()
                .add("userName",username)
                .add("date",time)
                .build();
        reflashSchedule(requestBody);

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String time = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
                String username = logininfo_sp.getString("username","");
                RequestBody requestBody = new FormBody.Builder()
                        .add("userName",username)
                        .add("date",time)
                        .build();
                reflashSchedule(requestBody);
            }
        });


        return view;
    }

    private void reflashSchedule(RequestBody requestBody){
        HttpUtil.postOKHttpRequest("http://10.0.2.2:3000/schedule/schedulelist",requestBody, new okhttp3.Callback() {
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new ScheduleAdapter(result);
                                mRecyclerView.setAdapter(adapter);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                                mRecyclerView.setLayoutManager(layoutManager);
                            }
                        });
                    }else if(status.equals("1001")){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),"此日没有日程",Toast.LENGTH_SHORT).show();
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

}
