package com.zucc.ccm31501396.mdays.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zucc.ccm31501396.mdays.R;
import com.zucc.ccm31501396.mdays.SearchAccount;
import com.zucc.ccm31501396.mdays.SingleAccount;
import com.zucc.ccm31501396.mdays.SingleSchedule;
import com.zucc.ccm31501396.mdays.data.Account;
import com.zucc.ccm31501396.mdays.data.Schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 2018/7/13.
 */

public class AccountAdapter extends BaseAdapter implements View.OnClickListener{
    Context context;
    ArrayList<Account> list;

    public AccountAdapter(Context context, ArrayList<Account> list){
        this.context=context;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        View view;
        final ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.account_item,null);
            viewHolder = new ViewHolder();
            viewHolder.content = view.findViewById(R.id.content);
            viewHolder.id_text =  view.findViewById(R.id.accountid);
            viewHolder.type_img = view.findViewById(R.id.img);
            viewHolder.type_text = view.findViewById(R.id.type);
            viewHolder.date_text = view.findViewById(R.id.date);
            viewHolder.price_text =view.findViewById(R.id.number);
            viewHolder.content = view.findViewById(R.id.content);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder =(ViewHolder) view.getTag();
        }
        if(list.get(i).getTypeName().equals("工资收入")){
            viewHolder.id_text.setText(list.get(i).getAccountId());
            viewHolder.type_text.setText(list.get(i).getNote());
            viewHolder.date_text.setText(list.get(i).getDate());
            viewHolder.price_text.setText(String.valueOf(list.get(i).getPrice())+"元");
            viewHolder.price_text.setTextColor(Color.GREEN);
            viewHolder.type_img.setImageResource(R.drawable.income);

        }else{
            viewHolder.id_text.setText(list.get(i).getAccountId());
            viewHolder.type_text.setText(list.get(i).getNote());
            viewHolder.date_text.setText(list.get(i).getDate());
            viewHolder.price_text.setText("-"+String.valueOf(list.get(i).getPrice())+"元");
            viewHolder.price_text.setTextColor(Color.RED);
            viewHolder.type_img.setImageResource(typeImg(list.get(i).getTypeName()));
        }

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    class ViewHolder{
        TextView id_text;
        ImageView type_img;
        TextView type_text;
        TextView date_text;
        TextView price_text;
        RelativeLayout content;
    }

    private int typeImg(String typeString){
        String ImgRes;
        if(typeString.equals("食品支出")){
            return R.drawable.food;
        }else if(typeString.equals("交通支出")){
            return R.drawable.jiaotong;
        }else if(typeString.equals("娱乐支出")){
            return R.drawable.yule;
        }else if(typeString.equals("医药支出")){
            return R.drawable.yiliao;
        }else if(typeString.equals("其他支出")){
            return R.drawable.others;
        }
        return R.drawable.type_study;
    }
}
