package com.daemin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.daemin.community.Comment;
import com.daemin.community.github.FreeBoard;
import com.daemin.timetable.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun-yeong on 2015-07-07.
 */
public class ActionSlideExpandableListAdapter extends BaseAdapter {
    private List<FreeBoard.Data> data;
    private List<Comment> comment;

    public ActionSlideExpandableListAdapter(List<FreeBoard.Data> data) {
        this.data = data;

        comment = new ArrayList<>();
        comment.add(new Comment(1, "안녕하세요!", "07.07 21:00", "joyyir"));
        comment.add(new Comment(2, "반가워요 호호호호", "07.07 22:00", "skyrocket"));
        comment.add(new Comment(3, "이거 좋아요!", "07.07 23:00", "fade2black"));
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem_expandable, parent, false);
        }

        TextView tvGroupTitle = (TextView) convertView.findViewById(R.id.tvGroupTitle);
        TextView tvGroupTime = (TextView) convertView.findViewById(R.id.tvGroupTime);
        TextView tvGroupId = (TextView) convertView.findViewById(R.id.tvGroupId);
        TextView tvGroupContent = (TextView) convertView.findViewById(R.id.tvGroupContent);
        TextView tvCountComment = (TextView) convertView.findViewById(R.id.tvCountComment);

        tvGroupTitle.setText(((FreeBoard.Data)getItem(position)).getTitle());
        tvGroupTime.setText(((FreeBoard.Data) getItem(position)).getWhen());
        tvGroupId.setText(String.valueOf(((FreeBoard.Data) getItem(position)).getAccount_no()));
        tvGroupContent.setText(((FreeBoard.Data) getItem(position)).getBody());
        tvCountComment.setText(String.valueOf(comment.size()));

        ListView lvComment = (ListView) convertView.findViewById(R.id.lvComment);
        CommentListAdapter commentListAdapter = new CommentListAdapter(comment);
        lvComment.setAdapter(commentListAdapter);
        setListViewHeightBasedOnChildren(lvComment);
        return convertView;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            listView.setVisibility(View.GONE);
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}



class CommentListAdapter extends BaseAdapter{
    private List<Comment> comment;

    public CommentListAdapter(List<Comment> comment) {
        this.comment = comment;
    }

    @Override
    public int getCount() {
        return comment.size();
    }

    @Override
    public Object getItem(int position) {
        return comment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem_child, parent, false);
        }

        TextView tvChildId = (TextView) convertView.findViewById(R.id.tvChildId);
        TextView tvChildDate = (TextView) convertView.findViewById(R.id.tvChildDate);
        TextView tvChildContent = (TextView) convertView.findViewById(R.id.tvChildContent);

        tvChildId.setText(((Comment)getItem(position)).getUserId());
        tvChildDate.setText(((Comment)getItem(position)).getDate());
        tvChildContent.setText(((Comment) getItem(position)).getBody());

        return convertView;
    }
}