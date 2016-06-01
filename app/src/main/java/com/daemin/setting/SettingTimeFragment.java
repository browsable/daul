package com.daemin.setting;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.daemin.common.BasicFragment;
import com.daemin.dialog.DialSettingWeekPicker;
import com.daemin.dialog.DialTextSizePicker;
import com.daemin.enumclass.User;
import com.daemin.event.BackKeyEvent;
import com.daemin.event.ChangeFragEvent;
import com.daemin.timetable.R;
import com.daemin.main.MainActivity;

import org.greenrobot.eventbus.EventBus;

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
        ibBack = MainActivity.getInstance().getIbBack();
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ChangeFragEvent(SettingFragment.class, "설정"));
                EventBus.getDefault().post(new BackKeyEvent("", new String[]{"ibMenu"}, new String[]{"ibBack"}));
            }
        });
        btSettingWeek = (RelativeLayout)root.findViewById(R.id.btSettingWeek);
        btSettingTime = (RelativeLayout)root.findViewById(R.id.btSettingTime);
        btSettingTextSize = (RelativeLayout)root.findViewById(R.id.btSettingTextSize);
        tvWeek = (TextView)root.findViewById(R.id.tvWeek);
        tvTime = (TextView)root.findViewById(R.id.tvTime);
        tvTextSize = (TextView)root.findViewById(R.id.tvTextSize);
        startTime = User.INFO.getStartTime();
        endTime = User.INFO.getEndTime();
        final int startDay = getResources().getIdentifier("day" + User.INFO.getStartDay(), "string", getActivity().getPackageName());
        final int endDay = getResources().getIdentifier("day"+User.INFO.getEndDay(),"string",getActivity().getPackageName());
        String week = getString(startDay)+" ~ " +getString(endDay);
        tvWeek.setText(week);
        String time = startTime+getString(R.string.hour)+" ~ " + endTime+getString(R.string.hour);
        tvTime.setText(time);
        tvTextSize.setText(User.INFO.getTextSize()+"");
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
                        final TimePickerDialog endTpd = new TimePickerDialog(getActivity(), R.style.MyDialogTheme, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endTime = hourOfDay;
                                if(startTime<endTime) {
                                    if (endTime-startTime>15){
                                        Toast.makeText(getActivity(), getString(R.string.setting_time_interval), Toast.LENGTH_SHORT).show();
                                        endTime = startTime+15;

                                    }
                                    tvTime.setText(startTime + getString(R.string.hour) + " ~ " + endTime + getString(R.string.hour));
                                    User.INFO.getEditor().putInt("startTime", startTime).commit();
                                    User.INFO.getEditor().putInt("endTime", endTime).commit();
                                    MainActivity.getInstance().getInitSurfaceView().setTime(startTime,endTime);
                                }else{
                                    if(startTime!=0&&endTime!=0)
                                        Toast.makeText(getActivity(), getString(R.string.setting_time_time_error), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, endTime, 0, false);
                        TextView tv = new TextView(getActivity());
                        tv.setText(getString(R.string.setting_time_end));
                        tv.setTextColor(getResources().getColor(android.R.color.white));
                        tv.setTypeface(null, Typeface.BOLD);
                        tv.setGravity(Gravity.CENTER);
                        tv.setBackgroundColor(getResources().getColor(R.color.maincolor));
                        endTpd.setCustomTitle(tv);
                        endTpd.show();
                        endTpd.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.btDialCancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == DialogInterface.BUTTON_NEGATIVE) {
                                    dialog.cancel();
                                }
                            }
                        });
                    }
                }, startTime, 0, false);
                startTpd.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.btDialCancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.cancel();
                        }
                    }
                });
                TextView tv = new TextView(getActivity());
                tv.setText(getString(R.string.setting_time_start));
                tv.setTextColor(getResources().getColor(android.R.color.white));
                tv.setTypeface(null, Typeface.BOLD);
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundColor(getResources().getColor(R.color.maincolor));
                startTpd.setCustomTitle(tv);
                startTpd.show();
            }
        });

        btSettingTextSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialTextSizePicker dtsp = new DialTextSizePicker(getActivity(),tvTextSize, User.INFO.getTextSize());
                dtsp.show();
            }
        });
        return root;
    }
    ImageButton ibBack;
    RelativeLayout btSettingWeek,btSettingTime,btSettingTextSize;
    TextView tvWeek, tvTime, tvTextSize;
    int startTime;
    int endTime;

}
