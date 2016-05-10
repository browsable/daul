package com.daemin.dialog;

/**
 * Created by hernia on 2015-09-08.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.data.BottomNormalData;
import com.daemin.enumclass.Dates;
import com.daemin.enumclass.DayOfMonthPos;
import com.daemin.enumclass.DayOfMonthPosState;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.event.SetTimeEvent;
import com.daemin.timetable.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hernia on 2015-09-08.
 */
public class DialAddTimePicker extends Dialog {
    Context context;
    String[] MD;
    TextView tvMD,tvTime;
    int dayIndex, startHour, startMin, endHour, endMin, position, timeType;
    private boolean editMode;
    private Button btDialCancel;
    private Button btDialSetting;
    private NumberPicker npMD;
    private NumberPicker npStartHour;
    private NumberPicker npStartMin;
    private NumberPicker npEndHour;
    private NumberPicker npEndMin;
    public DialAddTimePicker(Context context, String[] MD) {
        // Dialog 배경을 투명 처리 해준다.
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.context = context;
        this.MD = MD;
    }
    public DialAddTimePicker(Context context, int timeType, String[] MD,int dayIndex,TextView tvMD,TextView tvTime, String startHour, String startMin, String endHour, String endMin) {
        // Dialog 배경을 투명 처리 해준다.
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.context = context;
        this.MD = MD;
        this.dayIndex = dayIndex;
        this.tvMD = tvMD;
        this.tvTime = tvTime;
        this.startHour = Integer.parseInt(startHour);
        this.startMin = Integer.parseInt(startMin);
        this.endHour = Integer.parseInt(endHour);
        this.endMin = Integer.parseInt(endMin);
        this.editMode = true;
        this.timeType = timeType;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_addtimepicker);
        setCancelable(true);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        layoutParams.width = dm.widthPixels*5/6;
        layoutParams.height = dm.heightPixels*5/9;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        setLayout();
        setNP();

        btDialCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        btDialSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                npMD.clearFocus();
                npStartHour.clearFocus();
                npStartMin.clearFocus();
                npEndHour.clearFocus();
                npEndMin.clearFocus();
                if(editMode) {
                    int startHour = npStartHour.getValue();
                    int startMin = npStartMin.getValue();
                    int endHour = npEndHour.getValue();
                    int endMin = npEndMin.getValue();
                    int dayIndex;
                    if(timeType==0) {
                        dayIndex = Integer.parseInt(MD[npMD.getValue()].split("\\.")[1]);
                        tvMD.setText(Dates.NOW.month + context.getResources().getString(R.string.month)
                                + dayIndex
                                + context.getResources().getString(R.string.day));
                    }else{
                        dayIndex = 2 * npMD.getValue() + 1;
                        tvMD.setText(Convert.XthToDayOfWeek(dayIndex));
                    }
                    String time = Convert.IntToString(startHour) + ":"
                            + Convert.IntToString(startMin) + "~"
                            + Convert.IntToString(endHour) + ":"
                            + Convert.IntToString(endMin);
                    tvTime.setText(time);
                    EventBus.getDefault().post(new SetTimeEvent(timeType, position, dayIndex, startHour, startMin, endHour, endMin));
                }else{
                    if (MD.length == 7) {
                        weekSetting(npStartHour.getValue(), npStartMin.getValue(),
                                npEndHour.getValue(), npEndMin.getValue());
                    } else monthSetting(MD[npMD.getValue()]);
                }
                cancel();
            }
        });
    }

    private void setNP() {
        npMD.setMinValue(0);
        npMD.setMaxValue(MD.length - 1);
        npMD.setDisplayedValues(MD);
        npMD.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        npStartHour.setMaxValue(22);
        npStartHour.setMinValue(8);
        npStartHour.setValue(8);
        npStartMin.setMaxValue(59);
        npStartMin.setMinValue(0);
        npStartMin.setValue(0);
        npEndHour.setMaxValue(23);
        npEndHour.setMinValue(8);
        npEndHour.setValue(9);
        npEndMin.setMaxValue(59);
        npEndMin.setMinValue(0);
        npEndMin.setValue(0);
        npStartMin.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        npEndMin.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        npStartHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                npEndHour.setMinValue(newVal);
                npEndHour.setValue(newVal + 1);
                if (newVal == npEndHour.getValue()) {
                    npEndMin.setMinValue(1);
                    npEndMin.setValue(1);
                } else {
                    if (newVal == 22) {
                        npEndMin.setMaxValue(0);
                        npEndMin.setValue(0);
                    } else {
                        npEndMin.setMinValue(0);
                        npEndMin.setMaxValue(59);
                        npStartMin.setValue(0);
                        npEndMin.setValue(0);
                    }
                }
            }
        });
        npStartMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (npStartHour.getValue() == npEndHour.getValue()) {
                    if (newVal == 59) {
                        npEndHour.setMinValue(npStartHour.getValue() + 1);
                        npEndHour.setValue(npStartHour.getValue() + 1);
                        npEndMin.setMinValue(0);
                        npEndMin.setMaxValue(59);
                        npEndMin.setValue(0);
                        if (npStartHour.getValue() + 1 == 23) {
                            npEndMin.setMinValue(0);
                            npEndMin.setMaxValue(0);
                            npEndMin.setValue(0);
                        }
                    } else {
                        npEndMin.setMinValue(newVal + 1);
                        npEndMin.setMaxValue(59);
                        npEndMin.setValue(newVal + 1);
                    }
                } else {
                    if (newVal == 59) {
                        npEndHour.setMinValue(npStartHour.getValue() + 1);
                    } else {
                        npEndHour.setMinValue(npStartHour.getValue());
                    }
                    npEndMin.setMinValue(0);
                }
            }
        });
        npEndHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (npStartHour.getValue() == newVal) {
                    npEndMin.setMinValue(npStartMin.getValue() + 1);
                    npEndMin.setMaxValue(59);
                } else {
                    npEndMin.setMinValue(0);
                    if (newVal == 23) {
                        npEndMin.setValue(0);
                        npEndMin.setMaxValue(0);
                    } else {
                        npEndMin.setMaxValue(59);
                        npEndMin.setValue(npStartMin.getValue());
                    }
                }

            }
        });
        npEndMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (npStartHour.getValue() == npEndHour.getValue()) {
                    picker.setMinValue(npStartMin.getValue() + 1);
                } else {
                    npEndMin.setMinValue(0);
                }
            }
        });
        if(editMode){
            if(timeType==0)
                npMD.setValue(dayIndex-1);
            else
                npMD.setValue(dayIndex/2);
            npStartHour.setValue(startHour);
            npStartMin.setValue(startMin);
            npEndHour.setValue(endHour);
            npEndMin.setValue(endMin);
        }
    }
    private void setLayout() {
        btDialCancel = (Button) findViewById(R.id.btDialCancel);
        btDialSetting = (Button) findViewById(R.id.btDialSetting);
        npMD = (NumberPicker) findViewById(R.id.npMD);
        npStartHour = (NumberPicker) findViewById(R.id.npStartHour);
        npStartMin = (NumberPicker) findViewById(R.id.npStartMin);
        npEndHour = (NumberPicker) findViewById(R.id.npEndHour);
        npEndMin = (NumberPicker) findViewById(R.id.npEndMin);
        /*npStartHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npStartMin.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npEndHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npEndMin.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);*/
    }
    private void weekSetting(int startHour, int startMin, int endHour, int endMin){
        if(startHour!=endHour) {
            if(endMin==0) endMin=60;
            else ++endHour;
            TimePos[] tp = new TimePos[endHour - startHour];
            int j = 0;
            for (int i = startHour; i < endHour; i++) {
                tp[j] = TimePos.valueOf(Convert.getxyMerge(2 * npMD.getValue() + 1, Convert.HourOfDayToYth(i)));
                ++j;
            }
            int tpSize = tp.length;
            for (int i = 0; i < tpSize; i++) {
                if (i == 0) {
                    tp[i].setMin(startMin, 60);
                    tp[i].setPosState(PosState.PAINT);
                } else if (i == tpSize - 1) {
                    tp[i].setMin(0, endMin);
                    tp[i].setPosState(PosState.PAINT);
                } else {
                    tp[i].setPosState(PosState.PAINT);
                }
                if(!Common.getTempTimePos().contains(tp[i].name()))
                    Common.getTempTimePos().add(tp[i].name());
            }
        }else{
            TimePos tp = TimePos.valueOf(Convert.getxyMerge(2 * npMD.getValue() + 1, Convert.HourOfDayToYth(startHour)));
            tp.setMin(startMin, endMin);
            tp.setPosState(PosState.PAINT);
        }
        EventBus.getDefault().post(new BottomNormalData(MD[npMD.getValue()],
                String.valueOf(npStartHour.getValue()),
                Convert.IntToString(npStartMin.getValue()),
                String.valueOf(npEndHour.getValue()),
                Convert.IntToString(npEndMin.getValue()),
                2 * npMD.getValue() + 1));
        cancel();
    }
    private void monthSetting(String day) {
        String[] tmp = day.split("\\.");
        int dayCnt = Integer.parseInt(tmp[1]) + Dates.NOW.dayOfWeek;
        int xth = (dayCnt+1)%7;
        if(xth==0) xth=7;
        int yth = dayCnt/7+1;
        DayOfMonthPos DOMP = DayOfMonthPos.valueOf(Convert.getxyMergeForMonth(xth, yth));
        if (DOMP.getPosState() != DayOfMonthPosState.PAINT) {
            DOMP.setPosState(DayOfMonthPosState.PAINT);
            if(!Common.getTempTimePos().contains(DOMP.name()))
            Common.getTempTimePos().add(DOMP.name());
            EventBus.getDefault().post(new BottomNormalData(MD[npMD.getValue()],
                    String.valueOf(npStartHour.getValue()),
                    Convert.IntToString(npStartMin.getValue()),
                    String.valueOf(npEndHour.getValue()),
                    Convert.IntToString(npEndMin.getValue()),
                    xth));
            cancel();
        }
        /*else{
            Toast.makeText(context, context.getResources().getString(R.string.univ_overlap), Toast.LENGTH_SHORT).show();
        }*/
    }
}
