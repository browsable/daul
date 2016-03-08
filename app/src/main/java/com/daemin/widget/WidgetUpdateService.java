package com.daemin.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.daemin.common.Common;
import com.daemin.dialog.DialSchedule;
import com.daemin.enumclass.Dates;
import com.daemin.repository.MyTimeRepo;
import com.daemin.repository.WidgetIDRepo;
import com.daemin.timetable.R;

import java.util.HashMap;

import timedao.MyTime;
import widget.WidgetID;

/**
 * Created by hernia on 2015-11-03.
 */
public class WidgetUpdateService extends Service {
    int deviceWidth, deviceHeight, viewMode;
    static int wIndex5_5,mIndex5_5, wIndex4_4,mIndex4_4;
    HashMap<Integer,Integer> enrollCntHash;
    SharedPreferences pref;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("widget", "service start");
        pref = getSharedPreferences("USERINFO", MODE_PRIVATE);
        deviceWidth = pref.getInt("deviceWidth", 0);
        deviceHeight= pref.getInt("deviceHeight", 0);
        enrollCntHash = new HashMap<>();
        wIndex5_5=0;
        mIndex5_5=0;
        wIndex4_4=0;
        mIndex4_4=0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("widget", "service died");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("widget", "service update");
        viewMode= pref.getInt("viewMode", 0);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        RemoteViews views5_5 = new RemoteViews(getPackageName(), R.layout.widget5_5);
        RemoteViews views4_4 = new RemoteViews(getPackageName(), R.layout.widget4_4);
        if(intent!=null){
            switch (intent.getStringExtra("action")) {
                case "update5_5":
                    if (viewMode == 0) widget5_5Week(views5_5, manager);
                    else widget5_5Month(views5_5, manager);
                    break;
                case "week5_5":
                    widget5_5Week(views5_5, manager);
                    break;
                case "month5_5":
                    widget5_5Month(views5_5, manager);
                    break;
                case "back5_5":
                    widget5_5Back(views5_5, manager);
                    break;
                case "forward5_5":
                    widget5_5Forward(views5_5, manager);
                    break;
                case "update4_4":
                    if (viewMode == 0) widget4_4Week(views4_4, manager);
                    else widget4_4Month(views4_4, manager);
                    break;
                case "week4_4":
                    widget4_4Week(views4_4, manager);
                    break;
                case "month4_4":
                    widget4_4Month(views4_4, manager);
                    break;
                case "back4_4":
                    widget4_4Back(views4_4, manager);
                    break;
                case "forward4_4":
                    widget4_4Forward(views4_4, manager);
                    break;
            }
        }else{
            if(pref.getBoolean("widget5_5", false)) {
                if (viewMode == 0) widget5_5Week(views5_5, manager);
                else widget5_5Month(views5_5, manager);
            }
            if(pref.getBoolean("widget4_4", false)) {
                if (viewMode == 0) widget4_4Week(views4_4, manager);
                else widget4_4Month(views4_4, manager);
            }
        }
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void fetchMonthData(RemoteViews views){
        enrollCntHash.clear();
        for(WidgetID wID : WidgetIDRepo.getAllWidgetID(this)){
            views.removeAllViews(wID.getTvID());
            WidgetIDRepo.deleteWithId(this,wID.getId());
        }
        int year = Dates.NOW.year;
        int month = Dates.NOW.month;
        long month_startMillies = Dates.NOW.getDateMillis(year, month, 1, 8, 0);
        long month_endMillies = Dates.NOW.getDateMillis(year, month, Dates.NOW.dayNumOfMonth, 23, 0);
        for (MyTime mt : MyTimeRepo.getMonthTimes(this, month_startMillies, month_endMillies)){
            int dayCnt = mt.getDayofmonth()+Dates.NOW.dayOfWeek;
            int lID = getResources().getIdentifier("l" + dayCnt, "id", "com.daemin.timetable");
            if(enrollCntHash.containsKey(lID)){
                int cnt = enrollCntHash.get(lID);
                if(cnt<4){
                    enrollCntHash.put(lID, ++cnt);
                    RemoteViews tv = new RemoteViews(getPackageName(), R.layout.widget_item);
                    tv.setTextViewText(R.id.widget_item, mt.getName());
                    views.addView(lID, tv);
                    WidgetIDRepo.insertOrUpdate(this, new WidgetID(lID));
                }
            }else{
                enrollCntHash.put(lID, 0);
                RemoteViews tv = new RemoteViews(getPackageName(), R.layout.widget_item);
                tv.setTextViewText(R.id.widget_item, mt.getName());
                views.addView(lID, tv);
                WidgetIDRepo.insertOrUpdate(this, new WidgetID(lID));
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void widget5_5Setting(RemoteViews views, AppWidgetManager manager, Bitmap bitmap){
        Intent main5 = new Intent(Common.ACTION_HOME5_5);
        PendingIntent mainP5 = PendingIntent.getBroadcast(this, 0, main5, 0);
        views.setOnClickPendingIntent(R.id.btHome, mainP5);
        Intent home = new Intent(Common.ACTION_HOME5_5);
        PendingIntent dialP = PendingIntent.getBroadcast(this, 0, home, 0);
        views.setOnClickPendingIntent(R.id.ivWeek, dialP);
        Intent month5 = new Intent(Common.ACTION_MONTH5_5);
        PendingIntent monthP5 = PendingIntent.getBroadcast(this, 0, month5, 0);
        views.setOnClickPendingIntent(R.id.btMonth, monthP5);
        Intent week5 = new Intent(Common.ACTION_WEEK5_5);
        PendingIntent weekP5 = PendingIntent.getBroadcast(this, 0, week5, 0);
        views.setOnClickPendingIntent(R.id.btWeek, weekP5);
        Intent back5 = new Intent(Common.ACTION_BACK5_5);
        PendingIntent backP5 = PendingIntent.getBroadcast(this, 0, back5, 0);
        views.setOnClickPendingIntent(R.id.btBack, backP5);
        Intent forward5 = new Intent(Common.ACTION_FORWARD5_5);
        PendingIntent forwardP5 = PendingIntent.getBroadcast(this, 0, forward5, 0);
        views.setOnClickPendingIntent(R.id.btForward, forwardP5);
        if(viewMode!=0){
            for (int i = 0; i < 42; i++) {
                int llID = getResources().getIdentifier("ll" + i, "id", "com.daemin.timetable");
                int tvID = getResources().getIdentifier("tv" + i, "id", "com.daemin.timetable");
                if (i >= Dates.NOW.dayOfWeek + 1 && i < Dates.NOW.dayOfWeek + Dates.NOW.dayNumOfMonth + 1) {
                    Intent in = new Intent(this, DialSchedule.class);
                    in.putExtra("widgetFlag", true);
                    in.putExtra("widget5_5", true);
                    in.putExtra("dayCnt", i);
                    in.putExtra("day", Dates.NOW.mData[i]);
                    PendingIntent dP = PendingIntent.getActivity(this, i+100, in,PendingIntent.FLAG_UPDATE_CURRENT);
                    views.setOnClickPendingIntent(llID, dP);
                    if(Dates.NOW.isToday)  {
                        if(i==Dates.NOW.dayOfMonth+Dates.NOW.dayOfWeek) {
                            views.setInt(llID, "setBackgroundResource", R.color.transmaincolor);
                        }
                    }else{
                        views.setInt(llID, "setBackgroundResource", R.drawable.bt_click);
                    }
                    int j = i % 7;
                    switch (j) {
                        case 0:
                            views.setTextColor(tvID, getResources().getColor(R.color.red));
                            break;
                        case 6:
                            views.setTextColor(tvID, getResources().getColor(R.color.blue));
                            break;
                        default:
                            views.setTextColor(tvID, getResources().getColor(android.R.color.black));
                            break;
                    }
                    views.setTextViewText(tvID, Dates.NOW.mData[i]);
                } else { //달력에서 월에 해당하는 날짜 이외의 날짜들은 회색으로 표시
                    Intent in = new Intent(Common.ACTION_DUMMY5_5);
                    PendingIntent dP = PendingIntent.getBroadcast(this, 0, in, 0);
                    views.setOnClickPendingIntent(llID, dP);
                    views.setTextViewText(tvID, Dates.NOW.mData[i]);
                    views.setTextColor(tvID, getResources().getColor(R.color.middlegray));
                }

            }
            fetchMonthData(views);
        }
        for (int appWidgetId : manager.getAppWidgetIds(new ComponentName(this, Widget5_5.class))) {
            manager.updateAppWidget(appWidgetId, views);
        }
        if(bitmap!=null)
            bitmap.recycle();
    }
    public void widget5_5Week(RemoteViews views, AppWidgetManager manager) {
        pref.edit().putInt("viewMode", 0).apply();
        viewMode=0;
        wIndex5_5 = 0;
        mIndex5_5 = 0;
        views.setViewVisibility(R.id.tvYear, View.VISIBLE);
        views.setViewVisibility(R.id.btWeek, View.GONE);
        views.setViewVisibility(R.id.btMonth, View.VISIBLE); //Visible
        views.setViewVisibility(R.id.ivWeek, View.VISIBLE);
        views.setViewVisibility(R.id.llMonth, View.GONE);
        WeekCaptureView iv = new WeekCaptureView(this);
        Dates.NOW.setWeekData(wIndex5_5);
        views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
        views.setTextViewText(R.id.tvDate, setMonthWeek());
        Common.fetchWeekData();
        iv.layout(0, 0, deviceWidth, deviceHeight*7/10);
        iv.setDrawingCacheEnabled(true);
        Bitmap bitmap = iv.getDrawingCache();
        views.setImageViewBitmap(R.id.ivWeek, bitmap);
        widget5_5Setting(views, manager,bitmap);

    }
    public void widget5_5Month(RemoteViews views, AppWidgetManager manager) {
        pref.edit().putInt("viewMode", 1).apply();
        viewMode = 1;
        wIndex5_5 = 0;
        views.setViewVisibility(R.id.tvYear, View.GONE);
        views.setViewVisibility(R.id.btWeek, View.VISIBLE); //Visible
        views.setViewVisibility(R.id.ivWeek, View.GONE);
        views.setViewVisibility(R.id.llMonth, View.VISIBLE);
        Dates.NOW.setMonthData(mIndex5_5);
        views.setViewVisibility(R.id.btMonth, View.GONE);
        views.setTextViewText(R.id.tvDate, setYearMonth());
        widget5_5Setting(views, manager, null);
    }
    public void widget5_5Back(RemoteViews views, AppWidgetManager manager){
        if (viewMode == 0) {
            views.setViewVisibility(R.id.tvYear, View.VISIBLE);
            WeekCaptureView iv = new WeekCaptureView(this);
            views.setViewVisibility(R.id.btWeek, View.GONE);
            views.setViewVisibility(R.id.btMonth, View.VISIBLE); //Visible
            Dates.NOW.setWeekData(--wIndex5_5);
            views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
            views.setTextViewText(R.id.tvDate, setMonthWeek());
            Common.fetchWeekData();
            iv.layout(0, 0, deviceWidth, deviceHeight*7/10);
            iv.setDrawingCacheEnabled(true);
            Bitmap bitmap = iv.getDrawingCache();
            views.setImageViewBitmap(R.id.ivWeek, bitmap);
            widget5_5Setting(views, manager,bitmap);

        } else {
            views.setViewVisibility(R.id.tvYear, View.GONE);
            views.setViewVisibility(R.id.btWeek, View.VISIBLE); //Visible
            views.setViewVisibility(R.id.btMonth, View.GONE);
            Dates.NOW.setMonthData(--mIndex5_5);
            views.setTextViewText(R.id.tvDate, setYearMonth());
            widget5_5Setting(views, manager,null);
        }

    }
    public void widget5_5Forward(RemoteViews views, AppWidgetManager manager){
        if(viewMode==0) {
            views.setViewVisibility(R.id.tvYear, View.VISIBLE);
            WeekCaptureView iv = new WeekCaptureView(this);
            views.setViewVisibility(R.id.btWeek, View.GONE);
            views.setViewVisibility(R.id.btMonth, View.VISIBLE); //Visible
            Dates.NOW.setWeekData(++wIndex5_5);
            views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
            views.setTextViewText(R.id.tvDate, setMonthWeek());
            Common.fetchWeekData();
            iv.layout(0, 0, deviceWidth, deviceHeight*7/10);
            iv.setDrawingCacheEnabled(true);
            Bitmap bitmap=iv.getDrawingCache();
            views.setImageViewBitmap(R.id.ivWeek, bitmap);
            widget5_5Setting(views, manager, bitmap);
        }
        else {
            views.setViewVisibility(R.id.tvYear, View.GONE);
            views.setViewVisibility(R.id.btWeek, View.VISIBLE); //Visible
            views.setViewVisibility(R.id.btMonth, View.GONE);
            Dates.NOW.setMonthData(++mIndex5_5);
            views.setTextViewText(R.id.tvDate, setYearMonth());
            widget5_5Setting(views, manager, null);
        }

    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void widget4_4Setting(RemoteViews views, AppWidgetManager manager, Bitmap bitmap){
        Intent main4 = new Intent(Common.ACTION_HOME4_4);
        PendingIntent mainP4 = PendingIntent.getBroadcast(this, 0, main4, 0);
        views.setOnClickPendingIntent(R.id.btHome, mainP4);
        Intent home = new Intent(Common.ACTION_HOME4_4);
        PendingIntent dialP = PendingIntent.getBroadcast(this, 0, home, 0);
        views.setOnClickPendingIntent(R.id.ivWeek, dialP);
        Intent month4 = new Intent(Common.ACTION_MONTH4_4);
        PendingIntent monthP4 = PendingIntent.getBroadcast(this, 0, month4, 0);
        views.setOnClickPendingIntent(R.id.btMonth, monthP4);
        Intent week4 = new Intent(Common.ACTION_WEEK4_4);
        PendingIntent weekP4 = PendingIntent.getBroadcast(this, 0, week4, 0);
        views.setOnClickPendingIntent(R.id.btWeek, weekP4);
        Intent back4 = new Intent(Common.ACTION_BACK4_4);
        PendingIntent backP4 = PendingIntent.getBroadcast(this, 0, back4, 0);
        views.setOnClickPendingIntent(R.id.btBack, backP4);
        Intent forward4 = new Intent(Common.ACTION_FORWARD4_4);
        PendingIntent forwardP4 = PendingIntent.getBroadcast(this, 0, forward4, 0);
        views.setOnClickPendingIntent(R.id.btForward, forwardP4);
        if(viewMode!=0){
            for (int i = 0; i < 42; i++) {
                int llID = getResources().getIdentifier("ll" + i, "id", "com.daemin.timetable");
                int tvID = getResources().getIdentifier("tv" + i, "id", "com.daemin.timetable");
                if (i >= Dates.NOW.dayOfWeek + 1 && i < Dates.NOW.dayOfWeek + Dates.NOW.dayNumOfMonth + 1) {
                    Intent in = new Intent(this, DialSchedule.class);
                    in.putExtra("widgetFlag", true);
                    in.putExtra("widget4_4", true);
                    in.putExtra("dayCnt", i);
                    in.putExtra("day", Dates.NOW.mData[i]);
                    PendingIntent dP = PendingIntent.getActivity(this, i+200, in,PendingIntent.FLAG_UPDATE_CURRENT);
                    views.setOnClickPendingIntent(llID, dP);
                    if(Dates.NOW.isToday)  {
                        if(i==Dates.NOW.dayOfMonth+Dates.NOW.dayOfWeek) {
                            views.setInt(llID, "setBackgroundResource", R.color.transmaincolor);
                        }
                    }else{
                        views.setInt(llID, "setBackgroundResource", R.drawable.bt_click);
                    }
                    int j = i % 7;
                    switch (j) {
                        case 0:
                            views.setTextColor(tvID, getResources().getColor(R.color.red));
                            break;
                        case 6:
                            views.setTextColor(tvID, getResources().getColor(R.color.blue));
                            break;
                        default:
                            views.setTextColor(tvID, getResources().getColor(android.R.color.black));
                            break;
                    }
                    views.setTextViewText(tvID, Dates.NOW.mData[i]);
                } else { //달력에서 월에 해당하는 날짜 이외의 날짜들은 회색으로 표시
                    Intent in = new Intent(Common.ACTION_DUMMY4_4);
                    PendingIntent dP = PendingIntent.getBroadcast(this, 0, in, 0);
                    views.setOnClickPendingIntent(llID, dP);
                    views.setTextViewText(tvID, Dates.NOW.mData[i]);
                    views.setTextColor(tvID, getResources().getColor(R.color.middlegray));
                }

            }
            fetchMonthData(views);
        }
        for (int appWidgetId : manager.getAppWidgetIds(new ComponentName(this, Widget4_4.class))) {
            manager.updateAppWidget(appWidgetId, views);
        }
        bitmap.recycle();
    }
    public void widget4_4Week(RemoteViews views, AppWidgetManager manager) {
        pref.edit().putInt("viewMode", 0).apply();
        viewMode=0;
        wIndex4_4 = 0;
        mIndex4_4 = 0;
        views.setViewVisibility(R.id.tvYear, View.VISIBLE);
        views.setViewVisibility(R.id.btWeek, View.GONE);
        views.setViewVisibility(R.id.btMonth, View.VISIBLE); //Visible
        views.setViewVisibility(R.id.ivWeek, View.VISIBLE);
        views.setViewVisibility(R.id.llMonth, View.GONE);
        WeekCaptureView iv = new WeekCaptureView(this);
        Dates.NOW.setWeekData(wIndex4_4);
        views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
        views.setTextViewText(R.id.tvDate, setMonthWeek());
        Common.fetchWeekData();
        iv.layout(0, 0, deviceWidth, deviceHeight*7/10);
        iv.setDrawingCacheEnabled(true);
        Bitmap bitmap = iv.getDrawingCache();
        views.setImageViewBitmap(R.id.ivWeek, bitmap);
        widget4_4Setting(views, manager, bitmap);

    }
    public void widget4_4Month(RemoteViews views, AppWidgetManager manager) {
        pref.edit().putInt("viewMode", 1).apply();
        viewMode = 1;
        wIndex4_4 = 0;
        views.setViewVisibility(R.id.tvYear, View.GONE);
        views.setViewVisibility(R.id.btWeek, View.VISIBLE); //Visible
        views.setViewVisibility(R.id.ivWeek, View.GONE);
        views.setViewVisibility(R.id.llMonth, View.VISIBLE);
        Dates.NOW.setMonthData(mIndex4_4);
        views.setViewVisibility(R.id.btMonth, View.GONE);
        views.setTextViewText(R.id.tvDate, setYearMonth());
        widget4_4Setting(views, manager, null);
    }
    public void widget4_4Back(RemoteViews views, AppWidgetManager manager){
        if (viewMode == 0) {
            views.setViewVisibility(R.id.tvYear, View.VISIBLE);
            WeekCaptureView iv = new WeekCaptureView(this);
            views.setViewVisibility(R.id.btWeek, View.GONE);
            views.setViewVisibility(R.id.btMonth, View.VISIBLE); //Visible
            Dates.NOW.setWeekData(--wIndex4_4);
            views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
            views.setTextViewText(R.id.tvDate, setMonthWeek());
            Common.fetchWeekData();
            iv.layout(0, 0, deviceWidth, deviceHeight*7/10);
            iv.setDrawingCacheEnabled(true);
            Bitmap bitmap = iv.getDrawingCache();
            views.setImageViewBitmap(R.id.ivWeek, bitmap);
            widget4_4Setting(views, manager, bitmap);

        } else {
            views.setViewVisibility(R.id.tvYear, View.GONE);
            views.setViewVisibility(R.id.btWeek, View.VISIBLE); //Visible
            views.setViewVisibility(R.id.btMonth, View.GONE);
            Dates.NOW.setMonthData(--mIndex4_4);
            views.setTextViewText(R.id.tvDate, setYearMonth());
            widget4_4Setting(views, manager, null);
        }

    }
    public void widget4_4Forward(RemoteViews views, AppWidgetManager manager){
        if(viewMode==0) {
            views.setViewVisibility(R.id.tvYear, View.VISIBLE);
            WeekCaptureView iv = new WeekCaptureView(this);
            views.setViewVisibility(R.id.btWeek, View.GONE);
            views.setViewVisibility(R.id.btMonth, View.VISIBLE); //Visible
            Dates.NOW.setWeekData(++wIndex4_4);
            views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
            views.setTextViewText(R.id.tvDate, setMonthWeek());
            Common.fetchWeekData();
            iv.layout(0, 0, deviceWidth, deviceHeight*7/10);
            iv.setDrawingCacheEnabled(true);
            Bitmap bitmap=iv.getDrawingCache();
            views.setImageViewBitmap(R.id.ivWeek, bitmap);
            widget4_4Setting(views, manager, bitmap);
        } else {
            views.setViewVisibility(R.id.tvYear, View.GONE);
            views.setViewVisibility(R.id.btWeek, View.VISIBLE); //Visible
            views.setViewVisibility(R.id.btMonth, View.GONE);
            Dates.NOW.setMonthData(++mIndex4_4);
            views.setTextViewText(R.id.tvDate, setYearMonth());
            widget4_4Setting(views, manager, null);
        }

    }
    public String setMonthWeek(){
        return " " + Dates.NOW.month + this.getString(R.string.month) + " "
                + Dates.NOW.weekOfMonth + this.getString((R.string.weekofmonth));
    }
    public String setYearMonth(){
        return " " + Dates.NOW.year + this.getString(R.string.year) + " "
                + Dates.NOW.month + this.getString((R.string.month));
    }
}
