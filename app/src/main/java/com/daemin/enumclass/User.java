package com.daemin.enumclass;

import android.content.Context;
import android.content.SharedPreferences;

import com.daemin.common.AppController;

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
    }
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    public double latitude, longitude;
    public String userPK;
    public String[] wData, mData;
    public int groupPK;
    public int dayOfWeekOfLastMonth;
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
    public double getLatitude() {
        return latitude;
    }
    public String[] getwData() {
        return wData;
    }
    public int getDayOfWeekOfLastMonth() {
        return dayOfWeekOfLastMonth;
    }

    public void setDayOfWeekOfLastMonth(int dayOfWeekOfLastMonth) {
        this.dayOfWeekOfLastMonth = dayOfWeekOfLastMonth;
    }
    public void setwData(String[] wData) {
        this.wData = wData;
    }

    public String[] getmData() {
        return mData;
    }

    public void setmData(String[] mData) {
        this.mData = mData;
    }
    //Pref Getter
    public boolean getFirstFlag(){
        return pref.getBoolean("firstFlag",true);
    }
    public int getDeviceWidth(){
        return pref.getInt("deviceWidth", 0);
    }
    public int getDeviceHeight(){
        return pref.getInt("deviceHeight", 0);
    }
    public String getUserPK(){
        return pref.getString("userPK", "0");
    }
    public String getKorGroupName(){
        return pref.getString("korGroupName", "");
    }
    public String getEngGroupName(){
        return pref.getString("engGroupName", "");
    }
    public int getGroupPK(){
        return pref.getInt("groupPK", 0);
    }
    public boolean getSubjectDownFlag(){
        return pref.getBoolean("subjectDown", false);
    }
    public int getViewMode(){
        return pref.getInt("viewMode", 0);
    }
    public boolean getWidget5_5(){
        return pref.getBoolean("widget5_5", false);
    }
    public boolean getWidget4_4(){
        return pref.getBoolean("widget4_4",false);
    }
}
