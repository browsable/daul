package com.daemin.common;

import android.app.Activity;

import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.setting.SettingFragment;

import de.greenrobot.event.EventBus;

public class BackPressCloseHandler {

    private Activity activity;


    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }
 
    public void onBackPressed(String name) {
        switch (name) {
            case "SettingVerFragment":
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class,"설정"));
                EventBus.getDefault().post(new BackKeyEvent("",new String[]{"ibMenu"},new String[]{"ibBack"}));
                break;
            case "SettingInitFragment":
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class,"설정"));
                EventBus.getDefault().post(new BackKeyEvent("",new String[]{"ibMenu"},new String[]{"ibBack"}));
                break;
            case "SettingCalendarFragment":
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class,"설정"));
                EventBus.getDefault().post(new BackKeyEvent("",new String[]{"ibMenu"},new String[]{"ibBack"}));
                break;
            case "WritePostFragment" :
                /*EventBus.getDefault().post(new ChangeFragEvent(CommunityFragment.class, "커뮤니티"));
                EventBus.getDefault().post(new BackKeyEvent(""));
                WritePostFragment.getInstance().initEditText();*/
                break;
            default:
                activity.finish();
                break;
        }
    }
    
}