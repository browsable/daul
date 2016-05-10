package com.daemin.common;

import android.app.Activity;
import android.widget.Toast;

import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.setting.SettingFragment;
import com.daemin.timetable.R;

import org.greenrobot.eventbus.EventBus;

public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Activity activity;
    private Toast toast;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }
    public void onBackPressed(String name) {
        switch (name) {
            case "SettingFragment":
                    if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                        backKeyPressedTime = System.currentTimeMillis();
                        showGuide();
                        return;
                    }
                    if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                        activity.finish();
                        toast.cancel();
                    }
                break;
            case "SettingVerFragment":case "SettingGroupFragment":case "SettingInitFragment":
            case "SettingCalendarFragment": case "SettingTimeFragment":case "SettingOpenSrcFragment":
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class, "설정"));
                EventBus.getDefault().post(new BackKeyEvent("", new String[]{"ibMenu"}, new String[]{"ibBack"}));
                break;
            case "WritePostFragment":
                /*EventBus.getDefault().post(new ChangeFragEvent(CommunityFragment.class, "커뮤니티"));
                EventBus.getDefault().post(new BackKeyEvent(""));
                WritePostFragment.getInstance().initEditText();*/
                break;
            default:
                activity.finish();
                break;
            }
        }
        public void showGuide() {
            toast = Toast.makeText(activity, activity.getResources().getString(R.string.backpress)
                    , Toast.LENGTH_SHORT);
            toast.show();
        }
}