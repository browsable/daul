package com.daemin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.daemin.data.SubjectData;
import com.daemin.timetable.R;

import java.util.List;

/**
 * Created by hernia on 2015-07-02.
 */
public class HorizontalListAdapter extends ArrayAdapter<SubjectData> {
    private LayoutInflater mInflater;

    public HorizontalListAdapter(Context context, List<SubjectData> values) {
        super(context, R.layout.listitem_subject, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.listitem_subject, parent, false);
            holder._id = (TextView) convertView.findViewById(R.id._id);
            holder.subnum = (TextView) convertView.findViewById(R.id.subnum);
            holder.subtitle = (TextView) convertView.findViewById(R.id.subtitle);
            holder.prof = (TextView) convertView.findViewById(R.id.prof);
            holder.credit = (TextView) convertView.findViewById(R.id.credit);
            holder.classnum = (TextView) convertView.findViewById(R.id.classnum);
            holder.limitnum = (TextView) convertView.findViewById(R.id.limitnum);
            holder.dep_detail = (TextView) convertView.findViewById(R.id.dep_detail);
            holder.dep_grade = (TextView) convertView.findViewById(R.id.dep_grade);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        // Populate the text
        holder._id.setText(getItem(position).get_id());
        holder.subnum.setText(getItem(position).getSubnum());
        holder.subtitle.setText(getItem(position).getSubtitle());
        holder.prof.setText(getItem(position).getProf());
        holder.credit.setText(getItem(position).getCredit());
        holder.classnum.setText(getItem(position).getClassnum());
        holder.limitnum.setText(getItem(position).getLimitnum());
        holder.dep_detail.setText(getItem(position).getDep_detail());
        holder.dep_grade.setText(getItem(position).getDep_grade());

        return convertView;
    }

    /** View holder for the views we need access to */
    private static class Holder {
        public TextView _id;
        public TextView subnum;
        public TextView subtitle;
        public TextView prof;
        public TextView credit;
        public TextView classnum;
        public TextView limitnum;
        public TextView dep_detail;
        public TextView dep_grade;
    }
}