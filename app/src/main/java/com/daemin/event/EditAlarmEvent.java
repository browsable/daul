package com.daemin.event;

import android.widget.TextView;

/**
 * Created by hernia on 2015-10-16.
 */
public class EditAlarmEvent {
    String alarmType;
    TextView tvAlarmType;
    int position;

    public String getAlarmType() {
        return alarmType;
    }

    public TextView getTvAlarmType() {
        return tvAlarmType;
    }

    public int getPosition() {
        return position;
    }
    public EditAlarmEvent(TextView tvAlarmType, String alarmType, int position) {
        this.tvAlarmType = tvAlarmType;
        this.alarmType = alarmType;
        this.position = position;
    }
}
