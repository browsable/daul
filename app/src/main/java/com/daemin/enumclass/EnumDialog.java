package com.daemin.enumclass;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.daemin.timetable.R;
import com.daemin.timetable.common.Common;
import com.daemin.timetable.common.Convert;
import com.daemin.timetable.common.CurrentTime;

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

    String dialFlag = "",colorName;
    Button btNormal, btUniv, btCancel, btAddTime, btSetting, btColor, btDialCancel;
    LinearLayout llColor, llNormal, llUniv;
    Dialog dialog;
    CalendarView cal;
    GradientDrawable gd;
    Boolean colorFlag = false;
    Context context;

    EnumDialog(String dialFlag) {
        this.dialFlag = dialFlag;
    }
    public void DialogInit(Dialog dialog, String dialFlag) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        switch (dialFlag) {
            case "BottomDialogUpByBtn":
                dialog.setContentView(R.layout.dialog_addtime_bybtn);
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
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
        // This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }
    public void DialogSetting() {
        btCancel = (Button) dialog.findViewById(R.id.btCancel);
        btAddTime = (Button) dialog.findViewById(R.id.btAddTime);
        btColor = (Button) dialog.findViewById(R.id.btColor);
        llColor = (LinearLayout) dialog.findViewById(R.id.llColor);
        llNormal = (LinearLayout) dialog.findViewById(R.id.llNormal);
        llUniv = (LinearLayout) dialog.findViewById(R.id.llUniv);
        btNormal = (Button) dialog.findViewById(R.id.btNormal);
        btUniv = (Button) dialog.findViewById(R.id.btUniv);
        colorName = Common.MAIN_COLOR;
    }
    public void EnrollEvent() {
        btCancel.setOnClickListener(this);
        btNormal.setOnClickListener(this);
        btUniv.setOnClickListener(this);
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btCancel:
                dialog.cancel();
                break;
            case R.id.btNormal:
                llNormal.setVisibility(View.VISIBLE);
                llUniv.setVisibility(View.GONE);
                btNormal.setTextColor(context.getResources().getColor(
                        R.color.white));
                btUniv.setTextColor(context.getResources().getColor(
                        R.color.gray));
                break;
            case R.id.btUniv:
                llNormal.setVisibility(View.GONE);
                llUniv.setVisibility(View.VISIBLE);
                btNormal.setTextColor(context.getResources().getColor(
                        R.color.gray));
                btUniv.setTextColor(context.getResources().getColor(
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
}