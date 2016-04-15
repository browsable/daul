package com.daemin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.daemin.common.Convert;
import com.daemin.enumclass.User;
import com.daemin.event.PostGroupListEvent;
import com.daemin.timetable.R;

import java.util.ArrayList;
import java.util.Collections;

import de.greenrobot.event.EventBus;

/**
 * Created by hernia on 2015-09-08.
 */
public class DialSettingWeekPicker extends Dialog {
    private Button btDialCancel;
    private Button btDialSetting;
    private int startDay, endDay;
    private ArrayList<String> startList, endList;
    private TextView tvWeek;
    ArrayAdapter<String> endAdapter;
    private Context context;
    public DialSettingWeekPicker(Context context, TextView tvWeek, int startDay, int endDay) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        startList = new ArrayList<>();
        endList = new ArrayList<>();
        Collections.addAll(startList, context.getResources().getStringArray(R.array.dayArray));
        this.context = context;
        this.tvWeek = tvWeek;
        this.startDay = startDay;
        this.endDay = endDay;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_setting_time_week);
        setCancelable(true);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        layoutParams.width = dm.widthPixels * 7/10;
        layoutParams.height = dm.heightPixels*3/7;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        btDialCancel = (Button) findViewById(R.id.btDialCancel);
        btDialSetting = (Button) findViewById(R.id.btDialSetting);
        final Spinner sStartDay = (Spinner)findViewById(R.id.sStartDay);
        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(context,
                R.layout.spinner, startList);
        startAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        sStartDay.setAdapter(startAdapter);
        sStartDay.setSelection(startDay);
        for(int i= startDay; i<7; ++i){
            endList.add(startList.get(i));
        }
        final Spinner sEndDay = (Spinner)findViewById(R.id.sEndDay);
        endAdapter = new ArrayAdapter<>(context,
                R.layout.spinner, endList);
        endAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        sEndDay.setAdapter(endAdapter);
        sEndDay.setSelection(endDay-startDay);


        sStartDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                startDay = position;
                endList.clear();
                for (int i = position; i < 7; ++i) {
                    endList.add(startList.get(i));
                }
                endAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        sEndDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                endDay= position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
                startDay = sStartDay.getSelectedItemPosition();
                endDay = Convert.DayOfWeekToInt(sEndDay.getSelectedItem().toString());
                int start = context.getResources().getIdentifier("day" + startDay, "string", context.getPackageName());
                int end = context.getResources().getIdentifier("day" + endDay, "string", context.getPackageName());
                String week = context.getString(start) + " ~ " + context.getString(end);
                tvWeek.setText(week);
                User.INFO.getEditor().putInt("startDay", startDay).commit();
                User.INFO.getEditor().putInt("endDay", endDay).commit();
                cancel();
            }
        });
    }
}
