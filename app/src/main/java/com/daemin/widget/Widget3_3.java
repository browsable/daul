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
import android.util.Log;
import android.widget.RemoteViews;

import com.daemin.common.Common;
import com.daemin.timetable.R;

public class Widget3_3 extends AppWidgetProvider {
	
	private static final String TAG = "Widget3_3";
	private Context context;
	
	@Override
	public void onEnabled(Context context) {
		Log.i(TAG, "======================= onEnabled() =======================");
		super.onEnabled(context);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		

		this.context = context;
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		// To prevent any ANR timeouts, we perform the update in a service
		context.startService(new Intent(context, UpdateService.class));
		        
		for(int i=0; i<appWidgetIds.length; i++){ 
			int appWidgetId = appWidgetIds[i];
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
			appWidgetManager.updateAppWidget(appWidgetId, views);
		} 
    }
	
	
	public static class UpdateService extends Service {
		 @Override
	        public void onStart(Intent intent, int startId) {

	            // Build the widget update for today
	            RemoteViews updateViews = buildUpdate(this);

	            // Push update for this widget to the home screen
	            ComponentName thisWidget = new ComponentName(this, Widget3_3.class);
	            AppWidgetManager manager = AppWidgetManager.getInstance(this);
	            manager.updateAppWidget(thisWidget, updateViews);
	        }
		 	
		 @Override
	        public IBinder onBind(Intent intent) {
	            return null;
	        }
		 public RemoteViews buildUpdate(Context context) {
			 
			
	            RemoteViews views = null;
	
	            views = new RemoteViews(context.getPackageName(), R.layout.widget);
	            String imgFile = Environment.getExternalStorageDirectory().toString() + "/.TimeDAO/timetable.jpg";

	            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile);
	      
	                if (myBitmap != null){
	                	views.setImageViewBitmap(R.id.timetableimage, myBitmap);
	                }
	                
	            return views;
		 }
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		Log.i(TAG, "======================= onDeleted() =======================");
		super.onDeleted(context, appWidgetIds);
	}
	
	@Override
	public void onDisabled(Context context) {
		Log.i(TAG, "======================= onDisabled() =======================");
		super.onDisabled(context);
	}
	
	/**
	 * UI 설정 이벤트 설정
	 */
	public void initUI(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.i(TAG, "======================= initUI() =======================");
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
		 String imgFile = Environment.getExternalStorageDirectory().toString() + "/.TimeDAO/timetable.jpg";

         Bitmap myBitmap = BitmapFactory.decodeFile(imgFile);
   
             if (myBitmap != null){
             	views.setImageViewBitmap(R.id.timetableimage, myBitmap);
             }
             
		Intent activityIntent 			= new Intent(Common.ACTION_CALL_ACTIVITY);
		PendingIntent activityPIntent 		= PendingIntent.getBroadcast(context, 0, activityIntent	, 0);

		//views.setOnClickPendingIntent(R.id.btn_event          	, eventPIntent);
		views.setOnClickPendingIntent(R.id.mywidget, activityPIntent);
		//views.setOnClickPendingIntent(R.id.btn_set_alram        , dialogPIntent);
 
		for(int appWidgetId : appWidgetIds) {
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

	/**
	 * Receiver 수신
	 */
	@Override
	public void onReceive(Context context, Intent intent) { 
		super.onReceive(context, intent);
		
		String action = intent.getAction();
		Log.d(TAG, "onReceive() action = " + action);
		
		// Default Recevier
		if(AppWidgetManager.ACTION_APPWIDGET_ENABLED.equals(action)){
			
		}
		else if(AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)){
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			initUI(context, manager, manager.getAppWidgetIds(new ComponentName(context, getClass())));
		}
		else if(AppWidgetManager.ACTION_APPWIDGET_DELETED.equals(action)){
			
		}
		else if(AppWidgetManager.ACTION_APPWIDGET_DISABLED.equals(action)){
			
		}
		
		// Custom Recevier
		/*else if(Const.ACTION_EVENT.equals(action)){
			Toast.makeText(context, "Receiver 수신 완료!!.", Toast.LENGTH_SHORT).show();
		}*/
		else if(Common.ACTION_CALL_ACTIVITY.equals(action)){
			callActivity(context);
		}
		else if(Common.ACTION_UPDATE.equals(action)){
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			initUI(context, manager, manager.getAppWidgetIds(new ComponentName(context, getClass())));
		}
		
		/*
		else if(Const.ACTION_DIALOG.equals(action)){
			createDialog(context);
		}*/
	}
	
	
	private void callActivity(Context context){  
		Log.d(TAG, "callActivity()");
		Intent intent = new Intent("com.daemin.timetable.widget.CALL_ACTIVITY");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent); 
	}
	/*
	private void createDialog(Context context){
		Log.d(TAG, "createDialog()");
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		 
        Intent Intent = new Intent("com.daemin.widget.CALL_PROGRESSDIALOG");
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, Intent, 0);
         
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), pIntent);
	}*/
	
}
