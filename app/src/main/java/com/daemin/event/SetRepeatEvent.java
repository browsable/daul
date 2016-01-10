package com.daemin.event;

import android.content.Context;

import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-10-16.
 */
public class SetRepeatEvent {
    String repeatType;
    String repeatPeriod;
    String repeatNumber;
    Context context;
    public SetRepeatEvent(Context context, String repeatType, String repeatPeriod, String repeatNumber) {
        this.repeatType = repeatType;
        this.repeatPeriod = repeatPeriod;
        this.repeatNumber = repeatNumber;
        this.context = context;
    }
    public int getRepeatType() {
        int type=0;
        if(repeatType.equals(context.getResources().getString(R.string.repeat_radio0))){
            type=0;
        }else if(repeatType.equals(context.getResources().getString(R.string.repeat_radio1))){
            type=1;
        }else if(repeatType.equals(context.getResources().getString(R.string.everyweek))){
            type=2;
        }else if(repeatType.equals(context.getResources().getString(R.string.everymonth))){
            type=3;
        }else if(repeatType.equals(context.getResources().getString(R.string.everyyear))){
            type=4;
        }
        return type;
    }

    public String getRepeatPeriod() {
        return repeatPeriod;
    }

    public String getRepeatNumber() {
        return repeatNumber;
    }

    @Override
    public String toString() {
        if(!repeatType.equals(context.getResources().getString(R.string.repeat_radio0))&&!repeatType.equals(context.getResources().getString(R.string.repeat_radio1))) repeatNumber= ":"+repeatNumber+context.getResources().getString(R.string.term);
        return repeatPeriod
                + repeatType+repeatNumber;
    }
}
