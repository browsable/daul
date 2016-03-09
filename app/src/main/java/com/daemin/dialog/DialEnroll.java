package com.daemin.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.daemin.adapter.EnrollAdapter;
import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.data.EnrollData;
import com.daemin.enumclass.Dates;
import com.daemin.enumclass.TimePos;
import com.daemin.event.CreateDialEvent;
import com.daemin.event.EditEvent;
import com.daemin.event.FinishDialogEvent;
import com.daemin.event.RemoveEnrollEvent;
import com.daemin.event.SetTimeEvent;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import timedao.MyTime;

/**
 * Created by hernia on 2015-09-08.
 */
public class DialEnroll extends Activity {

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mtList.clear();
        enrollList.clear();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new CreateDialEvent(true));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_enroll);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT));
        //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        layoutParams.width = dm.widthPixels * 7 / 9;
        layoutParams.height = layoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        setLayout();
    }
    private void setLayout() {
        if(getIntent()!=null) {
            this.xth = getIntent().getIntExtra("xth", 1);
            this.yth = getIntent().getIntExtra("yth", 1);
            this.weekFlag = getIntent().getBooleanExtra("weekFlag", true);
            this.startMin = getIntent().getIntExtra("startMin", 1);
        }
        mtList = new ArrayList<>();
        enrollList = new HashMap<>();
        btDialCancel = (Button) findViewById(R.id.btDialCancel);
        llNewEnroll = (LinearLayout) findViewById(R.id.llNewEnroll);
        tvMonthDay = (TextView) findViewById(R.id.tvMonthDay);
        if(weekFlag) {
            startHour = Integer.parseInt(Convert.YthToHourOfDay(yth));
            String wMonthDay = Dates.NOW.getwMonthDay(xth);
            String[] tmp = wMonthDay.split("\\.");
            int year;
            int titleMonth = Dates.NOW.month;
            int monthOfYear = Integer.parseInt(tmp[0]);
            if (monthOfYear != titleMonth && titleMonth == 1) year = Dates.NOW.year - 1;
            else year = Dates.NOW.year;
            dayOfMonth = Integer.parseInt(tmp[1]);
            enrollMyTime(MyTimeRepo.getHourTimes(DialEnroll.this,
                    Dates.NOW.getDateMillis(year, monthOfYear, dayOfMonth, startHour, 0),//startmillis
                    Dates.NOW.getDateMillis(year, monthOfYear, dayOfMonth, startHour + 1, 0) - 1,//endmillis
                    xth, startHour,1, 59));
            if(xth==1){
                tvMonthDay.setTextColor(getResources().getColor(R.color.red));
            }else if(xth==13){
                tvMonthDay.setTextColor(getResources().getColor(R.color.blue));
            }
            String title= tmp[0]+getResources().getString(R.string.month)+" "+
                    tmp[1]+getResources().getString(R.string.day)+" "+ Convert.XthToDayOfWeek(xth);
            tvMonthDay.setText(title);
        }else{
            dayOfMonth = Integer.parseInt(Dates.NOW.getmMonthDay(xth - 1, 7 * (yth - 1)));
                    enrollMyTime(MyTimeRepo.getOneDayTimes(DialEnroll.this,
                            Dates.NOW.year, Dates.NOW.month, dayOfMonth));
            if(xth==1){
                tvMonthDay.setTextColor(getResources().getColor(R.color.red));
            }else if(xth==7){
                tvMonthDay.setTextColor(getResources().getColor(R.color.blue));
            }
            String title = Dates.NOW.month+getResources().getString(R.string.month)
                    +" "+dayOfMonth+getResources().getString(R.string.day)+" "+Convert.XthToDayOfWeekInMonth(xth);
            tvMonthDay.setText(title);
        }

        llNewEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new FinishDialogEvent());
                Intent i = new Intent(DialEnroll.this, DialSchedule.class);
                i.putExtra("overlapEnrollFlag", true);
                if(weekFlag) i.putExtra("weekFlag", true);
                else i.putExtra("weekFlag", false);
                i.putExtra("xth", xth);
                i.putExtra("yth", yth);
                startActivity(i);
                finish();
            }
        });
        btDialCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv = (ListView) findViewById(R.id.lv);

        Log.i("test", dayOfMonth + "");
        enrollAdapter = new EnrollAdapter(DialEnroll.this, mtList,weekFlag,xth,dayOfMonth);
        lv.setAdapter(enrollAdapter);
        //lv.setItemsCanFocus(true);
    }
    private void enrollMyTime(List<MyTime> mt){
        for(MyTime m : mt){
            int timeType = m.getTimetype();
            if(timeType==0){
                String timeCode = m.getTimecode();
                EnrollData ed = new EnrollData(
                        Dates.NOW.month+getResources().getString(R.string.month)+" "+dayOfMonth+getResources().getString(R.string.day),
                        Convert.IntToString(m.getStarthour()),
                        Convert.IntToString(m.getStartmin()),
                        Convert.IntToString(m.getEndhour()),
                        Convert.IntToString(m.getEndmin()),
                        m.getName(),m.getMemo(),timeCode,
                        String.valueOf(timeType),m.getColor(),m.getPlace(),m.getRepeat(),m.getId());
                mtList.add(ed);
                enrollList.put(timeCode,ed);
            }else{
                String timeCode = m.getTimecode();
                EnrollData ed =new EnrollData(
                        Dates.NOW.month+getResources().getString(R.string.month)+" "+dayOfMonth+getResources().getString(R.string.day),
                        Convert.IntToString(m.getStarthour()),
                        Convert.IntToString(m.getStartmin()),
                        Convert.IntToString(m.getEndhour()),
                        Convert.IntToString(m.getEndmin()),
                        m.getName(), m.getMemo(), timeCode,
                        String.valueOf(timeType),m.getColor(), m.getPlace(),m.getRepeat(), m.getId());
                mtList.add(ed);
                enrollList.put(timeCode, ed);
            }
        }
        mt.clear();
    }
    private int xth,yth,startHour,startMin,dayOfMonth;
    private Boolean weekFlag;
    private TextView tvMonthDay;
    private Button btDialCancel;
    private ListView lv;
    private List<EnrollData> mtList;
    private HashMap enrollList;
    private LinearLayout llNewEnroll;
    private EnrollAdapter enrollAdapter;
    public void onEventMainThread(RemoveEnrollEvent e) {
        mtList.remove(enrollList.get(e.getTimeCode()));
        if(mtList.size()==0){
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }else {
            enrollAdapter.notifyDataSetChanged();
            for (TimePos ETP : TimePos.values()) {
                ETP.setInitText();
            }
            Common.fetchWeekData();
        }
    }
    public void onEventMainThread(SetTimeEvent e) {
        int position = e.getPosition();
        String[] tmp = e.getMd().split("\\.");
        String title= tmp[0]+getResources().getString(R.string.month)+" "+
                tmp[1]+getResources().getString(R.string.day);
        enrollAdapter.getItem(position).setMd(title);
        enrollAdapter.getItem(position).setStartHour(Convert.IntToString(e.getStartHour()));
        enrollAdapter.getItem(position).setStartMin(Convert.IntToString(e.getStartMin()));
        enrollAdapter.getItem(position).setEndHour(Convert.IntToString(e.getEndHour()));
        enrollAdapter.getItem(position).setEndMin(Convert.IntToString(e.getEndMin()));
        enrollAdapter.notifyDataSetChanged();
    }
    public void onEventMainThread(EditEvent e) {
        mtList.clear();
        enrollAdapter.clear();
        finish();
        Intent i = new Intent(this, DialEnroll.class);
        i.putExtra("xth", xth);
        i.putExtra("yth", yth);
        i.putExtra("startMin", startMin);
        i.putExtra("weekFlag", weekFlag);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}