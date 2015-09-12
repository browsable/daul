package com.daemin.community;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daemin.adapter.CommunityListAdapter;
import com.daemin.common.BasicFragment;
import com.daemin.common.MyVolley;
import com.daemin.community.github.FreeBoard;
import com.daemin.community.github.GithubActivity;
import com.daemin.main.SubMainActivity;
import com.daemin.timetable.R;
import com.navercorp.volleyextensions.request.Jackson2Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hernia on 2015-07-07.
 */
public class CommunityFragment extends BasicFragment {
    private final String GET_PERSON_URL = "http://timedao.heeguchi.me/app/getArticleList";
    private static CommunityFragment singleton;
    private final String userId = "joyyir";
    private int userAccountNum = 921111;
    private List<FreeBoard.Data> data;
    private View root;
    CommunityListAdapter adapter;
    ListView list;
    private ProgressBar loadingProgress;
    public CommunityFragment(){
        super(R.layout.fragment_community, "CommunityFragment");
        singleton = this;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
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
            loadingProgress = (ProgressBar) root.findViewById(R.id.loadingProgress);
            list = (ListView) root.findViewById(R.id.list);
            data = new ArrayList<>();
            adapter = new CommunityListAdapter(getActivity(),data);
            list.setAdapter(adapter);
            RequestQueue requestQueue = MyVolley.getRequestQueue();
            Jackson2Request<FreeBoard> jackson2Request = new Jackson2Request<FreeBoard>(
                    Request.Method.POST,GET_PERSON_URL, FreeBoard.class,
                    new Response.Listener<FreeBoard>() {
                        @Override
                        public void onResponse(FreeBoard response) {
                            Log.d("junyeong", response.toString());
                            for(FreeBoard.Data dt: response.getData()) {
                                loadingProgress.setVisibility(View.INVISIBLE);
                                data.add(dt);
                                adapter.notifyDataSetChanged();
                            }
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

    public static CommunityFragment getInstance(){
        return singleton;
    }

    public List<FreeBoard.Data> getData(){
        return data;
    }
}