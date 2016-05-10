package com.daemin.event;


/**
 * Created by hernia on 2015-10-16.
 */
public class ExcuteMethodEvent {
    String methodName;

    public String getMethodName() {
        return methodName;
    }

    public ExcuteMethodEvent(String methodName) {
        this.methodName = methodName;
    }
}
