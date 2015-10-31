package com.daemin.common;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daemin.community.github.GithubActivity;
import com.daemin.data.GroupListData;
import com.daemin.enumclass.User;
import com.daemin.repository.GroupListFromServerRepository;
import com.navercorp.volleyextensions.request.Jackson2Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timedao_group.GroupListFromServer;

/**
 * Created by hernia on 2015-07-02.
 */
public class MyRequest {
    public static RequestQueue requestQueue = MyVolley.getRequestQueue();

    public static Context context = AppController.getInstance();
    public static final String GET_GROUPLIST_URL = "http://timedao.heeguchi.me/app/getGroupList";
    public static ArrayList<String> getGroupListFromLocal() {
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
                                Log.i(d.getEngname(),d.getKorname());
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
                //Toast.makeText(context, context.getString(R.string.dataloading_error), Toast.LENGTH_SHORT).show();
            }

        });
        requestQueue.add(jackson2Request);
    }

    public static final String POST_TEST = "http://54.64.223.92/users/register";
    private static String KEY_SUCCESS = "success";
    public static void test(final Context context){
        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.POST, POST_TEST, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString(KEY_SUCCESS) != null) {
                                int success = Integer.parseInt(response.getString(KEY_SUCCESS));
                                if (success == 1) {
                                    Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                                    /*Toast.makeText(context, response.getString("tag"), Toast.LENGTH_LONG).show();
                                    Toast.makeText(context, response.getString("username"), Toast.LENGTH_LONG).show();
                                    Toast.makeText(context, response.getString("email"), Toast.LENGTH_LONG).show();
                                    Toast.makeText(context, response.getString("password"), Toast.LENGTH_LONG).show();*/
                                } else if (success == 0) {
                                    Toast.makeText(context, "Invalid email or password..", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, "Something went wrong.Please try again..", Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response Error", error.toString());
                Toast.makeText(context, "network error", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "register");
                params.put("email", "hernia@koreatech.ac.kr");
                params.put("username", "daemin");
                params.put("password", Common.md5("qlalfqjsgh"));
                return params;
            }

        };
        requestQueue.add(rq);
    }
}
