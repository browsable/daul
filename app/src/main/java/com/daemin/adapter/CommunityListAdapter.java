package com.daemin.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.daemin.common.MyVolley;
import com.daemin.common.RoundedCornerNetworkImageView;
import com.daemin.community.github.FreeBoard;
import com.daemin.timetable.R;

import java.util.List;

/**
 * Created by hernia on 2015-07-02.
 */
public class CommunityListAdapter extends ArrayAdapter<FreeBoard.Data> {
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;
    private static final String SAMPLE_IMAGE_URL = "http://hernia.cafe24.com/android/test.png";
    private static final String SAMPLE_IMAGE_URL2 = "http://hernia.cafe24.com/android/test2.png";

    public CommunityListAdapter(Context context, List<FreeBoard.Data> values) {
        super(context, R.layout.listitem_community_post, values);
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = MyVolley.getImageLoader();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.listitem_community_post, parent, false);
            holder.tvGroupTitle = (TextView) convertView.findViewById(R.id.tvGroupTitle);
            holder.tvGroupTime = (TextView) convertView.findViewById(R.id.tvGroupTime);
            holder.tvGroupId = (TextView) convertView.findViewById(R.id.tvGroupId);
            holder.tvGroupContent = (TextView) convertView.findViewById(R.id.tvGroupContent);
            holder.tvCountComment = (TextView) convertView.findViewById(R.id.tvCountComment);
            holder.btEdit = (Button) convertView.findViewById(R.id.btEdit);
            holder.btRemove = (Button) convertView.findViewById(R.id.btRemove);
            holder.ivContent = (NetworkImageView) convertView.findViewById(R.id.ivContent);
            holder.ivProfile = (RoundedCornerNetworkImageView) convertView.findViewById(R.id.ivProfile);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        /*holder.ivContent.setImageBitmap(null);
        imageLoader.get(SAMPLE_IMAGE_URL,
                ImageLoader.getImageListener(holder.ivContent, 0, 0));*/
        //holder.ivProfile.setImageBitmap(null);
        holder.ivContent.setImageUrl(SAMPLE_IMAGE_URL,
                imageLoader);
        holder.ivProfile.setImageUrl(SAMPLE_IMAGE_URL2,
                imageLoader);
        holder.tvGroupTitle.setText((getItem(position)).getTitle());
        holder.tvGroupTime.setText((getItem(position)).getDate() + ' ' + (getItem(position)).getTime());
        holder.tvGroupId.setText(String.valueOf((getItem(position)).getNickname()));
        holder.tvGroupContent.setText((getItem(position)).getBody_t());
        return convertView;
    }
    /**
     * View holder for the views we need access to
     */
    private static class Holder {
        public TextView tvGroupTitle;
        public TextView tvGroupTime;
        public TextView tvGroupId;
        public TextView tvGroupContent;
        public TextView tvCountComment;
        public Button btEdit;
        public Button btRemove;
        public NetworkImageView ivContent;
        public RoundedCornerNetworkImageView ivProfile;
    }
}