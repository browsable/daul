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
import com.daemin.enumclass.Dates;
import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-11-03.
 */
public class WidgetUpdateService extends Service {
    int deviceWidth, deviceHeight, viewMode;
    static int wIndex5_5,mIndex5_5,wIndex4_4,mIndex4_4;
    SharedPreferences pref;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("widget", "service start");
        pref = getSharedPreferences("USERINFO", MODE_PRIVATE);
        deviceWidth = pref.getInt("deviceWidth", 0);
        deviceHeight= pref.getInt("deviceHeight", 0);
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
                    if (viewMode == 0) widget5_5Week(views5_5,manager);
                    else widget5_5Month(views5_5,manager);
                    break;
                case "week5_5":
                    Log.i("widget", "service week5_5");
                    widget5_5Week(views5_5,manager);
                    break;
                case "month5_5":
                    Log.i("widget", "service month5_5");
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
        pref.edit().putInt("viewMode", 0).commit();
        viewMode=0;
        wIndex5_5 = 0;
        mIndex5_5 = 0;
        views.setViewVisibility(R.id.tvYear, View.VISIBLE);
        WeekCaptureView iv5 = new WeekCaptureView(this);
        views.setViewVisibility(R.id.btWeek, View.GONE);
        views.setViewVisibility(R.id.btMonth, View.GONE); //Visible
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
        pref.edit().putInt("viewMode", 1).commit();
        viewMode=1;
        wIndex5_5 = 0;
        mIndex5_5 = 0;
        views.setViewVisibility(R.id.tvYear, View.GONE);
        MonthCaptureView im5 = new MonthCaptureView(this);
        views.setViewVisibility(R.id.btWeek, View.GONE); //Visible
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
        pref.edit().putInt("viewMode", 0).commit();
        viewMode=0;
        wIndex4_4 = 0;
        mIndex4_4 = 0;
        views.setViewVisibility(R.id.tvYear, View.VISIBLE);
        WeekCaptureView iv4 = new WeekCaptureView(this);
        views.setViewVisibility(R.id.btWeek, View.GONE);
        views.setViewVisibility(R.id.btMonth, View.GONE); //Visible
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
        pref.edit().putInt("viewMode", 1).commit();
        viewMode=1;
        wIndex4_4 = 0;
        mIndex4_4 = 0;
        views.setViewVisibility(R.id.tvYear, View.GONE);
        MonthCaptureView im4 = new MonthCaptureView(this);
        views.setViewVisibility(R.id.btWeek, View.GONE); //Visible
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
            views.setViewVisibility(R.id.btMonth, View.GONE); //Visible
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
            views.setViewVisibility(R.id.btWeek, View.GONE); //Visible
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
            views.setViewVisibility(R.id.btMonth, View.GONE); //Visible
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
            views.setViewVisibility(R.id.btWeek, View.GONE); //Visible
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
            views.setViewVisibility(R.id.btMonth, View.GONE); //Visible
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
            views.setViewVisibility(R.id.btWeek, View.GONE); //Visible
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
            views.setViewVisibility(R.id.btMonth, View.GONE); //Visible
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
            views.setViewVisibility(R.id.btWeek, View.GONE); //Visible
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
