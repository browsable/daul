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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daemin.adapter.EnrollAdapter;
import com.daemin.adapter.EventListAdapter;
import com.daemin.common.Convert;
import com.daemin.data.EnrollData;
import com.daemin.enumclass.Dates;
import com.daemin.enumclass.User;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;

import java.util.ArrayList;
import java.util.List;
import timedao.MyTime;

/**
 * Created by hernia on 2015-09-08.
 */
public class DialEnroll extends Dialog {

    public DialEnroll(Context context, int xth, int yth, int startMin) {
        super(context, android.R.style.Theme_Holo_Light_Dialog);
        this.context = context;
        this.xth=xth;
        this.yth=yth;
        this.startMin=startMin;
        mtList = new ArrayList<>();
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
                Dates.NOW.getDateMillis(year, monthOfYear, dayOfMonth, startHour, 59),//endmillis
                xth, startHour));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_enroll);
        setCancelable(true);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        layoutParams.width = dm.widthPixels * 2 / 3;
        layoutParams.height = layoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.CENTER);
        setLayout();
    }
    private void setLayout() {
        btDialCancel = (Button) findViewById(R.id.btDialCancel);
        tvMonthDay = (TextView) findViewById(R.id.tvMonthDay);
        if(xth==1){
            tvMonthDay.setTextColor(Color.RED);
        }else if(xth==13){
            tvMonthDay.setTextColor(Color.BLUE);
        }
        tvMonthDay.setText(Dates.NOW.getwMonthDay(xth));
        btDialCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        lv = (ListView) findViewById(R.id.lv);
        EnrollAdapter enrollAdapter = new EnrollAdapter(context, mtList);
        lv.setAdapter(enrollAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.tvId);
                if (tv.getText().toString().equals("0")) {
                    Intent i = new Intent(context, DialSchedule.class);
                    i.putExtra("overlapEnrollFlag", true);
                    context.startActivity(i);
                    //EventBus.getDefault().post(new UpdateNormalEvent(String.valueOf(startHour), "00", String.valueOf(startHour + 1), "00", xth, 1));
                } else {
                    Intent i = new Intent(context, DialSchedule.class);
                    i.putExtra("enrollFlag", true);
                    i.putExtra("overlapEnrollFlag", true);
                    i.putExtra("_id",tv.getText().toString());
                    i.putExtra("xth", xth);
                    i.putExtra("yth", yth);
                    i.putExtra("startMin", startMin);
                    context.startActivity(i);
                }
                cancel();
            }
        });
    }
    private void enrollMyTime(List<MyTime> mt){
        for(MyTime m : mt){
            String time = m.getStarthour()+":"+Convert.IntToString(m.getStartmin())+"~"
                    +m.getEndhour()+ ":"+Convert.IntToString(m.getEndmin());
            mtList.add(new EnrollData(time,m.getName(),m.getId()));
        }
        mtList.add(new EnrollData("",context.getResources().getString(R.string.overlapEnroll),0));
    }
    private int xth,yth,startHour,startMin;
    private Context context;
    private TextView tvMonthDay;
    private Button btDialCancel;
    private ListView lv;
    private List<EnrollData> mtList;
}
