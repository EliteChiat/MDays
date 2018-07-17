package com.zucc.ccm31501396.mdays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zucc.ccm31501396.mdays.Adapter.AccountAdapter;
import com.zucc.ccm31501396.mdays.Util.HttpUtil;
import com.zucc.ccm31501396.mdays.Util.JsonUtil;
import com.zucc.ccm31501396.mdays.data.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mac on 2018/7/12.
 */

public class Fragment2 extends Fragment {
    private static final String TAG = "Fragment2";
    private String userName;
    private ListView listview;
    private TextView income_text;
    private TextView outcome_text;
    private TextView last_text;
    private AccountAdapter adapter;
    private SharedPreferences login_sp;
    private FloatingActionButton addAccount_button;
    private TextView back_button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.fragment_account_list,container,false);
        listview = view.findViewById(R.id.listview);
        income_text = view.findViewById(R.id.shouru);
        outcome_text = view.findViewById(R.id.zhichu);
        last_text = view.findViewById(R.id.jieyu);
        back_button = view.findViewById(R.id.back);
        login_sp = this.getActivity().getSharedPreferences("loginInfo",MODE_PRIVATE);
        userName = login_sp.getString("username","");
        addAccount_button = view.findViewById(R.id.fab);
        Log.d(TAG,userName);

        addAccount_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddAccountActivity.class);
                startActivity(intent);
            }
        });


        //发送Http请求来获取账单内容
        RequestBody requestBody = new FormBody.Builder()
                .add("userName",userName)
                .build();
        HttpUtil.postOKHttpRequest("http://10.0.2.2:3000/price/accountlist2",requestBody,new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(responseData);
                    final String status = jsonObject.getString("status");
                    if(status.equals("1")){
                        final ArrayList<Account> result = JsonUtil.DealAccountResponse(responseData);
                        final Float price = JsonUtil.AddIncome(result);
                        final Float outcome = JsonUtil.AddOutcome(result);
                        getActivity().runOnUiThread(new Runnable(){

                            @Override
                            public void run() {
                                adapter = new AccountAdapter(getActivity(), result, getActivity().getPackageName());
                                listview.setAdapter(adapter);
                                income_text.setText(String.valueOf(price));
                                outcome_text.setText(String.valueOf(outcome));
                                last_text.setText(String.valueOf(price+outcome));
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        back_button.setOnClickListener(new ViewPager.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Account account = (Account)parent.getItemAtPosition(position);
                Intent intent=new Intent(getActivity(),SingleAccount.class);
                intent.putExtra("result",account.getAccountId());
                startActivity(intent);

                return false;
            }
        });

        return view;
    }

//    private void initdata() {
//        List<Account> list1= new ArrayList<Account>();
//        List<Account> list2= new ArrayList<Account>();
//        list1.add(new Account(0, "10", "工作收入", 2700, new Date()));
//        list2.add(new Account(1, "01", "餐饮食品", -200, new Date()));
//        list2.add(new Account(2, "02", "衣服饰品", -120, new Date()));
//        list2.add(new Account(3, "04", "行车交通", -330, new Date()));
//        list2.add(new Account(3, "04", "行车交通", -330, new Date()));
//        list2.add(new Account(3, "04", "行车交通", -330, new Date()));
//        list2.add(new Account(3, "04", "行车交通", -330, new Date()));
//        list2.add(new Account(3, "04", "行车交通", -330, new Date()));
//        list.addAll(list1);
//        list.addAll(list2);
//        list.add(0, null);
//        list.add(list1.size() + 1, null);
//    }
}