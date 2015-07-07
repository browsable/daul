
// 커밋푸시 테스트
package com.daemin.community;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
    View root;

    public CommunityFragment() {
        super(R.layout.listitem_community, "CommunityFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = super.onCreateView(inflater, container, savedInstanceState);
        if (layoutId > 0) {


            expListView = (ExpandableListView) root.findViewById(R.id.lvExpComment);
            getResponse();
           /* // 현재 이 부분 작동하지 않음...
            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    ImageView groupIndicator = (ImageView) root.findViewById(R.id.commentIndicator);

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
            // 끝*/


        }

        return root;
    }


    private void prepareListData() {
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
    private void setListIndicator() {
        DisplayMetrics metrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
            expListView.setIndicatorBounds(GetPixelFromDips(100), GetPixelFromDips(130));
        else
            expListView.setIndicatorBoundsRelative(GetPixelFromDips(100), GetPixelFromDips(130));
    }

    public int GetPixelFromDips(float pixels) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pixels * scale + 0.5f);
    }

    private void getResponse() {
        requestQueue = MyVolley.getRequestQueue();

        Jackson2Request<FreeBoard> jackson2Request = new Jackson2Request<FreeBoard>(
                Request.Method.POST, GET_PERSON_URL, FreeBoard.class,
                new Response.Listener<FreeBoard>() {
                    @Override
                    public void onResponse(FreeBoard response) {
                        listDataHeader = response.getData();

                        prepareListData();

                        listAdapter = new com.daemin.adapter.ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

                        expListView.setAdapter(listAdapter);

                        //setListIndicator(); // group indicator의 위치 변경
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(GithubActivity.class.getSimpleName(), ""
                        + error.getMessage());
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("table", "freeboard");
                map.put("page", "1");
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
