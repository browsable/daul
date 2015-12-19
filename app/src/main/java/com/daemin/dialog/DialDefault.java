package com.daemin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.daemin.event.SetShareEvent;
import com.daemin.timetable.R;

import de.greenrobot.event.EventBus;

/**
 * Created by hernia on 2015-09-08.
 */
public class DialDefault extends Dialog {
    public DialDefault(Context context, String title, String content,int callFuncIndex) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.context=context;
        this.title=title;
        this.content=content;
        this.callFuncIndex=callFuncIndex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_default);
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
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvTitle.setText(title);
        tvContent.setText(content);
        btDialSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    switch (callFuncIndex){
                        case 0:
                            goToPlayMarket();
                            break;
                    }
                    cancel();
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
    private TextView tvTitle;
    private TextView tvContent;
    private String title, content;
    private int callFuncIndex;
    private Context context;
    public void goToPlayMarket(){
        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://market.android.com/details?id=com.daemin.timetable"));
        context.startActivity(intent);
    }
}
