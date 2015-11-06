package com.daemin.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.daemin.common.Common;
import com.daemin.common.CurrentTime;
import com.daemin.enumclass.MyPreferences;
import com.daemin.main.MainActivity;
import com.daemin.timetable.R;

public class Widget4_4 extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        int deviceWidth = MyPreferences.USERINFO.getPref().getInt("deviceWidth", 0);
        int deviceHeight = MyPreferences.USERINFO.getPref().getInt("deviceHeight", 0);
        ImageView iv;
        views.setViewVisibility(R.id.tvDate, View.VISIBLE);
        iv = new WeekCaptureView(context);
        views.setInt(R.id.btWeek, "setBackgroundResource",
                R.color.maincolor);
        views.setInt(R.id.btMonth, "setBackgroundResource",
                R.color.lightgray);
        views.setTextViewText(R.id.tvYear, CurrentTime.getYear() + context.getString(R.string.year));
        views.setTextViewText(R.id.tvDate, CurrentTime.getTitleMonthWeek(context));
        iv.layout(0, 0, deviceWidth, deviceHeight);
        iv.setDrawingCacheEnabled(true);
        Bitmap bitmap = iv.getDrawingCache();
        views.setImageViewBitmap(R.id.timetableimage, bitmap);
        Intent main = new Intent(context, MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent mainP = PendingIntent.getActivity(context, 0, main, 0);
        views.setOnClickPendingIntent(R.id.timetableimage, mainP);
        Intent week = new Intent(Common.ACTION_WEEK4_4);
        PendingIntent weekP = PendingIntent.getBroadcast(context, 0, week, 0);
        views.setOnClickPendingIntent(R.id.btWeek, weekP);
        Intent month = new Intent(Common.ACTION_MONTH4_4);
        PendingIntent monthP = PendingIntent.getBroadcast(context, 0, month, 0);
        views.setOnClickPendingIntent(R.id.btMonth, monthP);
        Intent back = new Intent(Common.ACTION_BACK4_4);
        PendingIntent backP = PendingIntent.getBroadcast(context, 0, back, 0);
        views.setOnClickPendingIntent(R.id.btBack, backP);
        Intent forward = new Intent(Common.ACTION_FORWARD4_4);
        PendingIntent forwardP = PendingIntent.getBroadcast(context, 0, forward, 0);
        views.setOnClickPendingIntent(R.id.btForward, forwardP);
        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        bitmap.recycle();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        switch (intent.getAction()) {
            case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
                Log.i("widget", "update call");
                break;
            case Common.ACTION_UPDATE:
                Log.i("widget", "update4_4 call");
                Intent update = new Intent(context, WidgetUpdateService.class);
                update.putExtra("widget5_5", false);
                update.putExtra("viewMode", intent.getIntExtra("viewMode", 0));
                context.startService(update);
                break;
            case Common.ACTION_WEEK4_4:
                Log.i("widget", "week");
                Intent week = new Intent(context, WidgetUpdateService.class);
                week.putExtra("viewMode", 0);
                week.putExtra("widget5_5", false);
                week.putExtra("week", true);
                context.startService(week);
                break;
            case Common.ACTION_MONTH4_4:
                Log.i("widget", "month");
                Intent month = new Intent(context, WidgetUpdateService.class);
                month.putExtra("viewMode", 2);
                month.putExtra("widget5_5", false);
                month.putExtra("week", false);
                context.startService(month);
                break;
            case Common.ACTION_BACK4_4:
                Log.i("widget", "back");
                Intent back = new Intent(context, WidgetChangeDateService4_4.class);
                back.putExtra("back", true);
                context.startService(back);
                break;
            case Common.ACTION_FORWARD4_4:
                Log.i("widget", "forward");
                Intent forward = new Intent(context, WidgetChangeDateService4_4.class);
                forward.putExtra("back", false);
                context.startService(forward);
                break;
        }
    }
}