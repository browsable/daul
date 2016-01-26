package com.daemin.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.daemin.common.AppController;
import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.enumclass.Dates;
import com.daemin.enumclass.DayOfMonthPos;
import com.daemin.enumclass.TimePos;
import com.daemin.event.ChangeFragEvent;
import com.daemin.event.RemoveEnrollEvent;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import timedao.MyTime;

/**
 * Created by hernia on 2015-11-03.
 */
public class WidgetUpdateService extends Service {
    int deviceWidth, deviceHeight, viewMode;
    ArrayList<Integer> tvIdList;
    static int wIndex5_5_1,mIndex5_5_1,wIndex5_5,mIndex5_5,wIndex4_4,mIndex4_4;
    SharedPreferences pref;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("widget", "service start");
        tvIdList= new ArrayList<>();
        pref = getSharedPreferences("USERINFO", MODE_PRIVATE);
        deviceWidth = pref.getInt("deviceWidth", 0);
        deviceHeight= pref.getInt("deviceHeight", 0);
        wIndex5_5_1=0;
        mIndex5_5_1=0;
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
        RemoteViews views5_5_1 = new RemoteViews(getPackageName(), R.layout.widget5_5_1);
        RemoteViews views5_5 = new RemoteViews(getPackageName(), R.layout.widget5_5);
        RemoteViews views4_4 = new RemoteViews(getPackageName(), R.layout.widget4_4);
        if(intent!=null){
            switch (intent.getStringExtra("action")) {
                case "update5_5_1":
                    if (viewMode == 0) widget5_5_1Week(views5_5_1, manager);
                    else widget5_5_1Month(views5_5_1, manager);
                    break;
                case "week5_5_1":
                    widget5_5_1Week(views5_5_1,manager);
                    break;
                case "month5_5_1":
                    widget5_5_1Month(views5_5_1,manager);
                    break;
                case "back5_5_1":
                    widget5_5_1Back(views5_5_1, manager);
                    break;
                case "forward5_5_1":
                    widget5_5_1Forward(views5_5_1, manager);
                    break;
                case "update5_5":
                    if (viewMode == 0) widget5_5Week(views5_5,manager);
                    else widget5_5Month(views5_5,manager);
                    break;
                case "week5_5":
                    widget5_5Week(views5_5,manager);
                    break;
                case "month5_5":
                    widget5_5Month(views5_5,manager);
                    break;
                case "back5_5":
                    widget5_5Back(views5_5, manager);
                    break;
                case "forward5_5":
                    widget5_5Forward(views5_5, manager);
                    break;
                case "update4_4":
                    if (viewMode == 0) widget4_4Week(views4_4,manager);
                    else widget4_4Month(views4_4,manager);
                    break;
                case "week4_4":
                    widget4_4Week(views4_4,manager);
                    break;
                case "month4_4":
                    widget4_4Month(views4_4,manager);
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
                if (viewMode == 0) widget5_5Week(views5_5,manager);
                else widget5_5Month(views5_5,manager);
            }
            if(pref.getBoolean("widget4_4", false)){
                if (viewMode == 0) widget4_4Week(views4_4,manager);
                else widget4_4Month(views4_4,manager);
            }
        }
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void fetchMonthData(RemoteViews views){
        for(Integer id : tvIdList){
            views.removeAllViews(id);
        }
        int year = Dates.NOW.year;
        int month = Dates.NOW.month;
        long month_startMillies = Dates.NOW.getDateMillis(year, month, 1, 8, 0);
        long month_endMillies = Dates.NOW.getDateMillis(year, month, Dates.NOW.dayNumOfMonth, 23, 0);
        for (MyTime mt : MyTimeRepo.getMonthTimes(AppController.getInstance(), month_startMillies, month_endMillies)){
            int dayCnt = mt.getDayofmonth()+Dates.NOW.dayOfWeek;
            int lID = getResources().getIdentifier("l" + dayCnt, "id", "com.daemin.timetable");
            RemoteViews tv = new RemoteViews(getPackageName(), R.layout.widget_item);
            tv.setTextViewText(R.id.widget_item, mt.getName());
            views.addView(lID, tv);
            tvIdList.add(lID);
        }
    }
    public void widget5_5_1Setting(RemoteViews views, AppWidgetManager manager){
        for(int i=0; i<42; i++){
            int llID = getResources().getIdentifier("ll" + i, "id", "com.daemin.timetable");
            int tvID = getResources().getIdentifier("tv" + i, "id", "com.daemin.timetable");
            if(i>=Dates.NOW.dayOfWeek+1&&i<Dates.NOW.dayOfWeek+Dates.NOW.dayNumOfMonth+1){
                Intent dial = new Intent(Common.ACTION_DIAL5_5_1);
                PendingIntent dialP = PendingIntent.getBroadcast(this, 0, dial, 0);
                views.setOnClickPendingIntent(llID, dialP);
                int j = i%7;
                switch(j){
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
            }
            else{ //달력에서 월에 해당하는 날짜 이외의 날짜들은 회색으로 표시
                Intent dial = new Intent(Common.ACTION_DUMMY5_5_1);
                PendingIntent dialP = PendingIntent.getBroadcast(this, 0, dial, 0);
                views.setOnClickPendingIntent(llID, dialP);
                views.setTextViewText(tvID, Dates.NOW.mData[i]);
            }

        }
        fetchMonthData(views);
        Intent main5 = new Intent(Common.ACTION_HOME5_5_1);
        PendingIntent mainP5 = PendingIntent.getBroadcast(this, 0, main5, 0);
        views.setOnClickPendingIntent(R.id.btHome, mainP5);
        Intent dial = new Intent(Common.ACTION_DIAL5_5_1);
        PendingIntent dialP = PendingIntent.getBroadcast(this, 0, dial, 0);
        views.setOnClickPendingIntent(R.id.timetableimage, dialP);
        Intent week5 = new Intent(Common.ACTION_WEEK5_5_1);
        PendingIntent weekP5 = PendingIntent.getBroadcast(this, 0, week5, 0);
        views.setOnClickPendingIntent(R.id.btWeek, weekP5);
        Intent month5 = new Intent(Common.ACTION_MONTH5_5_1);
        PendingIntent monthP5 = PendingIntent.getBroadcast(this, 0, month5, 0);
        views.setOnClickPendingIntent(R.id.btMonth, monthP5);
        Intent back5 = new Intent(Common.ACTION_BACK5_5_1);
        PendingIntent backP5 = PendingIntent.getBroadcast(this, 0, back5, 0);
        views.setOnClickPendingIntent(R.id.btBack, backP5);
        Intent forward5 = new Intent(Common.ACTION_FORWARD5_5_1);
        PendingIntent forwardP5 = PendingIntent.getBroadcast(this, 0, forward5, 0);
        views.setOnClickPendingIntent(R.id.btForward, forwardP5);
        for (int appWidgetId : manager.getAppWidgetIds(new ComponentName(this, Widget5_5_1.class))) {
            manager.updateAppWidget(appWidgetId, views);
        }
    }
    public void widget5_5_1Week(RemoteViews views, AppWidgetManager manager) {
        pref.edit().putInt("viewMode", 0).apply();
        viewMode=0;
        wIndex5_5_1 = 0;
        mIndex5_5_1 = 0;
        views.setViewVisibility(R.id.tvYear, View.VISIBLE);
        views.setViewVisibility(R.id.btWeek, View.GONE);
        views.setViewVisibility(R.id.btMonth, View.VISIBLE); //Visible
        //Dates.NOW.setWeekData();
        views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
        views.setTextViewText(R.id.tvDate, setMonthWeek());
        //
        //widget5_5_1Setting(views, manager);

    }
    public void widget5_5_1Month(RemoteViews views, AppWidgetManager manager) {
        pref.edit().putInt("viewMode", 1).apply();
        viewMode = 1;
        wIndex5_5_1 = 0;
        mIndex5_5_1 = 0;
        views.setViewVisibility(R.id.tvYear, View.GONE);
        views.setViewVisibility(R.id.btWeek, View.VISIBLE); //Visible
        Dates.NOW.setMonthData();
        views.setViewVisibility(R.id.btMonth, View.GONE);
        views.setTextViewText(R.id.tvDate, setYearMonth());
        widget5_5_1Setting(views, manager);
    }
    public void widget5_5_1Back(RemoteViews views, AppWidgetManager manager){
        Bitmap bitmap;
        if (viewMode == 0) {
            /*views.setViewVisibility(R.id.tvYear, View.VISIBLE);
            WeekCaptureView iv = new WeekCaptureView(this);
            views.setViewVisibility(R.id.btWeek, View.GONE);
            views.setViewVisibility(R.id.btMonth, View.VISIBLE); //Visible
            --wIndex5_5_1;
            if (wIndex5_5_1 < 0) {
                Dates.NOW.setBackWeekData(-wIndex5_5_1);
                views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, setMonthWeek());
            } else {
                Dates.NOW.setPreWeekData(wIndex5_5_1);
                views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, setMonthWeek());

            }
            Common.fetchWeekData();
            iv.layout(0, 0, deviceWidth, deviceHeight);
            iv.setDrawingCacheEnabled(true);
            bitmap = iv.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);*/
        } else {
            views.setViewVisibility(R.id.tvYear, View.GONE);
            views.setViewVisibility(R.id.btWeek, View.VISIBLE); //Visible
            views.setViewVisibility(R.id.btMonth, View.GONE);
            --mIndex5_5_1;
            if (mIndex5_5_1 < 0) {
                Dates.NOW.setBackMonthData(-mIndex5_5_1);
                views.setTextViewText(R.id.tvDate, setYearMonth());
            } else {
                Dates.NOW.setPreMonthData(mIndex5_5_1);
                views.setTextViewText(R.id.tvDate, setYearMonth());
            }
        }
        widget5_5_1Setting(views, manager);
        //bitmap.recycle();
    }
    public void widget5_5_1Forward(RemoteViews views, AppWidgetManager manager){
        Bitmap bitmap;
        if(viewMode==0) {
            /*views.setViewVisibility(R.id.tvYear, View.VISIBLE);
            WeekCaptureView iv = new WeekCaptureView(this);
            views.setViewVisibility(R.id.btWeek, View.GONE);
            views.setViewVisibility(R.id.btMonth, View.VISIBLE); //Visible
            ++wIndex5_5_1;
            if (wIndex5_5_1 < 0) {
                Dates.NOW.setBackWeekData(-wIndex5_5_1);
                views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, setMonthWeek());
            } else {
                Dates.NOW.setPreWeekData(wIndex5_5_1);
                views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, setMonthWeek());

            }
            Common.fetchWeekData();
            iv.layout(0, 0, deviceWidth, deviceHeight);
            iv.setDrawingCacheEnabled(true);
            bitmap=iv.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);*/
        }
        else {
            views.setViewVisibility(R.id.tvYear, View.GONE);
            views.setViewVisibility(R.id.btWeek, View.VISIBLE); //Visible
            views.setViewVisibility(R.id.btMonth, View.GONE);
            ++mIndex5_5_1;
            if (mIndex5_5_1 < 0) {
                Dates.NOW.setBackMonthData(-mIndex5_5_1);
                views.setTextViewText(R.id.tvDate, setYearMonth());
            } else {
                Dates.NOW.setPreMonthData(mIndex5_5_1);
                views.setTextViewText(R.id.tvDate, setYearMonth());
            }
        }
        widget5_5_1Setting(views, manager);
    }
    public void widget5_5Setting(RemoteViews views, AppWidgetManager manager){
        Intent main5 = new Intent(Common.ACTION_HOME5_5);
        PendingIntent mainP5 = PendingIntent.getBroadcast(this, 0, main5, 0);
        views.setOnClickPendingIntent(R.id.btHome, mainP5);
        Intent dial = new Intent(Common.ACTION_DIAL5_5);
        PendingIntent dialP = PendingIntent.getBroadcast(this, 0, dial, 0);
        views.setOnClickPendingIntent(R.id.timetableimage, dialP);
        Intent week5 = new Intent(Common.ACTION_WEEK5_5);
        PendingIntent weekP5 = PendingIntent.getBroadcast(this, 0, week5, 0);
        views.setOnClickPendingIntent(R.id.btWeek, weekP5);
        Intent month5 = new Intent(Common.ACTION_MONTH5_5);
        PendingIntent monthP5 = PendingIntent.getBroadcast(this, 0, month5, 0);
        views.setOnClickPendingIntent(R.id.btMonth, monthP5);
        Intent back5 = new Intent(Common.ACTION_BACK5_5);
        PendingIntent backP5 = PendingIntent.getBroadcast(this, 0, back5, 0);
        views.setOnClickPendingIntent(R.id.btBack, backP5);
        Intent forward5 = new Intent(Common.ACTION_FORWARD5_5);
        PendingIntent forwardP5 = PendingIntent.getBroadcast(this, 0, forward5, 0);
        views.setOnClickPendingIntent(R.id.btForward, forwardP5);
        for (int appWidgetId : manager.getAppWidgetIds(new ComponentName(this, Widget5_5.class))) {
            manager.updateAppWidget(appWidgetId, views);
        }
    }
    public void widget4_4Setting(RemoteViews views, AppWidgetManager manager){
        Intent main4 = new Intent(Common.ACTION_HOME4_4);
        PendingIntent mainP4 = PendingIntent.getBroadcast(this, 0, main4, 0);
        views.setOnClickPendingIntent(R.id.btHome, mainP4);
        Intent dial = new Intent(Common.ACTION_DIAL4_4);
        PendingIntent dialP = PendingIntent.getBroadcast(this, 0, dial, 0);
        views.setOnClickPendingIntent(R.id.timetableimage, dialP);
        Intent week4 = new Intent(Common.ACTION_WEEK4_4);
        PendingIntent weekP4 = PendingIntent.getBroadcast(this, 0, week4, 0);
        views.setOnClickPendingIntent(R.id.btWeek, weekP4);
        Intent month4 = new Intent(Common.ACTION_MONTH4_4);
        PendingIntent monthP4 = PendingIntent.getBroadcast(this, 0, month4, 0);
        views.setOnClickPendingIntent(R.id.btMonth, monthP4);
        Intent back4 = new Intent(Common.ACTION_BACK4_4);
        PendingIntent backP4 = PendingIntent.getBroadcast(this, 0, back4, 0);
        views.setOnClickPendingIntent(R.id.btBack, backP4);
        Intent forward4 = new Intent(Common.ACTION_FORWARD4_4);
        PendingIntent forwardP4 = PendingIntent.getBroadcast(this, 0, forward4, 0);
        views.setOnClickPendingIntent(R.id.btForward, forwardP4);
        for (int appWidgetId : manager.getAppWidgetIds(new ComponentName(this, Widget4_4.class))) {
            manager.updateAppWidget(appWidgetId, views);
        }
    }

    public void widget5_5Week(RemoteViews views, AppWidgetManager manager) {
        pref.edit().putInt("viewMode", 0).apply();
        viewMode=0;
        wIndex5_5 = 0;
        mIndex5_5 = 0;
        views.setViewVisibility(R.id.tvYear, View.VISIBLE);
        WeekCaptureView iv5 = new WeekCaptureView(this);
        views.setViewVisibility(R.id.btWeek, View.GONE);
        views.setViewVisibility(R.id.btMonth, View.VISIBLE); //Visible
        Dates.NOW.setWeekData();
        views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
        views.setTextViewText(R.id.tvDate, setMonthWeek());
        iv5.layout(0, 0, deviceWidth, deviceHeight);
        iv5.setDrawingCacheEnabled(true);
        Bitmap week5 = iv5.getDrawingCache();
        views.setImageViewBitmap(R.id.timetableimage, week5);
        widget5_5Setting(views, manager);
        week5.recycle();
    }
    public void widget5_5Month(RemoteViews views, AppWidgetManager manager) {
        pref.edit().putInt("viewMode", 1).apply();
        viewMode=1;
        wIndex5_5 = 0;
        mIndex5_5 = 0;
        views.setViewVisibility(R.id.tvYear, View.GONE);
        MonthCaptureView im5 = new MonthCaptureView(this);
        views.setViewVisibility(R.id.btWeek, View.VISIBLE); //Visible
        views.setViewVisibility(R.id.btMonth, View.GONE);
        Dates.NOW.setMonthData();
        views.setTextViewText(R.id.tvDate, setYearMonth());
        im5.layout(0, 0, deviceWidth, deviceHeight);
        im5.setDrawingCacheEnabled(true);
        Bitmap month5 = im5.getDrawingCache();
        views.setImageViewBitmap(R.id.timetableimage, month5);
        widget5_5Setting(views, manager);
        month5.recycle();
    }
    public void widget4_4Week(RemoteViews views, AppWidgetManager manager) {
        pref.edit().putInt("viewMode", 0).apply();
        viewMode=0;
        wIndex4_4 = 0;
        mIndex4_4 = 0;
        views.setViewVisibility(R.id.tvYear, View.VISIBLE);
        WeekCaptureView iv4 = new WeekCaptureView(this);
        views.setViewVisibility(R.id.btWeek, View.GONE);
        views.setViewVisibility(R.id.btMonth, View.VISIBLE); //Visible
        Dates.NOW.setWeekData();

        views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
        views.setTextViewText(R.id.tvDate, setMonthWeek());
        iv4.layout(0, 0, deviceWidth, deviceHeight);
        iv4.setDrawingCacheEnabled(true);
        Bitmap week4 = iv4.getDrawingCache();
        views.setImageViewBitmap(R.id.timetableimage, week4);
        widget4_4Setting(views, manager);
        week4.recycle();
    }
    public void widget4_4Month(RemoteViews views, AppWidgetManager manager) {
        pref.edit().putInt("viewMode", 1).apply();
        viewMode=1;
        wIndex4_4 = 0;
        mIndex4_4 = 0;
        views.setViewVisibility(R.id.tvYear, View.GONE);
        MonthCaptureView im4 = new MonthCaptureView(this);
        views.setViewVisibility(R.id.btWeek, View.VISIBLE); //Visible
        views.setViewVisibility(R.id.btMonth, View.GONE);
        Dates.NOW.setMonthData();
        views.setTextViewText(R.id.tvDate, setYearMonth());
        im4.layout(0, 0, deviceWidth, deviceHeight);
        im4.setDrawingCacheEnabled(true);
        Bitmap month4_4 = im4.getDrawingCache();
        views.setImageViewBitmap(R.id.timetableimage, month4_4);
        widget4_4Setting(views, manager);
        month4_4.recycle();
    }

    public void widget5_5Back(RemoteViews views, AppWidgetManager manager){
        Bitmap bitmap;
        if (viewMode == 0) {
            views.setViewVisibility(R.id.tvYear, View.VISIBLE);
            WeekCaptureView iv = new WeekCaptureView(this);
            views.setViewVisibility(R.id.btWeek, View.GONE);
            views.setViewVisibility(R.id.btMonth, View.VISIBLE); //Visible
            --wIndex5_5;
            if (wIndex5_5 < 0) {
                Dates.NOW.setBackWeekData(-wIndex5_5);
                views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, setMonthWeek());
            } else {
                Dates.NOW.setPreWeekData(wIndex5_5);
                views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, setMonthWeek());

            }
            Common.fetchWeekData();
            iv.layout(0, 0, deviceWidth, deviceHeight);
            iv.setDrawingCacheEnabled(true);
            bitmap = iv.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);
        } else {
            views.setViewVisibility(R.id.tvYear, View.GONE);
            MonthCaptureView im = new MonthCaptureView(this);
            views.setViewVisibility(R.id.btWeek, View.VISIBLE); //Visible
            views.setViewVisibility(R.id.btMonth, View.GONE);
            --mIndex5_5;
            if (mIndex5_5 < 0) {
                Dates.NOW.setBackMonthData(-mIndex5_5);
                views.setTextViewText(R.id.tvDate, setYearMonth());
            } else {
                Dates.NOW.setPreMonthData(mIndex5_5);
                views.setTextViewText(R.id.tvDate, setYearMonth());
            }
            im.layout(0, 0, deviceWidth, deviceHeight);
            im.setDrawingCacheEnabled(true);
            bitmap = im.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);
        }
        widget5_5Setting(views, manager);
        bitmap.recycle();
    }
    public void widget5_5Forward(RemoteViews views, AppWidgetManager manager){
        Bitmap bitmap;
        if(viewMode==0) {
            views.setViewVisibility(R.id.tvYear, View.VISIBLE);
            WeekCaptureView iv = new WeekCaptureView(this);
            views.setViewVisibility(R.id.btWeek, View.GONE);
            views.setViewVisibility(R.id.btMonth, View.VISIBLE); //Visible
            ++wIndex5_5;
            if (wIndex5_5 < 0) {
                Dates.NOW.setBackWeekData(-wIndex5_5);
                views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, setMonthWeek());
            } else {
                Dates.NOW.setPreWeekData(wIndex5_5);
                views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, setMonthWeek());

            }
            Common.fetchWeekData();
            iv.layout(0, 0, deviceWidth, deviceHeight);
            iv.setDrawingCacheEnabled(true);
            bitmap=iv.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);
        }
        else {
            views.setViewVisibility(R.id.tvYear, View.GONE);
            MonthCaptureView im = new MonthCaptureView(this);
            views.setViewVisibility(R.id.btWeek, View.VISIBLE); //Visible
            views.setViewVisibility(R.id.btMonth, View.GONE);
            ++mIndex5_5;
            if (mIndex5_5 < 0) {
                Dates.NOW.setBackMonthData(-mIndex5_5);
                views.setTextViewText(R.id.tvDate, setYearMonth());
            } else {
                Dates.NOW.setPreMonthData(mIndex5_5);
                views.setTextViewText(R.id.tvDate, setYearMonth());
            }
            im.layout(0, 0, deviceWidth, deviceHeight);
            im.setDrawingCacheEnabled(true);
            bitmap=im.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);
        }
        widget5_5Setting(views, manager);
        bitmap.recycle();
    }
    public void widget4_4Back(RemoteViews views, AppWidgetManager manager){
        Bitmap bitmap;
        if (viewMode == 0) {
            views.setViewVisibility(R.id.tvYear, View.VISIBLE);
            WeekCaptureView iv = new WeekCaptureView(this);
            views.setViewVisibility(R.id.btWeek, View.GONE);
            views.setViewVisibility(R.id.btMonth, View.VISIBLE); //Visible
            --wIndex4_4;
            if (wIndex4_4 < 0) {
                Dates.NOW.setBackWeekData(-wIndex4_4);
                views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, setMonthWeek());
            } else {
                Dates.NOW.setPreWeekData(wIndex4_4);
                views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, setMonthWeek());
            }
            Common.fetchWeekData();
            iv.layout(0, 0, deviceWidth, deviceHeight);
            iv.setDrawingCacheEnabled(true);
            bitmap = iv.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);
        } else {
            views.setViewVisibility(R.id.tvYear, View.GONE);
            MonthCaptureView im = new MonthCaptureView(this);
            views.setViewVisibility(R.id.btWeek, View.VISIBLE); //Visible
            views.setViewVisibility(R.id.btMonth, View.GONE);
            --mIndex4_4;
            if (mIndex4_4 < 0) {
                Dates.NOW.setBackMonthData(-mIndex4_4);
                views.setTextViewText(R.id.tvDate, setYearMonth());
            } else {
                Dates.NOW.setPreMonthData(mIndex4_4);
                views.setTextViewText(R.id.tvDate, setYearMonth());
            }
            im.layout(0, 0, deviceWidth, deviceHeight);
            im.setDrawingCacheEnabled(true);
            bitmap = im.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);
        }
        widget4_4Setting(views, manager);
        bitmap.recycle();
    }
    public void widget4_4Forward(RemoteViews views, AppWidgetManager manager){
        Bitmap bitmap;
        if(viewMode==0) {
            views.setViewVisibility(R.id.tvYear, View.VISIBLE);
            WeekCaptureView iv = new WeekCaptureView(this);
            views.setViewVisibility(R.id.btWeek, View.GONE);
            views.setViewVisibility(R.id.btMonth, View.VISIBLE); //Visible
            ++wIndex4_4;
            if (wIndex4_4 < 0) {
                Dates.NOW.setBackWeekData(-wIndex4_4);
                views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, setMonthWeek());
            } else {
                Dates.NOW.setPreWeekData(wIndex4_4);
                views.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, setMonthWeek());

            }
            Common.fetchWeekData();
            iv.layout(0, 0, deviceWidth, deviceHeight);
            iv.setDrawingCacheEnabled(true);
            bitmap=iv.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);
        }
        else {
            views.setViewVisibility(R.id.tvYear, View.GONE);
            MonthCaptureView im = new MonthCaptureView(this);
            views.setViewVisibility(R.id.btWeek, View.VISIBLE); //Visible
            views.setViewVisibility(R.id.btMonth, View.GONE);
            ++mIndex4_4;
            if (mIndex4_4 < 0) {
                Dates.NOW.setBackMonthData(-mIndex4_4);
                views.setTextViewText(R.id.tvDate, setYearMonth());
            } else {
                Dates.NOW.setPreMonthData(mIndex4_4);
                views.setTextViewText(R.id.tvDate, setYearMonth());
            }
            im.layout(0, 0, deviceWidth, deviceHeight);
            im.setDrawingCacheEnabled(true);
            bitmap=im.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);
        }
        widget4_4Setting(views, manager);
        bitmap.recycle();
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
