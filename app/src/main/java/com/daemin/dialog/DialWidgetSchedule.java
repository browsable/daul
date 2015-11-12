package com.daemin.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.daemin.common.BackPressCloseHandler;
import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.enumclass.User;
import com.daemin.event.ExcuteMethodEvent;
import com.daemin.event.FinishDialogEvent;
import com.daemin.event.SetAlarmEvent;
import com.daemin.event.SetBtPlusEvent;
import com.daemin.event.SetBtUnivEvent;
import com.daemin.event.SetBtUnivNoticeEvent;
import com.daemin.event.SetPlaceEvent;
import com.daemin.event.SetRepeatEvent;
import com.daemin.event.SetShareEvent;
import com.daemin.main.MainActivity;
import com.daemin.map.MapActivity;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;

import java.lang.reflect.Method;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import timedao.MyTime;

public class DialWidgetSchedule extends Activity implements View.OnClickListener, View.OnTouchListener{
    public void onBackPressed() {
        EventBus.getDefault().post(new SetBtPlusEvent(true));
        Common.stateFilter(Common.getTempTimePos(), viewMode);
        finish();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);

    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        System.gc();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_schedule);
        EventBus.getDefault().post(new SetBtPlusEvent(false));
        setLayout();
        colorButtonSetting();
        window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        lp = window.getAttributes();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels *3/5;
        lp.width = dm.widthPixels*24/25;
        lp.height = screenHeight*2/3;
        window.setAttributes(lp);
        window.setGravity(Gravity.BOTTOM);
        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    public void colorButtonSetting() {
        gd = (GradientDrawable) btColor.getBackground().mutate();
        String[] dialogColorBtn = getResources().getStringArray(R.array.dialogColorBtn);
        for (int i = 0; i < dialogColorBtn.length; i++) {
            int resID = getResources().getIdentifier(dialogColorBtn[i], "id", getPackageName());
            final int resColor = getResources().getIdentifier(dialogColorBtn[i], "color", getPackageName());
            ImageButton B = (ImageButton) findViewById(resID);
            B.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btColor.setChecked(false);
                    llColor.setVisibility(View.INVISIBLE);
                    colorName = getResources().getString(resColor);
                    gd.setColor(getResources().getColor(resColor));
                    gd.invalidateSelf();
                }
            });
        }
    }
    //3:9:00:10:00 , 3:9:00:11:00, 3:9:00:11:30
    public void addWeek(int xth, int startHour, int startMin, int endHour, int endMin){
        if(endMin!=0) ++endHour;
        else endMin=60;

        TimePos[] tp = new TimePos[endHour - startHour];
        int j = 0;
        for (int i = startHour; i < endHour; i++) {
            tp[j] = TimePos.valueOf(Convert.getxyMerge(xth, Convert.HourOfDayToYth(i)));
            if (tp[j].getPosState() == PosState.NO_PAINT) {
                if(i==startHour && startMin!=0) tp[j].setMin(startMin, 60);
                if(i==endHour-1) tp[j].setMin(0, endMin);
                tp[j].setPosState(PosState.ADJUST);
                Common.getTempTimePos().add(tp[j].name());
            }
            ++j;
        }
    }
    public AutoCompleteTextView SettingACTV(AutoCompleteTextView actv,ArrayAdapter<String> adapter){
        actv.requestFocus();
        actv.setThreshold(1);// will start working from first character
        actv.setAdapter(adapter);// setting the adapter data into the
        actv.setTextColor(Color.DKGRAY);
        actv.setDropDownVerticalOffset(10);
        return actv;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btColor:
                if (btColor.isChecked()) {
                    llColor.setVisibility(View.VISIBLE);
                } else {
                    llColor.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.btAddSchedule:
                MyTime myTime = new MyTime(null,User.INFO.userPK,etName.getText().toString(),
                        2015,10,11,3,9,30,10,30,0,0,etMemo.getText().toString(),etPlace.getText().toString(),
                        User.INFO.latitude,User.INFO.longitude,0,"10","10:10","#000000");
                MyTimeRepo.insertOrUpdate(this,myTime);
                break;
            case R.id.btCancel:
                EventBus.getDefault().post(new SetBtPlusEvent(true));
                EventBus.getDefault().postSticky(new SetBtUnivEvent(true));
                //btUniv.setVisibility(View.VISIBLE);
                switch (DrawMode.CURRENT.getMode()) {
                    case 1:
                        Common.stateFilter(Common.getTempTimePos(), viewMode);
                        DrawMode.CURRENT.setMode(0);
                        break;
                }
                finish();
                break;
            case R.id.btCommunity:
                break;
            case R.id.btInvite:
                break;
            case R.id.btRemove:
                break;
            case R.id.btNew1:case R.id.btNew2:
                DialAddTimePicker datp = null;
                /*switch(viewMode) {
                    case 0:
                        InitWeekThread iw = (InitWeekThread) initSurfaceView.getInitThread();
                        datp = new DialAddTimePicker(DialWidgetSchedule.this, iw.getAllMonthAndDay());
                        break;
                    case 2:
                        InitMonthThread im = (InitMonthThread) initSurfaceView.getInitThread();
                        datp = new DialAddTimePicker(DialWidgetSchedule.this, im.getMonthData(),im.getDayOfWeekOfLastMonth());
                        break;
                }*/
                datp.show();
                break;
            case R.id.btPlace:
                Intent in = new Intent(DialWidgetSchedule.this, MapActivity.class);
                startActivity(in);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.btShare:
                DialShare ds = new DialShare(DialWidgetSchedule.this);
                ds.show();
                break;
            case R.id.btAlarm:
                DialAlarm da = new DialAlarm(DialWidgetSchedule.this);
                da.show();
                break;
            case R.id.btRepeat:
                dayIndex.clear();
                /*for(BottomNormalData d : normalList){
                    if(!dayIndex.containsKey(d.getXth()))
                        dayIndex.put(d.getXth(),d.getXth());
                }*/
                DialRepeat dr = new DialRepeat(DialWidgetSchedule.this,dayIndex);
                dr.show();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()){
            case R.id.dragToggle:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dy = mPosY + (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mPosY = (int) (dy - event.getRawY());
                        lp.y = mPosY;
                        if (lp.y < 0) {
                            lp.y = mPosY = 0;
                        }
                        if (lp.y > screenHeight) {
                            lp.y = mPosY = screenHeight;
                        }
                        window.setAttributes(lp);
                        break;
                }
                break;
        }
        return true;
    }
    private void setLayout() {
        main = MainActivity.getInstance();
        viewMode = main.getViewMode();
        btNormal = (Button) findViewById(R.id.btNormal);
        btUniv = (Button) findViewById(R.id.btUniv);
        btUniv.setVisibility(View.INVISIBLE);
        btAddSchedule = (Button) findViewById(R.id.btAddSchedule);
        btCancel = (Button) findViewById(R.id.btCancel);
        btCommunity = (Button) findViewById(R.id.btCommunity);
        btInvite = (Button) findViewById(R.id.btInvite);
        btRemove = (Button) findViewById(R.id.btRemove);
        btColor = (ToggleButton) findViewById(R.id.btColor);
        llColor = (LinearLayout) findViewById(R.id.llColor);
        llNormal = (LinearLayout) findViewById(R.id.llNormal);
        llButtonArea = (LinearLayout) findViewById(R.id.llButtonArea);
        btNew1 = (LinearLayout) findViewById(R.id.btNew1);
        btNew2 = (LinearLayout) findViewById(R.id.btNew2);
        btPlace = (LinearLayout) findViewById(R.id.btPlace);
        btShare = (LinearLayout) findViewById(R.id.btShare);
        btAlarm = (LinearLayout) findViewById(R.id.btAlarm);
        btRepeat = (LinearLayout) findViewById(R.id.btRepeat);
        tvShare = (TextView) findViewById(R.id.tvShare);
        tvAlarm = (TextView) findViewById(R.id.tvAlarm);
        tvRepeat = (TextView) findViewById(R.id.tvRepeat);
        btUnivNotice = (TextView) findViewById(R.id.btUnivNotice);
        etName = (EditText) findViewById(R.id.etName);
        etPlace = (EditText) findViewById(R.id.etPlace);
        etMemo = (EditText) findViewById(R.id.etMemo);
        btNormal.setOnClickListener(this);
        btUniv.setOnClickListener(this);
        btColor.setOnClickListener(this);
        btAddSchedule.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        btCommunity.setOnClickListener(this);
        btInvite.setOnClickListener(this);
        btRemove.setOnClickListener(this);
        btUnivNotice.setOnClickListener(this);
        btNew1.setOnClickListener(this);
        btNew2.setOnClickListener(this);
        btPlace.setOnClickListener(this);
        btShare.setOnClickListener(this);
        btAlarm.setOnClickListener(this);
        btRepeat.setOnClickListener(this);
        colorName = Common.MAIN_COLOR;
        dy =mPosY = 0;
        dayIndex = new HashMap<>();
        SetBtUnivEvent sbue = EventBus.getDefault().getStickyEvent(SetBtUnivEvent.class);
        if(sbue != null){
            EventBus.getDefault().removeStickyEvent(sbue);
            if(sbue.isSetVisable())
                btUniv.setVisibility(View.VISIBLE);
            else
                btUniv.setVisibility(View.INVISIBLE);
        }
    }
    private Button btNormal, btUniv, btAddSchedule, btCancel, btCommunity, btInvite, btRemove;
    private ToggleButton btColor;
    private LinearLayout llColor, llNormal, llButtonArea, btNew1, btNew2, btPlace, btShare, btAlarm, btRepeat;
    private TextView tvShare, tvAlarm, tvRepeat, btUnivNotice;
    private EditText etName, etPlace, etMemo;
    private Window window;
    private GradientDrawable gd;
    private String colorName;
    private MainActivity main;
    private WindowManager.LayoutParams lp;
    private HashMap<Integer,Integer> dayIndex;//어느 요일이 선택됬는지
    private BackPressCloseHandler backPressCloseHandler;
    private int dy, mPosY, screenHeight,viewMode;
    public void onEventMainThread(FinishDialogEvent e) {
        finish();
        EventBus.getDefault().post(new SetBtPlusEvent(true));
    }

    public void onEventMainThread(SetAlarmEvent e) {
        tvAlarm.setText(e.getTime());
    }

    public void onEventMainThread(SetShareEvent e) {
        tvShare.setText(e.getShare());
    }

    public void onEventMainThread(SetRepeatEvent e) {
        tvRepeat.setText(e.toString());
    }
    public void onEventMainThread(SetPlaceEvent e){
        etPlace.setText(e.getPlace());
    }

    public void onEventMainThread(SetBtUnivNoticeEvent e) {
        btUnivNotice.setTextColor(Color.GRAY);
        btUnivNotice.setBackgroundResource(R.drawable.bg_lightgray_bottomline);
    }
    public void onEventMainThread(ExcuteMethodEvent e){
        try {
            Method m = DialWidgetSchedule.this.getClass().getDeclaredMethod(e.getMethodName());
            m.invoke(DialWidgetSchedule.this);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
