package com.daemin.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.daemin.common.CurrentTime;
import com.daemin.enumclass.MyPreferences;
import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-11-03.
 */
public class WidgetChangeDateService5_5 extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("widget", "service start");
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget);
        int deviceWidth = MyPreferences.USERINFO.getPref().getInt("deviceWidth", 0);
        int deviceHeight = MyPreferences.USERINFO.getPref().getInt("deviceHeight", 0);
        int viewMode= MyPreferences.USERINFO.getPref().getInt("viewMode", 0);
        int weekIndex = MyPreferences.USERINFO.getPref().getInt("weekIndex5_5", 0);
        int monthIndex = MyPreferences.USERINFO.getPref().getInt("monthIndex5_5", 0);
        Bitmap bitmap;
        if(intent.getBooleanExtra("back",true)) {
            if (viewMode == 0) {
                views.setViewVisibility(R.id.tvDate, View.VISIBLE);
                WeekCaptureView iv = new WeekCaptureView(this);
                views.setInt(R.id.btWeek, "setBackgroundResource",
                        R.color.maincolor);
                views.setInt(R.id.btMonth, "setBackgroundResource",
                        R.color.lightgray);
                --weekIndex;
                if (weekIndex < 0) {
                    views.setTextViewText(R.id.tvYear, CurrentTime.backTitleYear(-weekIndex) + getString(R.string.year));
                    views.setTextViewText(R.id.tvDate, CurrentTime.backTitleMonthWeek(this, -weekIndex));
                    iv.setCurrentTime(CurrentTime.getBackDateOfWeek(-weekIndex));
                } else {
                    views.setTextViewText(R.id.tvYear, CurrentTime.preTitleYear(weekIndex) + getString(R.string.year));
                    views.setTextViewText(R.id.tvDate, CurrentTime.preTitleMonthWeek(this, weekIndex));
                    iv.setCurrentTime(CurrentTime.getPreDateOfWeek(weekIndex));
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
                --monthIndex;
                if (monthIndex < 0) {
                    views.setTextViewText(R.id.tvYear, CurrentTime.backTitleYearMonth(this, -monthIndex));
                    im.setCurrentTime(CurrentTime.getBackDayOfLastMonth(-monthIndex),
                            CurrentTime.getBackDayOfWeekOfLastMonth(-monthIndex),
                            CurrentTime.getBackDayNumOfMonth(-monthIndex));
                } else {
                    views.setTextViewText(R.id.tvYear, CurrentTime.preTitleYearMonth(this, monthIndex));
                    im.setCurrentTime(CurrentTime.getPreDayOfLastMonth(monthIndex),
                            CurrentTime.getPreDayOfWeekOfLastMonth(monthIndex),
                            CurrentTime.getPreDayNumOfMonth(monthIndex));
                }
                im.layout(0, 0, deviceWidth, deviceHeight);
                im.setDrawingCacheEnabled(true);
                bitmap = im.getDrawingCache();
                views.setImageViewBitmap(R.id.timetableimage, bitmap);
            }
        }else{
            if(viewMode==0) {
                views.setViewVisibility(R.id.tvDate, View.VISIBLE);
                WeekCaptureView iv = new WeekCaptureView(this);
                views.setInt(R.id.btWeek, "setBackgroundResource",
                        R.color.maincolor);
                views.setInt(R.id.btMonth, "setBackgroundResource",
                        R.color.lightgray);
                ++weekIndex;
                if (weekIndex < 0) {
                    views.setTextViewText(R.id.tvYear, CurrentTime.backTitleYear(-weekIndex) + getString(R.string.year));
                    views.setTextViewText(R.id.tvDate, CurrentTime.backTitleMonthWeek(this, -weekIndex));
                    iv.setCurrentTime(CurrentTime.getBackDateOfWeek(-weekIndex));
                } else {
                    views.setTextViewText(R.id.tvYear, CurrentTime.preTitleYear(weekIndex) + getString(R.string.year));
                    views.setTextViewText(R.id.tvDate, CurrentTime.preTitleMonthWeek(this, weekIndex));
                    iv.setCurrentTime(CurrentTime.getPreDateOfWeek(weekIndex));
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
                ++monthIndex;
                if (monthIndex < 0) {
                    views.setTextViewText(R.id.tvYear, CurrentTime.backTitleYearMonth(this, -monthIndex));
                    im.setCurrentTime(CurrentTime.getBackDayOfLastMonth(-monthIndex),
                            CurrentTime.getBackDayOfWeekOfLastMonth(-monthIndex),
                            CurrentTime.getBackDayNumOfMonth(-monthIndex));
                } else {
                    views.setTextViewText(R.id.tvYear, CurrentTime.preTitleYearMonth(this, monthIndex));
                    im.setCurrentTime(CurrentTime.getPreDayOfLastMonth(monthIndex),
                            CurrentTime.getPreDayOfWeekOfLastMonth(monthIndex),
                            CurrentTime.getPreDayNumOfMonth(monthIndex));
                }
                im.layout(0, 0, deviceWidth, deviceHeight);
                im.setDrawingCacheEnabled(true);
                bitmap=im.getDrawingCache();
                views.setImageViewBitmap(R.id.timetableimage, bitmap);
            }
        }
        for (int appWidgetId : manager.getAppWidgetIds(new ComponentName(this, Widget5_5.class))) {
            manager.updateAppWidget(appWidgetId, views);
        }
        SharedPreferences.Editor wEditor = MyPreferences.USERINFO.getEditor();
        wEditor.putInt("weekIndex5_5", weekIndex);
        wEditor.putInt("monthIndex5_5", monthIndex);
        wEditor.commit();
        bitmap.recycle();
        Log.i("widget", "service end");
        stopSelf();
        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
