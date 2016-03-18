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
import com.daemin.enumclass.Dates;
import com.daemin.event.CreateDialEvent;
import com.daemin.event.EditCheckEvent;
import com.daemin.event.EditRepeatEvent;
import com.daemin.event.FinishDialogEvent;
import com.daemin.event.RemoveEnrollEvent;
import com.daemin.event.SetTimeEvent;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;

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
        if (getIntent() != null) {
            this.xth = getIntent().getIntExtra("xth", 1);
            this.yth = getIntent().getIntExtra("yth", 1);
            this.weekFlag = getIntent().getBooleanExtra("weekFlag", true);
            this.startMin = getIntent().getIntExtra("startMin", 1);
        }
        enrollList = new HashMap<>();
        btDialCancel = (Button) findViewById(R.id.btDialCancel);
        llNewEnroll = (LinearLayout) findViewById(R.id.llNewEnroll);
        tvMonthDay = (TextView) findViewById(R.id.tvMonthDay);
        if (weekFlag) {
            startHour = Integer.parseInt(Convert.YthToHourOfDay(yth));
            String wMonthDay = Dates.NOW.getwMonthDay(xth);
            String[] tmp = wMonthDay.split("\\.");
            int year;
            int titleMonth = Dates.NOW.month;
            int monthOfYear = Integer.parseInt(tmp[0]);
            if (monthOfYear != titleMonth && titleMonth == 1) year = Dates.NOW.year - 1;
            else year = Dates.NOW.year;
            dayOfMonth = Integer.parseInt(tmp[1]);
            mtList = MyTimeRepo.getHourTimes(DialEnroll.this,
                    Dates.NOW.getDateMillis(year, monthOfYear, dayOfMonth, startHour, 0),//startmillis
                    Dates.NOW.getDateMillis(year, monthOfYear, dayOfMonth, startHour + 1, 0) - 1,//endmillis
                    xth, startHour, 1, 59);
            if (xth == 1) {
                tvMonthDay.setTextColor(getResources().getColor(R.color.red));
            } else if (xth == 13) {
                tvMonthDay.setTextColor(getResources().getColor(R.color.blue));
            }
            String title = tmp[0] + getResources().getString(R.string.month) + " " +
                    tmp[1] + getResources().getString(R.string.day) + " " + Convert.XthToDayOfWeek(xth);
            tvMonthDay.setText(title);
        } else {
            dayOfMonth = Integer.parseInt(Dates.NOW.getmMonthDay(xth - 1, 7 * (yth - 1)));
            mtList = MyTimeRepo.getOneDayTimes(DialEnroll.this,
                    Dates.NOW.year, Dates.NOW.month, dayOfMonth);
            if (xth == 1) {
                tvMonthDay.setTextColor(getResources().getColor(R.color.red));
            } else if (xth == 7) {
                tvMonthDay.setTextColor(getResources().getColor(R.color.blue));
            }
            String title = Dates.NOW.month + getResources().getString(R.string.month)
                    + " " + dayOfMonth + getResources().getString(R.string.day) + " " + Convert.XthToDayOfWeekInMonth(xth);
            tvMonthDay.setText(title);
        }
        for (MyTime m : mtList) {
            enrollList.put(m.getId(), m);
        }
        llNewEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new FinishDialogEvent());
                Intent i = new Intent(DialEnroll.this, DialSchedule.class);
                i.putExtra("overlapEnrollFlag", true);
                if (weekFlag) i.putExtra("weekFlag", true);
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
                if (weekFlag) Common.fetchWeekData();
                else Common.fetchMonthData();
                finish();
            }
        });
        lv = (ListView) findViewById(R.id.lv);
        enrollAdapter = new EnrollAdapter(DialEnroll.this, mtList);
        lv.setAdapter(enrollAdapter);
    }

    private int xth, yth, startHour, startMin, dayOfMonth;
    private Boolean weekFlag;
    private TextView tvMonthDay;
    private Button btDialCancel;
    private ListView lv;
    private List<MyTime> mtList;
    private HashMap enrollList;
    private LinearLayout llNewEnroll;
    private EnrollAdapter enrollAdapter;
    public void onEventMainThread(RemoveEnrollEvent e) {
        MyTime mt = (MyTime) enrollList.get(e.getId());
        mtList.remove(mt);
        if(mtList.size()==0) finish();
        else
            enrollAdapter.notifyDataSetChanged();
        if (weekFlag) Common.fetchWeekData();
        else Common.fetchMonthData();
    }
    public void onEventMainThread(EditRepeatEvent e) {
        enrollAdapter.getItem(e.getPosition()).setRepeat(e.toString());
    }

    public void onEventMainThread(SetTimeEvent e) {
        int position = e.getPosition();
        if(e.getTimeType()==0){
            enrollAdapter.getItem(position).setDayofmonth(e.getDay());
            e.getTvMD().setText(Dates.NOW.month + getResources().getString(R.string.month) + " " + e.getDay() + getResources().getString(R.string.day));
        }
        else {
            enrollAdapter.getItem(position).setDayofweek(e.getDay());
            e.getTvMD().setText(Convert.XthToDayOfWeek(e.getDay()));
        }
        enrollAdapter.getItem(position).setStarthour(e.getStartHour());
        enrollAdapter.getItem(position).setStartmin(e.getStartMin());
        enrollAdapter.getItem(position).setEndhour(e.getEndHour());
        enrollAdapter.getItem(position).setEndmin(e.getEndMin());

        String time = Convert.IntToString(e.getStartHour()) + ":"
                + Convert.IntToString(e.getStartMin()) + "~"
                + Convert.IntToString(e.getEndHour()) + ":"
                + Convert.IntToString(e.getEndMin());
        e.getTvTime().setText(time);
    }
    public void onEventMainThread(EditCheckEvent e) {
        if(e.isReStart()) {
            Intent i = new Intent(this, DialEnroll.class);
            i.putExtra("xth", xth);
            i.putExtra("yth", yth);
            i.putExtra("startMin", startMin);
            i.putExtra("weekFlag", weekFlag);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        finish();

    }


}