package com.daemin.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daemin.adapter.BottomNormalListAdapter;
import com.daemin.adapter.HorizontalListAdapter;
import com.daemin.common.BackPressCloseHandler;
import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.common.CurrentTime;
import com.daemin.common.DatabaseHandler;
import com.daemin.common.HorizontalListView;
import com.daemin.data.BottomNormalData;
import com.daemin.data.SubjectData;
import com.daemin.enumclass.DayOfMonthPos;
import com.daemin.enumclass.DayOfMonthPosState;
import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.enumclass.User;
import com.daemin.event.ClearNormalEvent;
import com.daemin.event.ExcuteMethodEvent;
import com.daemin.event.FinishDialogEvent;
import com.daemin.event.SetAlarmEvent;
import com.daemin.event.SetBtPlusEvent;
import com.daemin.event.SetBtUnivEvent;
import com.daemin.event.SetBtUnivNoticeEvent;
import com.daemin.event.SetPlaceEvent;
import com.daemin.event.SetRepeatEvent;
import com.daemin.event.SetShareEvent;
import com.daemin.event.UpdateNormalEvent;
import com.daemin.main.MainActivity;
import com.daemin.map.MapActivity;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.InitMonthThread;
import com.daemin.timetable.InitSurfaceView;
import com.daemin.timetable.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import timedao.MyTime;

public class DialSchedule extends Activity implements View.OnClickListener, View.OnTouchListener{
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
        System.gc();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_schedule);
        EventBus.getDefault().post(new SetBtPlusEvent(false));
        if(!getIntent().equals(null))//widget에서 Dialog 호출한 경우
            widgetFlag = getIntent().getBooleanExtra("widgetFlag", false);
        setLayout();
        colorButtonSetting();
        makeNormalList();
        window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        lp = window.getAttributes();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels *3/5;
        lp.width = lp.MATCH_PARENT;
        lp.height = screenHeight*2/3;
        window.setAttributes(lp);
        window.setGravity(Gravity.BOTTOM);
        if(viewMode==0)
            updateWeekList();
        else
            updateMonthList();
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
                        DialWeekPicker dwp = new DialWeekPicker(DialSchedule.this, position, xth, startHour, startMin, endHour, endMin);
                        dwp.show();
                        break;
                    case 2:
                        DialMonthPicker dmp = new DialMonthPicker(DialSchedule.this);
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
        InitMonthThread im = (InitMonthThread)initSurfaceView.getInitThread();
        int yth = (im.getDayOfWeekOfLastMonth()+day)/7+1;
        DayOfMonthPos DOMP = DayOfMonthPos.valueOf(Convert.getxyMergeForMonth(xth, yth));
        if (DOMP.getPosState() != DayOfMonthPosState.NO_PAINT) {
            DOMP.setPosState(DayOfMonthPosState.NO_PAINT);
        }
    }
    public void updateWeekList(){
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

    }
    public void updateMonthList(){
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
    }
    public void clearView(){
        normalList.clear();
        normalAdapter.notifyDataSetChanged();
        Common.stateFilter(Common.getTempTimePos(), viewMode);
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override

        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String ...univName) {
            int count;
            try {
                URL url = new URL("http://hernia.cafe24.com/android/db/"+univName[0]+"/subject.sqlite");
                URLConnection conection = url.openConnection();
                conection.connect();
                // input stream to read file - with 8k buffer
                createFolder();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream("/sdcard/.TimeDAO/subject.sqlite");
                ///data/data/com.daemin.timetable/databases
                byte data[] = new byte[2048];
                while ((count = input.read(data)) != -1) {
                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String param) {
            User.INFO.getEditor().putBoolean("subjectDown", true).commit();
            settingUniv();
        }

    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void settingUniv(){
        llDep.setVisibility(View.VISIBLE);
        ArrayList<String>
                depList = new ArrayList<>(),
                gradeList = new ArrayList<>(),
                subOrProfList = new ArrayList<>();
        depList.addAll(db.getDepList());
        gradeList.add("학년 : 전체");
        gradeList.add("1학년");
        gradeList.add("2학년");
        gradeList.add("3학년");
        gradeList.add("4학년");
        subOrProfList.addAll(db.getSubOrProfList());
        ArrayAdapter<String>
                depAdapter = new ArrayAdapter<>(this,R.layout.dropdown_dep, depList),
                gradeAdapter = new ArrayAdapter<>(this,R.layout.dropdown_dep, gradeList),
                subAdapter = new ArrayAdapter<>(this,R.layout.dropdown_dep, subOrProfList);
        SettingACTV(actvDep, depAdapter);
        SettingACTV(actvGrade, gradeAdapter);
        SettingACTV(actvSub, subAdapter);

        //subject
        final List<SubjectData> subjects = db.getAllSubjectDatas();
        hoAdapter = new HorizontalListAdapter(this, subjects);
        hlv.setAdapter(hoAdapter);
        hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] temps = null;
                Common.stateFilter(Common.getTempTimePos(), viewMode);
                for (String timePos : getTimeList(((TextView) view.findViewById(R.id.time)).getText()
                        .toString())) {
                    temps = timePos.split(":");
                    if (!temps[0].equals(" ")) {
                        addWeek(Integer.parseInt(temps[0])
                                , Integer.parseInt(temps[1])
                                , Integer.parseInt(temps[2])
                                , Integer.parseInt(temps[3])
                                , Integer.parseInt(temps[4]));
                    } else {
                        Toast.makeText(DialSchedule.this, main.getResources().getString(R.string.univ_notice_emtpy), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });

        actvDep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                depFlag = false;
                actvSub.setText("");
                subjects.clear();
                subjects.addAll(db.getAllWithDepAndGrade(actvDep.getText().toString(),Convert.indexOfGrade(actvGrade.getText().toString())));
                hoAdapter.notifyDataSetChanged();
            }
        });
        actvGrade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                gradeFlag = false;
                subjects.clear();
                subjects.addAll(db.getAllWithDepAndGrade(actvDep.getText().toString(), Convert.indexOfGrade(actvGrade.getText().toString())));
                //actvSub.setText("");
                hoAdapter.notifyDataSetChanged();
            }
        });
        actvSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                actvDep.setText("");
                actvGrade.setText("");
                subjects.clear();
                subjects.addAll(db.getAllWithSubOrProf(actvSub.getText().toString()));
                hoAdapter.notifyDataSetChanged();
            }
        });
        actvDep.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                btShowDep.setBackgroundResource(R.drawable.ic_expand);
            }
        });
        actvGrade.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                btShowGrade.setBackgroundResource(R.drawable.ic_expand);
            }
        });

    }
    public static void createFolder(){
        try{
            //check sdcard mount state
            String str = Environment.getExternalStorageState();
            if ( str.equals(Environment.MEDIA_MOUNTED)) {
                String mTargetDirPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                        +      "/.TimeDAO/";

                File file = new File(mTargetDirPath);
                if(!file.exists()){
                    file.mkdirs();
                }
            }else{
            }
        }catch(Exception e){
        }
    }
    private String[] getTimeList(String time){
        String[] timeList=null; //요일 인덱스와 이벤트에 따른 시간(시작~끝)으로 자름, 예)3:9:00:10:00/3:14:00:15:00을 분리
        try {
            if(!time.equals(null))
                timeList = time.split("/");
            else
                return timeList;
        }catch(NullPointerException e){
            Toast.makeText(DialSchedule.this, getString(R.string.e_learning), Toast.LENGTH_SHORT).show();
        }
        return timeList;
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
            case R.id.btNormal:
                clearView();
                DrawMode.CURRENT.setMode(0);
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
                clearView();
                DrawMode.CURRENT.setMode(1);
                llNormal.setVisibility(View.GONE);
                llUniv.setVisibility(View.VISIBLE);
                /*if (User.USER.isSubjectDownloadState()) {
                    llSelectUniv.setVisibility(View.GONE);
                    llDep.setVisibility(View.VISIBLE);
                }
                else {
                    llSelectUniv.setVisibility(View.VISIBLE);
                    llDep.setVisibility(View.GONE);
                }*/
                btNormal.setTextColor(getResources().getColor(
                        R.color.gray));
                btUniv.setTextColor(getResources().getColor(
                        android.R.color.white));
                //ArrayAdapter<String> univAdapter = new ArrayAdapter<>(this,R.layout.dropdown_univ, MyRequest.getGroupListFromLocal());
                ArrayList<String> univList = new ArrayList<>();
                //임시목록
                univList.add("한국기술교육대학교");
                univList.add("공주대학교");
                univList.add("충남대학교");
                ArrayAdapter<String>
                        univAdapter = new ArrayAdapter<>(this, R.layout.dropdown_univ, univList);
                SettingACTV(actvUniv, univAdapter);
                actvUniv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
						/*korName = actvUniv.getText().toString();
						User.USER.setKorUnivName(korName);
						engName = GroupListFromServerRepository
								.getEngByKor(MainActivity.this, korName);
						User.USER.setEngUnivName(engName);*/
                        // 열려있는 키패드 닫기
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(actvUniv.getWindowToken(), 0);
                        btShowUniv.setVisibility(View.GONE);
                        btEnter.setVisibility(View.VISIBLE);
                    }
                });
               actvUniv.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        btShowUniv.setBackgroundResource(R.drawable.ic_expand);
                    }
                });
                break;
            case R.id.btShowUniv:
                if (univFlag) {
                    actvUniv.dismissDropDown();
                    univFlag = false;
                } else {
                    actvUniv.showDropDown();
                    btShowUniv.setBackgroundResource(R.drawable.ic_collapse);
                    univFlag = true;
                }
                break;
            case R.id.btShowDep:
                if (depFlag) {
                    actvDep.dismissDropDown();
                    depFlag = false;
                } else {
                    actvDep.showDropDown();
                    btShowDep.setBackgroundResource(R.drawable.ic_collapse);
                    depFlag = true;
                }
                break;
           case R.id.btShowGrade:
               if (gradeFlag) {
                   actvGrade.dismissDropDown();
                   gradeFlag = false;
               } else {
                   actvGrade.showDropDown();
                   btShowGrade.setBackgroundResource(R.drawable.ic_collapse);
                   gradeFlag = true;
               }
               break;
            case R.id.btEnter:
                Toast.makeText(DialSchedule.this, getString(R.string.univ_notice_setting), Toast.LENGTH_SHORT).show();
                if (Common.isOnline()) {
                    if (User.INFO.getSubjectDownFlag()) {
                        Toast.makeText(DialSchedule.this, "다운로드 되어 있음", Toast.LENGTH_SHORT).show();
                        settingUniv();
                    } else {
                        Toast.makeText(DialSchedule.this, "첫 과목 다운로드", Toast.LENGTH_SHORT).show();
                        new DownloadFileFromURL().execute("koreatech");
                    }
                } else {
                    if (User.INFO.getSubjectDownFlag()) {
                        Toast.makeText(DialSchedule.this, "네트워크는 비연결, 오프라인으로 조회", Toast.LENGTH_SHORT).show();
                        settingUniv();
                    } else {
                        Toast.makeText(DialSchedule.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
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
                switch(viewMode) {
                    case 0:
                            datp = new DialAddTimePicker(DialSchedule.this, User.INFO.getwData());
                        break;
                    case 2:
                            datp = new DialAddTimePicker(DialSchedule.this, User.INFO.getmData(),User.INFO.getDayOfWeekOfLastMonth());
                        break;
                }
                datp.show();
                break;
            case R.id.btPlace:
                Intent in = new Intent(DialSchedule.this, MapActivity.class);
                startActivity(in);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case R.id.btShare:
                DialShare ds = new DialShare(DialSchedule.this);
                ds.show();
                break;
            case R.id.btAlarm:
                DialAlarm da = new DialAlarm(DialSchedule.this);
                da.show();
                break;
            case R.id.btRepeat:
                dayIndex.clear();
                for(BottomNormalData d : normalList){
                    if(!dayIndex.containsKey(d.getXth()))
                        dayIndex.put(d.getXth(),d.getXth());
                }
                DialRepeat dr = new DialRepeat(DialSchedule.this,dayIndex);
                dr.show();
                break;
            case R.id.btUnivNotice:
                btUnivNotice.setTextColor(Color.BLACK);
                btUnivNotice.setBackgroundResource(R.drawable.bg_black_bottomline);
                DialUnivNotice du = new DialUnivNotice(DialSchedule.this);
                du.show();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()){
            case R.id.dragToggle:
                univFlag = false; depFlag = false; gradeFlag = false;
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
            case R.id.llSelectUniv:
                univFlag = false;
                break;
        }
        return true;
    }
    private void setLayout() {
        db = new DatabaseHandler(this);
        btUniv = (Button) findViewById(R.id.btUniv);
        if(widgetFlag) {
            viewMode = User.INFO.getViewMode();
            initSurfaceView = new InitSurfaceView(this, viewMode);
            btUniv.setVisibility(View.INVISIBLE);
        }
        else {
            main = MainActivity.getInstance();
            viewMode = main.getViewMode();
            initSurfaceView = main.getInitSurfaceView();
        }
        btNormal = (Button) findViewById(R.id.btNormal);
        btAddSchedule = (Button) findViewById(R.id.btAddSchedule);
        btCancel = (Button) findViewById(R.id.btCancel);
        btCommunity = (Button) findViewById(R.id.btCommunity);
        btInvite = (Button) findViewById(R.id.btInvite);
        btRemove = (Button) findViewById(R.id.btRemove);
        btEnter = (Button) findViewById(R.id.btEnter);
        btShowUniv = findViewById(R.id.btShowUniv);
        btShowDep = findViewById(R.id.btShowDep);
        btShowGrade = findViewById(R.id.btShowGrade);
        btColor = (ToggleButton) findViewById(R.id.btColor);
        llColor = (LinearLayout) findViewById(R.id.llColor);
        llNormal = (LinearLayout) findViewById(R.id.llNormal);
        llButtonArea = (LinearLayout) findViewById(R.id.llButtonArea);
        llUniv = (LinearLayout) findViewById(R.id.llUniv);
        btNew1 = (LinearLayout) findViewById(R.id.btNew1);
        btNew2 = (LinearLayout) findViewById(R.id.btNew2);
        btPlace = (LinearLayout) findViewById(R.id.btPlace);
        btShare = (LinearLayout) findViewById(R.id.btShare);
        btAlarm = (LinearLayout) findViewById(R.id.btAlarm);
        btRepeat = (LinearLayout) findViewById(R.id.btRepeat);
        llDep = (LinearLayout) findViewById(R.id.llDep);
        llSelectUniv= (LinearLayout) findViewById(R.id.llSelectUniv);
        dragToggle = findViewById(R.id.dragToggle);
        tvShare = (TextView) findViewById(R.id.tvShare);
        tvAlarm = (TextView) findViewById(R.id.tvAlarm);
        tvRepeat = (TextView) findViewById(R.id.tvRepeat);
        btUnivNotice = (TextView) findViewById(R.id.btUnivNotice);
        etName = (EditText) findViewById(R.id.etName);
        etPlace = (EditText) findViewById(R.id.etPlace);
        etMemo = (EditText) findViewById(R.id.etMemo);
        lvTime = (HorizontalListView) findViewById(R.id.lvTime);
        hlv = (HorizontalListView) findViewById(R.id.hlv);
        actvUniv = (AutoCompleteTextView) findViewById(R.id.actvUniv);
        actvDep = (AutoCompleteTextView) findViewById(R.id.actvDep);
        actvGrade = (AutoCompleteTextView) findViewById(R.id.actvGrade);
        actvSub = (AutoCompleteTextView) findViewById(R.id.actvSub);
        btNormal.setOnClickListener(this);
        btUniv.setOnClickListener(this);
        btColor.setOnClickListener(this);
        btAddSchedule.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        btCommunity.setOnClickListener(this);
        btInvite.setOnClickListener(this);
        btRemove.setOnClickListener(this);
        btUnivNotice.setOnClickListener(this);
        btShowUniv.setOnClickListener(this);
        btShowDep.setOnClickListener(this);
        btShowGrade.setOnClickListener(this);
        btEnter.setOnClickListener(this);
        btNew1.setOnClickListener(this);
        btNew2.setOnClickListener(this);
        btPlace.setOnClickListener(this);
        btShare.setOnClickListener(this);
        btAlarm.setOnClickListener(this);
        btRepeat.setOnClickListener(this);
        dragToggle.setOnTouchListener(this);
        llSelectUniv.setOnTouchListener(this);
        univFlag=false;depFlag=false;gradeFlag=false;
        colorName = Common.MAIN_COLOR;
        dy =mPosY = 0;
        dayIndex = new HashMap<>();
        if(!widgetFlag) {
            SetBtUnivEvent sbue = EventBus.getDefault().getStickyEvent(SetBtUnivEvent.class);
            if (sbue != null) {
                EventBus.getDefault().removeStickyEvent(sbue);
                if (sbue.isSetVisable())
                    btUniv.setVisibility(View.VISIBLE);
                else
                    btUniv.setVisibility(View.INVISIBLE);
            }
        }
    }
    private Button btNormal, btUniv, btAddSchedule, btCancel, btCommunity, btInvite, btRemove,btEnter;
    private ToggleButton btColor;
    private LinearLayout llColor, llNormal, llButtonArea, llUniv,llSelectUniv, llDep, btNew1, btNew2, btPlace, btShare, btAlarm, btRepeat;
    private TextView tvShare, tvAlarm, tvRepeat, btUnivNotice;
    private EditText etName, etPlace, etMemo;
    private View dragToggle,btShowUniv,btShowDep,btShowGrade;
    private HorizontalListView lvTime,hlv;
    private HorizontalListAdapter hoAdapter;
    private Window window;
    private GradientDrawable gd;
    private String colorName;
    private InitSurfaceView initSurfaceView;
    private MainActivity main;
    private WindowManager.LayoutParams lp;
    private ArrayList<BottomNormalData> normalList;
    private ArrayAdapter normalAdapter;
    private HashMap<Integer,Integer> dayIndex;//어느 요일이 선택됬는지
    private AutoCompleteTextView actvUniv,actvDep,actvGrade,actvSub;
    private DatabaseHandler db;
    private BackPressCloseHandler backPressCloseHandler;
    private int dy, mPosY, screenHeight,viewMode;
    private boolean univFlag,depFlag,gradeFlag,widgetFlag;
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
    public void onEventMainThread(BottomNormalData e){
        normalList.add(e);
        normalAdapter.notifyDataSetChanged();
    }
    public void onEventMainThread(ClearNormalEvent e) {
        normalList.clear();
        normalAdapter.notifyDataSetChanged();
    }
    public void onEventMainThread(UpdateNormalEvent e){
        normalList.remove(e.getPosition());
        normalList.add(e.getPosition(), new BottomNormalData(
                initSurfaceView.getInitThread().getMonthAndDay(e.getXth()), e.getStartHour(), e.getStartMin(),
                e.getEndHour(), e.getEndMin(), e.getXth()));
        normalAdapter.notifyDataSetChanged();
    }
    public void onEventMainThread(ExcuteMethodEvent e){
        try {
            Method m = DialSchedule.this.getClass().getDeclaredMethod(e.getMethodName());
            m.invoke(DialSchedule.this);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
