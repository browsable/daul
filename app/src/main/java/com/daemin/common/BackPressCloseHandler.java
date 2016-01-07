package com.daemin.common;

import android.app.Activity;

import com.daemin.community.CommunityFragment;
import com.daemin.community.WritePostFragment;
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
            case "SettingInitFragment":
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class,"설정"));
                EventBus.getDefault().post(new BackKeyEvent("",new String[]{"ibMenu"},new String[]{"ibBack"}));
                //EventBus.getDefault().post(new ViewVisibleEvent(new String[]{"ibMenu"}));
                //EventBus.getDefault().post(new ViewGoneEvent(new String[]{"ibBack"}));
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