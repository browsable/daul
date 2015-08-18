package com.daemin.timetable;

/**
 * Created by hernia on 2015-08-18.
 */
public abstract class InitThread extends Thread {
    public abstract void setRunning(boolean isLoop);
    public abstract void getDownXY(int xth, int yth);
    public abstract void getMoveXY(int xth, int yth);
    public abstract int getWidth();
    public abstract int getHeight();
    public abstract void initScreen(int day);
    public abstract void setCurrentTime(DayOfWeekData dowd);
}
