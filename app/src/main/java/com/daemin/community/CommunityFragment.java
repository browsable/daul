package com.daemin.community;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.daemin.common.BasicFragment;
import com.daemin.community.amazon.AmazonActivity;
import com.daemin.community.github.GithubActivity;
import com.daemin.community.lib.DoubleTapZoomNetworkImageViewActivity;
import com.daemin.community.lib.SingleTapZoomNetworkImageViewActivity;
import com.daemin.timetable.R;

public class CommunityFragment extends BasicFragment {


    public CommunityFragment() {
        super(R.layout.fragment_community, "CommunityFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
        if (layoutId > 0) {

            Button githubActivityButton = (Button) root.findViewById(R.id.github_activity_button);
            Button amazonActivityButton = (Button) root.findViewById(R.id.amazon_activity_button);
            Button singleTapZoomNetworkImageViewActivityButton = (Button) root.findViewById(R.id.single_tap_zoom_network_image_view_activity_button);
            Button doubleTapZoomNetworkImageViewActivityButton = (Button) root.findViewById(R.id.double_tap_zoom_network_image_view_activity_button);
            githubActivityButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), GithubActivity.class);
                    getActivity().startActivity(intent);
                }});

            amazonActivityButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AmazonActivity.class);
                    getActivity().startActivity(intent);
                }});

            singleTapZoomNetworkImageViewActivityButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SingleTapZoomNetworkImageViewActivity.class);
                    getActivity().startActivity(intent);
                }});

            doubleTapZoomNetworkImageViewActivityButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), DoubleTapZoomNetworkImageViewActivity.class);
                    getActivity().startActivity(intent);
                }});

        }

        return root;
    }

}

/*

package com.daemin.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.daemin.adapter.BaseExpandableAdapter2;
import com.daemin.common.BasicFragment;
import com.daemin.timetable.R;

import java.util.ArrayList;

public class CommunityFragment extends BasicFragment {
    private ArrayList<String> mGroupList = null;
    private ArrayList<ArrayList<String>> mChildList = null;
    private ArrayList<String> mChildListContent = null;
    private ExpandableListView mListView;
    View root;

    public CommunityFragment() {
        super(R.layout.listitem_community, "CommunityFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = super.onCreateView(inflater, container, savedInstanceState);
        if (layoutId > 0) {
            mListView = (ExpandableListView) root.findViewById(R.id.lvExpComment);
            mGroupList = new ArrayList<String>();
            mChildList = new ArrayList<ArrayList<String>>();
            mChildListContent = new ArrayList<String>();

            mGroupList.add("가위");
            mGroupList.add("바위");
            mGroupList.add("보");

            mChildListContent.add("1");
            mChildListContent.add("2");
            mChildListContent.add("3");

            mChildList.add(mChildListContent);
            mChildList.add(mChildListContent);
            mChildList.add(mChildListContent);

            mListView.setAdapter(new BaseExpandableAdapter2(getActivity(), mGroupList, mChildList));

            // 그룹 클릭 했을 경우 이벤트
            mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    Toast.makeText(getActivity(), "g click = " + groupPosition,
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            // 차일드 클릭 했을 경우 이벤트
            mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    Toast.makeText(getActivity(), "c click = " + childPosition,
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            // 그룹이 닫힐 경우 이벤트
            mListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                @Override
                public void onGroupCollapse(int groupPosition) {
                    Toast.makeText(getActivity(), "g Collapse = " + groupPosition,
                            Toast.LENGTH_SHORT).show();
                }
            });

            // 그룹이 열릴 경우 이벤트
            mListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    Toast.makeText(getActivity(), "g Expand = " + groupPosition,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        return root;
    }
}*/
