package com.daemin.enumclass;

import android.content.Context;
import android.content.SharedPreferences;

import com.daemin.common.AppController;
import com.daemin.data.GroupListData;
import com.daemin.timetable.R;

import java.util.ArrayList;
import java.util.List;

import timedao.MyTime;

/**
 * Created by hernia on 2015-06-27.
 */
public enum User {
    INFO(AppController.getInstance());
    User(Context context){
        pref = context.getSharedPreferences("USERINFO", context.MODE_PRIVATE);
        editor = pref.edit();
        latitude=0;
        longitude=0;
        userPK = getUserPK();
        groupPK = getGroupPK();
        weekData=new ArrayList<>();
        monthData=new ArrayList<>();
        groupListData=new ArrayList<>();
        overlapFlag = false;
        textSize = context.getResources().getDimensionPixelSize(R.dimen.textsize_xs);
        dateSize = context.getResources().getDimensionPixelSize(R.dimen.textsize_s);
        intervalSize = context.getResources().getDimensionPixelSize(R.dimen.margin_xxs);
        editor.putInt("textSize", textSize).commit();
        editor.putInt("dateSize", dateSize).commit();
    }
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public double latitude, longitude;
    public String userPK;
    public String appVer;//로컬기기버전;
    public String appServerVer;//서버상의 버전;
    public String dbServerVer;//서버상의 디비 버전;
    public List<MyTime> weekData;
    public List<MyTime> monthData;
    public List<GroupListData.Data> groupListData;
    public int groupPK, textSize, dateSize,intervalSize;
    public boolean overlapFlag;
    public SharedPreferences.Editor getEditor() {
        return editor;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public double getLatitude() {return latitude;}
    //Pref Getter
    public boolean getFirstFlag(){
        return pref.getBoolean("firstFlag", true);
    }
    public String getUserPK(){
        String userPK = pref.getString("userPK", "0");
        return userPK.substring(0, userPK.length() / 2);
    }
    public String getGroupName(){
        return pref.getString("groupName", "");
    }
    public String getGroupDBVer(){
        return pref.getString("groupDBVer", "v1.0");
    }
    public int getGroupPK(){
        return pref.getInt("groupPK", 0);
    }
    public int getViewMode(){
        return pref.getInt("viewMode", 0);
    }
    public boolean getWidget5_5_1(){
        return pref.getBoolean("widget5_5_1", false);
    }
    public boolean getWidget5_5(){
        return pref.getBoolean("widget5_5", false);
    }
    public boolean getWidget4_4(){
        return pref.getBoolean("widget4_4",false);
    }
}
