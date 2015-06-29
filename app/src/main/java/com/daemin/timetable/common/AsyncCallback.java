package com.daemin.timetable.common;

/**
 * Created by hernia on 2015-06-09.
 */
public interface AsyncCallback<T> {
    public void onResult(T result);

    public void exceptionOccured(Exception e);

    public void cancelled();
}