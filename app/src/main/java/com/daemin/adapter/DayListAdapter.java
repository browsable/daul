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
            convertView.setTag(holder);
        } else {
            holder = (Holder_Area) convertView.getTag();
        }
        // Populate the text
        if(getItem(position).getDayofmonth()==0){
            holder.tvMonth.setVisibility(View.VISIBLE);
            holder.tvDayOfMonth.setVisibility(View.GONE);
            holder.tvDayOfWeek.setVisibility(View.GONE);
            holder.tvContent.setVisibility(View.GONE);
            holder.ll.setBackgroundColor(context.getResources().getColor(R.color.holo_blue));
            holder.tvMonth.setText(getItem(position).getMonthofyear()+context.getString(R.string.month));
        }else {
            holder.tvMonth.setVisibility(View.GONE);
            holder.tvDayOfMonth.setVisibility(View.VISIBLE);
            holder.tvDayOfWeek.setVisibility(View.VISIBLE);
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.ll.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            holder.tvDayOfMonth.setText(Convert.IntToString(getItem(position).getDayofmonth()));
            holder.tvDayOfWeek.setText(Convert.XthToDayOfWeek(getItem(position).getDayofweek()).substring(0, 1));
            holder.tvContent.setText(getItem(position).getName());
        }
        return convertView;
    }
    private static class Holder_Area {
        public LinearLayout ll;
        public TextView tvMonth;
        public TextView tvDayOfMonth;
        public TextView tvDayOfWeek;
        public TextView tvContent;
    }
}
