package com.daemin.community;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daemin.adapter.ActionSlideExpandableListAdapter;
import com.daemin.common.BasicFragment;
import com.daemin.common.MyVolley;
import com.daemin.community.github.FreeBoard;
import com.daemin.community.github.GithubActivity;
import com.daemin.community.lib.AbstractSlideExpandableListAdapter;
import com.daemin.community.lib.ActionSlideExpandableListView;
import com.daemin.community.lib.SlideExpandableListAdapter;
import com.daemin.main.SubMainActivity;
import com.daemin.timetable.R;
import com.navercorp.volleyextensions.request.Jackson2Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hernia on 2015-07-07.
 */
public class CommunityFragment2 extends BasicFragment {
    private final String GET_PERSON_URL = "http://timedao.heeguchi.me/app/getArticleList";
    private static CommunityFragment2 singleton;
    private final String userId = "joyyir";
    private int userAccountNum = 921111;
    private List<FreeBoard.Data> data;
    private View root;
    ActionSlideExpandableListAdapter adapter;

    public CommunityFragment2(){
        super(R.layout.fragment_community2, "CommunityFragment");
        singleton = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = super.onCreateView(inflater, container, savedInstanceState);

        if (layoutId > 0) {
            Button btWriteArticle = (Button) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.btWriteArticle);
            btWriteArticle.setVisibility(View.VISIBLE);
            btWriteArticle.setText("글쓰기");
            btWriteArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubMainActivity.getInstance().changeFragment(WritePostFragment.class, "커뮤니티", R.color.orange);
                }
            });

            RequestQueue requestQueue = MyVolley.getRequestQueue();

            Jackson2Request<FreeBoard> jackson2Request = new Jackson2Request<FreeBoard>(
                    Request.Method.POST,GET_PERSON_URL, FreeBoard.class,
                    new Response.Listener<FreeBoard>() {
                        @Override
                        public void onResponse(FreeBoard response) {
                            Log.d("junyeong", response.toString());
                            data = response.getData();

                            if(Post.isWritten() == true){
                                FreeBoard.Data addedData = new FreeBoard.Data();

                                addedData.setDate(Post.getDate());
                                addedData.setTime(Post.getTime());
                                addedData.setBody_t(Post.getContent());
                                addedData.setTitle(Post.getTitle());
                                addedData.setAccount_no(userAccountNum);
                                addedData.setNickname(userId);

                                data.add(0, addedData);
                                Post.setIsWritten(false);
                            }

                            ActionSlideExpandableListView list = (ActionSlideExpandableListView) root.findViewById(R.id.list);
                            adapter = new ActionSlideExpandableListAdapter(data, userId, userAccountNum, getActivity());
                            list.setAdapter(adapter);
                            ((SlideExpandableListAdapter)list.getAdapter()).setItemExpandCollapseListener(new AbstractSlideExpandableListAdapter.OnItemExpandCollapseListener() {
                                @Override
                                public void onExpand(View itemView, int position) {
                                    ImageView groupIndicator = (ImageView) ((View) itemView.getParent()).findViewById(R.id.expandable_arrow);
                                    groupIndicator.setImageResource(R.drawable.ic_action_collapse);
                                    adapter.setExpandCollapseList(position, adapter.EXPANDED);
                                }

                                @Override
                                public void onCollapse(View itemView, int position) {
                                    ImageView groupIndicator = (ImageView) ((View) itemView.getParent()).findViewById(R.id.expandable_arrow);
                                    groupIndicator.setImageResource(R.drawable.ic_action_expand);
                                    adapter.setExpandCollapseList(position, adapter.COLLAPSED);
                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(GithubActivity.class.getSimpleName(), "" + error.getMessage());
                        }
                    }
            ){
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

        return root;
    }

    public static CommunityFragment2 getInstance(){
        return singleton;
    }

    public List<FreeBoard.Data> getData(){
        return data;
    }
}