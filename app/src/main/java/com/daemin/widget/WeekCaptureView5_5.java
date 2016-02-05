package com.daemin.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.daemin.common.Convert;
import com.daemin.enumclass.Dates;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;

import java.util.HashMap;

import timedao.MyTime;

/**
 * Created by hernia on 2015-11-05.
 */
public class WeekCaptureView5_5 extends ImageView {
    private Context context;
    private int width, height,dayOfWeek, textSize,dateSize,intervalSize,hourIntervalSize,posIndex; //화면의 전체 너비, 높이
    private Paint hp; // 1시간 간격 수평선
    private Paint hpvp; // 30분 간격 수평선, 수직선
    private Paint tp, tpred, tpblue, rp; // 시간 텍스트
    static int tempxth, tempyth;
    private Canvas canvas;
    private HashMap<String,String> ETP;
    public String sun,mon,tue,wed,thr,fri,sat;
    public WeekCaptureView5_5(Context context)
    {
        super(context);
        this.context = context;
        this.dayOfWeek = Dates.NOW.getDayOfWeek();
        sun=context.getResources().getString(R.string.sun);
        mon=context.getResources().getString(R.string.mon);
        tue=context.getResources().getString(R.string.tue);
        wed=context.getResources().getString(R.string.wed);
        thr=context.getResources().getString(R.string.thr);
        fri=context.getResources().getString(R.string.fri);
        sat=context.getResources().getString(R.string.sat);
        rp = new Paint(Paint.ANTI_ALIAS_FLAG);
        dateSize = context.getResources().getDimensionPixelSize(R.dimen.textsize_s);
        textSize=context.getResources().getDimensionPixelSize(R.dimen.textsize_xs);
        intervalSize=context.getResources().getDimensionPixelSize(R.dimen.margin_xs);
        hourIntervalSize=context.getResources().getDimensionPixelSize(R.dimen.margin_m);
        tempxth = 0;
        tempyth = 0;
        hp = new Paint(Paint.ANTI_ALIAS_FLAG);
        hp.setColor(context.getResources().getColor(R.color.maincolor));
        hpvp = new Paint(Paint.ANTI_ALIAS_FLAG);
        hpvp.setAlpha(70);
        tp = new Paint(Paint.ANTI_ALIAS_FLAG);
        tp.setTextSize(dateSize);
        tp.setTextAlign(Paint.Align.CENTER);
        tpred = new Paint(Paint.ANTI_ALIAS_FLAG);
        tpred.setTextSize(dateSize);
        tpred.setTextAlign(Paint.Align.CENTER);
        tpred.setColor(context.getResources().getColor(R.color.red));
        tpblue = new Paint(Paint.ANTI_ALIAS_FLAG);
        tpblue.setTextSize(dateSize);
        tpblue.setTextAlign(Paint.Align.CENTER);
        tpblue.setColor(context.getResources().getColor(R.color.blue));
        ETP = new HashMap<>();
    }
    public WeekCaptureView5_5(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public WeekCaptureView5_5(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        this.canvas = canvas;
        width = canvas.getWidth();
        height = canvas.getHeight();
        initScreen();
        fetchWeekData();
    }
    public void fetchWeekData(){
        int week_startMonth = Dates.NOW.monthOfSun;
        int week_startDay = Dates.NOW.dayOfSun;
        int week_endMonth = Dates.NOW.monthOfSat;
        int week_endDay = Dates.NOW.dayOfSat;
        int week_startYear;
        int week_endYear;
        if(week_startMonth==12&&week_endMonth==1){
            week_endYear=Dates.NOW.year;
            week_startYear=week_endYear-1;
        }else
            week_endYear=week_startYear=Dates.NOW.year;
        long week_startMillies = Dates.NOW.getDateMillis(week_startYear, week_startMonth, week_startDay, 8, 0);
        long week_endMillies = Dates.NOW.getDateMillis(week_endYear, week_endMonth, week_endDay, 23, 0);
        int realStartYth;
        int realStartMin;
        tp.setTextSize(textSize);
        tp.setTextAlign(Paint.Align.CENTER);
        tp.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        for(MyTime mt :  MyTimeRepo.getWeekTimes(context, week_startMillies, week_endMillies)) {
            String title="";
            String second ="";
            String place=mt.getPlace();
            if(place.equals(""))place=" ";
            posIndex=0;
            rp.setColor(Color.parseColor(mt.getColor()));
            rp.setAlpha(130);
            int xth = mt.getDayofweek();
            int startMin = mt.getStartmin();
            int endMin = mt.getEndmin();
            int startHour = mt.getStarthour();
            int endHour = mt.getEndhour();
            int startYth = Convert.HourOfDayToYthForWidget(startHour);
            int endYth = Convert.HourOfDayToYthForWidget(endHour);
            realStartYth = startYth;
            realStartMin = startMin;
             if(startYth!=11) { //18시 내의 시간대에서만 그려줌
                canvas.drawRect(width * xth / 15, height * (28 * startYth + 10) / (30 * 10) + intervalSize + (28 * height / (30 * 10)) * startMin / 60,
                        width * (xth + 2) / 15, height * (28 * endYth + 10) / (30 * 10) + (28 * height / (30 * 10)) * endMin / 60, rp);

                if (endHour == startHour && endMin != 0) ++endHour;
                String[] ETPName = new String[endHour - startHour];
                for (int i = 0; i < ETPName.length; i++) {
                    ETPName[i] = Convert.getxyMerge(xth, Convert.HourOfDayToYth(startHour++));
                }
                for (int i = 0; i < ETPName.length; i++) {
                    if (ETP.containsKey(ETPName[i])) {
                        if (i == 0) {
                            String[] tmp = ETP.get(ETPName[i]).split(":");
                            title = tmp[0];
                            place = tmp[1];
                            realStartYth = Integer.parseInt(tmp[2]);
                            realStartMin = Integer.parseInt(tmp[3]);
                        }
                    } else {
                        if (i == 0) {
                            title = mt.getName();
                            ETP.put(ETPName[i], title + ":" + place + ":" + realStartYth + ":" + realStartMin);
                        } else {
                            ETP.put(ETPName[i], " " + ":" + " " + ":" + realStartYth + ":" + realStartMin);
                        }
                    }
                }
                if (title.length() > 5) {
                    second = title.substring(5);
                    title = title.substring(0, 5);
                    if (second.length() > 5) {
                        second = second.substring(0, 5) + "..";
                    }
                    ++posIndex;
                }
                if (place != null) {
                    ++posIndex;
                    if (place.length() > 5) place = place.substring(0, 6);
                }
                canvas.drawText(title, width * (xth + 1) / 15, (height * (realStartYth + 1) / 32) + (2 * height / 32) * realStartMin / 60 - intervalSize / 2, tp);
                if (posIndex == 2)
                    canvas.drawText(second, width * (xth + 1) / 15, (height * (realStartYth + 1) / 32) + (2 * height / 32) * realStartMin / 60 - intervalSize / 2 + (posIndex - 1) * dateSize * 9 / 10, tp);
                canvas.drawText(place, width * (xth + 1) / 15, (height * (realStartYth + 1) / 32) + (2 * height / 32) * realStartMin / 60 - intervalSize / 2 + posIndex * dateSize * 9 / 10, tp);
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO Auto-generated method stub
        Log.d("Hello Android", "Got a touch event: " + event.getAction());
        return super.onTouchEvent(event);

    }
    public float getDip(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = (int)getDip(10);
        int height = (int)getDip(10);
        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED: // unspecified
                width = widthMeasureSpec;
                break;
            case MeasureSpec.AT_MOST:  // wrap_content
                break;
            case MeasureSpec.EXACTLY:  // match_parent
                width = MeasureSpec.getSize(widthMeasureSpec);
                break;
        }
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED: // unspecified
                height = heightMeasureSpec;
                break;
            case MeasureSpec.AT_MOST:  // wrap_content
                break;
            case MeasureSpec.EXACTLY:  // match_parent
                height = MeasureSpec.getSize(heightMeasureSpec);
                break;
        }
        setMeasuredDimension(width, height);
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
    }
    public void initScreen() {

        float[] hp_hour = {
                // 가로선 : 1시간 간격
                width / 20, height / 30 + intervalSize, width, height / 32 + intervalSize,
                width / 20, height * (28*1+10) / (30*10) + + intervalSize, width,height * (28*1+10) / (30*10) + intervalSize,
                width / 20, height * (28*2+10) / (30*10) + + intervalSize, width,height * (28*2+10) / (30*10) + intervalSize,
                width / 20, height * (28*3+10) / (30*10) + + intervalSize, width,height * (28*3+10) / (30*10) + intervalSize,
                width / 20, height * (28*4+10) / (30*10) + + intervalSize, width,height * (28*4+10) / (30*10) + intervalSize,
                width / 20, height * (28*5+10) / (30*10) + + intervalSize, width,height * (28*5+10) / (30*10) + intervalSize,
                width / 20, height * (28*6+10) / (30*10) + + intervalSize, width,height * (28*6+10) / (30*10) + intervalSize,
                width / 20, height * (28*7+10) / (30*10) + + intervalSize, width,height * (28*7+10) / (30*10) + intervalSize,
                width / 20, height * (28*8+10) / (30*10) + + intervalSize, width,height * (28*8+10) / (30*10) + intervalSize,
                width / 20, height * (28*9+10) / (30*10) + + intervalSize, width,height * (28*9+10) / (30*10) + intervalSize,
                width / 20, height * 29/30 + + intervalSize, width,height *29/30 + intervalSize};
        float[] vp = {
                // 세로 선
                width / 15, height / 32 + intervalSize, width / 15, height * 31 / 32 + intervalSize, width * 3 / 15,
                height / 32 + intervalSize, width * 3 / 15, height * 31 / 32 + intervalSize, width * 5 / 15,
                height / 32 + intervalSize, width * 5 / 15, height * 31 / 32 + intervalSize, width * 7 / 15,
                height / 32 + intervalSize, width * 7 / 15, height * 31 / 32 + intervalSize, width * 9 / 15,
                height / 32 + intervalSize, width * 9 / 15, height * 31 / 32 + intervalSize, width * 11 / 15,
                height / 32 + intervalSize, width * 11 / 15, height * 31 / 32 + intervalSize, width * 13 / 15,
                height / 32 + intervalSize, width * 13 / 15, height * 31 / 32 + intervalSize,width-2,
                height / 32 + intervalSize, width-2, height * 31 / 32 + intervalSize};

        canvas.drawColor(Color.WHITE);
        canvas.drawLines(hp_hour, hp);
        canvas.drawLines(vp, hpvp);
        canvas.drawText("8", (width / 20) * 5 / 8, height * 1 / 30 + hourIntervalSize, tp);
        canvas.drawText("9", (width / 20) * 5 / 8,height * (28*1+10) / (30*10) + hourIntervalSize, tp);
        for (int i = 2; i < 11; i++) {
            canvas.drawText(String.valueOf(i + 8), width / 40,
                    height *(28*i+10)/(30*10) + hourIntervalSize, tp);
        }
        canvas.drawText(Dates.NOW.mdOfSun, width * 2 / 15, (height / 32 + intervalSize) * 7 / 16, tpred);
        canvas.drawText(Dates.NOW.mdOfMon, width * 4 / 15, (height / 32 + intervalSize) * 7 / 16, tp);
        canvas.drawText(Dates.NOW.mdOfTue, width * 6 / 15, (height / 32 + intervalSize) * 7 / 16, tp);
        canvas.drawText(Dates.NOW.mdOfWed, width * 8 / 15, (height / 32 + intervalSize) * 7 / 16, tp);
        canvas.drawText(Dates.NOW.mdOfThr, width * 10 / 15, (height / 32 + intervalSize) * 7 / 16, tp);
        canvas.drawText(Dates.NOW.mdOfFri, width * 12 / 15, (height / 32 + intervalSize) * 7 / 16, tp);
        canvas.drawText(Dates.NOW.mdOfSat, width * 14 / 15, (height / 32 + intervalSize) * 7 / 16, tpblue);

        canvas.drawText(sun, width * 2 / 15, (height / 32 + intervalSize) * 15 / 16-1, tpred);
        canvas.drawText(mon, width * 4 / 15, (height / 32 + intervalSize) * 15 / 16-1, tp);
        canvas.drawText(tue, width * 6 / 15, (height / 32 + intervalSize) * 15 / 16-1, tp);
        canvas.drawText(wed, width * 8 / 15, (height / 32 + intervalSize) * 15 / 16-1, tp);
        canvas.drawText(thr, width * 10 / 15, (height / 32 + intervalSize) * 15 / 16-1, tp);
        canvas.drawText(fri, width * 12 / 15, (height / 32 + intervalSize) * 15 / 16-1, tp);
        canvas.drawText(sat, width * 14 / 15, (height / 32 + intervalSize) * 15 / 16-1, tpblue);
        hp.setAlpha(40);
        if(Dates.NOW.isToday)canvas.drawRect(width * (2 * dayOfWeek + 1) / 15, ((height * 2) - 10) / 64 + intervalSize, width * (2 * dayOfWeek + 3) / 15, height * 62 / 64 + intervalSize, hp);
        hp.setAlpha(100);
    }
}

