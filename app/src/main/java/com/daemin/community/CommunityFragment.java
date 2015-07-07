// 커밋푸시 테스트
package com.daemin.community;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daemin.adapter.ExpandableListAdapter;
import com.daemin.common.BasicFragment;
import com.daemin.common.MyVolley;
import com.daemin.community.github.FreeBoard;
import com.daemin.community.github.GithubActivity;
import com.daemin.timetable.R;
import com.navercorp.volleyextensions.request.Jackson2Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunityFragment extends BasicFragment {
    private static final String GET_PERSON_URL = "http://timedao.heeguchi.me/app/getArticleList";
    private RequestQueue requestQueue;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<FreeBoard.Data> listDataHeader;
    HashMap<FreeBoard.Data, List<Comment>> listDataChild;
    Activity activity;

    public CommunityFragment() {
        super(R.layout.listitem_community, "CommunityFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
        if (layoutId > 0) {

            expListView = (ExpandableListView) root.findViewById(R.id.lvExpComment);

            activity = this.getActivity();

            getResponse();

            // 현재 이 부분 작동하지 않음...
            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    ImageView groupIndicator = (ImageView) activity.findViewById(R.id.commentIndicator);

                    if (parent.isGroupExpanded(groupPosition)) {
                        parent.collapseGroup(groupPosition);
                        groupIndicator.setImageResource(R.drawable.ic_action_expand);
                    } else {
                        parent.expandGroup(groupPosition);
                        groupIndicator.setImageResource(R.drawable.ic_action_collapse);
                    }
                    return true;
                }
            });
            // 끝
        }

        return root;
    }

    private void prepareListData(  ){
        listDataChild = new HashMap<>();

        List<Comment> comment = new ArrayList<>();

        Comment comment1 = new Comment(1, "우와 신난다ㅎㅎㅎㅎ", "06.05 14:07", "joyyir");
        Comment comment2 = new Comment(2, "안녕하십니까?", "06.05 14:16", "skyrocket");

        comment.add(comment1);
        comment.add(comment2);

        listDataChild.put(listDataHeader.get(0), comment); // Header, Child data
        listDataChild.put(listDataHeader.get(1), comment); // Header, Child data
        listDataChild.put(listDataHeader.get(2), comment); // Header, Child data
    }

    @SuppressLint("NewApi")
    private void setListIndicator()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
            expListView.setIndicatorBounds(GetPixelFromDips(100), GetPixelFromDips(130));
        else
            expListView.setIndicatorBoundsRelative(GetPixelFromDips(100), GetPixelFromDips(130));
    }

    public int GetPixelFromDips(float pixels){
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pixels * scale + 0.5f);
    }

    private void getResponse(){
        requestQueue = MyVolley.getRequestQueue();

        Jackson2Request<FreeBoard> jackson2Request = new Jackson2Request<FreeBoard>(
                Request.Method.POST,GET_PERSON_URL,  FreeBoard.class,
                new Response.Listener<FreeBoard>() {
                    @Override
                    public void onResponse(FreeBoard response) {
                        listDataHeader = response.getData();

                        prepareListData();

                        listAdapter = new ExpandableListAdapter(activity, listDataHeader, listDataChild);

                        expListView.setAdapter(listAdapter);

                        setListIndicator(); // group indicator의 위치 변경
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(GithubActivity.class.getSimpleName(), ""
                        + error.getMessage());
            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("table","freeboard");
                map.put("page","1");
                return map;
            }
        };

        requestQueue.add(jackson2Request);
    }
}

/*
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.daemin.common.BasicFragment;
import com.daemin.community.amazon.AmazonActivity;
import com.daemin.community.github.GithubActivity;
import com.daemin.community.view.DoubleTapZoomNetworkImageViewActivity;
import com.daemin.community.view.SingleTapZoomNetworkImageViewActivity;
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
*/