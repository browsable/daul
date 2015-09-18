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

import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-09-08.
 */
public class DialWeekPicker extends Dialog {
        private TextView tvDailStartTime;
        private TextView tvDialEndTime;
        private Button btDialCancel;
        private Button btDialSetting;
        private String startHour, startMin,endHour;
        private NumberPicker npWeekPicker;

        public DialWeekPicker(Context context, String startHour, String startMin, String endHour) {
            super(context , android.R.style.Theme_Holo_Light_Dialog);
            this.startHour = startHour;
            this.startMin = startMin;
            this.endHour = endHour;
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
            layoutParams.width = dm.widthPixels*2/3;;
            layoutParams.height = dm.heightPixels/3;
            window.setAttributes(layoutParams);
            window.setGravity(Gravity.CENTER);
            setLayout();
            tvDailStartTime.setText(startHour + " : " + startMin);
            tvDialEndTime.setText(endHour.split(":")[0] + " : ");
            npWeekPicker.setMaxValue(59);
            npWeekPicker.setMinValue(0);
            npWeekPicker.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int i) {
                    return String.format("%02d", i);
                }
            });
            npWeekPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    if (newVal == 0) tvDialEndTime.setText(endHour.split(":")[0] + " : ");
                    else
                        tvDialEndTime.setText(String.valueOf(Integer.parseInt(endHour.split(":")[0]) - 1) + " : ");
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
                    cancel();
                }
            });
        }

        private void setLayout() {
            tvDailStartTime = (TextView) findViewById(R.id.tvDialStartTime);
            tvDialEndTime = (TextView) findViewById(R.id.tvDialEndTime);
            btDialCancel = (Button) findViewById(R.id.btDialCancel);
            btDialSetting = (Button) findViewById(R.id.btDialSetting);
            npWeekPicker = (NumberPicker) findViewById(R.id.npWeekPicker);
        }

}
