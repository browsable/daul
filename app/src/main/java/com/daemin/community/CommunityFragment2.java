package com.daemin.community;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.Toast;

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
import com.daemin.community.lib.ActionSlideExpandableListView;
import com.daemin.timetable.R;
import com.navercorp.volleyextensions.request.Jackson2Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hernia on 2015-07-07.
 */
public class CommunityFragment2 extends BasicFragment {
    private List<FreeBoard.Data> data;
    private final String myId = "joyyir";
    View root;

    public CommunityFragment2()
    {
        super(R.layout.fragment_community2, "CommunityFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = super.onCreateView(inflater, container, savedInstanceState);
        if (layoutId > 0) {

            final String GET_PERSON_URL = "http://timedao.heeguchi.me/app/getArticleList";
            RequestQueue requestQueue = MyVolley.getRequestQueue();

            Jackson2Request<FreeBoard> jackson2Request = new Jackson2Request<FreeBoard>(
                    Request.Method.POST,GET_PERSON_URL,  FreeBoard.class,
                    new Response.Listener<FreeBoard>() {
                        @Override
                        public void onResponse(FreeBoard response) {
                            data = response.getData();

                            ActionSlideExpandableListView list = (ActionSlideExpandableListView) root.findViewById(R.id.list);

                            // fill the list with data
                            list.setAdapter(buildDummyData());

                            // listen for events in the two buttons for every list item.
                            // the 'position' var will tell which list item is clicked
                            list.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {

                                @Override
                                public void onClick(View listView, View buttonview, int position) {
                                    /**
                                     * Normally you would put a switch
                                     * statement here, and depending on
                                     * view.getId() you would perform a
                                     * different action.
                                     */
                                    String actionName = "";
                                    if (buttonview.getId() == R.id.btChildEdit) {
                                        actionName = "btChildEdit";
                                    } else {
                                        actionName = "btChildRemove";
                                    }
                                    /**
                                     * For testing sake we just show a toast
                                     */
                                    Toast.makeText(
                                            getActivity(),
                                            "버튼이름: " + actionName + ", position: " + String.valueOf(position),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }

                                // note that we also add 1 or more ids to the setItemActionListener
                                // this is needed in order for the listview to discover the buttons
                            }, R.id.btChildEdit, R.id.btChildRemove);

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

            /*@Override
            public byte[] getBody() throws AuthFailureError {
                return "{\"table\":\"freeboard\", \"page\": \"1\"}".getBytes();
                //return postFreeboard.getBytes();
            }*/
            };
            requestQueue.add(jackson2Request);

            //loadPersonInfo();


        }
        return root;
    }
    public ListAdapter buildDummyData() {
        return new ActionSlideExpandableListAdapter(data);
    }

}
