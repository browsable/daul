
package com.daemin.adapter;


/**
 * Created by Jun-yeong on 2015-06-24.
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daemin.community.Comment;
import com.daemin.community.github.FreeBoard;
import com.daemin.timetable.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter{

	private List<FreeBoard.Data> listDataHeader;
	private HashMap<FreeBoard.Data, List<Comment>> listDataChild;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	public ExpandableListAdapter(Context c, List<FreeBoard.Data> listDataHeader,
								 HashMap<FreeBoard.Data, List<Comment>> listDataChild){
		super();
		this.inflater = LayoutInflater.from(c);
		this.listDataHeader = listDataHeader;
		this.listDataChild = listDataChild;
	}

	// 그룹 포지션을 반환한다.
	@Override
	public FreeBoard.Data getGroup(int groupPosition) {
		return listDataHeader.get(groupPosition);
	}

	// 그룹 사이즈를 반환한다.
	@Override
	public int getGroupCount() {
		return listDataHeader.size();
	}

	// 그룹 ID를 반환한다.
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 그룹뷰 각각의 ROW
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		View v = convertView;
		FreeBoard.Data headerTitle = getGroup(groupPosition);
		if(v == null){
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.listitem_group, parent, false);
			viewHolder.tvGroupTitle = (TextView) v.findViewById(R.id.tvGroupTitle);
			viewHolder.tvGroupTime = (TextView) v.findViewById(R.id.tvGroupTime);
			viewHolder.tvGroupId = (TextView) v.findViewById(R.id.tvGroupId);
			viewHolder.tvGroupContent = (TextView) v.findViewById(R.id.tvGroupContent);
			viewHolder.commentIndicator = (ImageView) v.findViewById(R.id.commentIndicator);
			viewHolder.llComment = (LinearLayout) v.findViewById(R.id.llComment);
			v.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)v.getTag();
		}

		// 그룹을 펼칠때와 닫을때 아이콘을 변경해 준다.
		if(isExpanded){
			viewHolder.commentIndicator.setBackgroundResource(R.drawable.ic_action_expand);
		}else{
			viewHolder.commentIndicator.setBackgroundResource(R.drawable.ic_action_collapse);
		}

		viewHolder.tvGroupTitle.setText(headerTitle.getTitle());
		viewHolder.tvGroupTime.setText(headerTitle.getWhen());
		viewHolder.tvGroupId.setText(String.valueOf(headerTitle.getAccount_no()));
		viewHolder.tvGroupContent.setText(headerTitle.getBody());

		return v;
	}

	// 차일드뷰를 반환한다.
	@Override
	public Comment getChild(int groupPosition, int childPosition) {
		return listDataChild.get(groupPosition).get(childPosition);
	}

	// 차일드뷰 사이즈를 반환한다.
	@Override
	public int getChildrenCount(int groupPosition) {
		return listDataChild.get(groupPosition).size();
	}

	// 차일드뷰 ID를 반환한다.
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// 차일드뷰 각각의 ROW
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		View v = convertView;
		Comment childText = getChild(groupPosition, childPosition);
		if(v == null){
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.listitem_child, null);
			viewHolder.tvChildDate = (TextView) v.findViewById(R.id.tvChildDate);
			viewHolder.tvChildId = (TextView) v.findViewById(R.id.tvChildId);
			viewHolder.tvChildContent = (TextView) v.findViewById(R.id.tvChildContent);
			v.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)v.getTag();
		}
		viewHolder.tvChildId.setText(childText.getUserId());
		viewHolder.tvChildDate.setText(childText.getDate());
		viewHolder.tvChildContent.setText(childText.getBody());

		return v;
	}

	@Override
	public boolean hasStableIds() {	return true; }

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }

	class ViewHolder {
		public TextView tvGroupTitle;
		public TextView tvGroupTime;
		public TextView tvGroupId;
		public TextView tvGroupContent;
		public ImageView commentIndicator;
		public LinearLayout llComment;
		public TextView tvChildId;
		public TextView tvChildDate;
		public TextView tvChildContent;

	}

}