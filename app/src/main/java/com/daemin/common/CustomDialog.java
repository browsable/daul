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

import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-09-08.
 */
public class CustomDialog extends Dialog {
    Context context;
    private Button btDialCancel;
    private Button btDialSetting;
    public CustomDialog(Context context, String[] MD) {
        // Dialog 배경을 투명 처리 해준다.
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_addtimepicker);
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

        btDialCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        btDialSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
