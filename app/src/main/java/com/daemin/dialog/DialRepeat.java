package com.daemin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daemin.event.EditRepeatEvent;
import com.daemin.event.SetRepeatEvent;
import com.daemin.timetable.R;

import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by hernia on 2015-09-08.
 */
public class DialRepeat extends Dialog {

    public DialRepeat(Context context, HashMap<Integer, Integer> dayIndex) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.context = context;
        this.dayIndex = dayIndex;
        this.repeat="";
    }

    public DialRepeat(Context context, HashMap<Integer, Integer> dayIndex, String repeat, int position) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.context = context;
        this.dayIndex = dayIndex;
        this.repeat = repeat;
        this.position = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_repeat);
        setCancelable(true);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        layoutParams.width = dm.widthPixels * 7/ 9;
        layoutParams.height = dm.heightPixels * 2 / 3;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        setLayout();
    }

    private void setLayout() {
        btDialCancel = (Button) findViewById(R.id.btDialCancel);
        btDialSetting = (Button) findViewById(R.id.btDialSetting);
        etTerm = (EditText) findViewById(R.id.etTerm);
        etWeek = (EditText) findViewById(R.id.etWeek);
        etMonth = (EditText) findViewById(R.id.etMonth);
        etYear = (EditText) findViewById(R.id.etYear);
        repeatGroup = (RadioGroup) findViewById(R.id.repeatGroup);
        repeat_radio0 = (RadioButton) findViewById(R.id.repeat_radio0);
        repeat_radio1 = (RadioButton) findViewById(R.id.repeat_radio1);
        repeat_radio2 = (RadioButton) findViewById(R.id.repeat_radio2);
        repeat_radio3 = (RadioButton) findViewById(R.id.repeat_radio3);
        repeat_radio4 = (RadioButton) findViewById(R.id.repeat_radio4);
        llTerm = (LinearLayout) findViewById(R.id.llTerm);
        llWeek = (LinearLayout) findViewById(R.id.llWeek);
        llMonth = (LinearLayout) findViewById(R.id.llMonth);
        llYear = (LinearLayout) findViewById(R.id.llYear);
        tvSun = (TextView) findViewById(R.id.tvSun);
        tvMon = (TextView) findViewById(R.id.tvMon);
        tvTue = (TextView) findViewById(R.id.tvTue);
        tvWed = (TextView) findViewById(R.id.tvWed);
        tvThr = (TextView) findViewById(R.id.tvThr);
        tvFri = (TextView) findViewById(R.id.tvFri);
        tvSat = (TextView) findViewById(R.id.tvSat);
        if(!repeat.equals("")) {
            if(repeat.equals("0")){//반복없음
                repeatGroup.check(repeat_radio0.getId());
            }else if(repeat.equals("1")){
                repeatGroup.check(repeat_radio1.getId());
            }else{
                String s[];
                s = repeat.split(":");
                repeatType = s[0];
                repeatPeriod = s[1];
                repeatNumber = s[2];
                switch (repeatType){
                    case "2":
                        repeatGroup.check(repeat_radio2.getId());
                        llWeek.setVisibility(View.VISIBLE);
                        llTerm.setVisibility(View.VISIBLE);
                        etWeek.setText(repeatPeriod);
                        etTerm.setText(repeatNumber);
                        etTerm.setFocusable(true);
                        break;
                    case "3":
                        repeatGroup.check(repeat_radio3.getId());
                        llMonth.setVisibility(View.VISIBLE);
                        llTerm.setVisibility(View.VISIBLE);
                        etMonth.setText(repeatPeriod);
                        etTerm.setFocusable(true);
                        etTerm.setText(repeatNumber);

                        break;
                    case "4":
                        repeatGroup.check(repeat_radio4.getId());
                        llYear.setVisibility(View.VISIBLE);
                        llTerm.setVisibility(View.VISIBLE);
                        etYear.setText(repeatPeriod);
                        etTerm.setFocusable(true);
                        etTerm.setText(repeatNumber);
                        break;
                }
                etTerm.requestFocus();
            }
        }
        for (int i : dayIndex.keySet()) {
            switch (i) {
                case 1:
                    tvSun.setTextColor(Color.WHITE);
                    tvSun.setBackgroundResource(R.drawable.bg_circle_maincolor);
                    break;
                case 3:
                    tvMon.setTextColor(Color.WHITE);
                    tvMon.setBackgroundResource(R.drawable.bg_circle_maincolor);
                    break;
                case 5:
                    tvTue.setTextColor(Color.WHITE);
                    tvTue.setBackgroundResource(R.drawable.bg_circle_maincolor);
                    break;
                case 7:
                    tvWed.setTextColor(Color.WHITE);
                    tvWed.setBackgroundResource(R.drawable.bg_circle_maincolor);
                    break;
                case 9:
                    tvThr.setTextColor(Color.WHITE);
                    tvThr.setBackgroundResource(R.drawable.bg_circle_maincolor);
                    break;
                case 11:
                    tvFri.setTextColor(Color.WHITE);
                    tvFri.setBackgroundResource(R.drawable.bg_circle_maincolor);
                    break;
                case 13:
                    tvSat.setTextColor(Color.WHITE);
                    tvSat.setBackgroundResource(R.drawable.bg_circle_maincolor);
                    break;
            }
        }

        etWeek.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                llTerm.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                llTerm.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                llTerm.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        repeatGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                etTerm.setText("");
                RadioButton rb = (RadioButton) findViewById(checkedId);
                repeatType = rb.getText().toString();
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.repeat_radio0:
                        llTerm.setVisibility(View.INVISIBLE);
                        llWeek.setVisibility(View.INVISIBLE);
                        llMonth.setVisibility(View.INVISIBLE);
                        llYear.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.repeat_radio1:
                        llTerm.setVisibility(View.INVISIBLE);
                        llWeek.setVisibility(View.INVISIBLE);
                        llMonth.setVisibility(View.INVISIBLE);
                        llYear.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.repeat_radio2:
                        etWeek.setText("");
                        etWeek.setFocusable(true);
                        llWeek.setVisibility(View.VISIBLE);
                        llTerm.setVisibility(View.INVISIBLE);
                        llMonth.setVisibility(View.INVISIBLE);
                        llYear.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.repeat_radio3:
                        etMonth.setText("");
                        etMonth.setFocusable(true);
                        llTerm.setVisibility(View.INVISIBLE);
                        llWeek.setVisibility(View.INVISIBLE);
                        llYear.setVisibility(View.INVISIBLE);
                        llMonth.setVisibility(View.VISIBLE);
                        break;
                    case R.id.repeat_radio4:
                        etYear.setText("");
                        etYear.setFocusable(true);
                        llTerm.setVisibility(View.INVISIBLE);
                        llWeek.setVisibility(View.INVISIBLE);
                        llMonth.setVisibility(View.INVISIBLE);
                        llYear.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        btDialSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int limitNum = 0;//횟수제한
                if (repeatGroup.getCheckedRadioButtonId() != -1) {
                    int id = repeatGroup.getCheckedRadioButtonId();
                    View radioButton = repeatGroup.findViewById(id);
                    int radioId = repeatGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) repeatGroup.getChildAt(radioId);
                    switch (radioId) {
                        case 0:
                            repeatType = (String) btn.getText();
                            repeatPeriod = "";
                            repeatNumber = "";
                            break;
                        case 1:
                            repeatType = (String) btn.getText();
                            repeatPeriod = "";
                            repeatNumber = "";
                            break;
                        case 2:
                            repeatPeriod = etWeek.getText().toString();
                            repeatType = context.getResources().getString(R.string.everyweek);
                            if (!repeatPeriod.equals(""))
                                limitNum = 7 * Integer.parseInt(repeatPeriod);
                            break;
                        case 3:
                            repeatPeriod = etMonth.getText().toString();
                            repeatType = context.getResources().getString(R.string.everymonth);
                            if (!repeatPeriod.equals(""))
                                limitNum = 7 * 4 * Integer.parseInt(repeatPeriod);
                            break;
                        case 4:
                            repeatPeriod = etYear.getText().toString();
                            repeatType = context.getResources().getString(R.string.everyyear);
                            if (!repeatPeriod.equals(""))
                                limitNum = 7 * 4 * 12 * Integer.parseInt(repeatPeriod);
                            break;
                    }
                    String term = etTerm.getText().toString();
                    repeatNumber = term;
                    if(!repeatType.equals(context.getResources().getString(R.string.repeat_radio0))
                            &&!repeatType.equals(context.getResources().getString(R.string.repeat_radio1))){
                        if (repeatPeriod.equals("") || repeatNumber.equals(""))
                            Toast.makeText(context, context.getResources().getString(R.string.input_measure), Toast.LENGTH_SHORT).show();
                        else {
                            if (Integer.parseInt(repeatNumber) * limitNum <= 365 * 5) {
                                if(repeat.equals(""))
                                    EventBus.getDefault().post(new SetRepeatEvent(context,repeatType, repeatPeriod, repeatNumber));
                                else
                                    EventBus.getDefault().post(new EditRepeatEvent(context, repeatType, repeatPeriod, repeatNumber,position));
                                cancel();
                            } else {
                                Toast.makeText(context,context.getResources().getString(R.string.repeat_term), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        EventBus.getDefault().post(new SetRepeatEvent(context, repeatType, repeatPeriod, repeatNumber));
                        cancel();
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
    }

    Context context;
    HashMap<Integer, Integer> dayIndex;
    String repeat;
    RadioButton repeat_radio0,repeat_radio1,repeat_radio2,repeat_radio3,repeat_radio4;
    private int position;
    private Button btDialCancel, btDialSetting;
    private RadioGroup repeatGroup;
    private LinearLayout llTerm, llWeek, llMonth, llYear;
    private EditText etTerm, etWeek, etMonth, etYear;
    private TextView tvSun, tvMon, tvTue, tvWed, tvThr, tvFri, tvSat;
    private String repeatType, repeatPeriod, repeatNumber;
}
