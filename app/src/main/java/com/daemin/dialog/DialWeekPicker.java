package com.daemin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.event.UpdateNormalEvent;
import com.daemin.main.MainActivity;
import com.daemin.timetable.R;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by hernia on 2015-09-08.
 */
public class DialWeekPicker extends Dialog {
    private TextView tvDialStartTime;
    private TextView tvDialEndTime;
    private Button btDialCancel;
    private Button btDialSetting;
    private String xth, startHour, startMin, endHour, endMin;
    private NumberPicker npStartMin, npEndMin;
    private int position;
    public DialWeekPicker(Context context, int position, String xth, String startHour,String startMin, String endHour, String endMin) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.xth = xth;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
        this.position = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_weekpicker);
        setCancelable(true);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        layoutParams.width = dm.widthPixels * 2 / 3;
        layoutParams.height = dm.heightPixels*4/9;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        setLayout();
        tvDialStartTime.setText(startHour);
        tvDialEndTime.setText(endHour);
        npEndMin.setMaxValue(60);
        npEndMin.setMinValue(1);
        //npEndMin.setWrapSelectorWheel(false);
        if(endMin.equals("00")) npEndMin.setValue(60);
        else npEndMin.setValue(Integer.parseInt(endMin));
        npEndMin.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                int maxVal;
                if (i == 60) maxVal = 0;
                else maxVal = i;
                return String.format("%02d", maxVal);
            }
        });
        npStartMin.setMaxValue(npEndMin.getValue()-1);
        npStartMin.setMinValue(0);
        npStartMin.setValue(Integer.parseInt(startMin));
        npStartMin.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                int maxVal;
                if (i == 60) maxVal = 0;
                else maxVal = i;
                return String.format("%02d", maxVal);
            }
        });
        /*npStartMin.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                if(scrollState==SCROLL_STATE_IDLE){
                    Log.i("test","test");
                }
            }
        });*/
        npStartMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (startHour.equals(String.valueOf(Integer.parseInt(endHour) - 1))||startHour.equals(endHour)) {
                        if (newVal < 59) {
                            npEndMin.setMinValue(newVal + 1);
                        }
                }
            }
        });
        npEndMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (startHour.equals(endHour)) {
                    if (newVal == 60) {
                        tvDialEndTime.setText(String.valueOf(Integer.parseInt(endHour) + 1));
                    } else {
                        tvDialEndTime.setText(String.valueOf(endHour));
                    }
                    picker.setMinValue(npStartMin.getValue() + 1);
                    npStartMin.setMaxValue(newVal - 1);

                } else {
                    if (startHour.equals(String.valueOf(Integer.parseInt(endHour) - 1))&&startMin.equals(endMin) && startMin.equals("00") && endMin.equals("00")) {
                        if (newVal == 60) {
                            tvDialEndTime.setText(String.valueOf(endHour));
                        } else {
                            tvDialEndTime.setText(String.valueOf(Integer.parseInt(endHour) - 1));
                        }
                        picker.setMinValue(npStartMin.getValue() + 1);
                        npStartMin.setMaxValue(newVal - 1);
                    } else {
                        if (newVal == 60) {
                            tvDialEndTime.setText(endHour);
                        } else {
                            tvDialEndTime.setText(String.valueOf(Integer.parseInt(endHour) - 1));
                        }
                        picker.setMaxValue(60);
                        picker.setMinValue(1);
                        npStartMin.setMaxValue(59);
                        npStartMin.setMinValue(0);
                    }
                }
            }
        });
        btDialCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        btDialSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                npStartMin.clearFocus();
                npEndMin.clearFocus();
                weekSetting(Integer.parseInt(xth),
                        Integer.parseInt(tvDialStartTime.getText().toString()),
                        npStartMin.getValue(),
                        Integer.parseInt(tvDialEndTime.getText().toString()),
                        npEndMin.getValue()
                );

                cancel();
            }
        });
    }

    private void weekSetting(int xth, int startHour, int startMin, int endHour, int endMin) {
            if(startHour!=endHour) {
                if(endMin==0 || endMin==60) endMin=60;
                else ++endHour;
                TimePos[] tp = new TimePos[endHour - startHour];
                int j = 0;
                for (int i = startHour; i < endHour; i++) {
                    tp[j] = TimePos.valueOf(Convert.getxyMerge(xth, Convert.HourOfDayToYth(i)));
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
                TimePos tp = TimePos.valueOf(Convert.getxyMerge(xth, Convert.HourOfDayToYth(startHour)));
                tp.setMin(startMin, endMin);
                tp.setPosState(PosState.PAINT);
            }
            if(endMin==60)endMin=0;
            EventBus.getDefault().post(new UpdateNormalEvent(
                    tvDialStartTime.getText().toString(),
                Convert.IntToString(startMin),
                    tvDialEndTime.getText().toString(),
                Convert.IntToString(endMin),
                xth,position));
            cancel();
    }

    private void setLayout() {
        tvDialStartTime = (TextView) findViewById(R.id.tvDialStartTime);
        tvDialEndTime = (TextView) findViewById(R.id.tvDialEndTime);
        btDialCancel = (Button) findViewById(R.id.btDialCancel);
        btDialSetting = (Button) findViewById(R.id.btDialSetting);
        npStartMin = (NumberPicker) findViewById(R.id.npStartMin);
        npEndMin = (NumberPicker) findViewById(R.id.npEndMin);
        /*npStartMin.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npEndMin.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);*/
    }

}
