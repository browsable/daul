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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.daemin.data.BottomNormalData;
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
import com.daemin.event.SetAlarmEvent;
import com.daemin.event.SetBtPlusEvent;
import com.daemin.event.SetBtUnivEvent;
import com.daemin.event.SetBtUnivNoticeEvent;
import com.daemin.event.SetColorEvent;
import com.daemin.event.SetPlaceEvent;
import com.daemin.event.SetRepeatEvent;
import com.daemin.event.SetShareEvent;
import com.daemin.event.UpdateNormalEvent;
import com.daemin.main.MainActivity;
import com.daemin.map.MapActivity;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.InitSurfaceView;
import com.daemin.timetable.R;
import com.daemin.widget.WidgetUpdateService;

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

public class DialSchedule extends Activity implements View.OnClickListener, View.OnTouchListener {
    public void onBackPressed() {
        EventBus.getDefault().post(new SetBtPlusEvent(true));
        DrawMode.CURRENT.setMode(0);
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
        User.INFO.getEditor().putString("creditSum", tvCreditSum.getText().toString()).commit();
        Common.stateFilter(viewMode);
        DrawMode.CURRENT.setMode(0);
        System.gc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_schedule);
        EventBus.getDefault().post(new SetBtPlusEvent(false));
        if (!getIntent().equals(null)) {//widget에서 Dialog 호출한 경우
            widgetFlag = getIntent().getBooleanExtra("widgetFlag", false);
            enrollFlag = getIntent().getBooleanExtra("enrollFlag", false);
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
        screenHeight = dm.heightPixels * 3 / 5;
        lp.height = screenHeight * 22 / 36;
        lp.width = lp.MATCH_PARENT;
        if (enrollFlag) {
            llButtonArea.setVisibility(View.VISIBLE);
            rlEdit.setVisibility(View.VISIBLE);
            llEtName.setVisibility(View.GONE);
            llTime.setVisibility(View.GONE);
            btUniv.setVisibility(View.GONE);
            btNormal.setVisibility(View.GONE);
            btNew.setVisibility(View.GONE);
            lp.height = lp.WRAP_CONTENT;
            int startHour = Integer.parseInt(Convert.YthToHourOfDay(getIntent().getIntExtra("yth", 0)));
            int startMin = getIntent().getIntExtra("startMin", 0);
            long startmillis = Dates.NOW.getDateMillis(
                    Dates.NOW.year,
                    Dates.NOW.month,
                    Dates.NOW.getXthToDay(getIntent().getIntExtra("xth", 0)),
                    startHour, startMin);
            MyTime mt = MyTimeRepo.getEnrollTime(this, startmillis,getIntent().getIntExtra("xth", 0),startHour,startMin);
            tvTimeCode.setText(mt.getTimecode());
            etSavedName.setText(mt.getName());
            etMemo.setText(mt.getMemo());
            etPlace.setText(mt.getPlace());
            try {
                tvShare.setText(Convert.revertShare(mt.getShare()));
            }catch(NullPointerException e){
                tvShare.setText(Convert.revertShare(0));
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
        int tmpXth = 0, tmpYth = 0;
        String YMD = "";
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
        @Override

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... univName) {
            int count;
            try {
                URL url = new URL("http://hernia.cafe24.com/android/db/" + univName[0] + "/subject.sqlite");
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

    public void addWeek(int xth, int startHour, int startMin, int endHour, int endMin) {
        if (endMin != 0) ++endHour;
        else endMin = 60;

        TimePos[] tp = new TimePos[endHour - startHour];
        int j = 0;
        for (int i = startHour; i < endHour; i++) {
            tp[j] = TimePos.valueOf(Convert.getxyMerge(xth, Convert.HourOfDayToYth(i)));
            if (tp[j].getPosState() == PosState.NO_PAINT) {
                if (i == startHour && startMin != 0) tp[j].setMin(startMin, 60);
                if (i == endHour - 1) tp[j].setMin(0, endMin);
                tp[j].setPosState(PosState.PAINT);
                Common.getTempTimePos().add(tp[j].name());
            }else if(tp[j].getPosState() == PosState.ENROLL){
                tp[j].setColor("#FF0000");//RED
                overlapFlag = true;
            }
            ++j;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void settingUniv() {
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
                depAdapter = new ArrayAdapter<>(this, R.layout.dropdown_dep, depList),
                gradeAdapter = new ArrayAdapter<>(this, R.layout.dropdown_dep, gradeList),
                subAdapter = new ArrayAdapter<>(this, R.layout.dropdown_dep, subOrProfList);
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
                String[] temps;
                subId = ((TextView) view.findViewById(R.id._id)).getText().toString();
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
                subjects.addAll(db.getAllWithDepAndGrade(actvDep.getText().toString(), Convert.indexOfGrade(actvGrade.getText().toString())));
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
                DialColor dc = new DialColor(DialSchedule.this);
                dc.show();
                break;
            case R.id.btEdit:
                if (btEdit.isChecked()) {
                    etSavedName.setEnabled(true);
                    etSavedName.requestFocus();
                    etSavedName.setTextColor(Color.GRAY);
                    etSavedName.setSelection(etSavedName.length());
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                } else {
                    etSavedName.setSelection(0);
                    etSavedName.setTextColor(Color.WHITE);
                    etSavedName.setEnabled(false);
                }
                break;
            case R.id.btAddSchedule:
                Common.stateFilter(viewMode);
                long nowMillis = Dates.NOW.getNowMillis();
                switch (DrawMode.CURRENT.getMode()) {
                    case 0:
                        if (viewMode == 0) {
                            for (BottomNormalData d : normalList) {
                                int week_endMonth = Dates.NOW.monthOfSat;
                                int monthOfYear = Dates.NOW.month;
                                int year;
                                if (week_endMonth != monthOfYear) year = Dates.NOW.year - 1;
                                else year = Dates.NOW.year;
                                int dayOfMonth = Integer.parseInt(d.getYMD().split("\\.")[1]);
                                int startHour = Integer.parseInt(d.getStartHour());
                                int startMin = Integer.parseInt(d.getStartMin());
                                int endHour = Integer.parseInt(d.getEndHour());
                                int endMin = Integer.parseInt(d.getEndMin());
                                long startMillis = Dates.NOW.getDateMillis(year, monthOfYear, dayOfMonth, startHour, startMin);
                                long endMillis = Dates.NOW.getDateMillis(year, monthOfYear, dayOfMonth, endHour, endMin);
                                MyTime myTime = new MyTime(null,
                                        String.valueOf(nowMillis),0,
                                        etName.getText().toString(),
                                        year, monthOfYear, dayOfMonth,
                                        d.getXth(), startHour, startMin, endHour, endMin,
                                        startMillis, endMillis,
                                        etMemo.getText().toString(),
                                        etPlace.getText().toString(),
                                        User.INFO.latitude, User.INFO.longitude,
                                        Convert.Share(tvShare.getText().toString()),
                                        Convert.Alarm(startMillis, tvAlarm.getText().toString()),
                                        "10:10",
                                        colorName);
                                MyTimeRepo.insertOrUpdate(this, myTime);
                                Common.fetchWeekData();
                            }
                        } else {


                        }
                        break;
                    case 1:
                        if (subId!=null) {
                            SubjectData subjectData = db.getSubjectData(subId);
                            String[] temps;
                            String subtitle = subjectData.getSubtitle();
                            String prof = subjectData.getProf();
                            String credit = subjectData.getCredit();
                            String classnum = subjectData.getClassnum();
                            for (String timePos : getTimeList(subjectData.getTime())) {
                                temps = timePos.split(":");
                                if (!temps[0].equals(" ")) {
                                    MyTime myTime = new MyTime(null,
                                            User.INFO.groupPK+subtitle+prof,1,
                                            subtitle,
                                            null, null, null,
                                            Integer.parseInt(temps[0]),
                                            Integer.parseInt(temps[1]),
                                            Integer.parseInt(temps[2]),
                                            Integer.parseInt(temps[3]),
                                            Integer.parseInt(temps[4]),
                                            null, null,
                                            prof + "교수/" + credit + "학점/" + classnum + "분반",
                                            null,
                                            User.INFO.latitude, User.INFO.longitude,
                                            null,
                                            null,
                                            "10:10",
                                            colorName);
                                    MyTimeRepo.insertOrUpdate(this, myTime);
                                } else {
                                    Toast.makeText(DialSchedule.this, main.getResources().getString(R.string.univ_notice_emtpy), Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            tvCreditSum.setText(String.valueOf(Integer.parseInt(tvCreditSum.getText().toString())
                                    +Integer.parseInt(credit)));
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
                break;
            case R.id.btCancel:
                EventBus.getDefault().post(new SetBtPlusEvent(true));
                EventBus.getDefault().postSticky(new SetBtUnivEvent(true));
                //btUniv.setVisibility(View.VISIBLE);
                DrawMode.CURRENT.setMode(0);
                finish();
                break;
            case R.id.btCommunity:
                break;
            case R.id.btInvite:
                break;
            case R.id.btRemove:
                MyTimeRepo.deleteWithTimeCode(this, tvTimeCode.getText().toString());
                String creditSum = String.valueOf(Integer.parseInt(User.INFO.getCreditSum())
                                -Integer.parseInt(etMemo.getText().toString().split("/")[1].substring(0, 1)));
                User.INFO.getEditor().putString("creditSum",creditSum).commit();
                tvCreditSum.setText(creditSum);
                Common.fetchWeekData();
                EventBus.getDefault().post(new SetBtPlusEvent(true));
                EventBus.getDefault().postSticky(new SetBtUnivEvent(true));
                finish();
                break;
            case R.id.btNew:
                DialAddTimePicker datp = null;
                switch (viewMode) {
                    case 0:
                        datp = new DialAddTimePicker(DialSchedule.this, Dates.NOW.getWData());
                        break;
                    case 2:
                        datp = new DialAddTimePicker(DialSchedule.this,  Dates.NOW.mData, Dates.NOW.dayOfWeek);
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
                for (BottomNormalData d : normalList) {
                    if (!dayIndex.containsKey(d.getXth()))
                        dayIndex.put(d.getXth(), d.getXth());
                }
                DialRepeat dr = new DialRepeat(DialSchedule.this, dayIndex);
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
        switch (v.getId()) {
            case R.id.dragToggle:
                univFlag = false;
                depFlag = false;
                gradeFlag = false;
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
        if (widgetFlag) {
            viewMode = User.INFO.getViewMode();
            initSurfaceView = new InitSurfaceView(this, viewMode);
            btUniv.setVisibility(View.INVISIBLE);
        } else {
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
        btColor = (Button) findViewById(R.id.btColor);
        btShowUniv = findViewById(R.id.btShowUniv);
        btShowDep = findViewById(R.id.btShowDep);
        btShowGrade = findViewById(R.id.btShowGrade);
        btEdit = (ToggleButton) findViewById(R.id.btEdit);
        llNormal = (LinearLayout) findViewById(R.id.llNormal);
        llButtonArea = (LinearLayout) findViewById(R.id.llButtonArea);
        llEtName = (LinearLayout) findViewById(R.id.llEtName);
        llUniv = (LinearLayout) findViewById(R.id.llUniv);
        btNew = (LinearLayout) findViewById(R.id.btNew);
        btPlace = (LinearLayout) findViewById(R.id.btPlace);
        btShare = (LinearLayout) findViewById(R.id.btShare);
        btAlarm = (LinearLayout) findViewById(R.id.btAlarm);
        btRepeat = (LinearLayout) findViewById(R.id.btRepeat);
        llDep = (LinearLayout) findViewById(R.id.llDep);
        llSelectUniv = (LinearLayout) findViewById(R.id.llSelectUniv);
        llTime = (LinearLayout) findViewById(R.id.llTime);
        rlEdit = (RelativeLayout) findViewById(R.id.rlEdit);
        dragToggle = findViewById(R.id.dragToggle);
        tvShare = (TextView) findViewById(R.id.tvShare);
        tvAlarm = (TextView) findViewById(R.id.tvAlarm);
        tvRepeat = (TextView) findViewById(R.id.tvRepeat);
        tvTimeCode = (TextView) findViewById(R.id.tvTimeCode);
        btUnivNotice = (TextView) findViewById(R.id.btUnivNotice);
        tvCreditSum = (TextView) findViewById(R.id.tvCreditSum);
        tvCreditSum.setText(User.INFO.getCreditSum());
        etName = (EditText) findViewById(R.id.etName);
        etPlace = (EditText) findViewById(R.id.etPlace);
        etMemo = (EditText) findViewById(R.id.etMemo);
        etSavedName = (EditText) findViewById(R.id.etSavedName);
        lvTime = (HorizontalListView) findViewById(R.id.lvTime);
        hlv = (HorizontalListView) findViewById(R.id.hlv);
        actvUniv = (AutoCompleteTextView) findViewById(R.id.actvUniv);
        actvDep = (AutoCompleteTextView) findViewById(R.id.actvDep);
        actvGrade = (AutoCompleteTextView) findViewById(R.id.actvGrade);
        actvSub = (AutoCompleteTextView) findViewById(R.id.actvSub);
        gd = (GradientDrawable) btColor.getBackground().mutate();
        btNormal.setOnClickListener(this);
        btUniv.setOnClickListener(this);
        btColor.setOnClickListener(this);
        btEdit.setOnClickListener(this);
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
        btNew.setOnClickListener(this);
        btPlace.setOnClickListener(this);
        btShare.setOnClickListener(this);
        btAlarm.setOnClickListener(this);
        btRepeat.setOnClickListener(this);
        dragToggle.setOnTouchListener(this);
        llSelectUniv.setOnTouchListener(this);
        univFlag = false;
        depFlag = false;
        gradeFlag = false;
        overlapFlag = false;
        colorName = Common.MAIN_COLOR;
        dy = mPosY = 0;
        dayIndex = new HashMap<>();
        if (!widgetFlag) {
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

    private Button btNormal, btUniv, btAddSchedule, btCancel, btCommunity, btInvite, btRemove, btEnter, btColor;
    private ToggleButton btEdit;
    private LinearLayout llNormal, llButtonArea, llUniv, llSelectUniv, llDep, btNew, btPlace, btShare, btAlarm, btRepeat, llEtName,llTime;
    private RelativeLayout rlEdit;
    private TextView tvShare, tvAlarm, tvRepeat, btUnivNotice,tvCreditSum,tvTimeCode;
    private EditText etName, etPlace, etMemo, etSavedName;
    private View dragToggle, btShowUniv, btShowDep, btShowGrade;
    private HorizontalListView lvTime, hlv;
    private HorizontalListAdapter hoAdapter;
    private Window window;
    private GradientDrawable gd;
    private String colorName, subId;
    private InitSurfaceView initSurfaceView;
    private MainActivity main;
    private WindowManager.LayoutParams lp;
    private ArrayList<BottomNormalData> normalList;
    private ArrayAdapter normalAdapter;
    private HashMap<Integer, Integer> dayIndex;//어느 요일이 선택됬는지
    private AutoCompleteTextView actvUniv, actvDep, actvGrade, actvSub;
    private DatabaseHandler db;
    private BackPressCloseHandler backPressCloseHandler;
    private int dy, mPosY, screenHeight, viewMode;
    private boolean univFlag, depFlag, gradeFlag, widgetFlag, enrollFlag,overlapFlag;

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

    public void onEventMainThread(SetPlaceEvent e) {
        etPlace.setText(e.getPlace());
    }

    public void onEventMainThread(SetColorEvent e) {
        colorName = getResources().getString(e.getResColor());
        gd.setColor(getResources().getColor(e.getResColor()));
        gd.invalidateSelf();
    }

    public void onEventMainThread(SetBtUnivNoticeEvent e) {
        btUnivNotice.setTextColor(Color.GRAY);
        btUnivNotice.setBackgroundResource(R.drawable.bg_lightgray_bottomline);
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
        normalList.add(e.getPosition(), new BottomNormalData(
                Dates.NOW.getmMonthDay(e.getXth()), e.getStartHour(), e.getStartMin(),
                e.getEndHour(), e.getEndMin(), e.getXth()));
        normalAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(ExcuteMethodEvent e) {
        try {
            Method m = DialSchedule.this.getClass().getDeclaredMethod(e.getMethodName());
            m.invoke(DialSchedule.this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
