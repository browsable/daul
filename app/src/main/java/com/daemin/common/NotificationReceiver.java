package com.daemin.common;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by user on 2016-01-08.
 */
public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(Common.ALARM_PUSH)){
            Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();
        }
    }
}
