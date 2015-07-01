package com.daemin.timetable.common;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daemin.community.github.GithubActivity;
import com.daemin.data.GroupListData;
import com.daemin.main.SubMainActivity;
import com.daemin.timetable.R;
import com.navercorp.volleyextensions.request.Jackson2Request;

import java.util.ArrayList;


public class Common {

	/* BroadCastReceiver Filter */
	//public static final String ACTION_EVENT 			= "com.daemin.widget.widget.ACTION_EVENT";
	public static final String ACTION_CALL_ACTIVITY 	= "com.daemin.widget.widget.ACTION_CALL_ACTIVITY";
	public static final String ACTION_UPDATE = "com.daemin.widget.widget.ACTION_UPDATE";
	//public static final String ACTION_DIALOG 			= "com.daemin.widget.widget.ACTION_DIALOG";


	public static final String MAIN_COLOR = SubMainActivity.getInstance().getResources().getString(R.color.maincolor);

	public static boolean checkTableStateIsNothing = true;


	//network 통신
	// File url to download

	public static final String UNIVDB_URL = "http://browsable.cafe24.com/timetable/schedule/";
	public static final String GET_GROUPLIST_URL = "http://heeguchi.cafe24.com/app/getGroupList";
	public static RequestQueue requestQueue = MyVolley.getRequestQueue();
	public static ArrayList<String> groupListData = new ArrayList<>();
    public static ArrayList<String> getGroupList(){
		Jackson2Request<GroupListData> jackson2Request = new Jackson2Request<GroupListData>(
				Request.Method.POST,GET_GROUPLIST_URL, GroupListData.class,
				new Response.Listener<GroupListData>() {
					@Override
					public void onResponse(GroupListData response) {
						for(GroupListData.Data d : response.getData()){
							groupListData.add(d.getName());
						}
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
