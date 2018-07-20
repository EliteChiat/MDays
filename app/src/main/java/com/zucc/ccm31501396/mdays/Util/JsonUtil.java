package com.zucc.ccm31501396.mdays.Util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zucc.ccm31501396.mdays.data.Account;
import com.zucc.ccm31501396.mdays.data.Schedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 2018/7/14.
 */

public class JsonUtil {
    //用于处于账单列
    public static ArrayList<Account> DealAccountResponse(String response){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            String jsonData = jsonArray.toString();
            Gson gson = new Gson();
            ArrayList<Account> result = gson.fromJson(jsonData,new TypeToken<List<Account>>(){}.getType());
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
    //用于处理单个账单对象
    public static Account DealSingleAccountResponce(String response){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String jsonresult = jsonObject.getString("result");
            Gson gson = new Gson();
            Account result = gson.fromJson(jsonresult,Account.class);
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    //用于处理日程列
    public static ArrayList<Schedule> DealScheduleResponse(String response){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            String jsonData = jsonArray.toString();
            Gson gson = new Gson();
            ArrayList<Schedule> result = gson.fromJson(jsonData,new TypeToken<List<Schedule>>(){}.getType());
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
            return null;
    }
    //用于处理单个日程对象
    public static Schedule DealSingleScheduleResponce(String response){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            String jsonresult = jsonObject.getString("result");
            Gson gson = new Gson();
            Schedule result = gson.fromJson(jsonresult,Schedule.class);
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    //用于输出账单收入
    public static Float AddIncome(ArrayList<Account> accountList){
        Float result = 0.0f;
        for (Account account: accountList) {
            if(account.getTypeName().equals("工资收入")){
                result = result+account.getPrice();
            }
        }
        return result;
    }
    //用于输出账单消费
    public static Float AddOutcome(ArrayList<Account> accountList){
        Float result = 0.0f;
        for (Account account: accountList) {
            if(!account.getTypeName().equals("工资收入")){
                result = result+account.getPrice();
            }
        }
        return result;
    }
}
