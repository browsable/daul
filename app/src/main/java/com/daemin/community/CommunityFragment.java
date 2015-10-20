package com.daemin.community;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.daemin.event.ChangeFragEvent;
import com.daemin.timetable.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.navercorp.volleyextensions.request.Jackson2Request;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by hernia on 2015-07-07.
 */
public class CommunityFragment extends BasicFragment {
    private final String GET_PERSON_URL = "http://timedao.heeguchi.me/app/getArticleList";
    private static CommunityFragment singleton;
    private final String userId = "joyyir";
    private int userAccountNum = 921111;
    private LinkedList<FreeBoard.Data> data;
    private View root;
    CommunityListAdapter adapter;
    PullToRefreshListView list;
    private ProgressBar loadingProgress;
    RequestQueue requestQueue;
    public CommunityFragment(){
        super(R.layout.fragment_community, "CommunityFragment");
        singleton = this;
    }
    public static CommunityFragment getInstance(){
        return singleton;
    }
    public List<FreeBoard.Data> getData(){
        return data;
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
            requestQueue = MyVolley.getRequestQueue();
            Button btWriteArticle = (Button) ((View)container.getParent().getParent().getParent().getParent()).findViewById(R.id.btWriteArticle);
            btWriteArticle.setVisibility(View.VISIBLE);
            btWriteArticle.setText("글쓰기");
            btWriteArticle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new ChangeFragEvent(WritePostFragment.class, "커뮤니티"));
                }
            });
            loadingProgress = (ProgressBar) root.findViewById(R.id.loadingProgress);
            list = (PullToRefreshListView) root.findViewById(R.id.list);
            // Set a listener to be invoked when the list should be refreshed.
            list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                @Override
                public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                    String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                            DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                    // Update the LastUpdatedLabel
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                    // Do work to refresh the list here.
                    requestQueue.add(jackson2Request);
                }
            });

            // Add an end-of-list listener
            list.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

                @Override
                public void onLastItemVisible() {
                    Toast.makeText(getActivity(), "End of List!", Toast.LENGTH_SHORT).show();
                }
            });

            ListView actualListView = list.getRefreshableView();
            // Need to use the Actual ListView when registering for Context Menu
            registerForContextMenu(actualListView);

            data = new LinkedList<>();
            adapter = new CommunityListAdapter(getActivity(),data);
            list.setAdapter(adapter);
            requestQueue.add(jackson2Request);
        }

        return root;
    }

    Jackson2Request<FreeBoard> jackson2Request = new Jackson2Request<FreeBoard>(
            Request.Method.POST,GET_PERSON_URL, FreeBoard.class,
            new Response.Listener<FreeBoard>() {
                @Override
                public void onResponse(FreeBoard response) {
                    Log.d("junyeong", response.toString());
                    for(FreeBoard.Data dt: response.getData()) {
                        loadingProgress.setVisibility(View.INVISIBLE);
                        data.addFirst(dt);
                    }
                    adapter.notifyDataSetChanged();
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
}