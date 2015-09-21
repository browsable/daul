package com.daemin.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.main.SubMainActivity;
import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-09-08.
 */
public class DialWeekPicker extends Dialog {
    private TextView tvDailStartTime;
    private TextView tvDialEndTime;
    private Button btDialCancel;
    private Button btDialSetting;
    private String xth, startHour, endHour;
    private NumberPicker npStartMin, npEndMin;
    private int position;
    String[] MD;
    public DialWeekPicker(Context context, String xth, String startHour, String endHour, String[] MD, int position) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.xth = xth;
        this.startHour = startHour;
        if(startHour.equals(endHour)) this.endHour = String.valueOf(Integer.parseInt(endHour)+1);
        else this.endHour = endHour;
        this.MD = MD;
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
        ;
        layoutParams.height = dm.heightPixels / 3;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        setLayout();
        tvDailStartTime.setText(startHour);
        tvDialEndTime.setText(endHour);
        npStartMin.setMaxValue(59);
        npStartMin.setMinValue(0);
        npStartMin.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        npEndMin.setMaxValue(59);
        npEndMin.setMinValue(0);
        npEndMin.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        npStartMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            }
        });
        npEndMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal == 0) tvDialEndTime.setText(endHour);
                else {
                    tvDialEndTime.setText(String.valueOf(String.valueOf(Integer.parseInt(endHour) - 1)));
                    if (tvDailStartTime.getText().toString().equals(tvDialEndTime.getText().toString()))
                        picker.setMinValue(npStartMin.getValue() + 1);
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
                weekSetting(Integer.parseInt(xth),
                        Integer.parseInt(tvDailStartTime.getText().toString()),
                        npStartMin.getValue(),
                        Integer.parseInt(tvDialEndTime.getText().toString()),
                        npEndMin.getValue());

                cancel();
            }
        });
    }

    private void weekSetting(int xth, int startHour, int startMin, int endHour, int endMin) {
            if(startHour!=endHour) {
                if (endMin != 0) ++endHour;
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
                        tp[i].setPosState(PosState.ADJUST);
                    } else if (i == tpSize - 1) {
                        tp[i].setMin(0, endMin);
                        tp[i].setPosState(PosState.ADJUST);
                    } else {
                        tp[i].setPosState(PosState.PAINT);
                    }
                    Common.getTempTimePos().add(tp[i].name());
                }
            }else{
                TimePos tp = TimePos.valueOf(Convert.getxyMerge(xth, Convert.HourOfDayToYth(startHour)));
                tp.setMin(startMin,endMin);
                tp.setPosState(PosState.ADJUST);
            }
            SubMainActivity.getInstance()
                .updateListByDial(MD[(xth - 1) / 2],
                        tvDailStartTime.getText().toString(),
                        Convert.IntToString(npStartMin.getValue()),
                        tvDialEndTime.getText().toString(),
                        Convert.IntToString(npEndMin.getValue()),
                        xth,position);
            cancel();
    }

    private void setLayout() {
        tvDailStartTime = (TextView) findViewById(R.id.tvDialStartTime);
        tvDialEndTime = (TextView) findViewById(R.id.tvDialEndTime);
        btDialCancel = (Button) findViewById(R.id.btDialCancel);
        btDialSetting = (Button) findViewById(R.id.btDialSetting);
        npStartMin = (NumberPicker) findViewById(R.id.npStartMin);
        npEndMin = (NumberPicker) findViewById(R.id.npEndMin);
    }

}
