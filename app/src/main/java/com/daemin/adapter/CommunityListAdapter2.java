package com.daemin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.daemin.common.MyVolley;
import com.daemin.common.RoundedCornerNetworkImageView;
import com.daemin.data.CommentData;
import com.daemin.data.PostData;
import com.daemin.event.SetExpandableEvent;
import com.daemin.timetable.R;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

public class CommunityListAdapter2 extends BaseExpandableListAdapter {

    private Context _context;
    private List<PostData.Data> _listDataHeader;
    // child data in format of header title, child title
    private HashMap<String, List<CommentData>> _listDataChild;
    private ImageLoader imageLoader;
    private static final String SAMPLE_IMAGE_URL = "http://hernia.cafe24.com/android/test.png";
    private static final String SAMPLE_IMAGE_URL2 = "http://hernia.cafe24.com/android/test2.png";

    public CommunityListAdapter2(Context context, List<PostData.Data> listDataHeader,
                                 HashMap<String, List<CommentData>> listDataChild) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listDataChild;
        imageLoader = MyVolley.getImageLoader();
    }


    @Override
    public CommentData getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getNickname())
                .get(childPosititon);
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ChildHolder holder;
        if (convertView == null) {
            holder = new ChildHolder();
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listitem_community_comment, null);
            holder.tvChildId = (TextView) convertView.findViewById(R.id.tvChildId);
            holder.tvChildDate = (TextView) convertView.findViewById(R.id.tvChildDate);
            holder.tvChildContent = (TextView) convertView.findViewById(R.id.tvChildContent);
            holder.llCommentBts = (LinearLayout) convertView.findViewById(R.id.llCommentBts);
            holder.btChildEdit = (Button) convertView.findViewById(R.id.btChildEdit);
            holder.btChildRemove = (Button) convertView.findViewById(R.id.btChildRemove);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        holder.tvChildId.setText(getChild(groupPosition, childPosition).getUserId());
        holder.tvChildDate.setText(getChild(groupPosition, childPosition).getDate());
        holder.tvChildContent.setText(getChild(groupPosition,childPosition).getBody());
        holder.llCommentBts = (LinearLayout) convertView.findViewById(R.id.llCommentBts);
        holder.btChildEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        holder.btChildRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getNickname())
                .size();
    }

    @Override
    public PostData.Data getGroup(int groupPosition) {
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
        final GroupHolder holder;
        if (convertView == null) {
            holder = new GroupHolder();
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listitem_community_post, null);
            holder.tvGroupTitle = (TextView) convertView.findViewById(R.id.tvGroupTitle);
            holder.tvGroupTime = (TextView) convertView.findViewById(R.id.tvGroupTime);
            holder.tvGroupId = (TextView) convertView.findViewById(R.id.tvGroupId);
            holder.tvGroupContent = (TextView) convertView.findViewById(R.id.tvGroupContent);
            holder.tvCountComment = (TextView) convertView.findViewById(R.id.tvCountComment);
            holder.btEdit = (Button) convertView.findViewById(R.id.btEdit);
            holder.btRemove = (Button) convertView.findViewById(R.id.btRemove);
            holder.btIndicator = (LinearLayout) convertView.findViewById(R.id.btIndicator);
            holder.ivContent = (NetworkImageView) convertView.findViewById(R.id.ivContent);
            holder.ivProfile = (RoundedCornerNetworkImageView) convertView.findViewById(R.id.ivProfile);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        /*holder.ivContent.setImageBitmap(null);
        imageLoader.get(SAMPLE_IMAGE_URL,
                ImageLoader.getImageListener(holder.ivContent, 0, 0));*/
        //holder.ivProfile.setImageBitmap(null);
        holder.ivContent.setImageUrl(SAMPLE_IMAGE_URL,
                imageLoader);
        holder.ivProfile.setImageUrl(SAMPLE_IMAGE_URL2,
                imageLoader);
        holder.tvGroupTitle.setText(getGroup(groupPosition).getTitle());
        holder.tvGroupTime.setText((getGroup(groupPosition)).getDate() + ' ' + (getGroup(groupPosition)).getTime());
        holder.tvGroupId.setText(String.valueOf((getGroup(groupPosition)).getNickname()));
        holder.tvCountComment.setText(String.valueOf(getChildrenCount(groupPosition)));
        holder.tvGroupContent.setText((getGroup(groupPosition)).getBody_t());
        holder.btIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SetExpandableEvent(groupPosition));
            }
        });
        return convertView;
    }
    private static class GroupHolder {
        public TextView tvGroupTitle;
        public TextView tvGroupTime;
        public TextView tvGroupId;
        public TextView tvGroupContent;
        public TextView tvCountComment;
        public Button btEdit;
        public Button btRemove;
        public LinearLayout btIndicator;
        public NetworkImageView ivContent;
        public RoundedCornerNetworkImageView ivProfile;
    }
    private static class ChildHolder {
        public TextView tvChildId;
        public TextView tvChildDate;
        public TextView tvChildContent;
        public LinearLayout llCommentBts;
        public Button btChildEdit;
        public Button btChildRemove;
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