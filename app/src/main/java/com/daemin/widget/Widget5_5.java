package com.daemin.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.daemin.common.Common;

public class Widget5_5 extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
						 int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		// 앱위젯 처음 생성시만 실행됨
		Intent init = new Intent(context, WidgetInitService.class);
		init.putExtra("widget5_5", true);
		context.startService(init);
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		switch(intent.getAction()){
			case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
				Log.i("widget","update call");
				break;
			case Common.ACTION_UPDATE:
				// To prevent any ANR timeouts, we perform the update in a service
				Intent update = new Intent(context, WidgetUpdateService.class);
				update.putExtra("widget5_5", true);
				context.startService(update);
				break;
			case Common.ACTION_DIALOGFINISH:
				Intent init = new Intent(context, WidgetInitService.class);
				init.putExtra("widget5_5", true);
				context.startService(init);
				break;
			case Common.ACTION_REFRESH:
				Log.i("widget","refresh");
				Intent refresh = new Intent(context, WidgetRefreshService.class);
				refresh.putExtra("widget5_5", true);
				context.startService(refresh);
				break;
		}
	}
}