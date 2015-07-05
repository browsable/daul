package com.daemin.common;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.daemin.main.SubMainActivity;
import com.daemin.setting.SettingFragment;
import com.daemin.timetable.R;

public class BackPressCloseHandler {
 
    private long backKeyPressedTime = 0;
    private Toast toast;
    private ImageButton ibMenu,ibBack;
    private Activity activity;


    public BackPressCloseHandler(Activity context) {
        ibMenu = SubMainActivity.getInstance().getIbMenu();
        ibBack = SubMainActivity.getInstance().getIbBack();
        this.activity = context;
    }
 
    public void onBackPressed(String name) {
        switch (name) {
            case "SettingUnivFragment":
                SubMainActivity.getInstance().changeFragment(SettingFragment.class, "설정", R.color.maincolor);
                SubMainActivity.getInstance().setBackKeyName("");
                ibMenu.setVisibility(View.VISIBLE);
                ibBack.setVisibility(View.GONE);
                break;
            default:
                activity.finish();
                break;
        }
    }
    
}