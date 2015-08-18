package com.daemin.common;

import android.app.Activity;
import android.view.View;

import com.daemin.main.SubMainActivity;
import com.daemin.setting.SettingFragment;
import com.daemin.timetable.R;

public class BackPressCloseHandler {

    private Activity activity;
    private SubMainActivity submain;


    public BackPressCloseHandler(Activity context) {
        submain = SubMainActivity.getInstance();
        this.activity = context;
    }
 
    public void onBackPressed(String name) {
        switch (name) {
            case "SettingUnivFragment":
                submain.changeFragment(SettingFragment.class, "설정", R.color.maincolor);
                submain.setBackKeyName("");
                submain.getIbMenu().setVisibility(View.VISIBLE);
                submain.getIbBack().setVisibility(View.GONE);
                break;
            default:
                activity.finish();
                break;
        }
    }
    
}