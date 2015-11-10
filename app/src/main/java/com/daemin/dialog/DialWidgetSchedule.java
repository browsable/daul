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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.daemin.adapter.BottomNormalListAdapter;
import com.daemin.common.BackPressCloseHandler;
import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.common.DatabaseHandler;
import com.daemin.common.HorizontalListView;
import com.daemin.data.BottomNormalData;
import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.event.ClearNormalEvent;
import com.daemin.event.ExcuteMethodEvent;
import com.daemin.event.FinishDialogEvent;
import com.daemin.event.SetAlarmEvent;
import com.daemin.event.SetBtPlusEvent;
import com.daemin.event.SetBtUnivEvent;
import com.daemin.event.SetPlaceEvent;
import com.daemin.event.SetRepeatEvent;
import com.daemin.event.SetShareEvent;
import com.daemin.event.UpdateNormalEvent;
import com.daemin.main.MainActivity;
import com.daemin.map.MapActivity;
import com.daemin.timetable.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

public class DialWidgetSchedule extends Activity implements View.OnClickListener, View.OnTouchListener{
    public void onBackPressed() {
        EventBus.getDefault().post(new SetBtPlusEvent(true));
        switch (DrawMode.CURRENT.getMode()) {
            case 1:
                Common.stateFilter(Common.getTempTimePos(), viewMode);
                DrawMode.CURRENT.setMode(0);
                break;
        }
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
        makeNormalList();
        window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        lp = window.getAttributes();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels *3/5;
        lp.width = dm.widthPixels * 23 / 24;
        lp.height = screenHeight*2/3;
        window.setAttributes(lp);
        window.setGravity(Gravity.BOTTOM);
        /*if(viewMode==0)
            updateWeekList();
        else
            updateMonthList();*/
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
    public void makeNormalList(){
        normalList = new ArrayList<>();
        normalAdapter = new BottomNormalListAdapter(this, normalList);
        lvTime.setAdapter(normalAdapter);
        lvTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (viewMode) {
                    case 0:
                        String xth = ((TextView) view.findViewById(R.id.tvXth)).getText().toString();
                        String startHour = ((TextView) view.findViewById(R.id.tvStartHour)).getText().toString();
                        String startMin = ((TextView) view.findViewById(R.id.tvStartMin)).getText().toString();
                        String endHour = ((TextView) view.findViewById(R.id.tvEndHour)).getText().toString();
                        String endMin = ((TextView) view.findViewById(R.id.tvEndMin)).getText().toString();
                        DialWeekPicker dwp = new DialWeekPicker(DialWidgetSchedule.this, position, xth, startHour, startMin, endHour, endMin);
                        dwp.show();
                        break;
                    case 2:
                        DialMonthPicker dmp = new DialMonthPicker(DialWidgetSchedule.this);
                        dmp.show();
                        break;
                }
            }
        });
        lvTime.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                           int pos, long id) {
                switch (viewMode) {
                    case 0:
                        String startHour = ((TextView) view.findViewById(R.id.tvStartHour)).getText().toString();
                        String endHour = ((TextView) view.findViewById(R.id.tvEndHour)).getText().toString();
                        String endMin = ((TextView) view.findViewById(R.id.tvEndMin)).getText().toString();
                        String xth = ((TextView) view.findViewById(R.id.tvXth)).getText().toString();
                        removeWeek(Integer.parseInt(xth), Integer.parseInt(startHour), Integer.parseInt(endHour), Integer.parseInt(endMin));
                        break;
                    case 2:
                        String tvYMD = ((TextView) view.findViewById(R.id.tvYMD)).getText().toString();
                        String xth2 = ((TextView) view.findViewById(R.id.tvXth)).getText().toString();
                        removeMonth(Integer.parseInt(xth2), Integer.parseInt(tvYMD.split("/")[1]));
                        break;
                }
                normalList.remove(pos);
                normalAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }
    public void removeWeek(int xth, int startHour, int endHour, int endMin){
        if(startHour!=endHour) {
            if(endMin!=0)++endHour;
            TimePos[] tp = new TimePos[endHour - startHour];
            int j = 0;
            for (int i = startHour; i < endHour; i++) {
                tp[j] = TimePos.valueOf(Convert.getxyMerge(xth, Convert.HourOfDayToYth(i)));
                if (tp[j].getPosState() != PosState.NO_PAINT) {
                    tp[j].setMin(0, 60);
                    tp[j].setPosState(PosState.NO_PAINT);
                }
                ++j;
            }
        }else{
            TimePos tp = TimePos.valueOf(Convert.getxyMerge(xth, Convert.HourOfDayToYth(startHour)));
            tp.setMin(0, 60);
            tp.setPosState(PosState.NO_PAINT);
        }
    }
    public void removeMonth(int xth, int day){
       /* InitMonthThread im = (InitMonthThread)initSurfaceView.getInitThread();
        int yth = (im.getDayOfWeekOfLastMonth()+day)/7+1;
        DayOfMonthPos DOMP = DayOfMonthPos.valueOf(Convert.getxyMergeForMonth(xth, yth));
        if (DOMP.getPosState() != DayOfMonthPosState.NO_PAINT) {
            DOMP.setPosState(DayOfMonthPosState.NO_PAINT);
        }*/
    }
    /*public void updateWeekList(){
        normalList.clear();
        int startYth=0,startMin=0,endYth=0,endMin=0,tmpXth=0;
        int tmpStartYth=0, tmpStartMin=0, tmpEndYth=0, tmpEndMin=0;
        String YMD="";
        for (TimePos ETP : TimePos.values()) {
            if(ETP.getPosState()==PosState.PAINT||ETP.getPosState()==PosState.ADJUST){
                if(tmpXth!=ETP.getXth()){
                    tmpXth = ETP.getXth();
                    YMD = initSurfaceView.getInitThread().getMonthAndDay(tmpXth);
                    tmpStartYth=tmpStartMin=tmpEndYth=tmpEndMin=0;
                }
                if(tmpEndYth!=ETP.getYth()) {
                    tmpStartYth = startYth = ETP.getYth();
                    tmpStartMin = startMin = ETP.getStartMin();
                    tmpEndMin = endMin = ETP.getEndMin();
                    if(endMin!=0) tmpEndYth = endYth = startYth;
                    else tmpEndYth = endYth = startYth + 2;
                    normalList.add(new BottomNormalData(YMD,
                            Convert.YthToHourOfDay(startYth),
                            Convert.IntToString(startMin),
                            Convert.YthToHourOfDay(endYth),
                            Convert.IntToString(endMin),
                            tmpXth
                    ));
                }else if(tmpEndYth== ETP.getYth()&&tmpEndMin==ETP.getStartMin()){ //
                    normalList.remove(normalList.size()-1);
                    startYth = tmpStartYth;
                    startMin = tmpStartMin;
                    tmpEndMin = endMin = ETP.getEndMin();
                    if(endMin!=0) tmpEndYth = endYth = ETP.getYth();
                    else tmpEndYth = endYth = ETP.getYth() + 2;
                    normalList.add(new BottomNormalData(YMD,
                            Convert.YthToHourOfDay(startYth),
                            Convert.IntToString(startMin),
                            Convert.YthToHourOfDay(endYth),
                            Convert.IntToString(endMin),
                            tmpXth
                    ));
                }
            }
        }
        normalAdapter.notifyDataSetChanged();

    }*/
   /* public void updateMonthList(){
        normalList.clear();
        int tmpXth=0,tmpYth=0;
        String YMD="";
        for (DayOfMonthPos DOMP : DayOfMonthPos.values()) {
            if (DOMP.getPosState() == DayOfMonthPosState.PAINT) {
                tmpXth = DOMP.getXth();
                tmpYth = DOMP.getYth();
                YMD = CurrentTime.getTitleMonth()+"/"+initSurfaceView.getInitThread().getMonthAndDay(tmpXth - 1, 7 * (tmpYth - 1));
                normalList.add(new BottomNormalData(YMD,"8","00","9","00",tmpXth));
            }
        }
        normalAdapter.notifyDataSetChanged();
    }*/
    public void clearView(){
        normalList.clear();
        normalAdapter.notifyDataSetChanged();
        Common.stateFilter(Common.getTempTimePos(), viewMode);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btNormal:
                clearView();
                btColor.setVisibility(View.VISIBLE);
                break;
            case R.id.btColor:
                if (btColor.isChecked()) {
                    llColor.setVisibility(View.VISIBLE);
                } else {
                    llColor.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.btAddSchedule:
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
               /* DialAddTimePicker datp = null;
                switch(viewMode) {
                    case 0:
                        InitWeekThread iw = (InitWeekThread) initSurfaceView.getInitThread();
                        datp = new DialAddTimePicker(DialWidgetSchedule.this, iw.getAllMonthAndDay());
                        break;
                    case 2:
                        InitMonthThread im = (InitMonthThread) initSurfaceView.getInitThread();
                        datp = new DialAddTimePicker(DialWidgetSchedule.this, im.getMonthData(),im.getDayOfWeekOfLastMonth());
                        break;
                }
                datp.show();*/
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
                for(BottomNormalData d : normalList){
                    if(!dayIndex.containsKey(d.getXth()))
                        dayIndex.put(d.getXth(),d.getXth());
                }
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
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setLayout() {
        db = new DatabaseHandler(this);
        main = MainActivity.getInstance();
    //    initSurfaceView = main.getInitSurfaceView();
        viewMode = 0; //main.getViewMode();
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
        dragToggle = findViewById(R.id.dragToggle);
        tvShare = (TextView) findViewById(R.id.tvShare);
        tvAlarm = (TextView) findViewById(R.id.tvAlarm);
        tvRepeat = (TextView) findViewById(R.id.tvRepeat);
        etName = (EditText) findViewById(R.id.etName);
        etPlace = (EditText) findViewById(R.id.etPlace);
        etMemo = (EditText) findViewById(R.id.etMemo);
        lvTime = (HorizontalListView) findViewById(R.id.lvTime);
        btNormal.setOnClickListener(this);
        btColor.setOnClickListener(this);
        btAddSchedule.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        btCommunity.setOnClickListener(this);
        btInvite.setOnClickListener(this);
        btRemove.setOnClickListener(this);
        btNew1.setOnClickListener(this);
        btNew2.setOnClickListener(this);
        btPlace.setOnClickListener(this);
        btShare.setOnClickListener(this);
        btAlarm.setOnClickListener(this);
        btRepeat.setOnClickListener(this);
        dragToggle.setOnTouchListener(this);
        colorName = Common.MAIN_COLOR;
        dy =mPosY = 0;
        dayIndex = new HashMap<>();
    }
    private Button btNormal, btAddSchedule, btCancel, btCommunity, btInvite, btRemove,btUniv;
    private ToggleButton btColor;
    private LinearLayout llColor, llNormal, llButtonArea,btNew1, btNew2, btPlace, btShare, btAlarm, btRepeat;
    private TextView tvShare, tvAlarm, tvRepeat;
    private EditText etName, etPlace, etMemo;
    private View dragToggle;
    private HorizontalListView lvTime;
    private Window window;
    private GradientDrawable gd;
    private String colorName;
    //private InitSurfaceView initSurfaceView;
    private MainActivity main;
    private WindowManager.LayoutParams lp;
    private ArrayList<BottomNormalData> normalList;
    private ArrayAdapter normalAdapter;
    private HashMap<Integer,Integer> dayIndex;//어느 요일이 선택됬는지
    private DatabaseHandler db;
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
    public void onEventMainThread(BottomNormalData e){
        normalList.add(e);
        normalAdapter.notifyDataSetChanged();
    }
    public void onEventMainThread(ClearNormalEvent e) {
        normalList.clear();
        normalAdapter.notifyDataSetChanged();
    }
    public void onEventMainThread(UpdateNormalEvent e){
       /* normalList.remove(e.getPosition());
        normalList.add(e.getPosition(), new BottomNormalData(
                initSurfaceView.getInitThread().getMonthAndDay(e.getXth()), e.getStartHour(), e.getStartMin(),
                e.getEndHour(), e.getEndMin(), e.getXth()));
        normalAdapter.notifyDataSetChanged();*/
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
