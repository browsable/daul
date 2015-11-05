package com.daemin.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.daemin.common.Common;
import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-11-03.
 */
public class WidgetUpdateService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget);
        try {
            Bitmap captureImg = BitmapFactory.decodeFile(Common.CAPTURE);
            if (captureImg != null) {
                views.setImageViewBitmap(R.id.timetableimage, captureImg);
            }
            if(intent.getBooleanExtra("widget5_5",true)) {
                for (int appWidgetId : manager.getAppWidgetIds(new ComponentName(this, Widget5_5.class))) {
                    manager.updateAppWidget(appWidgetId, views);
                }
            }else {
                for (int appWidgetId : manager.getAppWidgetIds(new ComponentName(this, Widget4_4.class))) {
                    manager.updateAppWidget(appWidgetId, views);
                }
            }
            captureImg.recycle();
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }
        stopSelf();
        return START_NOT_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
