package com.zucc.ccm31501396.mdays.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zucc.ccm31501396.mdays.R;
import com.zucc.ccm31501396.mdays.SingleSchedule;
import com.zucc.ccm31501396.mdays.data.Schedule;

import org.w3c.dom.Text;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{
    private List<Schedule> mScheduleList;

    public ScheduleAdapter(List<Schedule> scheduleList){
        mScheduleList = scheduleList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View ScheduleView;
        TextView sTitle;
        TextView startTime;
        TextView endTime;
        TextView place;
        TextView sId;
        TextView date;
        public ViewHolder(View view) {
            super(view);
            ScheduleView = view;
            sTitle = (TextView) view.findViewById(R.id.sTitle);
            startTime = (TextView) view.findViewById(R.id.startTime);
            endTime = (TextView) view.findViewById(R.id.endTime);
            place = (TextView) view.findViewById(R.id.place);
            sId = (TextView) view.findViewById(R.id.sId);
            date = (TextView) view.findViewById(R.id.date);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item,parent,false);
        final  ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.ScheduleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击后调转到响应的单个日程显示界面
                int positon = viewHolder.getAdapterPosition();
                Schedule schedule = mScheduleList.get(positon);
                Intent intent = new Intent(v.getContext(), SingleSchedule.class);
                intent.putExtra("sId",schedule.getSId());
                v.getContext().startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Schedule schedule = mScheduleList.get(position);
        holder.sTitle.setText(schedule.getSTitle());
        holder.startTime.setText(schedule.getStartTime());
        holder.endTime.setText(schedule.getEndTime());
        holder.place.setText(schedule.getPlace());
        holder.sId.setText(schedule.getSId());
        holder.date.setText(schedule.getSDate());
    }

    @Override
    public int getItemCount() {
        return mScheduleList.size();
    }
}
