
package com.daemin.enumclass;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.daemin.adapter.HorizontalListAdapter;
import com.daemin.common.AsyncCallback;
import com.daemin.common.AsyncExecutor;
import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.common.CurrentTime;
import com.daemin.common.DatabaseHandler;
import com.daemin.common.HorizontalListView;
import com.daemin.common.MyRequest;
import com.daemin.data.SubjectData;
import com.daemin.repository.GroupListFromServerRepository;
import com.daemin.timetable.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * Created by hernia on 2015-06-27.
 */


public enum EnumDialog implements View.OnClickListener {
    BOTTOMDIAL("BottomDialogUpByBtn") {
        String sYear, sMonthOfYear, sDayOfMonth, startHour, startMinute, endHour, endMinute, sDayOfWeek;
        Button btChangeYMD, btTimeStart, btTimeEnd;
        CurrentTime ct;
        int sDayOfWeekIndex;
        String timeDialSetFlag="";

        @Override
        public void DialogSetting() {
            ct = new CurrentTime();
            sYear = ct.getCurYear();
            sMonthOfYear = ct.getCurMonth();
            sDayOfMonth = ct.getCurDay();
            startHour = ct.getCurHour();
            startMinute = "00";
            endHour = Convert.IntAddO(Integer.parseInt(startHour) + 1);
            endMinute = "00";
            sDayOfWeekIndex = ct.getDayOfWeekIndex();
            sDayOfWeek = Convert.IndexToDayOfWeek(sDayOfWeekIndex);
            super.DialogSetting();
            btChangeYMD = (Button) dialog.findViewById(R.id.btChangeYMD);
            btTimeStart = (Button) dialog.findViewById(R.id.btTimeStart);
            btTimeEnd = (Button) dialog.findViewById(R.id.btTimeEnd);

        }

        @Override
        public void EnrollEvent() {
            super.EnrollEvent();
            btAddTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (7 < Integer.parseInt(startHour) && Integer.parseInt(startHour) < 23
                            && 7 < Integer.parseInt(endHour) && Integer.parseInt(endHour) < 24
                            && 0 < Integer.parseInt(endHour) - Integer.parseInt(startHour)) {
                        TimePos.valueOf(Convert.getxyMerge(
                                Convert.DayOfWeekToXth(sDayOfWeekIndex),
                                Convert.HourOfDayToYth(startHour))).setPosState(PosState.START);
                        dialog.cancel();
                    } else {
                        btTimeStart.setTextColor(context.getResources().getColor(R.color.red));
                        btTimeEnd.setTextColor(context.getResources().getColor(R.color.red));
                    }
                }
            });
            //년도 셋팅
            btChangeYMD.setText(sYear + "-" + sMonthOfYear
                    + "-" + sDayOfMonth + "   " + sDayOfWeek);
            btChangeYMD.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    final Dialog dialog = new Dialog(context, android.R.style.Theme_Holo_Light_Dialog);
                    DialogInit(dialog, "Calendar");
                    cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

                        @Override
                        public void onSelectedDayChange(CalendarView view, int year, int monthOfYear,
                                                        int dayOfMonth) {
                            // TODO Auto-generated method stub
                            sYear = String.valueOf(year);
                            sDayOfWeek = Convert.getDayofWeek(year, monthOfYear, dayOfMonth);

                            sDayOfWeek = Convert.getDayofWeek(year, monthOfYear, dayOfMonth);
                            ++monthOfYear;
                            if (monthOfYear < 10) {
                                sMonthOfYear = Convert.IntAddO(monthOfYear);
                            } else {
                                sMonthOfYear = String.valueOf(monthOfYear);
                            }
                            if (dayOfMonth < 10) {
                                sDayOfMonth = Convert.IntAddO(dayOfMonth);
                            } else {
                                sDayOfMonth = String.valueOf(dayOfMonth);
                            }
                            sDayOfWeekIndex = Convert.DayOfWeekToIndex(sDayOfWeek);
                        }
                    });
                    btDialCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                    btSetting.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            btChangeYMD.setText(sYear + "-" + sMonthOfYear + "-" + sDayOfMonth + "   " + sDayOfWeek);
                            dialog.cancel();
                        }
                    });

                    dialog.show();
                }
            });
            //시작시간 셋팅
            btTimeStart.setText(startHour + " : " + startMinute);
            btTimeStart.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    timeDialSetFlag = "btTimeStart";
                    TimePickerDialog dialog = new TimePickerDialog(context,
                            android.R.style.Theme_Holo_Light_Dialog,
                            Timelistener, Integer.parseInt(startHour), 0, true);
                    DialogInit(dialog, "btTime");
                    dialog.show();
                }
            });
            //끝나는 시간 셋팅
            btTimeEnd.setText(endHour + " : " + endMinute);
            btTimeEnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timeDialSetFlag = "btTimeEnd";
                    TimePickerDialog dialog = new TimePickerDialog(context,
                            android.R.style.Theme_Holo_Light_Dialog,
                            Timelistener, Integer.parseInt(endHour), 0, true);
                    DialogInit(dialog, "btTime");
                    dialog.show();
                }
            });
        }

        private DatePickerDialog.OnDateSetListener Datelistener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                sMonthOfYear = "";
                sDayOfMonth = "";

                sDayOfWeek = Convert.getDayofWeek(year, monthOfYear, dayOfMonth);
                ++monthOfYear;

                if (monthOfYear < 10) {
                    sMonthOfYear = Convert.IntAddO(monthOfYear);
                } else {
                    sMonthOfYear = String.valueOf(monthOfYear);
                }
                if (dayOfMonth < 10) {
                    sDayOfMonth = Convert.IntAddO(dayOfMonth);
                } else {
                    sDayOfMonth = String.valueOf(dayOfMonth);
                }
                sDayOfWeekIndex = Convert.DayOfWeekToIndex(sDayOfWeek);
                btChangeYMD.setText(year + "-" + sMonthOfYear + "-" + sDayOfMonth + "   " + sDayOfWeek);
            }
        };
        private TimePickerDialog.OnTimeSetListener Timelistener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                switch (timeDialSetFlag) {
                    case "btTimeStart":
                        if (minute < 10) {
                            startMinute = Convert.IntAddO(minute);
                        } else {
                            startMinute = String.valueOf(minute);
                        }
                        if (hourOfDay < 10) {
                            startHour = Convert.IntAddO(hourOfDay);
                        } else {
                            startHour = String.valueOf(hourOfDay);
                        }
                        endHour = Convert.IntAddO(Integer.parseInt(startHour) + 1);
                        endMinute = startMinute;
                        btTimeStart.setText(startHour + " : " + startMinute);
                        btTimeEnd.setText(endHour + " : " + endMinute);
                        break;
                    case "btTimeEnd":
                        if (minute < 10) {
                            endMinute = Convert.IntAddO(minute);
                        } else {
                            endMinute = String.valueOf(minute);
                        }
                        if (hourOfDay < 10) {
                            endHour = Convert.IntAddO(hourOfDay);
                        } else {
                            endHour = String.valueOf(hourOfDay);
                        }
                        btTimeEnd.setText(endHour + " : " + endMinute);
                        break;
                }
                if (7 < Integer.parseInt(startHour) && Integer.parseInt(startHour) < 23
                        && 7 < Integer.parseInt(endHour) && Integer.parseInt(endHour) < 24
                        && 0 < Integer.parseInt(endHour) - Integer.parseInt(startHour)) {
                    btTimeStart.setTextColor(context.getResources().getColor(R.color.black));
                    btTimeEnd.setTextColor(context.getResources().getColor(R.color.black));
                } else {
                    btTimeStart.setTextColor(context.getResources().getColor(R.color.red));
                    btTimeEnd.setTextColor(context.getResources().getColor(R.color.red));
                }
            }
        };
    };

    String dialFlag = "",colorName,korName,engName;
    Button btNormal, btUniv, btCancel, btAddTime, btSetting, btColor, btDialCancel,btRecommend;
    LinearLayout llColor, llNormal, llUniv, llIncludeUniv, llIncludeDep, llRecommend,llIncludeDummy;
    Dialog dialog;
    CalendarView cal;
    GradientDrawable gd;
    Boolean colorFlag = false;
    Context context;
    AutoCompleteTextView actvSelectUniv;
    Button btShowDropDown, btForward;
    Boolean clickFlag=false;


    EnumDialog(String dialFlag) {
        this.dialFlag = dialFlag;
    }
    public void DialogInit(Dialog dialog, String dialFlag) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        switch (dialFlag) {
            case "BottomDialogUpByBtn":
                dialog.setContentView(R.layout.bottom_dialog_main);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
                window.setGravity(Gravity.BOTTOM);
                break;
            case "btTime":
                break;
            case "Calendar":
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_calendar);
                cal = (CalendarView) dialog.findViewById(R.id.calendarView);
                btDialCancel = (Button) dialog.findViewById(R.id.btDialCancel);
                btSetting = (Button) dialog.findViewById(R.id.btSetting);
                return;
        }

        window.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        ViewGroup.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        params.height = dm.heightPixels/3;
        window.setAttributes((WindowManager.LayoutParams) params);

    }
    public void DialogSetting() {
        btCancel = (Button) dialog.findViewById(R.id.btCancel);
        btAddTime = (Button) dialog.findViewById(R.id.btAddTime);
        btColor = (Button) dialog.findViewById(R.id.btColor);
        btRecommend = (Button) dialog.findViewById(R.id.btRecommend);
        llColor = (LinearLayout) dialog.findViewById(R.id.llColor);
        llIncludeDummy = (LinearLayout) dialog.findViewById(R.id.llIncludeDummy);
        llNormal = (LinearLayout) dialog.findViewById(R.id.llNormal);
        llUniv = (LinearLayout) dialog.findViewById(R.id.llUniv);
        llRecommend = (LinearLayout) dialog.findViewById(R.id.llRecommend);
        llIncludeUniv = (LinearLayout) dialog.findViewById(R.id.llIncludeUniv);
        llIncludeDep = (LinearLayout) dialog.findViewById(R.id.llIncludeDep);
        btNormal = (Button) dialog.findViewById(R.id.btNormal);
        btUniv = (Button) dialog.findViewById(R.id.btUniv);
        hlv = (HorizontalListView) dialog.findViewById(R.id.hlv);
        colorName = Common.MAIN_COLOR;
    }
    public void EnrollEvent() {
        btCancel.setOnClickListener(this);
        btNormal.setOnClickListener(this);
        btUniv.setOnClickListener(this);
        btRecommend.setOnClickListener(this);
        gd = (GradientDrawable) btColor.getBackground().mutate();
        btColor.setOnClickListener(this);

        String[] dialogColorBtn = context.getResources().getStringArray(R.array.dialogColorBtn);
        for (int i = 0; i < dialogColorBtn.length; i++) {
            int resID = context.getResources().getIdentifier(dialogColorBtn[i], "id", context.getPackageName());
            final int resColor = context.getResources().getIdentifier(dialogColorBtn[i], "color", context.getPackageName());
            ImageButton B = (ImageButton) dialog.findViewById(resID);
            B.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llColor.setVisibility(View.INVISIBLE);
                    colorFlag = false;
                    colorName = context.getResources().getString(resColor);
                    gd.setColor(context.getResources().getColor(resColor));
                    gd.invalidateSelf();
                }
            });
        }
    }
    public void setContext(Context context) {
        this.context = context;
        dialog = new Dialog(context);
        DialogInit(dialog, dialFlag);
        DialogSetting();
        EnrollEvent();
    }
    public void Show(){
        dialog.show();
    }
    public void Cancel(){
        dialog.cancel();
    }


    private HorizontalListView hlv;

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btCancel:
                dialog.cancel();
                break;
            case R.id.btNormal:
                DrawMode.CURRENT.setMode(0);
                Common.stateFilter(Common.getTempTimePos());
                llNormal.setVisibility(View.VISIBLE);
                llUniv.setVisibility(View.GONE);
                llRecommend.setVisibility(View.GONE);
                btAddTime.setVisibility(View.VISIBLE);
                btNormal.setTextColor(context.getResources().getColor(
                        R.color.white));
                btUniv.setTextColor(context.getResources().getColor(
                        R.color.gray));
                btRecommend.setTextColor(context.getResources().getColor(
                        R.color.gray));
                break;
            case R.id.btUniv:
                DrawMode.CURRENT.setMode(1);
                Common.stateFilter(Common.getTempTimePos());
                llNormal.setVisibility(View.GONE);
                llUniv.setVisibility(View.VISIBLE);
                llRecommend.setVisibility(View.GONE);
                btNormal.setTextColor(context.getResources().getColor(
                        R.color.gray));
                btUniv.setTextColor(context.getResources().getColor(
                        R.color.white));
                btRecommend.setTextColor(context.getResources().getColor(
                        R.color.gray));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                        R.layout.dropdown_search, MyRequest.getGroupListFomServer());
                actvSelectUniv = (AutoCompleteTextView) dialog.findViewById(R.id.actvSelectUniv);
                actvSelectUniv.requestFocus();
                actvSelectUniv.setThreshold(1);// will start working from first character
                actvSelectUniv.setAdapter(adapter);// setting the adapter data into the
                actvSelectUniv.setTextColor(Color.DKGRAY);
                actvSelectUniv.setTextSize(16);
                actvSelectUniv.setDropDownVerticalOffset(10);
                btForward = (Button) dialog.findViewById(R.id.btForward);
                btShowDropDown = (Button) dialog.findViewById(R.id.btShowDropDown);
                actvSelectUniv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        korName = actvSelectUniv.getText().toString();
                        User.USER.setKorUnivName(korName);
                        engName = GroupListFromServerRepository
                                .getEngByKor(context, korName);
                        User.USER.setEngUnivName(engName);


                        // 열려있는 키패드 닫기
                        InputMethodManager imm = (InputMethodManager) context
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(actvSelectUniv.getWindowToken(), 0);
                        btShowDropDown.setVisibility(View.GONE);
                        btForward.setVisibility(View.VISIBLE);

                        btForward.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(Common.isOnline()){
                                    if(User.USER.isSubjectDownloadState()){
                                        Toast.makeText(context, "이미 다운로드된 상태이므로 과목셋팅만", Toast.LENGTH_SHORT).show();
                                        setupSubjectDatas();
                                    }
                                    else {
                                        Toast.makeText(context, "첫 과목 다운로드", Toast.LENGTH_SHORT).show();
                                        DownloadSqlite(engName);

                                    }
                                }else{
                                    if(User.USER.isSubjectDownloadState()){
                                        Toast.makeText(context, "네트워크는 비연결, 오프라인으로 조회", Toast.LENGTH_SHORT).show();
                                        setupSubjectDatas();
                                    }
                                    else {
                                        Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                });
                btShowDropDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickFlag) {
                            actvSelectUniv.dismissDropDown();
                            btShowDropDown.setBackgroundResource(R.drawable.ic_action_expand);
                            clickFlag = false;
                            if(!User.USER.isGroupListDownloadState()) {
                                MyRequest.getGroupList();
                            }
                        } else {
                            actvSelectUniv.showDropDown();
                            btShowDropDown.setBackgroundResource(R.drawable.ic_action_collapse);
                            clickFlag = true;
                        }
                    }
                });

                actvSelectUniv.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        btShowDropDown.setBackgroundResource(R.drawable.ic_action_expand);
                    }
                });
                break;

            case R.id.btRecommend:
                DrawMode.CURRENT.setMode(2);
                Common.stateFilter(Common.getTempTimePos());
                llNormal.setVisibility(View.GONE);
                llUniv.setVisibility(View.GONE);
                llRecommend.setVisibility(View.VISIBLE);
                btNormal.setTextColor(context.getResources().getColor(
                        R.color.gray));
                btUniv.setTextColor(context.getResources().getColor(
                        R.color.gray));
                btRecommend.setTextColor(context.getResources().getColor(
                        R.color.white));
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
        }
    }

     private void setupSubjectDatas() {
            llIncludeDep.setVisibility(View.VISIBLE);
            DatabaseHandler db = new DatabaseHandler(context);
            List<SubjectData> contacts = db.getAllSubjectDatas();
            HorizontalListAdapter adapter = new HorizontalListAdapter(context, contacts);
            User.USER.setSubjectDownloadState(true);
            hlv.setAdapter(adapter);
            hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     ArrayList<String> tempTimePos = new ArrayList<>();
                     Common.stateFilter(Common.getTempTimePos());
                     for (String timePos : getTimeList(((TextView) view.findViewById(R.id.time)).getText()
                             .toString())) {
                         tempTimePos.add(timePos);
                         TimePos.valueOf(timePos).setPosState(PosState.TEMPORARY);
                     }
                     Common.setTempTimePos(tempTimePos);
                 }
             });
     }
    private String[] getTimeList(String time){
            String[] timeList=null;
            try {
                if(!time.equals(null))
                    timeList = time.split("/");
                else
                    return timeList;
            }catch(NullPointerException e){
                Toast.makeText(context, context.getString(R.string.e_learning), Toast.LENGTH_SHORT).show();
            }
            return timeList;
    }

    public void DownloadSqlite(final String univName) {
        // 비동기로 실행될 코드
        Callable<Void> callable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                int count;
                try {
                    URL url = new URL("http://hernia.cafe24.com/android/db/"+univName+".sqlite");
                    URLConnection conection = url.openConnection();
                    conection.connect();

                    // input stream to read file - with 8k buffer
                    createFolder();
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    OutputStream output = new FileOutputStream("/sdcard/.TimeDAO/"+univName+".sqlite");

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

        };

        new AsyncExecutor<Void>()
                .setCallable(callable)
                .setCallback(callback)
                .execute();
    }

    // 비동기로 실행된 결과를 받아 처리하는 코드
    private AsyncCallback<Void> callback = new AsyncCallback<Void>() {
        @Override
        public void onResult(Void result) {
            User.USER.setSubjectDownloadState(true);
            setupSubjectDatas();
        }

        @Override
        public void exceptionOccured(Exception e) {
        }

        @Override
        public void cancelled() {
        }
    };
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
}

