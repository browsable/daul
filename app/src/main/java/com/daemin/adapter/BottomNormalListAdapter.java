/*
package com.daemin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daemin.data.BottomNormalData;
import com.daemin.timetable.R;

import java.util.List;

*/
/**
 * Created by hernia on 2015-07-02.
 *//*

public class BottomNormalListAdapter extends ArrayAdapter<BottomNormalData> {
    private LayoutInflater mInflater;

    public BottomNormalListAdapter(Context context, List<BottomNormalData> values) {
        super(context, R.layout.listitem_normal, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.listitem_normal, parent, false);
            holder.llStart = (LinearLayout) convertView.findViewById(R.id.llStart);
            holder.llEnd = (LinearLayout) convertView.findViewById(R.id.llEnd);
            holder.tvStartYear = (TextView) convertView.findViewById(R.id.tvStartYear);
            holder.tvStartMonthOfYear = (TextView) convertView.findViewById(R.id.tvStartMonthOfYear);
            holder.tvStartDayOfMonth = (TextView) convertView.findViewById(R.id.tvStartDayOfMonth);
            holder.tvEndYear = (TextView) convertView.findViewById(R.id.tvEndYear);
            holder.tvEndMonthOfYear = (TextView) convertView.findViewById(R.id.tvEndMonthOfYear);
            holder.tvEndDayOfMonth = (TextView) convertView.findViewById(R.id.tvEndDayOfMonth);
            holder.tvStartHour = (TextView) convertView.findViewById(R.id.tvStartHour);
            holder.tvStartMinute = (TextView) convertView.findViewById(R.id.tvStartMinute);
            holder.tvEndHour = (TextView) convertView.findViewById(R.id.tvEndHour);
            holder.tvEndMinute = (TextView) convertView.findViewById(R.id.tvEndMinute);
            holder.tvStartAMPM = (TextView) convertView.findViewById(R.id.tvStartAMPM);
            holder.tvEndAMPM = (TextView) convertView.findViewById(R.id.tvEndAMPM);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        // Populate the text
        holder.tvStartYear.setText(getItem(position).getStartYear());
        holder.tvStartMonthOfYear.setText(" "+getItem(position).getStartMonthOfYear());
        holder.tvStartDayOfMonth.setText(" "+getItem(position).getStartDayOfMonth());
        holder.tvEndYear.setText(getItem(position).getEndYear());
        holder.tvEndMonthOfYear.setText(" "+getItem(position).getEndMonthOfYear());
        holder.tvEndDayOfMonth.setText(" "+getItem(position).getEndDayOfMonth());
        holder.tvStartHour.setText(" "+getItem(position).getStartHour());
        holder.tvStartMinute.setText(getItem(position).getStartMinute());
        holder.tvEndHour.setText(" "+getItem(position).getEndHour());
        holder.tvEndMinute.setText(getItem(position).getEndMinute());*/
/*
        switch(getItem(position).getAMPM()){
            case Calendar.AM:
                holder.tvStartAMPM.setText(getContext().getResources().getString(R.string.am));
                holder.tvEndAMPM.setText(getContext().getResources().getString(R.string.am));
                break;
            case Calendar.PM:
                holder.tvStartAMPM.setText(getContext().getResources().getString(R.string.pm));
                holder.tvEndAMPM.setText(getContext().getResources().getString(R.string.pm));
                break;
        }*//*

        return convertView;
    }

    */
/** View holder for the views we need access to *//*

    private static class Holder {
        public LinearLayout llStart,llEnd;
        public TextView tvStartYear;
        public TextView tvStartMonthOfYear;
        public TextView tvStartDayOfMonth;
        public TextView tvEndYear;
        public TextView tvEndMonthOfYear;
        public TextView tvEndDayOfMonth;
        public TextView tvStartHour;
        public TextView tvStartMinute;
        public TextView tvEndHour;
        public TextView tvEndMinute;
        public TextView tvStartAMPM;
        public TextView tvEndAMPM;
    }
}*/
