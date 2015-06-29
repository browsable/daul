// 커밋푸시 테스트
package com.daemin.community;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.daemin.timetable.R;
import com.daemin.timetable.adapter.ExpandableListAdapter;
import com.daemin.timetable.common.BasicFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommunityFragment extends BasicFragment {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<List<String>>> listDataChild;


    public CommunityFragment() {
        super(R.layout.listitem_community, "CommunityFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
        if (layoutId > 0) {

            expListView = (ExpandableListView) root.findViewById(R.id.lvExpComment);

            prepareListData();

            listAdapter = new ExpandableListAdapter(this.getActivity(), listDataHeader, listDataChild);

            expListView.setAdapter(listAdapter);

            setListIndicator(); // group indicator의 위치 변경
        }

        return root;
    }

    private void prepareListData(  ){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<List<String>>>();

        listDataHeader.add("댓글");

        List<List<String>> comment = new ArrayList<List<String>>();

        List<String> comment1 = new ArrayList<String>();
        comment1 = new ArrayList<String>();
        comment1.add("joyyir");
        comment1.add("06.05 14:07");
        comment1.add("우와 신난다ㅎㅎㅎㅎ");

        List<String> comment2 = new ArrayList<String>();
        comment2 = new ArrayList<String>();
        comment2.add("skyrocket");
        comment2.add("06.05 14:16");
        comment2.add("안녕하십니까?");

        comment.add(comment1);
        comment.add(comment2);

        listDataChild.put(listDataHeader.get(0), comment); // Header, Child data
    }

    @SuppressLint("NewApi")
    private void setListIndicator()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
            //expListView.setIndicatorBounds(width - GetPixelFromDips(35), width - GetPixelFromDips(5));
            expListView.setIndicatorBounds(GetPixelFromDips(100), GetPixelFromDips(130));
        else
            //expListView.setIndicatorBoundsRelative(width - GetPixelFromDips(35), width - GetPixelFromDips(5));
            expListView.setIndicatorBoundsRelative(GetPixelFromDips(100), GetPixelFromDips(130));
    }

    public int GetPixelFromDips(float pixels){
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pixels * scale + 0.5f);
    }
}

/*
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.daemin.community.amazon.AmazonActivity;
import com.daemin.community.github.GithubActivity;
import com.daemin.community.view.DoubleTapZoomNetworkImageViewActivity;
import com.daemin.community.view.SingleTapZoomNetworkImageViewActivity;
import com.daemin.timetable.R;
import com.daemin.timetable.common.BasicFragment;

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
*/
