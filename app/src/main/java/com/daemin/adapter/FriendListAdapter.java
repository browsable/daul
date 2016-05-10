package com.daemin.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.daemin.data.FriendData;
import com.daemin.dialog.DialAddGroup;
import com.daemin.event.SetExpandableEvent;
import com.daemin.timetable.R;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

public class FriendListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<FriendData>> _listDataChild;

    public FriendListAdapter(Context context, List<String> listDataHeader,
                             HashMap<String, List<FriendData>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }
    public String getName(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon).getName();
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String name = getName(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listitem_friend_child, null);
        }

        TextView tvName = (TextView) convertView
                .findViewById(R.id.tvName);

        tvName.setText(name);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listitem_friend_group, null);
            if(groupPosition==1) {
                Button btAddGroup = (Button) convertView
                        .findViewById(R.id.btAddGroup);
                TextView tvTitle = (TextView) convertView
                        .findViewById(R.id.tvTitle);
                tvTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new SetExpandableEvent());
                    }
                });
                btAddGroup.setVisibility(View.VISIBLE);
                btAddGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialAddGroup dag = new DialAddGroup(_context);
                        dag.show();
                    }
                });
            }
        }

        TextView tvTitle = (TextView) convertView
                .findViewById(R.id.tvTitle);
        tvTitle.setTypeface(null, Typeface.BOLD);
        tvTitle.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}