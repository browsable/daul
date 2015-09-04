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
            holder.tvYMD = (TextView) convertView.findViewById(R.id.tvYMD);
            holder.tvStartTime = (TextView) convertView.findViewById(R.id.tvStartTime);
            holder.tvEndTime = (TextView) convertView.findViewById(R.id.tvEndTime);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        // Populate the text
        holder.tvYMD.setText(getItem(position).getYMD());
        holder.tvStartTime.setText(getItem(position).getStartTime());
        holder.tvEndTime.setText(getItem(position).getEndTime());

        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public TextView tvYMD;
        public TextView tvStartTime;
        public TextView tvEndTime;
    }
}