package com.daemin.common;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;

import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-09-08.
 */
public class DialMonthPicker extends Dialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_monthpicker);
        setCancelable(true);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        layoutParams.width = dm.widthPixels*2/3;;
        layoutParams.height = dm.heightPixels/3;
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
                cancel();
            }
        });
    }
    Context context;
    public DialMonthPicker(Context context) {
        // Dialog 배경을 투명 처리 해준다.
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.context = context;
    }
    private void setNP() {
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
                if(npStartHour.getValue()==npEndHour.getValue()){
                    if(newVal==59){
                        npEndHour.setMinValue(npStartHour.getValue() + 1);
                        npEndHour.setValue(npStartHour.getValue() + 1);
                        npEndMin.setMinValue(0);
                        npEndMin.setMaxValue(59);
                        npEndMin.setValue(0);
                        if(npStartHour.getValue()+1==23){
                            npEndMin.setMinValue(0);
                            npEndMin.setMaxValue(0);
                            npEndMin.setValue(0);
                        }
                    }else {
                        npEndMin.setMinValue(newVal+1);
                        npEndMin.setMaxValue(59);
                        npEndMin.setValue(newVal+1);
                    }
                }else{
                    if(newVal==59) {
                        npEndHour.setMinValue(npStartHour.getValue() + 1);
                    }else{
                        npEndHour.setMinValue(npStartHour.getValue());
                    }
                    npEndMin.setMinValue(0);
                }
            }
        });
        npEndHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(npStartHour.getValue()==newVal){
                        npEndMin.setMinValue(npStartMin.getValue() + 1);
                        npEndMin.setMaxValue(59);
                }else{
                    npEndMin.setMinValue(0);
                    if(newVal==23){
                        npEndMin.setValue(0);
                        npEndMin.setMaxValue(0);
                    }else{
                        npEndMin.setMaxValue(59);
                        npEndMin.setValue(npStartMin.getValue());
                    }
                }

            }
        });
        npEndMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(npStartHour.getValue()==npEndHour.getValue()){
                    picker.setMinValue(npStartMin.getValue()+1);
                }else{
                    npEndMin.setMinValue(0);
                }
            }
        });
    }
    private void setLayout() {
        btDialCancel = (Button) findViewById(R.id.btDialCancel);
        btDialSetting = (Button) findViewById(R.id.btDialSetting);
        npStartHour = (NumberPicker) findViewById(R.id.npStartHour);
        npStartMin = (NumberPicker) findViewById(R.id.npStartMin);
        npEndHour = (NumberPicker) findViewById(R.id.npEndHour);
        npEndMin = (NumberPicker) findViewById(R.id.npEndMin);
    }
    private Button btDialCancel;
    private Button btDialSetting;
    private NumberPicker npStartHour;
    private NumberPicker npStartMin;
    private NumberPicker npEndHour;
    private NumberPicker npEndMin;

}
