package com.daemin.common;

import android.app.Activity;
import android.view.View;

import com.daemin.main.SubMainActivity;
import com.daemin.setting.SettingFragment;
import com.daemin.timetable.R;
import com.daemin.timetable.TimetableFragment;

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
            case "CalendarFragment":
                submain.getIbMenu().setVisibility(View.VISIBLE);
                submain.getIbBack().setVisibility(View.GONE);
                submain.getLlTitle().setVisibility(View.VISIBLE);
                submain.getTvTitle().setVisibility(View.GONE);
                submain.getBtPlus().setVisibility(View.GONE);
                submain.getIbCalendar().setVisibility(View.VISIBLE);
                submain.getmLayout().setVisibility(View.VISIBLE);
                submain.getmLayout().setTouchEnabled(true);
                submain.changeFragment(TimetableFragment.class, "", R.color.maincolor);
                submain.getFlSurface().setVisibility(View.VISIBLE);
                submain.getFrame_container().setVisibility(View.GONE);
                if (submain.getSurfaceFlag()) {
                    submain.getInitSurfaceView().surfaceCreated(submain.getInitSurfaceView().getHolder());
                    submain.setSurfaceFlag(false);
                }
                submain.setBackKeyName("");
                break;
            default:
                activity.finish();
                break;
        }
    }
    
}