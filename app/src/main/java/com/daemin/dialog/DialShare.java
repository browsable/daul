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

import com.daemin.event.SetShareEvent;
import com.daemin.timetable.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hernia on 2015-09-08.
 */
public class DialShare extends Dialog {
    public DialShare(Context context) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_share);
        setCancelable(true);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        layoutParams.width = dm.widthPixels * 2 / 3;
        layoutParams.height = dm.heightPixels / 3;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        setLayout();
    }

    private void setLayout() {
        btDialCancel = (Button) findViewById(R.id.btDialCancel);
        btDialSetting = (Button) findViewById(R.id.btDialSetting);
        shareGroup = (RadioGroup)findViewById(R.id.shareGroup);
        btDialSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shareGroup.getCheckedRadioButtonId()!=-1){
                    int id= shareGroup.getCheckedRadioButtonId();
                    View radioButton = shareGroup.findViewById(id);
                    int radioId = shareGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) shareGroup.getChildAt(radioId);
                    String selection = (String) btn.getText();
                    EventBus.getDefault().post(new SetShareEvent(selection));
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
    private RadioGroup shareGroup;
}
