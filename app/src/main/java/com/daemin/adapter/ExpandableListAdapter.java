package com.daemin.adapter;

/**
 * Created by Jun-yeong on 2015-06-24.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.daemin.community.Comment;
import com.daemin.community.github.FreeBoard;
import com.daemin.timetable.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<FreeBoard.Data> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<FreeBoard.Data, List<Comment>> _listDataChild;

    public ExpandableListAdapter(Context context, List<FreeBoard.Data> listDataHeader,
                                 HashMap<FreeBoard.Data, List<Comment>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Comment childText = (Comment) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listitem_comment, null);
        }

        TextView txtCommentID = (TextView) convertView.findViewById(R.id.tvCommentID);
        txtCommentID.setText(childText.getUserId());

        TextView txtCommentDate = (TextView) convertView.findViewById(R.id.tvCommentDate);
        txtCommentDate.setText(childText.getDate());

        TextView txtCommentContent = (TextView) convertView.findViewById(R.id.tvCommentContent);
        txtCommentContent.setText(childText.getBody());

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
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        FreeBoard.Data headerTitle = (FreeBoard.Data) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvId = (TextView) convertView.findViewById(R.id.tvId);
        TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);

        tvTitle.setText(headerTitle.getTitle());
        tvTime.setText(headerTitle.getWhen());
        tvId.setText(String.valueOf(headerTitle.getAccount_no()));
        tvContent.setText(headerTitle.getBody());


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
