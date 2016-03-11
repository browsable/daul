package com.daemin.event;

import android.content.Context;

import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-10-16.
 */
public class EditRepeatEvent {
    String repeatType;
    String repeatPeriod;
    String repeatNumber;
    int position;
    Context context;

    public int getPosition() {
        return position;
    }

    public EditRepeatEvent(Context context, String repeatType, String repeatPeriod, String repeatNumber, int position) {
        this.repeatType = repeatType;
        this.repeatPeriod = repeatPeriod;
        this.repeatNumber = repeatNumber;
        this.context = context;
        this.position = position;
    }
    public int getRepeatType(){
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
    @Override
    public String toString() {
        if(getRepeatType()==0||getRepeatType()==1)
            return getRepeatType()+"";
        else
            return getRepeatType()+":"+repeatPeriod+":"+repeatNumber;
    }
}
