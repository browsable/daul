package com.daemin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.daemin.event.SetColorEvent;
import com.daemin.timetable.R;

import de.greenrobot.event.EventBus;

/**
 * Created by hernia on 2015-09-08.
 */
public class DialColor extends Dialog {
    public DialColor(Context context) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_color);
        setCancelable(true);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        layoutParams.width = dm.widthPixels * 5/ 9;
        layoutParams.height = dm.heightPixels * 1/ 3;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        setLayout();
    }

    private void setLayout() {
        btDialCancel = (Button) findViewById(R.id.btDialCancel);
        btDialSetting = (Button) findViewById(R.id.btDialSetting);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        String[] dialogColorBtn = context.getResources().getStringArray(R.array.dialogColorBtn);
        for (int i = 0; i < dialogColorBtn.length; i++) {
            int resID = context.getResources().getIdentifier(dialogColorBtn[i], "id", context.getPackageName());
            final int resColor = context.getResources().getIdentifier(dialogColorBtn[i], "color", context.getPackageName());
            Button B = (Button) findViewById(resID);
            B.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    color = resColor;
                    tvTitle.setTextColor(Color.parseColor(context.getResources().getString(resColor)));
                }
            });
        }
        btDialSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SetColorEvent(color));
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
    private Context context;
    private Button btDialCancel;
    private Button btDialSetting;
    private int color;
    private TextView tvTitle;
}
