package com.daemin.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.daemin.common.Common;
import com.daemin.common.CurrentTime;
import com.daemin.dialog.DialSchedule;
import com.daemin.enumclass.User;
import com.daemin.main.MainActivity;

public class Widget5_5 extends AppWidgetProvider {
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		registerAlarm(context);
		User.INFO.getEditor().putBoolean("widget5_5", true).commit();

	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Log.i("widget", "service end");
		unregisterAlarm(context);
		context.stopService(new Intent(context, WidgetUpdateService.class));
		User.INFO.getEditor().putBoolean("widget5_5", false).commit();
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
				init.putExtra("action","update5_5");
				context.startService(init);
				break;
			case Common.ACTION_WEEK5_5:
				Intent week = new Intent(context, WidgetUpdateService.class);
				week.putExtra("action","week5_5");
				context.startService(week);
				break;
			case Common.ACTION_MONTH5_5:
				Intent month = new Intent(context, WidgetUpdateService.class);
				month.putExtra("action","month5_5");
				context.startService(month);
				break;
			case Common.ACTION_BACK5_5:
				Intent back = new Intent(context, WidgetUpdateService.class);
				back.putExtra("action","back5_5");
				context.startService(back);
				break;
			case Common.ACTION_FORWARD5_5:
				Intent forward = new Intent(context, WidgetUpdateService.class);
				forward.putExtra("action", "forward5_5");
				context.startService(forward);
				break;
			case Common.ACTION_DIAL5_5:
				Intent dial = new Intent(context, DialSchedule.class);
				dial.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				dial.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				dial.putExtra("widgetFlag",true);
				context.startActivity(dial);
				break;
			case Common.ACTION_HOME5_5:
				Intent main5 = new Intent(context, MainActivity.class);
				main5.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				main5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(main5);
				break;
		}
	}

	public static void registerAlarm(Context context)
	{
		Log.i("widget", "alarm register");
		Intent init = new Intent(context, WidgetUpdateService.class);
		init.putExtra("action","update5_5");
		PendingIntent sender
				= PendingIntent.getService(context, 0, init, PendingIntent.FLAG_ONE_SHOT);
		AlarmManager manager
				= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		/*Log.i("widget", String.valueOf(CurrentTime.getMidnight()));
		Log.i("widget", String.valueOf(CurrentTime.getNowMillis()));
		Log.i("widget", CurrentTime.getDatefromMillis(CurrentTime.getMidnight()));*/
		manager.setRepeating(AlarmManager.RTC_WAKEUP, CurrentTime.getMidnight()+5000, AlarmManager.INTERVAL_DAY, sender);
	}
	public static void unregisterAlarm(Context context)
	{
		Log.i("widget", "alarm unregister");
		Intent intent = new Intent();
		PendingIntent sender
				= PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager manager =
				(AlarmManager)context
						.getSystemService(Context.ALARM_SERVICE);
		manager.cancel(sender);
	}
}