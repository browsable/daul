package com.daemin.common;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daemin.data.GroupListData;
import com.daemin.dialog.DialDefault;
import com.daemin.enumclass.User;
import com.daemin.event.PostGroupListEvent;
import com.daemin.timetable.R;
import com.navercorp.volleyextensions.request.Jackson2Request;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by hernia on 2015-07-02.
 */
public class MyRequest {
    public static RequestQueue requestQueue = MyVolley.getRequestQueue();
    public static Context context = AppController.getInstance();
    private static String KEY_STATUS = "status";
    public static final String GET_VERSION = "http://timenuri.com/ajax/app/get_version";
    public static void getVersionFromServer(final Context context) {
        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.GET, GET_VERSION, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString(KEY_STATUS).equals("Success")) {
                                JSONObject data = response.getJSONObject("data");
                                User.INFO.appServerVer = data.getString("appversion");
                                if (!User.INFO.appVer.equals(User.INFO.appServerVer)) {
                                    DialDefault dd = new DialDefault(context,
                                            context.getResources().getString(R.string.update_title),
                                            context.getResources().getString(R.string.update_notice),
                                            0);
                                    dd.show();
                                }
                            } else {
                                Toast.makeText(context, "Something went wrong.Please try again..", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(rq);
    }

    public static final String GET_GROUP_LIST = "http://timenuri.com/ajax/app/get_univ_list";
    public static void getGroupList(final Context context) {
        Jackson2Request<GroupListData> jackson2Request = new Jackson2Request<>(
                Request.Method.POST, GET_GROUP_LIST, GroupListData.class,
                new Response.Listener<GroupListData>() {
                    @Override
                    public void onResponse(GroupListData response) {
                        User.INFO.groupListData = response.getData();
                        EventBus.getDefault().post(new PostGroupListEvent());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jackson2Request);
    }

    public static final String GET_DBVERSION = " http://browsable.cafe24.com/timetable/get_version.php";
    public static void getDBVerWithMyGroup(final Context context,final int groupPK) {
        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.POST, GET_DBVERSION, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt("success");
                            if (success==1) {
                                JSONObject data = response.getJSONArray("product").getJSONObject(0);
                                User.INFO.dbServerVer = data.getString("db_version");
                            }else {
                                Toast.makeText(context, "Something went wrong.Please try again..", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
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
                params.put("num", String.valueOf(groupPK));
                return params;
            }
        };
        requestQueue.add(rq);
    }
    /*
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
                        *//*for(GroupListData.Data d : response.getData()){
                                Log.i(d.getEngname(),d.getKorname());
                        }*//*
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
*/
   /* public static final String POST_TEST = "http://54.64.223.92/users/register";
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
                                    *//*Toast.makeText(context, response.getString("tag"), Toast.LENGTH_LONG).show();
                                    Toast.makeText(context, response.getString("username"), Toast.LENGTH_LONG).show();
                                    Toast.makeText(context, response.getString("email"), Toast.LENGTH_LONG).show();
                                    Toast.makeText(context, response.getString("password"), Toast.LENGTH_LONG).show();*//*
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
                //params.put("password", Common.md5("qlalfqjsgh"));
                return params;
            }

        };
        requestQueue.add(rq);
    }*/
   /* public static final String GET_GROUP_LIST = "http://54.64.223.92/users/register";
    public static void getGroupList(final Context context){
        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.GET, GET_GROUP_LIST, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString(KEY_SUCCESS) != null) {
                                int success = Integer.parseInt(response.getString(KEY_SUCCESS));
                                if (success == 1) {
                                    Toast.makeText(context, response.getString("message"), Toast.LENGTH_LONG).show();
                                    *//*Toast.makeText(context, response.getString("tag"), Toast.LENGTH_LONG).show();
                                    Toast.makeText(context, response.getString("username"), Toast.LENGTH_LONG).show();
                                    Toast.makeText(context, response.getString("email"), Toast.LENGTH_LONG).show();
                                    Toast.makeText(context, response.getString("password"), Toast.LENGTH_LONG).show();*//*
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
                //params.put("password", Common.md5("qlalfqjsgh"));
                return params;
            }

        };
        requestQueue.add(rq);
    }
}*/
}