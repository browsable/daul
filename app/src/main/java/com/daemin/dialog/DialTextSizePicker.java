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
import android.widget.NumberPicker;
import android.widget.TextView;

import com.daemin.enumclass.User;
import com.daemin.timetable.R;


/**
 * Created by hernia on 2015-09-08.
 */
public class DialTextSizePicker extends Dialog {
    private TextView tvDialText,tvTextSize;
    private Button btDialCancel;
    private Button btDialSetting;
    private NumberPicker npTextSize;
    private int textSize;
    public DialTextSizePicker(Context context, TextView tvTextSize, int textSize) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.textSize = textSize;
        this.tvTextSize = tvTextSize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_textsizepicker);
        setCancelable(true);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        layoutParams.width = dm.widthPixels * 2 / 3;
        layoutParams.height = dm.heightPixels*4/9;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        setLayout();
        tvDialText.setTextSize(textSize);
        npTextSize.setMaxValue(20);
        npTextSize.setMinValue(10);
        npTextSize.setValue(textSize);
        npTextSize.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                tvDialText.setTextSize(newVal);
                textSize = newVal;
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
                User.INFO.getEditor().putInt("textSize", textSize).commit();
                tvTextSize.setText(textSize+"");
                cancel();
            }
        });
    }

    private void setLayout() {
        tvDialText = (TextView) findViewById(R.id.tvDialText);
        btDialCancel = (Button) findViewById(R.id.btDialCancel);
        btDialSetting = (Button) findViewById(R.id.btDialSetting);
        npTextSize = (NumberPicker) findViewById(R.id.npTextSize);
    }

}
