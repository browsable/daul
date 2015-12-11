package com.daemin.enumclass;

import android.content.Context;
import android.content.SharedPreferences;

import com.daemin.common.AppController;

/**
 * Created by hernia on 2015-06-27.
 */
public enum CurrentTIme {
    INFO(AppController.getInstance());
    CurrentTIme(Context context){
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
    public int groupPK,year,month, monthOfSun,monthOfMon,monthOfTue,monthOfWed,monthOfThr,monthOfFri,monthOfSat,
            dayOfSun,dayOfMon,dayOfTue,dayOfWed,dayOfThr,dayOfFri,dayOfSat,dayOfWeekOfLastMonth;
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
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public void setWeekDays(int sun, int mon, int tue, int wed, int thr, int fri, int sat){
        dayOfSun=sun;
        dayOfMon=mon;
        dayOfTue=tue;
        dayOfWed=wed;
        dayOfThr=thr;
        dayOfFri=fri;
        dayOfSat=sat;
    }
    public void setWeekMonth(int sun, int mon, int tue, int wed, int thr, int fri, int sat){
        monthOfSun=sun;
        monthOfMon=mon;
        monthOfTue=tue;
        monthOfWed=wed;
        monthOfThr=thr;
        monthOfFri=fri;
        monthOfSat=sat;
    }
    public int getXthToDay(int xth) {
        switch (xth) {
            case 1:
                return dayOfSun;
            case 3:
                return dayOfMon;
            case 5:
                return dayOfTue;
            case 7:
                return dayOfWed;
            case 9:
                return dayOfThr;
            case 11:
                return dayOfFri;
            case 13:
                return dayOfSat;
            default:
                return 0;
        }
    }
    public int getDayOfSun() {
        return dayOfSun;
    }
    public int getDayOfMon() {
        return dayOfMon;
    }
    public int getDayOfTue() {
        return dayOfTue;
    }
    public int getDayOfWed() {
        return dayOfWed;
    }
    public int getDayOfThr() {
        return dayOfThr;
    }
    public int getDayOfFri() {
        return dayOfFri;
    }
    public int getDayOfSat() {
        return dayOfSat;
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
        return pref.getBoolean("firstFlag", true);
    }
    public int getDeviceWidth(){
        return pref.getInt("deviceWidth", 0);
    }
    public int getDeviceHeight(){
        return pref.getInt("deviceHeight", 0);
    }
    public String getUserPK(){
        String userPK = pref.getString("userPK", "0");
        return userPK.substring(0, userPK.length() / 2);
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
    public String getCreditSum(){
        return pref.getString("creditSum", "0");
    }
    public boolean getWidget5_5(){
        return pref.getBoolean("widget5_5", false);
    }
    public boolean getWidget4_4(){
        return pref.getBoolean("widget4_4",false);
    }
}
