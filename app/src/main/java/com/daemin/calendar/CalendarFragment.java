package com.daemin.calendar;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daemin.common.BasicFragment;
import com.daemin.main.SubMainActivity;
import com.daemin.timetable.R;
import com.daemin.timetable.TimetableFragment;

/**
 * Created by hernia on 2015-06-20.
 */
public class CalendarFragment extends BasicFragment {
    public CalendarFragment() {
        super(R.layout.fragment_calendar, "CalendarFragment");
    }
    SubMainActivity submain;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = super.onCreateView(inflater, container, savedInstanceState);
        submain = SubMainActivity.getInstance();
        if (layoutId > 0) {
            submain.getIbMenu().setVisibility(View.GONE);
            submain.getIbBack().setVisibility(View.VISIBLE);
            submain.setBackKeyName("CalendarFragment");
        }
        SubMainActivity.getInstance().getIbBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submain.getIbMenu().setVisibility(View.VISIBLE);
                submain.getIbBack().setVisibility(View.GONE);
                submain.getLlTitle().setVisibility(View.VISIBLE);
                submain.getTvTitle().setVisibility(View.GONE);
                submain.getBtPlus().setVisibility(View.VISIBLE);
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
            }
        });
        return root;
    }
}
