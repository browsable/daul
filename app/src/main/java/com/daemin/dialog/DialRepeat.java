package com.daemin.dialog;

import android.app.Dialog;
import android.content.Context;
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

import com.daemin.event.SetRepeatEvent;
import com.daemin.timetable.R;

import de.greenrobot.event.EventBus;

/**
 * Created by hernia on 2015-09-08.
 */
public class DialRepeat extends Dialog {
    Context context;
    public DialRepeat(Context context) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.context = context;
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
        layoutParams.width = dm.widthPixels * 2 / 3;
        layoutParams.height = dm.heightPixels * 3 / 5;
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
        repeatGroup = (RadioGroup)findViewById(R.id.repeatGroup);
        llTerm = (LinearLayout)findViewById(R.id.llTerm);
        llWeek = (LinearLayout)findViewById(R.id.llWeek);
        llMonth = (LinearLayout)findViewById(R.id.llMonth);
        llYear = (LinearLayout)findViewById(R.id.llYear);
        tvTerm = (TextView)findViewById(R.id.tvTerm);
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
                RadioButton rb=(RadioButton)findViewById(checkedId);
                repeatType = rb.getText().toString();

                switch(group.getCheckedRadioButtonId()){
                    case R.id.repeat_radio0:
                        llTerm.setVisibility(View.INVISIBLE);
                        llWeek.setVisibility(View.INVISIBLE);
                        llMonth.setVisibility(View.INVISIBLE);
                        llYear.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.repeat_radio1:
                        etWeek.setText("");
                        etWeek.setFocusable(true);
                        tvTerm.setText(context.getResources().getString(R.string.week));
                        llWeek.setVisibility(View.VISIBLE);
                        llTerm.setVisibility(View.INVISIBLE);
                        llMonth.setVisibility(View.INVISIBLE);
                        llYear.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.repeat_radio2:
                        etMonth.setText("");
                        etMonth.setFocusable(true);
                        tvTerm.setText(context.getResources().getString(R.string.term_month));
                        llTerm.setVisibility(View.INVISIBLE);
                        llWeek.setVisibility(View.INVISIBLE);
                        llYear.setVisibility(View.INVISIBLE);
                        llMonth.setVisibility(View.VISIBLE);
                        break;
                    case R.id.repeat_radio3:
                        etYear.setText("");
                        etYear.setFocusable(true);
                        tvTerm.setText(context.getResources().getString(R.string.year));
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
                if(repeatGroup.getCheckedRadioButtonId()!=-1){
                    int id= repeatGroup.getCheckedRadioButtonId();
                    View radioButton = repeatGroup.findViewById(id);
                    int radioId = repeatGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) repeatGroup.getChildAt(radioId);
                    repeatType = (String) btn.getText();
                    switch(radioId){
                        case 0:
                            break;
                        case 1:
                            String week = etWeek.getText().toString();
                            String wterm = etTerm.getText().toString();
                            if(week.equals("")||wterm.equals(""))
                                Toast.makeText(context,"단위를 입력하세요",Toast.LENGTH_SHORT).show();
                            else {
                                repeatPeriod = Integer.parseInt(week);
                                repeatNumber = Integer.parseInt(wterm);
                            }

                            break;
                        case 2:
                            String month = etMonth.getText().toString();
                            String mterm = etTerm.getText().toString();
                            if(month.equals("")||mterm.equals(""))
                                Toast.makeText(context,"단위를 입력하세요",Toast.LENGTH_SHORT).show();
                            else {
                                repeatPeriod = Integer.parseInt(month);
                                repeatNumber = Integer.parseInt(mterm);
                            }

                            break;
                        case 3:
                            String year = etYear.getText().toString();
                            String yterm = etTerm.getText().toString();
                            if(year.equals("")||yterm.equals(""))
                                Toast.makeText(context,"단위를 입력하세요",Toast.LENGTH_SHORT).show();
                            else{
                                repeatPeriod = Integer.parseInt(year);
                                repeatNumber = Integer.parseInt(yterm);
                            }
                            break;
                    }
                    EventBus.getDefault().post(new SetRepeatEvent(repeatType,repeatPeriod,repeatNumber));
                    cancel();
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
    private Button btDialCancel;
    private Button btDialSetting;
    private RadioGroup repeatGroup;
    private LinearLayout llTerm;
    private LinearLayout llWeek;
    private LinearLayout llMonth;
    private LinearLayout llYear;
    private TextView tvTerm;
    private EditText etTerm;
    private EditText etWeek;
    private EditText etMonth;
    private EditText etYear;
    private String repeatType;
    private int repeatPeriod;
    private int repeatNumber;
}
