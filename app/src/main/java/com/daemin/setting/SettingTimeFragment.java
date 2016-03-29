package com.daemin.setting;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.daemin.common.BasicFragment;
import com.daemin.dialog.DialSettingWeekPicker;
import com.daemin.enumclass.User;
import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.timetable.R;
import com.daemin.working.MainActivity2;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

public class SettingTimeFragment extends BasicFragment {
    public SettingTimeFragment() {
        super(R.layout.fragment_setting_time, "SettingTimeFragment");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = super.onCreateView(inflater, container, savedInstanceState);
        EventBus.getDefault().post(new BackKeyEvent("SettingTimeFragment", new String[]{"ibBack"}, new String[]{"ibMenu"}));
        ibBack = MainActivity2.getInstance().getIbBack();
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class, "설정"));
                EventBus.getDefault().post(new BackKeyEvent("", new String[]{"ibMenu"}, new String[]{"ibBack"}));
            }
        });
        btSettingWeek = (RelativeLayout)root.findViewById(R.id.btSettingWeek);
        btSettingTime = (RelativeLayout)root.findViewById(R.id.btSettingTime);
        tvWeek = (TextView)root.findViewById(R.id.tvWeek);
        tvTime = (TextView)root.findViewById(R.id.tvTime);
        int startDay = getResources().getIdentifier("day" + User.INFO.getStartDay(), "string", getActivity().getPackageName());
        final int endDay = getResources().getIdentifier("day"+User.INFO.getEndDay(),"string",getActivity().getPackageName());
        String week = getString(startDay)+" ~ " +getString(endDay);
        tvWeek.setText(week);
        String time = User.INFO.getStartTime()+getString(R.string.hour)+" ~ " + User.INFO.getEndTime()+getString(R.string.hour);
        tvTime.setText(time);
        btSettingWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialSettingWeekPicker dswp = new DialSettingWeekPicker(getActivity(), tvWeek, User.INFO.getStartDay(), User.INFO.getEndDay());
                dswp.show();
            }
        });
        btSettingTime.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onClick(View v) {
                TimePickerDialog startTpd = new TimePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, startTimePikcerListener, 8, 0, false);
                startTpd.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.setting_time_next), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TimePickerDialog endTpd = new TimePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, endTimePikcerListener, 23, 0, false);
                        TextView tv = new TextView(getActivity());
                        tv.setText(getString(R.string.setting_time_end));
                        tv.setTextColor(getResources().getColor(android.R.color.white));
                        tv.setTypeface(null, Typeface.BOLD);
                        //tv.setTextSize(R.dimen.textsize_l);
                        tv.setGravity(Gravity.CENTER);
                        tv.setBackgroundColor(getResources().getColor(R.color.timepicker_title));
                        endTpd.setCustomTitle(tv);
                        endTpd.show();
                    }
                });
                TextView tv = new TextView(getActivity());
                tv.setText(getString(R.string.setting_time_start));
                tv.setTextColor(getResources().getColor(android.R.color.white));
                tv.setTypeface(null, Typeface.BOLD);
                //tv.setTextSize(R.dimen.textsize_l);
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundColor(getResources().getColor(R.color.timepicker_title));
                startTpd.setCustomTitle(tv);
                startTpd.show();
            }
        });

        return root;
    }
    protected TimePickerDialog.OnTimeSetListener startTimePikcerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        }
    };
    protected TimePickerDialog.OnTimeSetListener endTimePikcerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        }
    };
    ImageButton ibBack;
    RelativeLayout btSettingWeek,btSettingTime;
    TextView tvWeek, tvTime;

}
