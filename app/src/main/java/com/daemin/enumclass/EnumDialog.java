
package com.daemin.enumclass;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
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
        String sYear,sMonthOfYear,sDayOfMonth,startHour,startMinute,endHour,endMinute,sDayOfWeek;
        Button btChangeYMD,btTimeStart,btTimeEnd;
        CurrentTime ct;
        int sDayOfWeekIndex;
        String timeDialSetFlag = "";

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
            /*btChangeYMD = (Button) dialog.findViewById(R.id.btChangeYMD);
            btTimeStart = (Button) dialog.findViewById(R.id.btTimeStart);
            btTimeEnd = (Button) dialog.findViewById(R.id.btTimeEnd);*/

           }
    };


    /*    @Override
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
    };*/

    String dialFlag = "",colorName,korName,engName;
    Button btNormal, btUniv, btCancel, btAddTime, btSetting, btColor, btDialCancel,btRecommend,btUpDown;
    LinearLayout llColor, llNormal, llUniv, llIncludeUniv, llIncludeDep, llRecommend;
    TextView tvRecommendDummy;
    Dialog dialog;
    CalendarView cal;
    GradientDrawable gd;
    Boolean colorFlag = false;
    Context context;
    AutoCompleteTextView actvSelectUniv,actvSelectDep, actvSelectGrade;
    Button btShowUniv,btShowDep,btShowGrade, btForward;
    Boolean clickFlag1=false;
    Boolean clickFlag2=false;
    Boolean clickFlag3=false;
    Boolean clickFlag4=false;
    public void setAdapterFlag(Boolean adapterFlag) {
        this.adapterFlag = adapterFlag;
    }
    Boolean adapterFlag=false;
    WindowManager.LayoutParams layoutParams;
    HorizontalListAdapter adapter;
    DatabaseHandler db;
    public void setDb(DatabaseHandler db) {
        this.db = db;
    }
    private HorizontalListView hlv, hlvRecommend;
    Window window;
    EnumDialog(String dialFlag) {
        this.dialFlag = dialFlag;
    }
    public void DialogInit(Dialog dialog, String dialFlag) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        window = dialog.getWindow();
        switch (dialFlag) {
            case "BottomDialogUpByBtn":
                dialog.setContentView(R.layout.bottom_dialog_main);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        clickFlag4 = false;
                        window.setGravity(Gravity.BOTTOM);
                        layoutParams.y = 0;
                        window.setAttributes(layoutParams);
                        btUpDown.setBackgroundResource(R.drawable.ic_action_collapse);
                        switch (DrawMode.CURRENT.getMode()) {
                            case 0:
                                DrawMode.CURRENT.setMode(0);
                                break;
                            case 1:
                                Common.stateFilter(Common.getTempTimePos());
                                DrawMode.CURRENT.setMode(3);
                                adapterFlag=false;
                                break;
                            case 2:
                                DrawMode.CURRENT.setMode(2);
                                break;
                        }
                    }
                });
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
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
        layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        layoutParams.height = dm.heightPixels/3;
        window.setAttributes(layoutParams);

    }
    public void DialogSetting() {
        btCancel = (Button) dialog.findViewById(R.id.btCancel);
        btAddTime = (Button) dialog.findViewById(R.id.btAddTime);
        btColor = (Button) dialog.findViewById(R.id.btColor);
        btRecommend = (Button) dialog.findViewById(R.id.btRecommend);
        btUpDown = (Button) dialog.findViewById(R.id.btUpDown);
        llColor = (LinearLayout) dialog.findViewById(R.id.llColor);
        llNormal = (LinearLayout) dialog.findViewById(R.id.llNormal);
        llUniv = (LinearLayout) dialog.findViewById(R.id.llUniv);
        llRecommend = (LinearLayout) dialog.findViewById(R.id.llRecommend);
        llIncludeUniv = (LinearLayout) dialog.findViewById(R.id.llIncludeUniv);
        llIncludeDep = (LinearLayout) dialog.findViewById(R.id.llIncludeDep);
        tvRecommendDummy = (TextView) dialog.findViewById(R.id.tvRecommendDummy);
        btNormal = (Button) dialog.findViewById(R.id.btNormal);
        btUniv = (Button) dialog.findViewById(R.id.btUniv);
        hlv = (HorizontalListView) dialog.findViewById(R.id.hlv);
        hlvRecommend = (HorizontalListView) dialog.findViewById(R.id.hlvRecommend);
        colorName = Common.MAIN_COLOR;
    }
    public void EnrollEvent() {
        btCancel.setOnClickListener(this);
        btNormal.setOnClickListener(this);
        btUniv.setOnClickListener(this);
        btRecommend.setOnClickListener(this);
        btUpDown.setOnClickListener(this);
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




    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btCancel:
                Cancel();
                break;
            case R.id.btNormal:
                DrawMode.CURRENT.setMode(0);
                Common.stateFilter(Common.getTempTimePos());
                llNormal.setVisibility(View.VISIBLE);
                llUniv.setVisibility(View.GONE);
                llRecommend.setVisibility(View.GONE);
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
                btShowUniv = (Button) dialog.findViewById(R.id.btShowUniv);
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
                        btShowUniv.setVisibility(View.GONE);
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
                btShowUniv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickFlag1) {
                            actvSelectUniv.dismissDropDown();
                            btShowUniv.setBackgroundResource(R.drawable.ic_action_expand);
                            clickFlag1 = false;

                        } else {
                            if(!User.USER.isGroupListDownloadState()) {
                                MyRequest.getGroupList();
                            }
                            actvSelectUniv.showDropDown();
                            btShowUniv.setBackgroundResource(R.drawable.ic_action_collapse);
                            clickFlag1 = true;

                        }
                    }
                });

                actvSelectUniv.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        btShowUniv.setBackgroundResource(R.drawable.ic_action_expand);
                    }
                });
                break;

            case R.id.btRecommend:
                DrawMode.CURRENT.setMode(2);
                hlvRecommend.setVisibility(View.GONE);
                tvRecommendDummy.setVisibility(View.VISIBLE);
                tvRecommendDummy.setText(context.getResources().getString(R.string.select_time));
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
            case R.id.btUpDown:
                    Window window = dialog.getWindow();
                if (clickFlag4) {
                    window.setGravity(Gravity.BOTTOM);
                    layoutParams.y = 0;
                    window.setAttributes(layoutParams);
                    btUpDown.setBackgroundResource(R.drawable.ic_action_collapse);
                    clickFlag4 = false;
                } else {
                    window.setGravity(Gravity.TOP);
                    layoutParams.y = 100;
                    window.setAttributes(layoutParams);
                    btUpDown.setBackgroundResource(R.drawable.ic_action_expand);
                    clickFlag4 = true;
                }

                break;
        }
    }

     @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
     private void setupSubjectDatas() {
            llIncludeDep.setVisibility(View.VISIBLE);
            Common.setLlIncludeDepIn(true);
            db = new DatabaseHandler(context);
            List<SubjectData> subjects = db.getAllSubjectDatas();
            HorizontalListAdapter adapter = new HorizontalListAdapter(context, subjects);
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
         btShowDep = (Button) dialog.findViewById(R.id.btShowDep);
         btShowGrade = (Button) dialog.findViewById(R.id.btShowGrade);
         ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context,
                 R.layout.dropdown_search, MyRequest.getGroupListFomServer());
         actvSelectDep = (AutoCompleteTextView) dialog.findViewById(R.id.actvSelectDep);
         actvSelectDep.requestFocus();
         actvSelectDep.setThreshold(1);// will start working from first character
         actvSelectDep.setAdapter(adapter2);// setting the adapter data into the
         actvSelectDep.setTextColor(Color.DKGRAY);
         actvSelectDep.setDropDownVerticalOffset(10);
         actvSelectDep.setOnItemClickListener(new AdapterView.OnItemClickListener() {

             public void onItemClick(AdapterView<?> parent, View v,
                                     int position, long id) {

             }
         });
         btShowDep.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (clickFlag2) {
                     actvSelectDep.dismissDropDown();
                     btShowDep.setBackgroundResource(R.drawable.ic_action_expand);
                     clickFlag2 = false;
                     if (!User.USER.isGroupListDownloadState()) {
                         MyRequest.getGroupList();
                     }
                 } else {
                     actvSelectDep.showDropDown();
                     btShowDep.setBackgroundResource(R.drawable.ic_action_collapse);
                     clickFlag2 = true;
                 }
             }
         });
         actvSelectDep.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
             @Override
             public void onDismiss() {
                 btShowDep.setBackgroundResource(R.drawable.ic_action_expand);
             }
         });
         ArrayAdapter<String> adapter3 = new ArrayAdapter<>(context,
                 R.layout.dropdown_search, MyRequest.getGroupListFomServer());
         actvSelectGrade = (AutoCompleteTextView) dialog.findViewById(R.id.actvSelectGrade);
         actvSelectGrade.requestFocus();
         actvSelectGrade.setThreshold(1);// will start working from first character
         actvSelectGrade.setAdapter(adapter3);// setting the adapter data into the
         actvSelectGrade.setTextColor(Color.DKGRAY);
         actvSelectGrade.setDropDownVerticalOffset(10);
         actvSelectGrade.setOnItemClickListener(new AdapterView.OnItemClickListener() {

             public void onItemClick(AdapterView<?> parent, View v,
                                     int position, long id) {

             }
         });
         btShowGrade.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (clickFlag3) {
                     actvSelectGrade.dismissDropDown();
                     btShowGrade.setBackgroundResource(R.drawable.ic_action_expand);
                     clickFlag3 = false;
                     if (!User.USER.isGroupListDownloadState()) {
                         MyRequest.getGroupList();
                     }
                 } else {
                     actvSelectGrade.showDropDown();
                     btShowGrade.setBackgroundResource(R.drawable.ic_action_collapse);
                     clickFlag3 = true;
                 }
             }
         });
         actvSelectGrade.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
             @Override
             public void onDismiss() {
                 btShowGrade.setBackgroundResource(R.drawable.ic_action_expand);
             }
         });
     }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setupRecommendDatas(String time) {
        if(User.USER.isSubjectDownloadState()) {
            //if(db.equals(null)) db = new DatabaseHandler(context);
            List<SubjectData> recommends = db.getRecommendSubjectDatas(time);
            if(recommends.size()==0){
                hlvRecommend.setVisibility(View.GONE);
                tvRecommendDummy.setVisibility(View.VISIBLE);
                tvRecommendDummy.setText(context.getResources().getString(R.string.nothing_schedule));
            }else{
                hlvRecommend.setVisibility(View.VISIBLE);
                tvRecommendDummy.setVisibility(View.GONE);
            }
            if(!adapterFlag) {
                adapter = new HorizontalListAdapter(context, recommends);
                hlvRecommend.setAdapter(adapter);
                hlvRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                adapterFlag=true;
            }else{
                adapter.notifyDataSetChanged();
            }
        }else{
            Toast.makeText(context,"먼저 대학을 선택하세요", Toast.LENGTH_SHORT).show();
        }
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

