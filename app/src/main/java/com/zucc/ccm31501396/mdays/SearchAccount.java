package com.zucc.ccm31501396.mdays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.zucc.ccm31501396.mdays.Adapter.AccountAdapter;
import com.zucc.ccm31501396.mdays.Util.HttpUtil;
import com.zucc.ccm31501396.mdays.Util.JsonUtil;
import com.zucc.ccm31501396.mdays.data.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchAccount extends AppCompatActivity implements OnChartValueSelectedListener{
    private SharedPreferences user_sp;
    private Float sumSend;
    private Float typeFood;
    private Float typeJiaotong;
    private Float typeYule;
    private Float typeYiyao;
    private Float typeOther;
    private PieChart mPieChart;
    private ListView mListView;
    private ArrayList<Account> result;
    private List<PieEntry> pieItem;
    private String userName;
    private String URL = "http://10.0.2.2:3000/price/accountlist2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPieChart = (PieChart)findViewById(R.id.mPieChart);
        setContentView(R.layout.activity_search_account);
        user_sp = getSharedPreferences("loginInfo",MODE_PRIVATE);
        userName = user_sp.getString("username","");
        mListView = (ListView)findViewById(R.id.accountListView);
        typeFood = 0f;
        typeJiaotong = 0f;
        typeYule = 0f;
        typeYiyao = 0f;
        typeOther = 0f;
        sumSend = 0f;
        pieItem = new ArrayList<PieEntry>();
        RequestBody requestBody = new FormBody.Builder()
                .add("userName",userName)
                .build();
        forAccountList(URL,requestBody);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Account account = (Account)parent.getItemAtPosition(position);
                Intent intent=new Intent(SearchAccount.this,SingleAccount.class);
                intent.putExtra("result",account.getAccountId());
                startActivity(intent);
                return false;
            }
        });

    }



    private void sumAccountTypePrice(Account account){
        switch(account.getTypeName()){
            case "食品支出":
                typeFood += account.getPrice();
                break;
            case "交通支出":
                typeJiaotong += account.getPrice();
                break;
            case "娱乐支出":
                typeYule += account.getPrice();
                break;
            case "医药支出":
                typeYiyao += account.getPrice();
                break;
            case "其他支出":
                typeOther += account.getPrice();
                break;
        }
    }

    private void initPieEntry(float money,String typeName){
        if(money!=0){
            float zhanbi = new Float(money/sumSend);
            pieItem.add(new PieEntry(zhanbi,typeName));
        }
    }

    private void initView(){
        mPieChart = (PieChart) findViewById(R.id.mPieChart);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文件
        mPieChart.setCenterText("总支出："+String.valueOf(sumSend));

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);

        //变化监听
        mPieChart.setOnChartValueSelectedListener(this);

        initPieEntry(typeFood,"食品支出");
        initPieEntry(typeJiaotong,"交通支出");
        initPieEntry(typeYule,"娱乐支出");
        initPieEntry(typeYiyao,"医药支出");
        initPieEntry(typeOther,"其他支出");
        PieDataSet dataSet = new PieDataSet(pieItem,"");

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.DKGRAY);
        mPieChart.setData(data);
        mPieChart.highlightValues(null);
        //刷新
        mPieChart.invalidate();
        mPieChart.setEntryLabelColor(Color.DKGRAY);
        mPieChart.setEntryLabelTextSize(12f);
    }
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        String sType = ((PieEntry)e).getLabel();
        ArrayList<Account> reflsh = new ArrayList<>();
        for(Account Singletype : result){
            if(sType.equals(Singletype.getTypeName().toString())){
                reflsh.add(Singletype);
            }
        }
        AccountAdapter adapter = new AccountAdapter(this,reflsh);
        mListView.setAdapter(adapter);

    }

    @Override
    public void onNothingSelected() {
        AccountAdapter adapter = new AccountAdapter(this,result);
        mListView.setAdapter(adapter);
    }


    private void forAccountList(String address, RequestBody requestBody){
        HttpUtil.postOKHttpRequest(address,requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("forAccountList", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    final String status = jsonObject.getString("status");
                    if(status.equals("1")){
                        result = JsonUtil.DealAccountResponse(responseData);
                        sumSend = JsonUtil.AddOutcome(result);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //循环算出各种支出的总和
                                for(Account account:result){
                                    sumAccountTypePrice(account);
                                }
                                ArrayList<Account> reflsh = new ArrayList<>();
                                for(Account account:result){
                                    if(!account.getTypeName().equals("工资收入")){
                                        reflsh.add(account);
                                    }
                                }
                                initView();
                                AccountAdapter adapter = new AccountAdapter(SearchAccount.this,reflsh);
                                mListView.setAdapter(adapter);
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
