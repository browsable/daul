package com.daemin.event;

/**
 * Created by hernia on 2015-10-16.
 */
public class SetRepeatEvent {
    String repeatType;
    int repeatPeriod;
    int repeatNumber;

    public String getRepeatType() {
        return repeatType;
    }

    public int getRepeatPeriod() {
        return repeatPeriod;
    }

    public int getRepeatNumber() {
        return repeatNumber;
    }

    @Override
    public String toString() {
        return repeatType + ':'
                + repeatPeriod
                +"마다"+
                repeatNumber+
                '}';
    }

    public SetRepeatEvent(String repeatType, int repeatPeriod, int repeatNumber) {

        this.repeatType = repeatType;
        this.repeatPeriod = repeatPeriod;
        this.repeatNumber = repeatNumber;
    }
}
