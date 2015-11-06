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
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.daemin.common.CurrentTime;
import com.daemin.enumclass.MyPreferences;
import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-11-03.
 */
public class WidgetUpdateService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("widget", "service start");
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget);
        int deviceWidth = MyPreferences.USERINFO.getPref().getInt("deviceWidth", 0);
        int deviceHeight= MyPreferences.USERINFO.getPref().getInt("deviceHeight", 0);
        int viewMode=intent.getIntExtra("viewMode",0);
        ImageView iv;
        if(viewMode ==0) {
            views.setViewVisibility(R.id.tvDate, View.VISIBLE);
            iv = new WeekCaptureView(this);
            views.setInt(R.id.btWeek, "setBackgroundResource",
                    R.color.maincolor);
            views.setInt(R.id.btMonth, "setBackgroundResource",
                    R.color.lightgray);
            views.setTextViewText(R.id.tvYear, CurrentTime.getYear() + getString(R.string.year));
            views.setTextViewText(R.id.tvDate, CurrentTime.getTitleMonthWeek(this));
        }
        else {
            views.setViewVisibility(R.id.tvDate, View.GONE);
            iv = new MonthCaptureView(this);
            views.setInt(R.id.btWeek, "setBackgroundResource",
                    R.color.lightgray);
            views.setInt(R.id.btMonth, "setBackgroundResource",
                    R.color.maincolor);
            views.setTextViewText(R.id.tvYear, CurrentTime.getTitleYearMonth(this));
        }
        //capture.measure(0, 0);
        iv.layout(0, 0, deviceWidth, deviceHeight);
        iv.setDrawingCacheEnabled(true);
        Bitmap bitmap=iv.getDrawingCache();
        views.setImageViewBitmap(R.id.timetableimage, bitmap);
        if(intent.getBooleanExtra("widget5_5",true)) {
            for (int appWidgetId : manager.getAppWidgetIds(new ComponentName(this, Widget5_5.class))) {
                manager.updateAppWidget(appWidgetId, views);
            }
        }else {
            for (int appWidgetId : manager.getAppWidgetIds(new ComponentName(this, Widget4_4.class))) {
                manager.updateAppWidget(appWidgetId, views);
            }
        }
        Log.i("widget", "service end");
        bitmap.recycle();
        SharedPreferences.Editor wEditor = MyPreferences.USERINFO.getEditor();
        wEditor.putInt("viewMode", viewMode);
        wEditor.putInt("weekIndex5_5", 0);
        wEditor.putInt("monthIndex5_5", 0);
        wEditor.commit();
        stopSelf();
        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
