package com.daemin.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-11-03.
 */
public class WidgetRefreshService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("widget", "service start");
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget);
        /*RemoteViews newView = new RemoteViews(getPackageName(), R.layout.widget_capture);
        Log.i("widget", "service setvisible");
        views.addView(R.id.llRefresh, newView);
        Log.i("widget", "service addView");*/
        CaptureView capture = new CaptureView(this);
        capture.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY);
        capture.layout(0, 0, 50, 50);
        capture.setDrawingCacheEnabled(true);
        Bitmap bitmap=capture.getDrawingCache();
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
        stopSelf();
        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
