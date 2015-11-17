package com.daemin.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.daemin.common.Convert;
import com.daemin.common.CurrentTime;
import com.daemin.data.DayOfWeekData;
import com.daemin.enumclass.User;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;

import timedao.MyTime;

/**
 * Created by hernia on 2015-11-05.
 */
public class WeekCaptureView extends ImageView {
    private boolean isToday;
    private Context context;
    private int width, height, dayOfWeek; //화면의 전체 너비, 높이
    private String sun, mon, tue, wed, thr, fri, sat;
    private Paint hp; // 1시간 간격 수평선
    private Paint hpvp; // 30분 간격 수평선, 수직선
    private Paint tp, tpred, tpblue, rp; // 시간 텍스트
    static int tempxth, tempyth;
    private Canvas canvas;
    public WeekCaptureView(Context context, int dayOfMonth)
    {
        super(context);
        this.context = context;
        DayOfWeekData dowd = CurrentTime.getDateOfWeek();
        this.sun = dowd.getSun();
        this.mon = dowd.getMon();
        this.tue = dowd.getTue();
        this.wed = dowd.getWed();
        this.thr = dowd.getThr();
        this.fri = dowd.getFri();
        this.sat = dowd.getSat();
        this.dayOfWeek = dayOfMonth;
        rp = new Paint(Paint.ANTI_ALIAS_FLAG);
        postWeekData();
        isToday = true;
        tempxth = 0;
        tempyth = 0;
        hp = new Paint(Paint.ANTI_ALIAS_FLAG);
        hp.setColor(context.getResources().getColor(R.color.maincolor));
        hpvp = new Paint(Paint.ANTI_ALIAS_FLAG);
        hpvp.setAlpha(70);
        tp = new Paint(Paint.ANTI_ALIAS_FLAG);
        tp.setTextSize(30);
        tp.setTextAlign(Paint.Align.CENTER);
        tpred = new Paint(Paint.ANTI_ALIAS_FLAG);
        tpred.setTextSize(30);
        tpred.setTextAlign(Paint.Align.CENTER);
        tpred.setColor(context.getResources().getColor(R.color.red));
        tpblue = new Paint(Paint.ANTI_ALIAS_FLAG);
        tpblue.setTextSize(30);
        tpblue.setTextAlign(Paint.Align.CENTER);
        tpblue.setColor(context.getResources().getColor(R.color.blue));
    }
    public WeekCaptureView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public WeekCaptureView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    public String getMonthAndDay(int... index) {
        switch (index[0]) {
            case 1:
                return sun;
            case 3:
                return mon;
            case 5:
                return tue;
            case 7:
                return wed;
            case 9:
                return thr;
            case 11:
                return fri;
            case 13:
                return sat;
            default:
                return "";
        }
    }
    public void setCurrentTime(DayOfWeekData dowd) {
        this.sun = dowd.getSun();
        this.mon = dowd.getMon();
        this.tue = dowd.getTue();
        this.wed = dowd.getWed();
        this.thr = dowd.getThr();
        this.fri = dowd.getFri();
        this.sat = dowd.getSat();
        if(CurrentTime.getToday().equals(getMonthAndDay(2*dayOfWeek+1))) isToday = true;
        else isToday = false;
        postWeekData();
    }
    public void postWeekData(){
        String[] wData = new String[7];
        wData[0] = sun;
        wData[1] = mon;
        wData[2] = tue;
        wData[3] = wed;
        wData[4] = thr;
        wData[5] = fri;
        wData[6] = sat;
        User.INFO.setwData(wData);
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        this.canvas = canvas;
        width = canvas.getWidth();
        height = canvas.getHeight();
        initScreen(dayOfWeek);
        int week_startMonth = Integer.parseInt(sun.split("/")[0]);
        int week_startDay = Integer.parseInt(sun.split("/")[1]);
        int week_endMonth = Integer.parseInt(sat.split("/")[0]);
        int week_endDay = Integer.parseInt(sat.split("/")[1]);
        int week_startYear;
        int week_endYear;
        if(week_startMonth==12&&week_endMonth==1){
            week_endYear=User.INFO.year;
            week_startYear=week_endYear-1;
        }else
            week_endYear=week_startYear=User.INFO.year;
        long week_startMillies = CurrentTime.getDateMillis(week_startYear,week_startMonth,week_startDay,8,0);
        long week_endMillies = CurrentTime.getDateMillis(week_endYear,week_endMonth,week_endDay,23,0);
        for(MyTime mt :  MyTimeRepo.getWeekTime(context, week_startMillies, week_endMillies)) {
            rp.setColor(Color.parseColor(mt.getColor()));
            rp.setAlpha(130);
            int xth = mt.getDayofweek();
            int startMin = mt.getStartmin();
            int endMin = mt.getEndmin();
            int startYth = Convert.HourOfDayToYth(mt.getStarthour());
            int endYth = Convert.HourOfDayToYth(mt.getEndhour());
                canvas.drawRect(width * xth / 15, (height * startYth / 32 + 18) + (2 * height / 32) * startMin / 60,
                        width * (xth + 2) / 15, (height * endYth / 32 + 18) + (2 * height / 32) * endMin / 60, rp);
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
    public void initScreen(int day) {

        float[] hp_hour = {
                // 가로선 : 1시간 간격
                width / 20, height / 32 + 18, width, height / 32 + 18, width / 20, height * 3 / 32 + 18, width,
                height * 3 / 32 + 18, width / 20, height * 5 / 32 + 18, width, height * 5 / 32 + 18, width / 20,
                height * 7 / 32 + 18, width, height * 7 / 32 + 18, width / 20, height * 9 / 32 + 18, width,
                height * 9 / 32 + 18, width / 20, height * 11 / 32 + 18, width, height * 11 / 32 + 18, width / 20,
                height * 13 / 32 + 18, width, height * 13 / 32 + 18, width / 20, height * 15 / 32 + 18, width,
                height * 15 / 32 + 18, width / 20, height * 17 / 32 + 18, width, height * 17 / 32 + 18, width / 20,
                height * 19 / 32 + 18, width, height * 19 / 32 + 18, width / 20, height * 21 / 32 + 18, width,
                height * 21 / 32 + 18, width / 20, height * 23 / 32 + 18, width, height * 23 / 32 + 18, width / 20,
                height * 25 / 32 + 18, width, height * 25 / 32 + 18, width / 20, height * 27 / 32 + 18, width,
                height * 27 / 32 + 18, width / 20, height * 29 / 32 + 18, width, height * 29 / 32 + 18, width / 20,
                height * 31 / 32 + 18, width, height * 31 / 32 + 18};
        float[] vp = {
                // 세로 선
                width / 15, height / 32 + 18, width / 15, height * 31 / 32 + 18, width * 3 / 15,
                height / 32 + 18, width * 3 / 15, height * 31 / 32 + 18, width * 5 / 15,
                height / 32 + 18, width * 5 / 15, height * 31 / 32 + 18, width * 7 / 15,
                height / 32 + 18, width * 7 / 15, height * 31 / 32 + 18, width * 9 / 15,
                height / 32 + 18, width * 9 / 15, height * 31 / 32 + 18, width * 11 / 15,
                height / 32 + 18, width * 11 / 15, height * 31 / 32 + 18, width * 13 / 15,
                height / 32 + 18, width * 13 / 15, height * 31 / 32 + 18,width-2,
                height / 32 + 18, width-2, height * 31 / 32 + 18};

        canvas.drawColor(Color.WHITE);
        canvas.drawLines(hp_hour, hp);
        canvas.drawLines(vp, hpvp);
        canvas.drawText("8", (width / 20) * 5 / 8, height * 1 / 32 + 26, tp);
        canvas.drawText("9", (width / 20) * 5 / 8, height * 3 / 32 + 26, tp);
        for (int i = 2; i < 16; i++) {
            canvas.drawText(String.valueOf(i + 8), width / 40,
                    ((2 * i + 1) * height / 32) + 26, tp);
        }
        canvas.drawText(sun, width * 2 / 15, (height / 32 + 18) * 7 / 16, tpred);
        canvas.drawText(mon, width * 4 / 15, (height / 32 + 18) * 7 / 16, tp);
        canvas.drawText(tue, width * 6 / 15, (height / 32 + 18) * 7 / 16, tp);
        canvas.drawText(wed, width * 8 / 15, (height / 32 + 18) * 7 / 16, tp);
        canvas.drawText(thr, width * 10 / 15, (height / 32 + 18) * 7 / 16, tp);
        canvas.drawText(fri, width * 12 / 15, (height / 32 + 18) * 7 / 16, tp);
        canvas.drawText(sat, width * 14 / 15, (height / 32 + 18) * 7 / 16, tpblue);

        canvas.drawText("SUN", width * 2 / 15, (height / 32 + 18) * 15 / 16, tpred);
        canvas.drawText("MON", width * 4 / 15, (height / 32 + 18) * 15 / 16, tp);
        canvas.drawText("TUE", width * 6 / 15, (height / 32 + 18) * 15 / 16, tp);
        canvas.drawText("WED", width * 8 / 15, (height / 32 + 18) * 15 / 16, tp);
        canvas.drawText("THU", width * 10 / 15, (height / 32 + 18) * 15 / 16, tp);
        canvas.drawText("FRI", width * 12 / 15, (height / 32 + 18) * 15 / 16, tp);
        canvas.drawText("SAT", width * 14 / 15, (height / 32 + 18) * 15 / 16, tpblue);
        hp.setAlpha(40);
        if(isToday)canvas.drawRect(width * (2 * day + 1) / 15, ((height * 2) - 10) / 64 + 18, width * (2 * day + 3) / 15, height * 62 / 64 + 18, hp);
        hp.setAlpha(100);
    }
}
