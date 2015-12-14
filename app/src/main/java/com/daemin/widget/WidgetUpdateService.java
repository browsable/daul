package com.daemin.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.daemin.common.Common;
import com.daemin.enumclass.Dates;
import com.daemin.enumclass.User;
import com.daemin.event.FinishDialogEvent;
import com.daemin.timetable.R;

import de.greenrobot.event.EventBus;

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
        deviceWidth = User.INFO.getDeviceWidth();
        deviceHeight= User.INFO.getDeviceHeight();
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
        viewMode= User.INFO.getViewMode();
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        RemoteViews views5_5 = new RemoteViews(getPackageName(), R.layout.widget5_5);
        RemoteViews views4_4 = new RemoteViews(getPackageName(), R.layout.widget4_4);
        try {
            switch (intent.getStringExtra("action")) {
                case "update5_5":
                    wIndex5_5 = 0;
                    mIndex5_5 = 0;
                    if (viewMode == 0) {
                        views5_5.setViewVisibility(R.id.tvYear, View.VISIBLE);
                        WeekCaptureView iv = new WeekCaptureView(this);
                        views5_5.setViewVisibility(R.id.btWeek, View.GONE);
                        views5_5.setViewVisibility(R.id.btMonth, View.VISIBLE);
                        Dates.NOW.setWeekData();
                        views5_5.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                        views5_5.setTextViewText(R.id.tvDate, setMonthWeek());
                        iv.layout(0, 0, deviceWidth, deviceHeight);
                        iv.setDrawingCacheEnabled(true);
                        Bitmap week5_5 = iv.getDrawingCache();
                        views5_5.setImageViewBitmap(R.id.timetableimage, week5_5);
                        widget5_5Setting(views5_5, manager);
                        week5_5.recycle();
                    } else {
                        views5_5.setViewVisibility(R.id.tvYear, View.GONE);
                        MonthCaptureView iv = new MonthCaptureView(this);
                        views5_5.setViewVisibility(R.id.btWeek, View.VISIBLE);
                        views5_5.setViewVisibility(R.id.btMonth, View.GONE);
                        Dates.NOW.setMonthData();
                        views5_5.setTextViewText(R.id.tvDate, setYearMonth());
                        iv.layout(0, 0, deviceWidth, deviceHeight);
                        iv.setDrawingCacheEnabled(true);
                        Bitmap month5_5 = iv.getDrawingCache();
                        views5_5.setImageViewBitmap(R.id.timetableimage, month5_5);
                        widget5_5Setting(views5_5, manager);
                        month5_5.recycle();
                    }
                    break;
                case "week5_5":
                    EventBus.getDefault().post(new FinishDialogEvent());
                    User.INFO.getEditor().putInt("viewMode", 0).commit();
                    viewMode=0;
                    wIndex5_5 = 0;
                    mIndex5_5 = 0;
                    views5_5.setViewVisibility(R.id.tvYear, View.VISIBLE);
                    WeekCaptureView iv5 = new WeekCaptureView(this);
                    views5_5.setViewVisibility(R.id.btWeek, View.GONE);
                    views5_5.setViewVisibility(R.id.btMonth, View.VISIBLE);
                    Dates.NOW.setWeekData();
                    views5_5.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                    views5_5.setTextViewText(R.id.tvDate, setMonthWeek());
                    iv5.layout(0, 0, deviceWidth, deviceHeight);
                    iv5.setDrawingCacheEnabled(true);
                    Bitmap week5 = iv5.getDrawingCache();
                    views5_5.setImageViewBitmap(R.id.timetableimage, week5);
                    widget5_5Setting(views5_5, manager);
                    week5.recycle();
                    break;
                case "month5_5":
                    EventBus.getDefault().post(new FinishDialogEvent());
                    User.INFO.getEditor().putInt("viewMode", 1).commit();
                    viewMode=1;
                    wIndex5_5 = 0;
                    mIndex5_5 = 0;
                    views5_5.setViewVisibility(R.id.tvYear, View.GONE);
                    MonthCaptureView im5 = new MonthCaptureView(this);
                    views5_5.setViewVisibility(R.id.btWeek, View.VISIBLE);
                    views5_5.setViewVisibility(R.id.btMonth, View.GONE);
                    Dates.NOW.setMonthData();
                    views5_5.setTextViewText(R.id.tvDate, setYearMonth());
                    im5.layout(0, 0, deviceWidth, deviceHeight);
                    im5.setDrawingCacheEnabled(true);
                    Bitmap month5 = im5.getDrawingCache();
                    views5_5.setImageViewBitmap(R.id.timetableimage, month5);
                    widget5_5Setting(views5_5, manager);
                    month5.recycle();
                    break;
                case "back5_5":
                    widget5_5back(views5_5, manager);
                    break;
                case "forward5_5":
                    widget5_5Forward(views5_5, manager);
                    break;
                case "update4_4":
                    wIndex4_4 = 0;
                    mIndex4_4 = 0;
                    if (viewMode == 0) {
                        views4_4.setViewVisibility(R.id.tvYear, View.VISIBLE);
                        WeekCaptureView iv = new WeekCaptureView(this);
                        views4_4.setViewVisibility(R.id.btWeek, View.GONE);
                        views4_4.setViewVisibility(R.id.btMonth, View.VISIBLE);
                        Dates.NOW.setWeekData();
                        views4_4.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                        views4_4.setTextViewText(R.id.tvDate, setMonthWeek());
                        iv.layout(0, 0, deviceWidth, deviceHeight);
                        iv.setDrawingCacheEnabled(true);
                        Bitmap week4_4 = iv.getDrawingCache();
                        views4_4.setImageViewBitmap(R.id.timetableimage, week4_4);
                        widget4_4Setting(views4_4, manager);
                        week4_4.recycle();
                    } else {
                        views4_4.setViewVisibility(R.id.tvYear, View.GONE);
                        MonthCaptureView iv = new MonthCaptureView(this);
                        views4_4.setViewVisibility(R.id.btWeek, View.VISIBLE);
                        views4_4.setViewVisibility(R.id.btMonth, View.GONE);
                        Dates.NOW.setMonthData();
                        views4_4.setTextViewText(R.id.tvDate, setYearMonth());
                        iv.layout(0, 0, deviceWidth, deviceHeight);
                        iv.setDrawingCacheEnabled(true);
                        Bitmap month4_4 = iv.getDrawingCache();
                        views4_4.setImageViewBitmap(R.id.timetableimage, month4_4);
                        widget4_4Setting(views4_4, manager);
                        month4_4.recycle();
                    }
                    break;
                case "week4_4":
                    EventBus.getDefault().post(new FinishDialogEvent());
                    User.INFO.getEditor().putInt("viewMode", 0).commit();
                    viewMode=0;
                    wIndex4_4 = 0;
                    mIndex4_4 = 0;
                    views4_4.setViewVisibility(R.id.tvYear, View.VISIBLE);
                    WeekCaptureView iv4 = new WeekCaptureView(this);
                    views4_4.setViewVisibility(R.id.btWeek, View.GONE);
                    views4_4.setViewVisibility(R.id.btMonth, View.VISIBLE);
                    Dates.NOW.setWeekData();
                    views4_4.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                    views4_4.setTextViewText(R.id.tvDate, setMonthWeek());
                    iv4.layout(0, 0, deviceWidth, deviceHeight);
                    iv4.setDrawingCacheEnabled(true);
                    Bitmap week4 = iv4.getDrawingCache();
                    views4_4.setImageViewBitmap(R.id.timetableimage, week4);
                    widget4_4Setting(views4_4, manager);
                    week4.recycle();
                    break;
                case "month4_4":
                    EventBus.getDefault().post(new FinishDialogEvent());
                    User.INFO.getEditor().putInt("viewMode", 1).commit();
                    viewMode=1;
                    wIndex4_4 = 0;
                    mIndex4_4 = 0;
                    views4_4.setViewVisibility(R.id.tvYear, View.GONE);
                    MonthCaptureView im4 = new MonthCaptureView(this);
                    views4_4.setViewVisibility(R.id.btWeek, View.VISIBLE);
                    views4_4.setViewVisibility(R.id.btMonth, View.GONE);
                    Dates.NOW.setMonthData();
                    views4_4.setTextViewText(R.id.tvDate, setYearMonth());
                    im4.layout(0, 0, deviceWidth, deviceHeight);
                    im4.setDrawingCacheEnabled(true);
                    Bitmap month4_4 = im4.getDrawingCache();
                    views4_4.setImageViewBitmap(R.id.timetableimage, month4_4);
                    widget4_4Setting(views4_4, manager);
                    month4_4.recycle();
                    break;
                case "back4_4":
                    widget4_4back(views4_4, manager);
                    break;
                case "forward4_4":
                    widget4_4Forward(views4_4, manager);
                    break;
            }
        }catch(NullPointerException e){
            Log.i("nullpoint",e.toString());
            if(User.INFO.getWidget5_5()) {
                User.INFO.getEditor().putInt("viewMode", 0).commit();
                viewMode = 0;
                wIndex5_5 = 0;
                mIndex5_5 = 0;
                views5_5.setViewVisibility(R.id.tvYear, View.VISIBLE);
                WeekCaptureView iv = new WeekCaptureView(this);
                views5_5.setViewVisibility(R.id.btWeek, View.GONE);
                views5_5.setViewVisibility(R.id.btMonth, View.VISIBLE);
                Dates.NOW.setWeekData();
                views5_5.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                views5_5.setTextViewText(R.id.tvDate, setMonthWeek());
                iv.layout(0, 0, deviceWidth, deviceHeight);
                iv.setDrawingCacheEnabled(true);
                Bitmap week5 = iv.getDrawingCache();
                views5_5.setImageViewBitmap(R.id.timetableimage, week5);
                widget5_5Setting(views5_5, manager);
                week5.recycle();
            }
            if(User.INFO.getWidget4_4()){
                User.INFO.getEditor().putInt("viewMode", 0).commit();
                viewMode = 0;
                wIndex4_4 = 0;
                mIndex4_4 = 0;
                views4_4.setViewVisibility(R.id.tvYear, View.VISIBLE);
                WeekCaptureView iv = new WeekCaptureView(this);
                views4_4.setViewVisibility(R.id.btWeek, View.GONE);
                views4_4.setViewVisibility(R.id.btMonth, View.VISIBLE);
                Dates.NOW.setWeekData();
                views4_4.setTextViewText(R.id.tvYear, Dates.NOW.year + getString(R.string.year));
                views4_4.setTextViewText(R.id.tvDate, setMonthWeek());
                iv.layout(0, 0, deviceWidth, deviceHeight);
                iv.setDrawingCacheEnabled(true);
                Bitmap week4 = iv.getDrawingCache();
                views4_4.setImageViewBitmap(R.id.timetableimage, week4);
                widget4_4Setting(views4_4, manager);
                week4.recycle();
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

    public void widget5_5back(RemoteViews views, AppWidgetManager manager){
        Bitmap bitmap;
        if (viewMode == 0) {
            views.setViewVisibility(R.id.tvYear, View.VISIBLE);
            WeekCaptureView iv = new WeekCaptureView(this);
            views.setViewVisibility(R.id.btWeek, View.GONE);
            views.setViewVisibility(R.id.btMonth, View.VISIBLE);
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
            views.setViewVisibility(R.id.btWeek, View.VISIBLE);
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
            views.setViewVisibility(R.id.btMonth, View.VISIBLE);
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
            views.setViewVisibility(R.id.btWeek, View.VISIBLE);
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
    public void widget4_4back(RemoteViews views, AppWidgetManager manager){
        Bitmap bitmap;
        if (viewMode == 0) {
            views.setViewVisibility(R.id.tvYear, View.VISIBLE);
            WeekCaptureView iv = new WeekCaptureView(this);
            views.setViewVisibility(R.id.btWeek, View.GONE);
            views.setViewVisibility(R.id.btMonth, View.VISIBLE);
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
            views.setViewVisibility(R.id.btWeek, View.VISIBLE);
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
            views.setViewVisibility(R.id.btMonth, View.VISIBLE);
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
            views.setViewVisibility(R.id.btWeek, View.VISIBLE);
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
