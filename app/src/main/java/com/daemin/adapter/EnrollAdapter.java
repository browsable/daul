package com.daemin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.daemin.data.EnrollData;
import com.daemin.data.EventlistData;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;
import java.util.List;

import timedao.MyTime;

/**
 * Created by HOME on 2015-09-11.
 */
public class EnrollAdapter  extends ArrayAdapter<EnrollData> {
    private LayoutInflater mInflater;
    public EnrollAdapter(Context context, List<EnrollData> values) {
        super(context, R.layout.listitem_enroll, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.listitem_enroll, parent, false);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvId = (TextView) convertView.findViewById(R.id.tvId);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        // Populate the text
        holder.tvTime.setText(getItem(position).getTime());
        holder.tvTitle.setText(getItem(position).getTitle());
        holder.tvId.setText(String.valueOf(getItem(position).get_id()));
        return convertView;
    }
    private static class Holder {
        public TextView tvTime, tvTitle,tvId;
    }
}
