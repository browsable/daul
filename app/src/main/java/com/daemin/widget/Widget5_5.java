package com.daemin.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.daemin.common.Common;

public class Widget5_5 extends AppWidgetProvider {
	Context context;
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
						 int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		this.context = context;
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
				Intent i = new Intent(context, WidgetUpdateService.class);
				i.putExtra("widget5_5", true);
				context.startService(i);
				break;
			case Common.ACTION_DIALOGFINISH:
				Intent init = new Intent(context, WidgetInitService.class);
				init.putExtra("widget5_5", true);
				context.startService(init);
				break;
			case Common.ACTION_DELAY:
				mHandler.sendEmptyMessageDelayed(0, 1000);
				break;
			case Common.ACTION_TIMEOUT:
				Intent intentToSendInterrupt = new Intent(context,ProgressDialogActivity.class);
				intentToSendInterrupt.setAction(Common.ACTION_TIMEOUT);
				try {
					PendingIntent.getActivity(context, 0, intentToSendInterrupt, 0).send();
				} catch (PendingIntent.CanceledException e) {
					e.printStackTrace();
				}
				break;

		}
	}
	public Handler mHandler = new Handler(){

		// onReceive()의 MAKE_DELAY_ACTION 처리부에 의해 호출됨
		public void handleMessage(Message msg){
			Intent intent = new Intent(context,Widget5_5.class);
			intent.setAction(Common.ACTION_TIMEOUT);
			try {
				PendingIntent.getBroadcast(context, 0, intent, 0).send();
			} catch (PendingIntent.CanceledException e) {
				e.printStackTrace();
			}
		}

	};
}