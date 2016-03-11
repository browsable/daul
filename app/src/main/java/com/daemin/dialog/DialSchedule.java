package com.daemin.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daemin.adapter.BottomNormalListAdapter;
import com.daemin.adapter.HorizontalListAdapter;
import com.daemin.common.BackPressCloseHandler;
import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.common.DatabaseHandler;
import com.daemin.common.HorizontalListView;
import com.daemin.common.MyRequest;
import com.daemin.data.BottomNormalData;
import com.daemin.data.GroupListData;
import com.daemin.data.SubjectData;
import com.daemin.enumclass.Dates;
import com.daemin.enumclass.DayOfMonthPos;
import com.daemin.enumclass.DayOfMonthPosState;
import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.enumclass.User;
import com.daemin.event.ClearNormalEvent;
import com.daemin.event.ExcuteMethodEvent;
import com.daemin.event.FinishDialogEvent;
import com.daemin.event.PostGroupListEvent;
import com.daemin.event.SetAlarmEvent;
import com.daemin.event.SetBtPlusEvent;
import com.daemin.event.SetBtUnivEvent;
import com.daemin.event.SetColorEvent;
import com.daemin.event.SetCreditEvent;
import com.daemin.event.SetPlaceEvent;
import com.daemin.event.SetRepeatEvent;
import com.daemin.event.SetShareEvent;
import com.daemin.event.UpdateNormalEvent;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;
import com.daemin.widget.WidgetUpdateService;

import org.joda.time.DateTime;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.greenrobot.event.EventBus;
import timedao.MyTime;

public class DialSchedule extends Activity implements View.OnClickListener, View.OnTouchListener {
    public void onBackPressed() {
        EventBus.getDefault().post(new SetBtPlusEvent(true));
        DrawMode.CURRENT.setMode(0);
        if(viewMode==0)Common.fetchWeekData();
        else Common.fetchMonthData();
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
        lp.height = lp.WRAP_CONTENT;
        window.setAttributes(lp);
        if(User.INFO.dbServerVer==null){
            User.INFO.dbServerVer=User.INFO.getGroupDBVer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new SetBtPlusEvent(true));
        EventBus.getDefault().postSticky(new SetBtUnivEvent(true));
        EventBus.getDefault().unregister(this);
        Common.stateFilter(viewMode);
        DrawMode.CURRENT.setMode(0);
        Common.fetchWeekData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_schedule);
        EventBus.getDefault().post(new SetBtPlusEvent(false));
        if (getIntent() != null) {
            widgetFlag = getIntent().getBooleanExtra("widgetFlag", false);
            overlapEnrollFlag = getIntent().getBooleanExtra("overlapEnrollFlag", false);
            weekFlag = getIntent().getBooleanExtra("weekFlag", true);
        }

        setLayout();
        makeNormalList();
        window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        lp = window.getAttributes();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        lp.height = lp.WRAP_CONTENT;
        lp.width = lp.MATCH_PARENT;
        if (!widgetFlag) {
            SetBtUnivEvent sbue = EventBus.getDefault().getStickyEvent(SetBtUnivEvent.class);
            if (sbue != null) {
                EventBus.getDefault().removeStickyEvent(sbue);
                if (sbue.isSetVisable())
                    btUniv.setVisibility(View.VISIBLE);
                else
                    btUniv.setVisibility(View.GONE);
            }
            groupName = User.INFO.getGroupName();
            if(groupName.equals("")) { //학교를 한번도 선택한 적 없는 경우
                univList = new ArrayList<>();
                for (GroupListData.Data d : User.INFO.groupListData) {
                    univList.add(d.getKo() + "/" + d.getTt_version() + "/" + d.getDb_version());
                }
            }

        }else{
            btNormal.setVisibility(View.GONE);
            btUniv.setVisibility(View.GONE);
            if(getIntent().getBooleanExtra("widget5_5", false)||getIntent().getBooleanExtra("widget4_4", false)){
                int dayCnt = getIntent().getIntExtra("dayCnt",0);
                int xth = dayCnt%7+1;
                int yth = dayCnt/7+1;
                //String day= getIntent().getStringExtra("day");
                DayOfMonthPos DOMP = DayOfMonthPos.valueOf(Convert.getxyMergeForMonth(xth, yth));
                DOMP.setPosState(DayOfMonthPosState.PAINT);
                etName.requestFocus();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.showSoftInput(findViewById(R.id.etName), InputMethodManager.SHOW_IMPLICIT);
                    }
                }, 100);
                updateMonthList();
            }
        }
        screenHeight = dm.heightPixels - lp.height;
        if (overlapEnrollFlag) {
            if (weekFlag) {
                TimePos tp = TimePos.valueOf(
                        Convert.getxyMerge(
                                getIntent().getIntExtra("xth", 1),
                                getIntent().getIntExtra("yth", 1)));
                tp.setPosState(PosState.PAINT);
                tp.setMin(tp.getEndMin(), 60);
                //tp.setT
            } else {
                DayOfMonthPos DOMP = DayOfMonthPos.valueOf(
                        Convert.getxyMergeForMonth(
                                getIntent().getIntExtra("xth", 1),
                                getIntent().getIntExtra("yth", 1)));
                DOMP.setPosState(DayOfMonthPosState.PAINT);
            }
        }
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(lp);
        if (viewMode == 0)
            updateWeekList();
        else
            updateMonthList();
        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    public void makeNormalList() {
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
                    case 1:
                        beforeYMD = ((TextView) view.findViewById(R.id.tvYMD)).getText().toString();
                        String xth2 = ((TextView) view.findViewById(R.id.tvXth)).getText().toString();
                        DialMonthPicker dmp = new DialMonthPicker(DialSchedule.this, position, xth2);
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
                    case 1:
                        String tvYMD = ((TextView) view.findViewById(R.id.tvYMD)).getText().toString();
                        String xth2 = ((TextView) view.findViewById(R.id.tvXth)).getText().toString();
                        removeMonth(Integer.parseInt(xth2), Integer.parseInt(tvYMD.split("\\.")[1]));
                        break;
                }
                normalList.remove(pos);
                normalAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    public void removeWeek(int xth, int startHour, int endHour, int endMin) {
        if (startHour != endHour) {
            if (endMin != 0) ++endHour;
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
        } else {
            TimePos tp = TimePos.valueOf(Convert.getxyMerge(xth, Convert.HourOfDayToYth(startHour)));
            tp.setMin(0, 60);
            tp.setPosState(PosState.NO_PAINT);
        }
    }

    public void removeMonth(int xth, int day) {
        int yth = (Dates.NOW.dayOfWeek + day) / 7 + 1;
        DayOfMonthPos DOMP = DayOfMonthPos.valueOf(Convert.getxyMergeForMonth(xth, yth));
        if (DOMP.getPosState() != DayOfMonthPosState.NO_PAINT) {
            DOMP.setPosState(DayOfMonthPosState.NO_PAINT);
        }
    }

    public void updateWeekList() {
        normalList.clear();
        int startYth = 0, startMin = 0, endYth = 0, endMin = 0, tmpXth = 0;
        int tmpStartYth = 0, tmpStartMin = 0, tmpEndYth = 0, tmpEndMin = 0;
        String YMD = "";
        for (TimePos ETP : TimePos.values()) {
            if (ETP.getPosState() == PosState.PAINT) {
                if (tmpXth != ETP.getXth()) {
                    tmpXth = ETP.getXth();
                    YMD = Dates.NOW.getwMonthDay(tmpXth);
                    tmpStartYth = tmpStartMin = tmpEndYth = tmpEndMin = 0;
                }
                if (tmpEndYth != ETP.getYth()) {
                    tmpStartYth = startYth = ETP.getYth();
                    tmpStartMin = startMin = ETP.getStartMin();
                    tmpEndMin = endMin = ETP.getEndMin();
                    if (endMin != 0) tmpEndYth = endYth = startYth;
                    else tmpEndYth = endYth = startYth + 2;
                    normalList.add(new BottomNormalData(YMD,
                            Convert.YthToHourOfDay(startYth),
                            Convert.IntToString(startMin),
                            Convert.YthToHourOfDay(endYth),
                            Convert.IntToString(endMin),
                            tmpXth
                    ));
                } else if (tmpEndYth == ETP.getYth() && tmpEndMin == ETP.getStartMin()) { //
                    normalList.remove(normalList.size() - 1);
                    startYth = tmpStartYth;
                    startMin = tmpStartMin;
                    tmpEndMin = endMin = ETP.getEndMin();
                    if (endMin != 0) tmpEndYth = endYth = ETP.getYth();
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

    public void updateMonthList() {
        normalList.clear();
        int tmpXth, tmpYth;
        String YMD ;
        for (DayOfMonthPos DOMP : DayOfMonthPos.values()) {
            if (DOMP.getPosState() == DayOfMonthPosState.PAINT) {
                tmpXth = DOMP.getXth();
                tmpYth = DOMP.getYth();
                YMD = Dates.NOW.month + "." + Dates.NOW.getmMonthDay(tmpXth - 1, 7 * (tmpYth - 1));
                normalList.add(new BottomNormalData(YMD, "8", "00", "9", "00", tmpXth));
            }
        }
        normalAdapter.notifyDataSetChanged();
    }

    public void clearView() {
        normalList.clear();
        normalAdapter.notifyDataSetChanged();
        Common.stateFilter(viewMode);
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        Boolean downComplete;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            downComplete=false;
            pDialog = new ProgressDialog(DialSchedule.this);
            pDialog.setMessage(getString(R.string.downloding));
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... univName) {
            int count;
            try {
                URL url = new URL("http://timenuri.com/ajax/app/get_univ_db?school=" + URLEncoder.encode(univName[0]));
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                createFolder();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream("/sdcard/.TimeDAO/subject.sqlite");
                ///data/data/com.daemin.timetable/databases
                byte data[] = new byte[2048];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();
                if(total==lenghtOfFile) downComplete=true;
            } catch (Exception e) {
                e.printStackTrace();
                downComplete=false;
            }
            return null;
        }
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));


        }
        @Override
        protected void onPostExecute(String param) {
            if(downComplete) {
                pDialog.dismiss();
                settingUniv();
                User.INFO.getEditor().putString("groupName", groupName).commit();
                if(User.INFO.dbServerVer==null){
                    User.INFO.dbServerVer=User.INFO.getGroupDBVer();
                }
                User.INFO.getEditor().putString("groupTtVer", User.INFO.ttServerVer).commit();
                User.INFO.getEditor().putString("groupDBVer", User.INFO.dbServerVer).commit();
                User.INFO.getEditor().putInt("groupPK", User.INFO.groupPK).commit();
            }
            else{//다운로드 실패
                User.INFO.ttServerVer=User.INFO.getGroupTtVer();
                User.INFO.dbServerVer=User.INFO.getGroupDBVer();
                Toast.makeText(DialSchedule.this, getString(R.string.down_error), Toast.LENGTH_SHORT).show();
                btShowUniv.setVisibility(View.VISIBLE);
                btEnter.setVisibility(View.GONE);
            }
        }

    }
    public void addWeek(int xth, int startHour, int startMin, int endHour, int endMin) {
        if (endMin != 0) ++endHour;
        else endMin = 60;
        TimePos[] tp = new TimePos[endHour - startHour];
        int j = 0;
        for (int i = startHour; i < endHour; i++) {
            tp[j] = TimePos.valueOf(Convert.getxyMerge(xth, Convert.HourOfDayToYth(i)));
            if (i == startHour && startMin != 0) tp[j].setMin(startMin, 60);
            if (i == endHour - 1) tp[j].setMin(0, endMin);
            if (tp[j].getPosState() == PosState.NO_PAINT) {
                tp[j].setPosState(PosState.PAINT);
            }else if(tp[j].getPosState() == PosState.ENROLL){
                List<MyTime> mtList = MyTimeRepo.overLapCheck(this,xth,startHour,startMin,endHour,endMin);
                if(mtList.size()==0){
                    tp[j].setPosState(PosState.PAINT);
                }
                else {
                    if (i == startHour && startMin != 0) tp[j].setMin(startMin, 60);
                    if (i == endHour - 1) tp[j].setMin(0, endMin);
                    else if(i != startHour)tp[j].setMin(startMin,endMin);
                    tp[j].setPosState(PosState.OVERLAP);
                    User.INFO.overlapFlag=true;
                }
            }
            Common.getTempTimePos().add(tp[j].name());
            ++j;
        }
    }
    public void settingUniv() {
        try {
        llSelectUniv.setVisibility(View.GONE);
        llDep.setVisibility(View.VISIBLE);
        ArrayList<String>
                depList = new ArrayList<>(),
                gradeList = new ArrayList<>(),
                subList = new ArrayList<>(),
                profList = new ArrayList<>();
        depList.addAll(db.getDepList());
        gradeList.add("학년 : 전체");
        gradeList.add("1학년");
        gradeList.add("2학년");
        gradeList.add("3학년");
        gradeList.add("4학년");
        subList.addAll(db.getSubList());
        profList.addAll(db.getProfList());
        ArrayAdapter<String>
                depAdapter = new ArrayAdapter<>(this, R.layout.dropdown_dep, depList),
                gradeAdapter = new ArrayAdapter<>(this, R.layout.dropdown_dep, gradeList),
                subAdapter = new ArrayAdapter<>(this, R.layout.dropdown_dep, subList),
                profAdapter = new ArrayAdapter<>(this, R.layout.dropdown_dep, profList);
        SettingACTV(actvDep, depAdapter);
        SettingACTV(actvGrade, gradeAdapter);
        SettingACTV(actvSub, subAdapter);
        SettingACTV(actvProf, profAdapter);
        actvSub.requestFocus();
        //subject
        final List<SubjectData> subjects = db.getAllSubjectDatas();
        hoAdapter = new HorizontalListAdapter(this, subjects);
        hlv.setAdapter(hoAdapter);
        hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setRandomColor();
                try {
                    String[] temps;
                    subId = ((TextView) view.findViewById(R.id._id)).getText().toString();
                    User.INFO.overlapFlag = false;
                    subOverlapFlag = true;
                    Common.stateFilter(viewMode);

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
                                Toast.makeText(DialSchedule.this, getResources().getString(R.string.univ_notice_emtpy), Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }

                    String credit = ((TextView) view.findViewById(R.id.credit)).getText().toString();
                    tvCreditSum.setText(String.valueOf(Float.parseFloat(creditSum)
                            + Float.parseFloat(credit)));
                }catch (Exception e){
                    Toast.makeText(DialSchedule.this, getResources().getString(R.string.time_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

            actvDep.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    actvSub.setText("");
                    subjects.clear();
                    List<SubjectData> sdlist = db.getAllWithDepAndGrade(actvDep.getText().toString(), Convert.indexOfGrade(actvGrade.getText().toString()));
                    if (sdlist.size() != 0)
                        subjects.add(sdlist.remove(0));
                    hoAdapter.notifyDataSetChanged();
                    InputMethodManager imm= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(actvDep.getWindowToken(), 0);
                }
            });
            actvGrade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    subjects.clear();
                    List<SubjectData> sdlist = db.getAllWithDepAndGrade(actvDep.getText().toString(), Convert.indexOfGrade(actvGrade.getText().toString()));
                    if (sdlist.size() != 0)
                        subjects.add(sdlist.remove(0));
                    //actvSub.setText("");
                    hoAdapter.notifyDataSetChanged();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(actvGrade.getWindowToken(), 0);
                }
            });
            actvSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    btClear.setVisibility(View.VISIBLE);
                    actvDep.setText("");
                    actvGrade.setText("");
                    actvProf.setText("");
                    subjects.clear();
                    List<SubjectData> sdlist = db.getAllWithSub(actvSub.getText().toString());
                    if (sdlist.size() != 0)
                        subjects.add(sdlist.remove(0));
                    hoAdapter.notifyDataSetChanged();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(actvSub.getWindowToken(), 0);
                }
            });
            actvSub.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    btClear.setVisibility(View.VISIBLE);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (actvSub.getText().toString().equals(""))
                        btClear.setVisibility(View.INVISIBLE);
                }
            });
            actvProf.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    actvDep.setText("");
                    actvGrade.setText("");
                    actvSub.setText("");
                    subjects.clear();
                    List<SubjectData> sdlist = db.getAllWithProf(actvProf.getText().toString());
                    if (sdlist.size() != 0)
                        subjects.add(sdlist.remove(0));
                    hoAdapter.notifyDataSetChanged();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(actvProf.getWindowToken(), 0);
                }
            });
        }catch(Exception e){ //다운로드 완전하게 성공하지 못한 경우
            e.printStackTrace();
            Toast.makeText(DialSchedule.this, getString(R.string.down_error), Toast.LENGTH_SHORT).show();
            btShowUniv.setVisibility(View.VISIBLE);
            btEnter.setVisibility(View.GONE);
        }
    }
    public static void createFolder() {
        try {
            //check sdcard mount state
            String str = Environment.getExternalStorageState();
            if (str.equals(Environment.MEDIA_MOUNTED)) {
                String mTargetDirPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/.TimeDAO/";

                File file = new File(mTargetDirPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
            } else {
            }
        } catch (Exception e) {
        }
    }

    private String[] getTimeList(String time) {
        String[] timeList = null; //요일 인덱스와 이벤트에 따른 시간(시작~끝)으로 자름, 예)3:9:00:10:00/3:14:00:15:00을 분리
        try {
            if (!time.equals(null))
                timeList = time.split("/");
            else
                return timeList;
        } catch (NullPointerException e) {
            Toast.makeText(DialSchedule.this, getString(R.string.e_learning), Toast.LENGTH_SHORT).show();
        }
        return timeList;
    }

    public AutoCompleteTextView SettingACTV(AutoCompleteTextView actv, ArrayAdapter<String> adapter) {
        actv.requestFocus();
        actv.setThreshold(1);// will start working from first character
        actv.setAdapter(adapter);// setting the adapter data into the
        actv.setTextColor(Color.DKGRAY);
        actv.setDropDownVerticalOffset(User.INFO.intervalSize);
        return actv;
    }
    public void AddSchedule(int repeatType){
        String repeat = tvRepeat.getText().toString();
        String s[];
        int repeatNum, repeatPeriod; //반복횟수와 기간;
        repeatNum=1;
        repeatPeriod=0;
        if(repeatType==0){ //반복없음
            repeat="0";
        }else if(repeatType==1){ //매주마다
            repeat="1";
        }else{ //~주마다, ~개월마다, ~년마다
            s = repeat.split(":");
            repeatPeriod=Convert.onlyNum(s[0]);
            repeatNum=Convert.onlyNum(s[1]);
            repeat = repeatType+":"+repeatPeriod+":"+repeatNum;
        }
        long nowMilis = Dates.NOW.getNowMillis();
        for(int i=0; i<repeatNum; i++){
            for (BottomNormalData d : normalList) {
                String[] tmp = d.getYMD().split("\\.");
                int year;
                int titleMonth = Dates.NOW.month;
                int monthOfYear = Integer.parseInt(tmp[0]);
                if (monthOfYear != titleMonth && titleMonth == 1)
                    year = Dates.NOW.year - 1;
                else year = Dates.NOW.year;
                int dayOfMonth = Integer.parseInt(tmp[1]);
                int startHour = Integer.parseInt(d.getStartHour());
                int startMin = Integer.parseInt(d.getStartMin());
                int endHour = Integer.parseInt(d.getEndHour());
                int endMin = Integer.parseInt(d.getEndMin());
                int xth;
                DateTime startDt = Dates.NOW.getDateMillisWithRepeat(year, monthOfYear, dayOfMonth, startHour, startMin, repeatType, repeatPeriod * i);
                DateTime endDt =  Dates.NOW.getDateMillisWithRepeat(year, monthOfYear, dayOfMonth, endHour, endMin, repeatType, repeatPeriod * i);
                long startMillis = startDt.getMillis();
                xth =Convert.dayOfWeekTowXth(startDt.getDayOfWeek()); //getDayOfWeek index - mon:1,tue:2 ... sun:7
                MyTime myTime = new MyTime(null,
                        String.valueOf(nowMilis), 0,
                        etName.getText().toString(),
                        startDt.getYear(), startDt.getMonthOfYear(), startDt.getDayOfMonth(),
                        xth, startHour, startMin, endHour, endMin,
                        startMillis, endDt.getMillis() - 1,
                        etMemo.getText().toString(),
                        etPlace.getText().toString(),
                        User.INFO.latitude, User.INFO.longitude,
                        Convert.Share(tvShare.getText().toString()),
                        Convert.Alarm(startMillis, tvAlarm.getText().toString()),
                        repeat,
                        colorName);
                MyTimeRepo.insertOrUpdate(this, myTime);
            }
        }
        normalList.clear();
        normalAdapter.notifyDataSetChanged();
        etName.setText("");
        etMemo.setText("");
        etPlace.setText("");
        if(viewMode==0)Common.fetchWeekData();
        else Common.fetchMonthData();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btNormal:
                setRandomColor();
                if(viewMode==0) {
                    clearView();
                    User.INFO.overlapFlag = false;
                    DrawMode.CURRENT.setMode(0);
                    llNormal.setVisibility(View.VISIBLE);
                    llUniv.setVisibility(View.GONE);
                    btNormal.setTextColor(getResources().getColor(
                            android.R.color.white));
                    btUniv.setTextColor(getResources().getColor(
                            R.color.gray));
                }
                break;
            case R.id.btUniv:
                setRandomColor();
                if(User.INFO.dbServerVer==null){
                    User.INFO.dbServerVer=User.INFO.getGroupDBVer();
                }
                clearView();
                DrawMode.CURRENT.setMode(1);
                llNormal.setVisibility(View.GONE);
                llUniv.setVisibility(View.VISIBLE);
                btNormal.setTextColor(getResources().getColor(
                        R.color.gray));
                btUniv.setTextColor(getResources().getColor(
                        android.R.color.white));
                if(groupName.equals("")||ttVersion.equals(getResources().getString(R.string.wait1))) { //학교를 한번도 선택한 적 없거나 등록대기중
                    univList = new ArrayList<>();
                    for (GroupListData.Data d : User.INFO.groupListData) {
                        univList.add(d.getKo() + "/" + d.getTt_version() + "/" + d.getDb_version());
                    }
                    univAdapter = new ArrayAdapter<>(DialSchedule.this, R.layout.dropdown_univ, univList);
                    SettingACTV(actvUniv, univAdapter);
                    actvUniv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            try {
                                String[] tmp = actvUniv.getText().toString().split("/");
                                User.INFO.groupPK = position+1;
                                groupName = tmp[0];
                                ttVersion = tmp[1];
                                User.INFO.ttServerVer = ttVersion;
                                User.INFO.dbServerVer = tmp[2];
                            } catch (ArrayIndexOutOfBoundsException e) { //등록대기중 인 경우
                                e.printStackTrace();
                                User.INFO.ttServerVer ="";
                                User.INFO.dbServerVer = "";
                            }
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(actvUniv.getWindowToken(), 0);
                            btShowUniv.setVisibility(View.GONE);
                            btEnter.setVisibility(View.VISIBLE);
                        }
                    });
                    actvUniv.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                            btEnter.setVisibility(View.GONE);
                            btShowUniv.setVisibility(View.VISIBLE);
                        }
                    });
                }else{
                    if (User.INFO.getGroupDBVer().equals(User.INFO.dbServerVer))
                        settingUniv(); //DB버전에 변동이 없는경우
                    else {
                        Toast.makeText(DialSchedule.this, getString(R.string.univ_dbupdate), Toast.LENGTH_SHORT).show();
                        new DownloadFileFromURL().execute(groupName);
                    }
                }
                break;
            case R.id.btShowUniv:
                if (!Common.isOnline())
                    Toast.makeText(DialSchedule.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                else{
                    if(univList.size()==0)MyRequest.getGroupList(DialSchedule.this);
                }
                if (btShowUniv.isChecked()) actvUniv.showDropDown();
                else actvUniv.dismissDropDown();
                break;
            case R.id.btShowDep:
                if (btShowDep.isChecked()) actvDep.showDropDown();
                else actvDep.dismissDropDown();
                break;
            case R.id.btClear:
                actvSub.setText("");
                btClear.setVisibility(View.INVISIBLE);
                break;
            case R.id.btShowGrade:
                if (btShowGrade.isChecked()) actvGrade.showDropDown();
                else actvGrade.dismissDropDown();
                break;
            case R.id.btEnter:
                if (Common.isOnline()) {
                    if(ttVersion.equals(getResources().getString(R.string.wait1))){
                        Toast.makeText(DialSchedule.this, getString(R.string.wait2), Toast.LENGTH_SHORT).show();
                        btShowUniv.setVisibility(View.VISIBLE);
                        btEnter.setVisibility(View.GONE);
                        btShowUniv.setChecked(false);
                        actvUniv.setText("");
                    }else {
                        new DownloadFileFromURL().execute(groupName);
                    }
                } else {
                    Toast.makeText(DialSchedule.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btColor:
                DialColor dc = new DialColor(DialSchedule.this, null);
                dc.show();
                break;
            case R.id.btAddSchedule:
                if (User.INFO.overlapFlag) {
                    Toast.makeText(DialSchedule.this, getResources().getString(R.string.univ_overlap), Toast.LENGTH_SHORT).show();
                } else {
                        switch (DrawMode.CURRENT.getMode()) {
                            case 0:
                                if (etName.getText().toString().equals("")) {
                                    etName.requestFocus();
                                    etName.setHintTextColor(Color.RED);
                                    Toast.makeText(DialSchedule.this, getResources().getString(R.string.normal_empty), Toast.LENGTH_SHORT).show();
                                    break;
                                }else {
                                    if(normalList.size()==0){
                                        Toast.makeText(DialSchedule.this, getResources().getString(R.string.normal_empty_time), Toast.LENGTH_SHORT).show();
                                    }else {
                                        etName.setHintTextColor(getResources().getColor(R.color.gray));
                                        Common.stateFilter(viewMode);
                                        AddSchedule(repeatType);
                                        finish();
                                    }
                                }
                                break;
                            case 1:
                                Common.stateFilter(viewMode);
                                if (subId != null && subOverlapFlag) {
                                    SubjectData subjectData = db.getSubjectData(subId);
                                    String[] temps;
                                    String subtitle = subjectData.getSubtitle();
                                    String prof = subjectData.getProf();
                                    String credit = subjectData.getCredit();
                                    String classnum = subjectData.getClassnum();
                                    String place = subjectData.getPlace();
                                    for (String timePos : getTimeList(subjectData.getTime())) {
                                        temps = timePos.split(":");
                                        if (!temps[0].equals(" ")) {
                                            MyTime myTime = new MyTime(null,
                                                    User.INFO.groupPK + subtitle + prof + classnum, 1,
                                                    subtitle,
                                                    null, null, null,
                                                    Integer.parseInt(temps[0]),
                                                    Integer.parseInt(temps[1]),
                                                    Integer.parseInt(temps[2]),
                                                    Integer.parseInt(temps[3]),
                                                    Integer.parseInt(temps[4]),
                                                    null, null,
                                                    prof + "교수/" + credit + "학점/" + classnum+"분반",
                                                    place,
                                                    User.INFO.latitude, User.INFO.longitude,
                                                    null,
                                                    null,
                                                    "0",
                                                    colorName);
                                            MyTimeRepo.insertOrUpdate(this, myTime);
                                            subOverlapFlag = false;
                                            setColorFlag=false;
                                        } else {
                                            Toast.makeText(DialSchedule.this, getResources().getString(R.string.univ_notice_emtpy), Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                    creditSum = tvCreditSum.getText().toString();
                                    tvCreditSum.setText(creditSum);
                                    Common.fetchWeekData();
                                }
                                break;
                        }
                        if (widgetFlag) {
                            if (User.INFO.getWidget5_5()) {
                                Intent update = new Intent(this, WidgetUpdateService.class);
                                update.putExtra("action", "update5_5");
                                update.putExtra("viewMode", viewMode);
                                this.startService(update);
                            }
                            if (User.INFO.getWidget4_4()) {
                                Intent update = new Intent(this, WidgetUpdateService.class);
                                update.putExtra("action", "update4_4");
                                update.putExtra("viewMode", viewMode);
                                this.startService(update);
                            }
                        }
                }
                break;
            case R.id.btCancel:
                EventBus.getDefault().post(new SetBtPlusEvent(true));
                EventBus.getDefault().postSticky(new SetBtUnivEvent(true));
                if(viewMode==0)Common.fetchWeekData();
                else Common.fetchMonthData();
                DrawMode.CURRENT.setMode(0);
                finish();
                break;
            case R.id.btNew:
                DialAddTimePicker datp = null;
                switch (viewMode) {
                    case 0:
                        datp = new DialAddTimePicker(DialSchedule.this, Dates.NOW.getWData());
                        break;
                    case 1:
                        datp = new DialAddTimePicker(DialSchedule.this, Dates.NOW.getMData());
                        break;
                }
                datp.show();
                break;
            case R.id.btPlace:
               /* Intent in = new Intent(DialSchedule.this, MapActivity.class);
                startActivity(in);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);*/
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
                for (BottomNormalData d : normalList) {
                    if (!dayIndex.containsKey(d.getXth()))
                        dayIndex.put(d.getXth(), d.getXth());
                }
                DialRepeat dr = new DialRepeat(DialSchedule.this, dayIndex);
                dr.show();
                break;
        }
    }
    public void setRandomColor(){
        if(!setColorFlag) {
            Random mRand = new Random();
            int nResult = mRand.nextInt(18) + 1;
            int resId = getResources().getIdentifier("btcolor" + nResult, "color", getPackageName());
            try {
                colorName = "#" + Integer.toHexString(getResources().getColor(resId));
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
            gd.setColor(Color.parseColor(colorName));
        }

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
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
        db = new DatabaseHandler(this);
        btUniv = (Button) findViewById(R.id.btUniv);
        viewMode = User.INFO.getViewMode();
        btNormal = (Button) findViewById(R.id.btNormal);
        btAddSchedule = (Button) findViewById(R.id.btAddSchedule);
        btCancel = (Button) findViewById(R.id.btCancel);
        btEnter = (Button) findViewById(R.id.btEnter);
        btColor = (Button) findViewById(R.id.btColor);
        btShowUniv = (ToggleButton)findViewById(R.id.btShowUniv);
        btShowDep = (ToggleButton)findViewById(R.id.btShowDep);
        btShowGrade = (ToggleButton)findViewById(R.id.btShowGrade);
        btClear = (Button) findViewById(R.id.btClear);
        llNormal = (LinearLayout) findViewById(R.id.llNormal);
        llUniv = (LinearLayout) findViewById(R.id.llUniv);
        btNew = (LinearLayout) findViewById(R.id.btNew);
        btPlace = (LinearLayout) findViewById(R.id.btPlace);
        btShare = (LinearLayout) findViewById(R.id.btShare);
        btAlarm = (LinearLayout) findViewById(R.id.btAlarm);
        btRepeat = (LinearLayout) findViewById(R.id.btRepeat);
        llDep = (LinearLayout) findViewById(R.id.llDep);
        llSelectUniv = (LinearLayout) findViewById(R.id.llSelectUniv);
        dragToggle = findViewById(R.id.dragToggle);
        sb = (SeekBar) findViewById(R.id.sb);
        tvShare = (TextView) findViewById(R.id.tvShare);
        tvAlarm = (TextView) findViewById(R.id.tvAlarm);
        tvRepeat = (TextView) findViewById(R.id.tvRepeat);
        tvHyperText = (TextView) findViewById(R.id.tvHyperText);
        tvHyperText.setMovementMethod(LinkMovementMethod.getInstance());
        tvCreditSum = (TextView) findViewById(R.id.tvCreditSum);
        creditSum = Common.calCredit();
        tvCreditSum.setText(creditSum);
        etName = (EditText) findViewById(R.id.etName);
        etPlace = (EditText) findViewById(R.id.etPlace);
        etMemo = (EditText) findViewById(R.id.etMemo);
        lvTime = (HorizontalListView) findViewById(R.id.lvTime);
        hlv = (HorizontalListView) findViewById(R.id.hlv);
        actvUniv = (AutoCompleteTextView) findViewById(R.id.actvUniv);
        actvDep = (AutoCompleteTextView) findViewById(R.id.actvDep);
        actvGrade = (AutoCompleteTextView) findViewById(R.id.actvGrade);
        actvSub = (AutoCompleteTextView) findViewById(R.id.actvSub);
        actvProf = (AutoCompleteTextView) findViewById(R.id.actvProf);
        gd = (GradientDrawable) btColor.getBackground().mutate();
        setRandomColor();
        btNormal.setOnClickListener(this);
        btUniv.setOnClickListener(this);
        btColor.setOnClickListener(this);
        btAddSchedule.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        btShowUniv.setOnClickListener(this);
        btShowDep.setOnClickListener(this);
        btShowGrade.setOnClickListener(this);
        btEnter.setOnClickListener(this);
        btClear.setOnClickListener(this);
        btNew.setOnClickListener(this);
        btPlace.setOnClickListener(this);
        btShare.setOnClickListener(this);
        btAlarm.setOnClickListener(this);
        btRepeat.setOnClickListener(this);
        dragToggle.setOnTouchListener(this);
        subOverlapFlag = true;
        User.INFO.overlapFlag = false;
        ttVersion="";
        colorName = Common.MAIN_COLOR;
        brightness=100;
        dy = 0; mPosY = 0; repeatType = 0;
        dayIndex = new HashMap<>();
        sb.setProgress(brightness);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress<10)brightness=10;
                else if(progress>100) brightness=100;
                else brightness =progress;

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                lp.alpha=(float)brightness/100;
                window.setAttributes(lp);
            }
        });
    }
    private Button btNormal, btUniv, btAddSchedule, btCancel, btEnter, btColor,btClear;
    private LinearLayout llNormal, llUniv, llSelectUniv, llDep, btNew, btPlace, btShare, btAlarm, btRepeat;
    private View dragToggle;
    private SeekBar sb;
    private TextView tvShare, tvAlarm, tvRepeat, tvCreditSum, tvHyperText;
    private EditText etName, etPlace, etMemo;
    private ToggleButton btShowUniv,btShowDep, btShowGrade;
    private HorizontalListView lvTime, hlv;
    private HorizontalListAdapter hoAdapter;
    private Window window;
    private GradientDrawable gd;
    private String colorName, subId, creditSum, groupName,ttVersion, beforeYMD; //ttVersion: 학기버전
    private WindowManager.LayoutParams lp;
    private ArrayList<BottomNormalData> normalList;
    private ArrayAdapter<String> univAdapter;
    private ArrayList<String> univList;
    private ArrayAdapter normalAdapter;
    private HashMap<Integer, Integer> dayIndex;//어느 요일이 선택됬는지
    private AutoCompleteTextView actvUniv, actvDep, actvGrade, actvSub, actvProf;
    private DatabaseHandler db;
    private BackPressCloseHandler backPressCloseHandler;
    private int dy, mPosY, screenHeight, viewMode,repeatType,brightness;
    private boolean widgetFlag, overlapEnrollFlag,weekFlag, subOverlapFlag, setColorFlag;
    // Progress Dialog
    private ProgressDialog pDialog;
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
        repeatType=e.getRepeatType();
        tvRepeat.setText(e.toString());
    }

    public void onEventMainThread(SetPlaceEvent e) {
        etPlace.setText(e.getPlace());
    }

    public void onEventMainThread(SetColorEvent e) {
        colorName = getResources().getString(e.getResColor());
        gd.setColor(getResources().getColor(e.getResColor()));
        gd.invalidateSelf();
        setColorFlag=true;
    }

    public void onEventMainThread(BottomNormalData e) {
        normalList.add(e);
        normalAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(ClearNormalEvent e) {
        normalList.clear();
        normalAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(UpdateNormalEvent e) {
        normalList.remove(e.getPosition());
        if(viewMode==0) {
            normalList.add(e.getPosition(), new BottomNormalData(
                    Dates.NOW.getwMonthDay(e.getXth()), e.getStartHour(), e.getStartMin(),
                    e.getEndHour(), e.getEndMin(), e.getXth()));
        }else{
            normalList.add(e.getPosition(), new BottomNormalData(
                    beforeYMD, e.getStartHour(), e.getStartMin(),
                    e.getEndHour(), e.getEndMin(), e.getXth()));
        }
        normalAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(SetCreditEvent e) {
        creditSum=Common.calCredit();
        tvCreditSum.setText(creditSum);
    }

    public void onEventMainThread(ExcuteMethodEvent e) {
        try {
            Method m = DialSchedule.this.getClass().getDeclaredMethod(e.getMethodName());
            m.invoke(DialSchedule.this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void onEventMainThread(PostGroupListEvent e){
        univList.clear();
        for (GroupListData.Data d :  User.INFO.groupListData) {
            univList.add(d.getKo() + "/" + d.getTt_version() + "/" +d.getDb_version());
        }
        univAdapter.notifyDataSetChanged();
    }
    private final int REQUEST_STORAGE = 101;
    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                    // Explain to the user why we need to write the permission.
                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_STORAGE);
                // MY_PERMISSION_REQUEST_STORAGE is an
                // app-defined int constant
            }else {
                // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
                //firstSetting();
            }

        }else {
            // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
            //firstSetting();
        }
    }
}
