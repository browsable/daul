package com.daemin.common;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daemin.community.github.GithubActivity;
import com.daemin.data.GroupListData;
import com.daemin.enumclass.User;
import com.daemin.repository.GroupListFromServerRepository;
import com.daemin.timetable.R;
import com.navercorp.volleyextensions.request.Jackson2Request;

import java.util.ArrayList;

import timedao_group.GroupListFromServer;

/**
 * Created by hernia on 2015-07-02.
 */
public class MyRequest {
    public static RequestQueue requestQueue = MyVolley.getRequestQueue();

    public static Context context = AppController.getInstance();
    public static final String GET_GROUPLIST_URL = "http://timedao.heeguchi.me/app/getGroupList";
    public static ArrayList<String> getGroupListFomServer() {
        ArrayList<String> groupListFomServer = new ArrayList<>();
        for( GroupListFromServer GLFS : GroupListFromServerRepository.getAllGroupListFromServer(context)){
            groupListFomServer.add(GLFS.getKorname());
        }
        return groupListFomServer;
    }
    //public static GroupListFromServer groupListFromServer = new GroupListFromServer();
    public static void getGroupList(){
        Jackson2Request<GroupListData> jackson2Request = new Jackson2Request<>(
                Request.Method.POST,GET_GROUPLIST_URL, GroupListData.class,
                new Response.Listener<GroupListData>() {
                    @Override
                    public void onResponse(GroupListData response) {
                        /*for(GroupListData.Data d : response.getData()){
                                groupListData.add(d.getName());
                        }*/
                        GroupListFromServerRepository.clearGroupListFromServer(context);
                        for(GroupListData.Data d : response.getData()){
                            GroupListFromServer groupListFromServer = new GroupListFromServer();
                            groupListFromServer.setKorname(d.getKorname());
                            groupListFromServer.setEngname(d.getEngname());
                            groupListFromServer.setWhen(d.getWhen());
                            GroupListFromServerRepository.insertOrUpdate(context, groupListFromServer);
                        }
                        User.USER.setGroupListDownloadState(true);
                        Toast.makeText(context, "첫 그룹리스트 다운로드", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(GithubActivity.class.getSimpleName(), ""
                        + error.getMessage());
                Toast.makeText(context, context.getString(R.string.dataloading_error), Toast.LENGTH_SHORT).show();
            }

        });
        requestQueue.add(jackson2Request);
    }

}
