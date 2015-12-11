package com.daemin.dialog;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daemin.adapter.EnrollAdapter;
import com.daemin.adapter.EventListAdapter;
import com.daemin.common.Convert;
import com.daemin.data.EnrollData;
import com.daemin.enumclass.Dates;
import com.daemin.enumclass.User;
import com.daemin.event.RemoveEnrollEvent;
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
public class DialEnroll extends Dialog {
    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public DialEnroll(Context context, int xth, int yth, int startMin) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.context = context;
        this.xth=xth;
        this.yth=yth;
        this.startMin=startMin;
        mtList = new ArrayList<>();
        enrollList = new HashMap<>();
        startHour = Integer.parseInt(Convert.YthToHourOfDay(yth));
        String wMonthDay = Dates.NOW.getwMonthDay(xth);
        String [] tmp = wMonthDay.split("\\.");
        int year;
        int titleMonth = Dates.NOW.month;
        int monthOfYear = Integer.parseInt(tmp[0]);
        if (monthOfYear != titleMonth && titleMonth==1) year = Dates.NOW.year - 1;
        else year = Dates.NOW.year;
        int dayOfMonth = Integer.parseInt(tmp[1]);
        enrollMyTime(MyTimeRepo.getHourTimes(context,
                Dates.NOW.getDateMillis(year, monthOfYear, dayOfMonth, startHour, 0),//startmillis
                Dates.NOW.getDateMillis(year, monthOfYear, dayOfMonth, startHour+1, 0)-1,//endmillis
                xth, startHour));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_enroll);
        setCancelable(true);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        layoutParams.width = dm.widthPixels * 5 / 6;
        layoutParams.height = layoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        setLayout();
    }
    private void setLayout() {
        btDialCancel = (Button) findViewById(R.id.btDialCancel);
        llNewEnroll = (LinearLayout) findViewById(R.id.llNewEnroll);
        tvMonthDay = (TextView) findViewById(R.id.tvMonthDay);
        if(xth==1){
            tvMonthDay.setTextColor(Color.RED);
        }else if(xth==13){
            tvMonthDay.setTextColor(Color.BLUE);
        }
        tvMonthDay.setText(Dates.NOW.getwMonthDay(xth));
        llNewEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DialSchedule.class);
                i.putExtra("overlapEnrollFlag", true);
                i.putExtra("xth", xth);
                i.putExtra("yth", yth);
                context.startActivity(i);
                cancel();
            }
        });
        btDialCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        lv = (ListView) findViewById(R.id.lv);
        enrollAdapter = new EnrollAdapter(context, mtList);
        lv.setAdapter(enrollAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.tvId);
                Intent i = new Intent(context, DialSchedule.class);
                i.putExtra("enrollFlag", true);
                i.putExtra("overlapEnrollFlag", true);
                i.putExtra("_id",tv.getText().toString());
                i.putExtra("xth", xth);
                i.putExtra("yth", yth);
                i.putExtra("startMin", startMin);
                context.startActivity(i);
                cancel();
            }
        });
    }
    private void enrollMyTime(List<MyTime> mt){
        for(MyTime m : mt){
            String time = m.getStarthour()+":"+Convert.IntToString(m.getStartmin())+"~"
                    +m.getEndhour()+ ":"+Convert.IntToString(m.getEndmin());
            int timeType = m.getTimetype();
            if(timeType==0){
                String timeCode = m.getTimecode();
                EnrollData ed = new EnrollData(time,m.getName(),m.getMemo(),timeCode,
                        String.valueOf(timeType),
                        "0",m.getPlace(),m.getId());
                mtList.add(ed);
                enrollList.put(timeCode,ed);
            }else{
                String timeCode = m.getTimecode();
                String memo = m.getMemo();
                EnrollData ed =new EnrollData(time, m.getName(), memo, timeCode,
                        String.valueOf(timeType),
                        memo.split("/")[1].substring(0, 1), m.getPlace(), m.getId());
                mtList.add(ed);
                enrollList.put(timeCode, ed);
            }
        }
    }
    private int xth,yth,startHour,startMin;
    private Context context;
    private TextView tvMonthDay;
    private Button btDialCancel;
    private ListView lv;
    private List<EnrollData> mtList;
    private HashMap enrollList;
    private LinearLayout llNewEnroll;
    private EnrollAdapter enrollAdapter;
    public void onEventMainThread(RemoveEnrollEvent e) {
        mtList.remove(enrollList.get(e.getTimeCode()));
        if(mtList.size()==0)cancel();
        enrollAdapter.notifyDataSetChanged();
    }
}
