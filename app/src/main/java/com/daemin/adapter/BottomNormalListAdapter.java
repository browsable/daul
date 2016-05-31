package com.daemin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.daemin.data.BottomNormalData;
import com.daemin.timetable.R;

import java.util.List;

/**
 * Created by hernia on 2015-07-02.
 */
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
            holder.tvMD = (TextView) convertView.findViewById(R.id.tvMD);
            holder.tvStartHour = (TextView) convertView.findViewById(R.id.tvStartHour);
            holder.tvStartMin = (TextView) convertView.findViewById(R.id.tvStartMin);
            holder.tvEndHour = (TextView) convertView.findViewById(R.id.tvEndHour);
            holder.tvEndMin = (TextView) convertView.findViewById(R.id.tvEndMin);
            holder.tvXth = (TextView) convertView.findViewById(R.id.tvXth);
            holder.headLine = convertView.findViewById(R.id.headLine);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        if(position==0) holder.headLine.setVisibility(View.VISIBLE);
        // Populate the text
        holder.tvMD.setText(getItem(position).getMD());
        holder.tvStartHour.setText(getItem(position).getStartHour());
        holder.tvStartMin.setText(getItem(position).getStartMin());
        holder.tvEndHour.setText(getItem(position).getEndHour());
        holder.tvEndMin.setText(getItem(position).getEndMin());
        holder.tvXth.setText(String.valueOf(getItem(position).getXth()));
        //holder = 재활용
        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public TextView tvMD;
        public TextView tvStartHour;
        public TextView tvStartMin;
        public TextView tvEndHour;
        public TextView tvEndMin;
        public TextView tvXth;
        public View headLine;
    }
}