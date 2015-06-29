package com.daemin.timetable.common;

/**
 * Created by hernia on 2015-06-09.
 */
public interface AsyncExecutorAware<T> {

    void setAsyncExecutor(AsyncExecutor<T> asyncExecutor);

}