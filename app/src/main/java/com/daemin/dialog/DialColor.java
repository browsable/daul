package com.daemin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.daemin.event.SetColorEvent;
import com.daemin.timetable.R;

import de.greenrobot.event.EventBus;

/**
 * Created by hernia on 2015-09-08.
 */
public class DialColor extends Dialog {
    public DialColor(Context context, Button btColor) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.context = context;
        this.btColor = btColor;
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
        layoutParams.width = dm.widthPixels * 7/ 9;
        layoutParams.height = dm.heightPixels * 5/ 9;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        setLayout();
    }

    private void setLayout() {
        btDialCancel = (Button) findViewById(R.id.btDialCancel);
        btBack = (Button) findViewById(R.id.btBack);
        btForward = (Button) findViewById(R.id.btForward);
        llColor1 = (LinearLayout) findViewById(R.id.llColor1);
        llColor2 = (LinearLayout) findViewById(R.id.llColor2);
        btDialCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        String[] dialogColorBtn = context.getResources().getStringArray(R.array.dialogColorBtn);
        for (int i = 0; i < dialogColorBtn.length; i++) {
            int resID = context.getResources().getIdentifier(dialogColorBtn[i], "id", context.getPackageName());
            final int resColor = context.getResources().getIdentifier(dialogColorBtn[i], "color", context.getPackageName());
            Button B = (Button) findViewById(resID);
            B.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(btColor==null)
                        EventBus.getDefault().post(new SetColorEvent(resColor));
                    else{
                        gd = (GradientDrawable) btColor.getBackground().mutate();
                        gd.setColor(context.getResources().getColor(resColor));
                        gd.invalidateSelf();
                        btColor.setTag(resColor);
                    }
                    cancel();
                }
            });
        }
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btBack.setBackgroundResource(R.drawable.ic_back_on2);
                btForward.setBackgroundResource(R.drawable.ic_forward_off2);
                llColor1.setVisibility(View.VISIBLE);
                llColor2.setVisibility(View.GONE);
            }
        });
        btForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btBack.setBackgroundResource(R.drawable.ic_back_off2);
                btForward.setBackgroundResource(R.drawable.ic_forward_on2);
                llColor1.setVisibility(View.GONE);
                llColor2.setVisibility(View.VISIBLE);
            }
        });
    }
    private Context context;
    private Button btDialCancel;
    private Button btColor;
    private Button btBack;
    private Button btForward;
    private GradientDrawable gd;
    private LinearLayout llColor1,llColor2;
}
