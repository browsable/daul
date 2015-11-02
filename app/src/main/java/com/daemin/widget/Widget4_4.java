package com.daemin.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.daemin.common.Common;
import com.daemin.common.CurrentTime;
import com.daemin.main.MainActivity;
import com.daemin.timetable.R;

public class Widget4_4 extends AppWidgetProvider {

	private static final String imgFile = Environment.getExternalStorageDirectory().toString() + "/.TimeDAO/timetable.jpg";
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
						 int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		// To prevent any ANR timeouts, we perform the update in a service
		context.startService(new Intent(context, UpdateService.class));
	}
	public static class UpdateService extends Service {
		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			AppWidgetManager manager = AppWidgetManager.getInstance(this);
			drawUI(this, manager, manager.getAppWidgetIds(new ComponentName(this, getClass())));
			return super.onStartCommand(intent, flags, startId);
		}
		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}
	}
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}
	public static void drawUI(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
		Bitmap myBitmap = BitmapFactory.decodeFile(imgFile);
		if (myBitmap != null){
			views.setImageViewBitmap(R.id.timetableimage, myBitmap);
		}
		views.setTextViewText(R.id.tvDate, CurrentTime.getYear() + context.getString(R.string.year)+" "+(CurrentTime.getTitleMonthWeek(context)));
		Intent mainIntent = new Intent(context, MainActivity.class);
		mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent mainPIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
		views.setOnClickPendingIntent(R.id.mywidget, mainPIntent);
		for(int appWidgetId : appWidgetIds) {
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
		myBitmap.recycle();
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		switch(intent.getAction()){
			case Common.ACTION_UPDATE:
				AppWidgetManager manager = AppWidgetManager.getInstance(context);
				drawUI(context, manager, manager.getAppWidgetIds(new ComponentName(context, getClass())));
				break;
		}
	}
}