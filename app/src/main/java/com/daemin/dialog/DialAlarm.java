package com.daemin.dialog;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.daemin.event.EditAlarmEvent;
import com.daemin.event.SetAlarmEvent;
import com.daemin.timetable.R;

import de.greenrobot.event.EventBus;

/**
 * Created by hernia on 2015-09-08.
 */
public class DialAlarm extends Dialog {
    public DialAlarm(Context context) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
    }
    public DialAlarm(Context context, int alarmType, int position) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.alarmType = alarmType;
        this.editAlarm = true;
        this.position = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_alarm);
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
        alarmGroup = (RadioGroup)findViewById(R.id.alarmGroup);
        radio0 = (RadioButton)findViewById(R.id.radio0);
        radio1 = (RadioButton)findViewById(R.id.radio1);
        radio2 = (RadioButton)findViewById(R.id.radio2);
        radio3 = (RadioButton)findViewById(R.id.radio3);
        radio4 = (RadioButton)findViewById(R.id.radio4);
        radio5 = (RadioButton)findViewById(R.id.radio5);
        radio6 = (RadioButton)findViewById(R.id.radio6);
        if(editAlarm){
            switch (alarmType){
                case 0:
                    alarmGroup.check(radio0.getId());
                    break;
                case 1:
                    alarmGroup.check(radio1.getId());
                    break;
                case 2:
                    alarmGroup.check(radio2.getId());
                    break;
                case 3:
                    alarmGroup.check(radio3.getId());
                    break;
                case 4:
                    alarmGroup.check(radio4.getId());
                    break;
                case 5:
                    alarmGroup.check(radio5.getId());
                    break;
                case 6:
                    alarmGroup.check(radio6.getId());
                    break;
            }
        }
        btDialSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarmGroup.getCheckedRadioButtonId()!=-1){
                    int id= alarmGroup.getCheckedRadioButtonId();
                    View radioButton = alarmGroup.findViewById(id);
                    int radioId = alarmGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) alarmGroup.getChildAt(radioId);
                    String selection = (String) btn.getText();
                    if(editAlarm)
                        EventBus.getDefault().post(new EditAlarmEvent(selection, position));
                    else
                        EventBus.getDefault().post(new SetAlarmEvent(selection));
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
    private RadioGroup alarmGroup;
    private boolean editAlarm;
    private int alarmType, position;
    RadioButton radio0,radio1,radio2,radio3,radio4,radio5,radio6;
}
