package com.daemin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.daemin.data.EventlistData;
import com.daemin.timetable.R;
import java.util.List;

/**
 * Created by HOME on 2015-09-11.
 */
public class EventListAdapter  extends ArrayAdapter<EventlistData> {
    private LayoutInflater mInflater;

    public EventListAdapter(Context context, List<EventlistData> values) {
        super(context, R.layout.listitem_area, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder_Area holder;
        if (convertView == null) {
            holder = new Holder_Area();
            convertView = mInflater.inflate(R.layout.listitem_area, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.event_title);
            holder.time = (TextView) convertView.findViewById(R.id.event_time);
            holder.user = (TextView) convertView.findViewById(R.id.event_user);
            convertView.setTag(holder);
        } else {
            holder = (Holder_Area) convertView.getTag();
        }
        // Populate the text
        holder.title.setText(getItem(position).getTitle());
        holder.time.setText(getItem(position).getTime());
        holder.user.setText(getItem(position).getUser());
        return convertView;
    }
    private static class Holder_Area {
        public TextView title;
        public TextView time;
        public TextView user;
    }
}
