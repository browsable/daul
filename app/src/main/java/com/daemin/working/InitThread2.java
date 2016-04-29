package com.daemin.working;

/**
 * Created by hernia on 2015-08-18.
 */
public abstract class InitThread2 extends Thread {
    public abstract void setRunning(boolean isLoop);
    public abstract void setDate();
    public abstract void getDownXY(int xth, int yth);
    public abstract void getMoveXY(int xth, int yth);
    public abstract void getActionUp();
    public abstract int getWidth();
    public abstract int getHeight();
}
