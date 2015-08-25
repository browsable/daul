package com.daemin.common;

import android.app.Activity;
import android.view.View;

import com.daemin.community.CommunityFragment2;
import com.daemin.community.WritePostFragment;
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
            case "WritePostFragment" :
                submain.changeFragment(CommunityFragment2.class, "커뮤니티", R.color.orange);
                submain.setBackKeyName("");
                WritePostFragment.getInstance().initEditText();
                break;
            default:
                activity.finish();
                break;
        }
    }
    
}