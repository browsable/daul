package com.daemin.setting;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.daemin.working.WeekTableThread;

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
        //ibBack = MainActivity.getInstance().getIbBack();
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
        final int startDay = getResources().getIdentifier("day" + User.INFO.getStartDay(), "string", getActivity().getPackageName());
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
                TimePickerDialog startTpd = new TimePickerDialog(getActivity(), R.style.MyDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        startTime = hourOfDay;
                    }
                }, 8, 0, false);
                TextView tv = new TextView(getActivity());
                tv.setText(getString(R.string.setting_time_start));
                tv.setTextColor(getResources().getColor(android.R.color.white));
                tv.setTypeface(null, Typeface.BOLD);
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundColor(getResources().getColor(R.color.maincolor));
                startTpd.setCustomTitle(tv);
                startTpd.show();

                startTpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        final TimePickerDialog endTpd = new TimePickerDialog(getActivity(), R.style.MyDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endTime = hourOfDay;
                            }
                        }, 23, 0, false);
                        TextView tv = new TextView(getActivity());
                        tv.setText(getString(R.string.setting_time_end));
                        tv.setTextColor(getResources().getColor(android.R.color.white));
                        tv.setTypeface(null, Typeface.BOLD);
                        tv.setGravity(Gravity.CENTER);
                        tv.setBackgroundColor(getResources().getColor(R.color.maincolor));
                        endTpd.setCustomTitle(tv);
                        endTpd.show();
                        endTpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if(startTime<endTime) {
                                    if (endTime-startTime>15){
                                        int endTime = startTime+15;
                                        User.INFO.getEditor().putInt("startTime", startTime).commit();
                                        User.INFO.getEditor().putInt("endTime", endTime).commit();
                                        Toast.makeText(getActivity(), getString(R.string.setting_time_interval), Toast.LENGTH_SHORT).show();
                                        tvTime.setText(startTime + getString(R.string.hour) + " ~ " + endTime + getString(R.string.hour));
                                    }
                                    else{
                                        User.INFO.getEditor().putInt("startTime", startTime).commit();
                                        User.INFO.getEditor().putInt("endTime", endTime).commit();
                                        tvTime.setText(startTime + getString(R.string.hour) + " ~ " + endTime + getString(R.string.hour));
                                    }
                                }else{
                                    Toast.makeText(getActivity(), getString(R.string.setting_time_time_error), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
            }
        });

        return root;
    }
    ImageButton ibBack;
    RelativeLayout btSettingWeek,btSettingTime;
    TextView tvWeek, tvTime;
    int startTime;
    int endTime;

}
