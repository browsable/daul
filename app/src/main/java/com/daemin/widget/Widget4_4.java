package com.daemin.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.daemin.common.Common;
import com.daemin.dialog.DialWidgetSchedule;
import com.daemin.enumclass.MyPreferences;

public class Widget4_4 extends AppWidgetProvider {
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        SharedPreferences.Editor editor = MyPreferences.USERINFO.getEditor();
        editor.putBoolean("widget4_4", true);
        editor.commit();
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.i("widget", "service end");
        context.stopService(new Intent(context, WidgetUpdateService.class));
        SharedPreferences.Editor editor = MyPreferences.USERINFO.getEditor();
        editor.putBoolean("widget4_4", false);
        editor.commit();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        switch(intent.getAction()){
            case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
                Intent init = new Intent(context, WidgetUpdateService.class);
                init.putExtra("action","update4_4");
                context.startService(init);
                break;
            case Common.ACTION_WEEK4_4:
                Intent week = new Intent(context, WidgetUpdateService.class);
                week.putExtra("action","week4_4");
                context.startService(week);
                break;
            case Common.ACTION_MONTH4_4:
                Intent month = new Intent(context, WidgetUpdateService.class);
                month.putExtra("action","month4_4");
                context.startService(month);
                break;
            case Common.ACTION_BACK4_4:
                Intent back = new Intent(context, WidgetUpdateService.class);
                back.putExtra("action","back4_4");
                context.startService(back);
                break;
            case Common.ACTION_FORWARD4_4:
                Intent forward = new Intent(context, WidgetUpdateService.class);
                forward.putExtra("action","forward4_4");
                context.startService(forward);
                break;
            case Common.ACTION_DIAL4_4:
                Intent dial = new Intent(context, DialWidgetSchedule.class);
                dial.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                dial.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(dial);
                break;
        }
    }
}