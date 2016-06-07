package com.daemin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daemin.common.Convert;
import com.daemin.enumclass.Dates;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;
import java.util.List;

import timedao.MyTime;

/**
 * Created by HOME on 2015-09-11.
 */
public class DayListAdapter  extends ArrayAdapter<MyTime> {
    private LayoutInflater mInflater;
    private Context context;
    public DayListAdapter(Context context, List<MyTime> values) {
        super(context, R.layout.listitem_mytime, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder_Area holder;
        if (convertView == null) {
            holder = new Holder_Area();
            convertView = mInflater.inflate(R.layout.listitem_mytime, parent, false);
            holder.tvMonth = (TextView) convertView.findViewById(R.id.tvMonth);
            holder.tvDayOfMonth = (TextView) convertView.findViewById(R.id.tvDayOfMonth);
            holder.tvDayOfWeek = (TextView) convertView.findViewById(R.id.tvDayOfWeek);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            holder.ll = (LinearLayout) convertView.findViewById(R.id.ll);
            holder.divider = convertView.findViewById(R.id.divider);
            convertView.setTag(holder);
        } else {
            holder = (Holder_Area) convertView.getTag();
        }
        // Populate the text
        int dayOfMonth = getItem(position).getDayofmonth();
        int monthOfYear = getItem(position).getMonthofyear();
        int dayOfWeek = getItem(position).getDayofweek();
        if(dayOfMonth==0){
            holder.tvMonth.setVisibility(View.VISIBLE);
            holder.tvDayOfMonth.setVisibility(View.GONE);
            holder.tvDayOfWeek.setVisibility(View.GONE);
            holder.tvContent.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);
            holder.ll.setBackgroundColor(context.getResources().getColor(R.color.holo_blue));
            holder.tvMonth.setText(monthOfYear+context.getString(R.string.month));
        }else {
            int year = getItem(position).getYear();
            holder.divider.setVisibility(View.GONE);
            holder.tvMonth.setVisibility(View.GONE);
            holder.tvDayOfMonth.setVisibility(View.VISIBLE);
            holder.tvDayOfWeek.setVisibility(View.VISIBLE);
            holder.tvContent.setVisibility(View.VISIBLE);
            if(dayOfWeek==13) {//토
                holder.divider.setVisibility(View.VISIBLE);
                holder.tvDayOfWeek.setTextColor(context.getResources().getColor(R.color.holo_blue));
            }else if(dayOfWeek==1){//일
                holder.tvDayOfWeek.setTextColor(context.getResources().getColor(R.color.red));
            }else{
                holder.tvDayOfWeek.setTextColor(context.getResources().getColor(android.R.color.black));
            }
            if(Dates.NOW.getDayOfMonth()==dayOfMonth&&Dates.NOW.month==monthOfYear) {
                holder.tvDayOfMonth.setTextColor(context.getResources().getColor(android.R.color.white));
                holder.tvDayOfWeek.setTextColor(context.getResources().getColor(R.color.maincolor));
                holder.tvDayOfMonth.setBackgroundResource(R.drawable.bg_circle_maincolor);
            }else{
                holder.tvDayOfMonth.setTextColor(context.getResources().getColor(android.R.color.black));
                holder.tvDayOfMonth.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            }
            holder.ll.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            holder.tvDayOfMonth.setText(Convert.IntToString(dayOfMonth));
            holder.tvDayOfWeek.setText(Convert.XthToDayOfWeek(dayOfWeek).substring(0, 1));
            String content="";
            boolean first = true;
            for(MyTime mt : MyTimeRepo.getOneDayTimesForDayList(context,year,monthOfYear,dayOfMonth))
            {
                if (!first) content+="\n";
                String name = mt.getName();
                if(name.length()>11)name = name.substring(0,11)+"..";
                content += Convert.IntToString(mt.getStarthour()) + ":"
                        + Convert.IntToString(mt.getStartmin()) + " ~ "
                        + Convert.IntToString(mt.getEndhour()) + ":"
                        + Convert.IntToString(mt.getEndmin()) + "   " + name;
                first = false;
            }
            holder.tvContent.setText(content);

        }
        return convertView;
    }
    private static class Holder_Area {
        public LinearLayout ll;
        public View divider;
        public TextView tvMonth;
        public TextView tvDayOfMonth;
        public TextView tvDayOfWeek;
        public TextView tvContent;
    }
}
