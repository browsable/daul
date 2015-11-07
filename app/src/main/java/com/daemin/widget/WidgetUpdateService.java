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
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.daemin.common.Common;
import com.daemin.common.CurrentTime;
import com.daemin.enumclass.MyPreferences;
import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-11-03.
 */
public class WidgetUpdateService extends Service {
    int deviceWidth, deviceHeight, viewMode;
    static int wIndex5_5,mIndex5_5,wIndex4_4,mIndex4_4;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("widget", "service start");
        deviceWidth = MyPreferences.USERINFO.getPref().getInt("deviceWidth", 0);
        deviceHeight= MyPreferences.USERINFO.getPref().getInt("deviceHeight", 0);
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
        viewMode= MyPreferences.USERINFO.getPref().getInt("viewMode", 0);
        //viewMode=intent.getIntExtra("viewMode", 0);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        RemoteViews views5_5 = new RemoteViews(getPackageName(), R.layout.widget5_5);
        RemoteViews views4_4 = new RemoteViews(getPackageName(), R.layout.widget4_4);
        ImageView iv;
        try {
            switch (intent.getStringExtra("action")) {
                case "update5_5":
                    wIndex5_5 = 0;
                    mIndex5_5 = 0;
                    if (viewMode == 0) {
                        views5_5.setViewVisibility(R.id.tvDate, View.VISIBLE);
                        iv = new WeekCaptureView(this);
                        views5_5.setInt(R.id.btWeek, "setBackgroundResource",
                                R.color.maincolor);
                        views5_5.setInt(R.id.btMonth, "setBackgroundResource",
                                R.color.lightgray);
                        views5_5.setTextViewText(R.id.tvYear, CurrentTime.getYear() + getString(R.string.year));
                        views5_5.setTextViewText(R.id.tvDate, CurrentTime.getTitleMonthWeek(this));
                        iv.layout(0, 0, deviceWidth, deviceHeight);
                        iv.setDrawingCacheEnabled(true);
                        Bitmap week5_5 = iv.getDrawingCache();
                        views5_5.setImageViewBitmap(R.id.timetableimage, week5_5);
                        Widget5_5Setting(views5_5, manager);
                        week5_5.recycle();
                    } else {
                        views5_5.setViewVisibility(R.id.tvDate, View.GONE);
                        iv = new MonthCaptureView(this);
                        views5_5.setInt(R.id.btWeek, "setBackgroundResource",
                                R.color.lightgray);
                        views5_5.setInt(R.id.btMonth, "setBackgroundResource",
                                R.color.maincolor);
                        views5_5.setTextViewText(R.id.tvYear, CurrentTime.getTitleYearMonth(this));
                        iv.layout(0, 0, deviceWidth, deviceHeight);
                        iv.setDrawingCacheEnabled(true);
                        Bitmap month5_5 = iv.getDrawingCache();
                        views5_5.setImageViewBitmap(R.id.timetableimage, month5_5);
                        Widget5_5Setting(views5_5, manager);
                        month5_5.recycle();
                    }
                    break;
                case "week5_5":
                    SharedPreferences.Editor wEditor5 = MyPreferences.USERINFO.getEditor();
                    wEditor5.putInt("viewMode", 0);
                    wEditor5.commit();
                    wIndex5_5 = 0;
                    mIndex5_5 = 0;
                    views5_5.setViewVisibility(R.id.tvDate, View.VISIBLE);
                    iv = new WeekCaptureView(this);
                    views5_5.setInt(R.id.btWeek, "setBackgroundResource",
                            R.color.maincolor);
                    views5_5.setInt(R.id.btMonth, "setBackgroundResource",
                            R.color.lightgray);
                    views5_5.setTextViewText(R.id.tvYear, CurrentTime.getYear() + getString(R.string.year));
                    views5_5.setTextViewText(R.id.tvDate, CurrentTime.getTitleMonthWeek(this));
                    iv.layout(0, 0, deviceWidth, deviceHeight);
                    iv.setDrawingCacheEnabled(true);
                    Bitmap week5 = iv.getDrawingCache();
                    views5_5.setImageViewBitmap(R.id.timetableimage, week5);
                    Widget5_5Setting(views5_5, manager);
                    week5.recycle();
                    break;
                case "month5_5":
                    SharedPreferences.Editor mEditor5 = MyPreferences.USERINFO.getEditor();
                    mEditor5.putInt("viewMode", 2);
                    mEditor5.commit();
                    wIndex5_5 = 0;
                    mIndex5_5 = 0;
                    views5_5.setViewVisibility(R.id.tvDate, View.GONE);
                    iv = new MonthCaptureView(this);
                    views5_5.setInt(R.id.btWeek, "setBackgroundResource",
                            R.color.lightgray);
                    views5_5.setInt(R.id.btMonth, "setBackgroundResource",
                            R.color.maincolor);
                    views5_5.setTextViewText(R.id.tvYear, CurrentTime.getTitleYearMonth(this));
                    iv.layout(0, 0, deviceWidth, deviceHeight);
                    iv.setDrawingCacheEnabled(true);
                    Bitmap month5 = iv.getDrawingCache();
                    views5_5.setImageViewBitmap(R.id.timetableimage, month5);
                    Widget5_5Setting(views5_5, manager);
                    month5.recycle();
                    break;
                case "back5_5":
                    Widget5_5Back(views5_5, manager);
                    break;
                case "forward5_5":
                    Widget5_5Forward(views5_5, manager);
                    break;
                case "update4_4":
                    wIndex4_4 = 0;
                    mIndex4_4 = 0;
                    if (viewMode == 0) {
                        views4_4.setViewVisibility(R.id.tvDate, View.VISIBLE);
                        iv = new WeekCaptureView(this);
                        views4_4.setInt(R.id.btWeek, "setBackgroundResource",
                                R.color.maincolor);
                        views4_4.setInt(R.id.btMonth, "setBackgroundResource",
                                R.color.lightgray);
                        views4_4.setTextViewText(R.id.tvYear, CurrentTime.getYear() + getString(R.string.year));
                        views4_4.setTextViewText(R.id.tvDate, CurrentTime.getTitleMonthWeek(this));
                        iv.layout(0, 0, deviceWidth, deviceHeight);
                        iv.setDrawingCacheEnabled(true);
                        Bitmap week4_4 = iv.getDrawingCache();
                        views4_4.setImageViewBitmap(R.id.timetableimage, week4_4);
                        Widget4_4Setting(views4_4, manager);
                        week4_4.recycle();
                    } else {
                        views4_4.setViewVisibility(R.id.tvDate, View.GONE);
                        iv = new MonthCaptureView(this);
                        views4_4.setInt(R.id.btWeek, "setBackgroundResource",
                                R.color.lightgray);
                        views4_4.setInt(R.id.btMonth, "setBackgroundResource",
                                R.color.maincolor);
                        views4_4.setTextViewText(R.id.tvYear, CurrentTime.getTitleYearMonth(this));
                        iv.layout(0, 0, deviceWidth, deviceHeight);
                        iv.setDrawingCacheEnabled(true);
                        Bitmap month4_4 = iv.getDrawingCache();
                        views4_4.setImageViewBitmap(R.id.timetableimage, month4_4);
                        Widget4_4Setting(views4_4, manager);
                        month4_4.recycle();
                    }
                    break;
                case "week4_4":
                    SharedPreferences.Editor wEditor4 = MyPreferences.USERINFO.getEditor();
                    wEditor4.putInt("viewMode", 0);
                    wEditor4.commit();
                    wIndex4_4 = 0;
                    mIndex4_4 = 0;
                    views4_4.setViewVisibility(R.id.tvDate, View.VISIBLE);
                    iv = new WeekCaptureView(this);
                    views4_4.setInt(R.id.btWeek, "setBackgroundResource",
                            R.color.maincolor);
                    views4_4.setInt(R.id.btMonth, "setBackgroundResource",
                            R.color.lightgray);
                    views4_4.setTextViewText(R.id.tvYear, CurrentTime.getYear() + getString(R.string.year));
                    views4_4.setTextViewText(R.id.tvDate, CurrentTime.getTitleMonthWeek(this));
                    iv.layout(0, 0, deviceWidth, deviceHeight);
                    iv.setDrawingCacheEnabled(true);
                    Bitmap week4 = iv.getDrawingCache();
                    views4_4.setImageViewBitmap(R.id.timetableimage, week4);
                    Widget4_4Setting(views4_4, manager);
                    week4.recycle();
                    break;
                case "month4_4":
                    SharedPreferences.Editor mEditor4 = MyPreferences.USERINFO.getEditor();
                    mEditor4.putInt("viewMode", 2);
                    mEditor4.commit();
                    wIndex4_4 = 0;
                    mIndex4_4 = 0;
                    views4_4.setViewVisibility(R.id.tvDate, View.GONE);
                    iv = new MonthCaptureView(this);
                    views4_4.setInt(R.id.btWeek, "setBackgroundResource",
                            R.color.lightgray);
                    views4_4.setInt(R.id.btMonth, "setBackgroundResource",
                            R.color.maincolor);
                    views4_4.setTextViewText(R.id.tvYear, CurrentTime.getTitleYearMonth(this));
                    iv.layout(0, 0, deviceWidth, deviceHeight);
                    iv.setDrawingCacheEnabled(true);
                    Bitmap month4_4 = iv.getDrawingCache();
                    views4_4.setImageViewBitmap(R.id.timetableimage, month4_4);
                    Widget4_4Setting(views4_4, manager);
                    month4_4.recycle();
                    break;
                case "back4_4":
                    Widget4_4Back(views4_4, manager);
                    break;
                case "forward4_4":
                    Widget4_4Forward(views4_4, manager);
                    break;

            }
        }catch(NullPointerException e){
            views5_5.setViewVisibility(R.id.tvDate, View.VISIBLE);
            iv = new WeekCaptureView(this);
            views5_5.setInt(R.id.btWeek, "setBackgroundResource",
                    R.color.maincolor);
            views5_5.setInt(R.id.btMonth, "setBackgroundResource",
                    R.color.lightgray);
            views5_5.setTextViewText(R.id.tvYear, CurrentTime.getYear() + getString(R.string.year));
            views5_5.setTextViewText(R.id.tvDate, CurrentTime.getTitleMonthWeek(this));
            iv.layout(0, 0, deviceWidth, deviceHeight);
            iv.setDrawingCacheEnabled(true);
            Bitmap week5_5 = iv.getDrawingCache();
            views5_5.setImageViewBitmap(R.id.timetableimage, week5_5);
            Widget5_5Setting(views5_5, manager);
            week5_5.recycle();
        }
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void Widget5_5Setting(RemoteViews views, AppWidgetManager manager){
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
    public void Widget4_4Setting(RemoteViews views, AppWidgetManager manager){
        /*Intent main4 = new Intent(this, DialWidgetSchedule.class);
        main4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        main4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent mainP4 = PendingIntent.getActivity(this, 0, main4, 0);
        views.setOnClickPendingIntent(R.id.timetableimage, mainP4);*/
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
    public void Widget5_5Back(RemoteViews views, AppWidgetManager manager){
        int viewMode = MyPreferences.USERINFO.getPref().getInt("viewMode", 0);
        Bitmap bitmap;
        if (viewMode == 0) {
            views.setViewVisibility(R.id.tvDate, View.VISIBLE);
            WeekCaptureView iv = new WeekCaptureView(this);
            views.setInt(R.id.btWeek, "setBackgroundResource",
                    R.color.maincolor);
            views.setInt(R.id.btMonth, "setBackgroundResource",
                    R.color.lightgray);
            --wIndex5_5;
            if (wIndex5_5 < 0) {
                views.setTextViewText(R.id.tvYear, CurrentTime.backTitleYear(-wIndex5_5) + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, CurrentTime.backTitleMonthWeek(this, -wIndex5_5));
                iv.setCurrentTime(CurrentTime.getBackDateOfWeek(-wIndex5_5));
            } else {
                views.setTextViewText(R.id.tvYear, CurrentTime.preTitleYear(wIndex5_5) + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, CurrentTime.preTitleMonthWeek(this, wIndex5_5));
                iv.setCurrentTime(CurrentTime.getPreDateOfWeek(wIndex5_5));
            }
            iv.layout(0, 0, deviceWidth, deviceHeight);
            iv.setDrawingCacheEnabled(true);
            bitmap = iv.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);
        } else {
            views.setViewVisibility(R.id.tvDate, View.GONE);
            MonthCaptureView im = new MonthCaptureView(this);
            views.setInt(R.id.btWeek, "setBackgroundResource",
                    R.color.lightgray);
            views.setInt(R.id.btMonth, "setBackgroundResource",
                    R.color.maincolor);
            --mIndex5_5;
            if (mIndex5_5 < 0) {
                views.setTextViewText(R.id.tvYear, CurrentTime.backTitleYearMonth(this, -mIndex5_5));
                im.setCurrentTime(CurrentTime.getBackDayOfLastMonth(-mIndex5_5),
                        CurrentTime.getBackDayOfWeekOfLastMonth(-mIndex5_5),
                        CurrentTime.getBackDayNumOfMonth(-mIndex5_5));
            } else {
                views.setTextViewText(R.id.tvYear, CurrentTime.preTitleYearMonth(this, mIndex5_5));
                im.setCurrentTime(CurrentTime.getPreDayOfLastMonth(mIndex5_5),
                        CurrentTime.getPreDayOfWeekOfLastMonth(mIndex5_5),
                        CurrentTime.getPreDayNumOfMonth(mIndex5_5));
            }
            im.layout(0, 0, deviceWidth, deviceHeight);
            im.setDrawingCacheEnabled(true);
            bitmap = im.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);
        }
        Widget5_5Setting(views, manager);
        bitmap.recycle();
    }
    public void Widget5_5Forward(RemoteViews views, AppWidgetManager manager){
        int viewMode= MyPreferences.USERINFO.getPref().getInt("viewMode", 0);
        Bitmap bitmap;
        if(viewMode==0) {
            views.setViewVisibility(R.id.tvDate, View.VISIBLE);
            WeekCaptureView iv = new WeekCaptureView(this);
            views.setInt(R.id.btWeek, "setBackgroundResource",
                    R.color.maincolor);
            views.setInt(R.id.btMonth, "setBackgroundResource",
                    R.color.lightgray);
            ++wIndex5_5;
            if (wIndex5_5 < 0) {
                views.setTextViewText(R.id.tvYear, CurrentTime.backTitleYear(-wIndex5_5) + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, CurrentTime.backTitleMonthWeek(this, -wIndex5_5));
                iv.setCurrentTime(CurrentTime.getBackDateOfWeek(-wIndex5_5));
            } else {
                views.setTextViewText(R.id.tvYear, CurrentTime.preTitleYear(wIndex5_5) + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, CurrentTime.preTitleMonthWeek(this, wIndex5_5));
                iv.setCurrentTime(CurrentTime.getPreDateOfWeek(wIndex5_5));
            }
            iv.layout(0, 0, deviceWidth, deviceHeight);
            iv.setDrawingCacheEnabled(true);
            bitmap=iv.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);
        }
        else {
            views.setViewVisibility(R.id.tvDate, View.GONE);
            MonthCaptureView im = new MonthCaptureView(this);
            views.setInt(R.id.btWeek, "setBackgroundResource",
                    R.color.lightgray);
            views.setInt(R.id.btMonth, "setBackgroundResource",
                    R.color.maincolor);
            ++mIndex5_5;
            if (mIndex5_5 < 0) {
                views.setTextViewText(R.id.tvYear, CurrentTime.backTitleYearMonth(this, -mIndex5_5));
                im.setCurrentTime(CurrentTime.getBackDayOfLastMonth(-mIndex5_5),
                        CurrentTime.getBackDayOfWeekOfLastMonth(-mIndex5_5),
                        CurrentTime.getBackDayNumOfMonth(-mIndex5_5));
            } else {
                views.setTextViewText(R.id.tvYear, CurrentTime.preTitleYearMonth(this, mIndex5_5));
                im.setCurrentTime(CurrentTime.getPreDayOfLastMonth(mIndex5_5),
                        CurrentTime.getPreDayOfWeekOfLastMonth(mIndex5_5),
                        CurrentTime.getPreDayNumOfMonth(mIndex5_5));
            }
            im.layout(0, 0, deviceWidth, deviceHeight);
            im.setDrawingCacheEnabled(true);
            bitmap=im.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);
        }
        Widget5_5Setting(views, manager);
        bitmap.recycle();
    }
    public void Widget4_4Back(RemoteViews views, AppWidgetManager manager){
        int viewMode= MyPreferences.USERINFO.getPref().getInt("viewMode", 0);
        Bitmap bitmap;
        if (viewMode == 0) {
            views.setViewVisibility(R.id.tvDate, View.VISIBLE);
            WeekCaptureView iv = new WeekCaptureView(this);
            views.setInt(R.id.btWeek, "setBackgroundResource",
                    R.color.maincolor);
            views.setInt(R.id.btMonth, "setBackgroundResource",
                    R.color.lightgray);
            --wIndex4_4;
            if (wIndex4_4 < 0) {
                views.setTextViewText(R.id.tvYear, CurrentTime.backTitleYear(-wIndex4_4) + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, CurrentTime.backTitleMonthWeek(this, -wIndex4_4));
                iv.setCurrentTime(CurrentTime.getBackDateOfWeek(-wIndex4_4));
            } else {
                views.setTextViewText(R.id.tvYear, CurrentTime.preTitleYear(wIndex4_4) + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, CurrentTime.preTitleMonthWeek(this, wIndex4_4));
                iv.setCurrentTime(CurrentTime.getPreDateOfWeek(wIndex4_4));
            }
            iv.layout(0, 0, deviceWidth, deviceHeight);
            iv.setDrawingCacheEnabled(true);
            bitmap = iv.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);
        } else {
            views.setViewVisibility(R.id.tvDate, View.GONE);
            MonthCaptureView im = new MonthCaptureView(this);
            views.setInt(R.id.btWeek, "setBackgroundResource",
                    R.color.lightgray);
            views.setInt(R.id.btMonth, "setBackgroundResource",
                    R.color.maincolor);
            --mIndex4_4;
            if (mIndex4_4 < 0) {
                views.setTextViewText(R.id.tvYear, CurrentTime.backTitleYearMonth(this, -mIndex4_4));
                im.setCurrentTime(CurrentTime.getBackDayOfLastMonth(-mIndex4_4),
                        CurrentTime.getBackDayOfWeekOfLastMonth(-mIndex4_4),
                        CurrentTime.getBackDayNumOfMonth(-mIndex4_4));
            } else {
                views.setTextViewText(R.id.tvYear, CurrentTime.preTitleYearMonth(this, mIndex4_4));
                im.setCurrentTime(CurrentTime.getPreDayOfLastMonth(mIndex4_4),
                        CurrentTime.getPreDayOfWeekOfLastMonth(mIndex4_4),
                        CurrentTime.getPreDayNumOfMonth(mIndex4_4));
            }
            im.layout(0, 0, deviceWidth, deviceHeight);
            im.setDrawingCacheEnabled(true);
            bitmap = im.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);
        }
        Widget4_4Setting(views, manager);
        bitmap.recycle();
    }
    public void Widget4_4Forward(RemoteViews views, AppWidgetManager manager){
        int viewMode= MyPreferences.USERINFO.getPref().getInt("viewMode", 0);
        Bitmap bitmap;
        if(viewMode==0) {
            views.setViewVisibility(R.id.tvDate, View.VISIBLE);
            WeekCaptureView iv = new WeekCaptureView(this);
            views.setInt(R.id.btWeek, "setBackgroundResource",
                    R.color.maincolor);
            views.setInt(R.id.btMonth, "setBackgroundResource",
                    R.color.lightgray);
            ++wIndex4_4;
            if (wIndex4_4 < 0) {
                views.setTextViewText(R.id.tvYear, CurrentTime.backTitleYear(-wIndex4_4) + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, CurrentTime.backTitleMonthWeek(this, -wIndex4_4));
                iv.setCurrentTime(CurrentTime.getBackDateOfWeek(-wIndex4_4));
            } else {
                views.setTextViewText(R.id.tvYear, CurrentTime.preTitleYear(wIndex4_4) + getString(R.string.year));
                views.setTextViewText(R.id.tvDate, CurrentTime.preTitleMonthWeek(this, wIndex4_4));
                iv.setCurrentTime(CurrentTime.getPreDateOfWeek(wIndex4_4));
            }
            iv.layout(0, 0, deviceWidth, deviceHeight);
            iv.setDrawingCacheEnabled(true);
            bitmap=iv.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);
        }
        else {
            views.setViewVisibility(R.id.tvDate, View.GONE);
            MonthCaptureView im = new MonthCaptureView(this);
            views.setInt(R.id.btWeek, "setBackgroundResource",
                    R.color.lightgray);
            views.setInt(R.id.btMonth, "setBackgroundResource",
                    R.color.maincolor);
            ++mIndex4_4;
            if (mIndex4_4 < 0) {
                views.setTextViewText(R.id.tvYear, CurrentTime.backTitleYearMonth(this, -mIndex4_4));
                im.setCurrentTime(CurrentTime.getBackDayOfLastMonth(-mIndex4_4),
                        CurrentTime.getBackDayOfWeekOfLastMonth(-mIndex4_4),
                        CurrentTime.getBackDayNumOfMonth(-mIndex4_4));
            } else {
                views.setTextViewText(R.id.tvYear, CurrentTime.preTitleYearMonth(this, mIndex4_4));
                im.setCurrentTime(CurrentTime.getPreDayOfLastMonth(mIndex4_4),
                        CurrentTime.getPreDayOfWeekOfLastMonth(mIndex4_4),
                        CurrentTime.getPreDayNumOfMonth(mIndex4_4));
            }
            im.layout(0, 0, deviceWidth, deviceHeight);
            im.setDrawingCacheEnabled(true);
            bitmap=im.getDrawingCache();
            views.setImageViewBitmap(R.id.timetableimage, bitmap);
        }
        Widget4_4Setting(views, manager);
        bitmap.recycle();
    }
}
