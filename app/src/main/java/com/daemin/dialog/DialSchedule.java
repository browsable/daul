package com.daemin.dialog;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daemin.common.Common;
import com.daemin.common.HorizontalListView;
import com.daemin.event.SetAlarmEvent;
import com.daemin.event.SetRepeatEvent;
import com.daemin.event.SetShareEvent;
import com.daemin.timetable.R;

import de.greenrobot.event.EventBus;

public class DialSchedule extends Activity implements View.OnClickListener{
    @Override
    public void onResume() {
        super.onResume();
        if(!EventBus.getDefault().isRegistered(this))EventBus.getDefault().register(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_schedule);
        window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        layoutParams.width = layoutParams.MATCH_PARENT;
        layoutParams.height = dm.heightPixels * 4/ 9;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.BOTTOM);
        setLayout();
        colorButtonSetting();
    }
    public void colorButtonSetting(){
        gd = (GradientDrawable) btColor.getBackground().mutate();
        String[] dialogColorBtn = getResources().getStringArray(R.array.dialogColorBtn);
        for (int i = 0; i < dialogColorBtn.length; i++) {
            int resID = getResources().getIdentifier(dialogColorBtn[i], "id", getPackageName());
            final int resColor = getResources().getIdentifier(dialogColorBtn[i], "color", getPackageName());
            ImageButton B = (ImageButton) findViewById(resID);
            B.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llColor.setVisibility(View.INVISIBLE);
                    colorFlag = false;
                    colorName = getResources().getString(resColor);
                    gd.setColor(getResources().getColor(resColor));
                    gd.invalidateSelf();
                }
            });
        }
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btNormal:
                btColor.setVisibility(View.VISIBLE);
                llNormal.setVisibility(View.VISIBLE);
                llUniv.setVisibility(View.GONE);
                btColor.setVisibility(View.VISIBLE);
                btNormal.setTextColor(getResources().getColor(
                        android.R.color.white));
                btUniv.setTextColor(getResources().getColor(
                        R.color.gray));
                break;
            case R.id.btUniv:
                llNormal.setVisibility(View.GONE);
                llUniv.setVisibility(View.VISIBLE);
                btNormal.setTextColor(getResources().getColor(
                        R.color.gray));
                btUniv.setTextColor(getResources().getColor(
                        android.R.color.white));
                break;
            case R.id.btColor:
                if (!colorFlag) {
                    llColor.setVisibility(View.VISIBLE);
                    colorFlag = true;
                } else {
                    llColor.setVisibility(View.INVISIBLE);
                    colorFlag = false;
                }
                break;
            case R.id.btAddSchedule:
                finish();
                break;
            case R.id.btCancel:
                finish();
                break;
            case R.id.btCommunity:
                break;
            case R.id.btInvite:
                break;
            case R.id.btRemove:
                break;
            case R.id.btShowDep:
                break;
            case R.id.btNew1:
                break;
            case R.id.btNew2:
                break;
            case R.id.btPlace:
                break;
            case R.id.btShare:
                break;
            case R.id.btAlarm:
                break;
            case R.id.btRepeat:
                break;
        }
    }
    private void setLayout() {
        btNormal=(Button)findViewById(R.id.btNormal);
        btUniv=(Button)findViewById(R.id.btUniv);
        btColor=(Button)findViewById(R.id.btColor);
        btAddSchedule=(Button)findViewById(R.id.btAddSchedule);
        btCancel=(Button)findViewById(R.id.btCancel);
        btCommunity=(Button)findViewById(R.id.btCommunity);
        btInvite=(Button)findViewById(R.id.btInvite);
        btRemove=(Button)findViewById(R.id.btRemove);
        btShowDep=(Button)findViewById(R.id.btShowDep);
        llColor=(LinearLayout)findViewById(R.id.llColor);
        llNormal=(LinearLayout)findViewById(R.id.llNormal);
        llButtonArea=(LinearLayout)findViewById(R.id.llButtonArea);
        llUniv=(LinearLayout)findViewById(R.id.llUniv);
        btNew1=(LinearLayout)findViewById(R.id.btNew1);
        btNew2=(LinearLayout)findViewById(R.id.btNew2);
        btPlace=(LinearLayout)findViewById(R.id.btPlace);
        btShare=(LinearLayout)findViewById(R.id.btShare);
        btAlarm=(LinearLayout)findViewById(R.id.btAlarm);
        btRepeat=(LinearLayout)findViewById(R.id.btRepeat);
        llIncludeDep=(LinearLayout)findViewById(R.id.llIncludeDep);
        dragToggle = findViewById(R.id.dragToggle);
        tvShare=(TextView)findViewById(R.id.tvShare);
        tvAlarm=(TextView)findViewById(R.id.tvAlarm);
        tvRepeat=(TextView)findViewById(R.id.tvRepeat);
        etName=(EditText)findViewById(R.id.etName);
        etPlace=(EditText)findViewById(R.id.etPlace);
        etMemo=(EditText)findViewById(R.id.etMemo);
        lvTime=(HorizontalListView)findViewById(R.id.lvTime);
        btNormal.setOnClickListener(this);
        btUniv.setOnClickListener(this);
        btColor.setOnClickListener(this);
        btAddSchedule.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        btCommunity.setOnClickListener(this);
        btInvite.setOnClickListener(this);
        btRemove.setOnClickListener(this);
        btShowDep.setOnClickListener(this);
        btNew1.setOnClickListener(this);
        btNew2.setOnClickListener(this);
        btPlace.setOnClickListener(this);
        btShare.setOnClickListener(this);
        btAlarm.setOnClickListener(this);
        btRepeat.setOnClickListener(this);
        dx=dy=mPosX=mPosY=0;
        colorName = Common.MAIN_COLOR;
        dragToggle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dx = mPosX + (int) event.getRawX();
                        dy = mPosY + (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mPosX = (int) (dx - event.getRawX());
                        mPosY = (int) (dy - event.getRawY());
                        WindowManager.LayoutParams layoutParams = window.getAttributes();
                        layoutParams.x = mPosX;
                        layoutParams.y = mPosY;
                        window.setAttributes(layoutParams);
                        break;
                }
                return true;
            }
        });
    }
    private Button btNormal,btUniv,btColor,btAddSchedule,btCancel,btCommunity,btInvite,btRemove,btShowDep;
    private LinearLayout llColor,llNormal,llButtonArea,llUniv,llIncludeDep,btNew1,btNew2,btPlace,btShare,btAlarm,btRepeat;
    private TextView tvShare,tvAlarm,tvRepeat;
    private EditText etName,etPlace, etMemo;
    private View dragToggle, view;
    private HorizontalListView lvTime;
    private int dx, dy, mPosX, mPosY;
    private boolean colorFlag = false;
    private Window window;
    private GradientDrawable gd;
    private String colorName;
    public void onEventMainThread(SetAlarmEvent e){
        tvAlarm.setText(e.getTime());
    }
    public void onEventMainThread(SetShareEvent e){
        tvShare.setText(e.getShare());
    }
    public void onEventMainThread(SetRepeatEvent e){
        tvRepeat.setText(e.toString());
    }
}
