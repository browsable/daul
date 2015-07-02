package com.daemin.timetable.common;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daemin.community.github.GithubActivity;
import com.daemin.data.GroupListData;
import com.navercorp.volleyextensions.request.Jackson2Request;

import java.util.ArrayList;

/**
 * Created by hernia on 2015-07-02.
 */
public class MyRequest {
    public static RequestQueue requestQueue = MyVolley.getRequestQueue();
    public static ArrayList<String> groupListData = new ArrayList<>();
    //public static Context context = AppController.getInstance();
    public static final String GET_GROUPLIST_URL = "http://heeguchi.cafe24.com/app/getGroupList";
    //public static GroupListFromServer groupListFromServer = new GroupListFromServer();
    public static ArrayList<String> getGroupList(){
        Jackson2Request<GroupListData> jackson2Request = new Jackson2Request<>(
                Request.Method.POST,GET_GROUPLIST_URL, GroupListData.class,
                new Response.Listener<GroupListData>() {
                    @Override
                    public void onResponse(GroupListData response) {
                        for(GroupListData.Data d : response.getData()){
                                groupListData.add(d.getName());
                        }
                      /*  GroupListFromServerRepository.clearGroupListFromServer(context);
                        for(GroupListData.Data d : response.getData()){
                            GroupListFromServer groupListFromServer = new GroupListFromServer();
                            groupListFromServer.setName(d.getName());
                            GroupListFromServerRepository.insertOrUpdate(context, groupListFromServer);
                        }*/
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(GithubActivity.class.getSimpleName(), ""
                        + error.getMessage());
            }

        });
        requestQueue.add(jackson2Request);
        return groupListData;
    }
}
