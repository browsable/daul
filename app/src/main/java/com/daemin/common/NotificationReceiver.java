package com.daemin.common;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.daemin.main.MainActivity;
import com.daemin.main.SplashActivity;
import com.daemin.timetable.R;


/**
 * Created by user on 2016-01-08.
 */
public class NotificationReceiver extends BroadcastReceiver {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(Common.ALARM_PUSH)){
            String title = intent.getStringExtra("title");
            String place = intent.getStringExtra("place");
            String memo = intent.getStringExtra("memo");
            Intent in = new Intent(context, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, in, PendingIntent.FLAG_CANCEL_CURRENT);
            Notification.Builder builder = new Notification.Builder(context);
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                int color = context.getResources().getColor(R.color.alarmcolor);
                builder.setColor(color);
            }
            builder.setSmallIcon(R.drawable.ic_app_alarm);
            // 알림이 출력될 때 상단에 나오는 문구.
            builder.setTicker(title);
            // 알림 출력 시간.
            builder.setWhen(System.currentTimeMillis());
            // 알림 제목.
            builder.setContentTitle(title);
            // 알림 내용.
            builder.setContentText(place + "\n" + memo);
            // 알림시 사운드, 진동, 불빛을 설정 가능.
            builder.setDefaults(Notification.DEFAULT_ALL);
            // 알림 터치시 반응.
            builder.setContentIntent(pendingIntent);
            // 알림 터치시 반응 후 알림 삭제 여부.
            builder.setAutoCancel(true);
            builder.setPriority(Notification.PRIORITY_MAX);
            Notification.BigTextStyle style = new Notification.BigTextStyle(builder);
            style.setBigContentTitle(title);
            style.bigText(place+"\n"+memo);
            builder.setStyle(style);
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(0, builder.build());
        }
    }
}
