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

import com.daemin.common.AppController;
import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.enumclass.Dates;
import com.daemin.enumclass.DayOfMonthPos;
import com.daemin.repository.MyTimeRepo;
import com.daemin.timetable.R;

import java.util.HashMap;

import timedao.MyTime;

/**
 * Created by hernia on 2015-11-05.
 */
public class MonthCaptureView extends ImageView {
    private int width;
    private int height;
    private int ty;
    private int tx;//이전달의 마지막날 요일
    private int intervalSize;
    private Paint hp; // 1시간 간격 수평선
    private Paint hpvp; // 30분 간격 수평선, 수직선
    private Paint tp,tpred,tpblue,tpgray,rp; // 시간 텍스트
    static int tempxth;
    static int tempyth;
    private Canvas canvas;
    private HashMap<String,DOMPData> DOMP;
    public String sun,mon,tue,wed,thr,fri,sat;
    public MonthCaptureView(Context context)
    {
        super(context);
        sun=context.getResources().getString(R.string.sun);
        mon=context.getResources().getString(R.string.mon);
        tue=context.getResources().getString(R.string.tue);
        wed=context.getResources().getString(R.string.wed);
        thr=context.getResources().getString(R.string.thr);
        fri=context.getResources().getString(R.string.fri);
        sat=context.getResources().getString(R.string.sat);
        tempxth = 0;
        tempyth = 0;
        int textSize = context.getResources().getDimensionPixelSize(R.dimen.textsize_s);
        intervalSize = context.getResources().getDimensionPixelSize(R.dimen.margin_xxs);
        hp = new Paint(Paint.ANTI_ALIAS_FLAG);
        hp.setColor(context.getResources().getColor(R.color.maincolor));
        hpvp = new Paint(Paint.ANTI_ALIAS_FLAG);
        hpvp.setAlpha(70);
        tp = new Paint(Paint.ANTI_ALIAS_FLAG);
        tp.setTextSize(textSize);
        tp.setTextAlign(Paint.Align.CENTER);
        tpred = new Paint(Paint.ANTI_ALIAS_FLAG);
        tpred.setTextSize(textSize);
        tpred.setTextAlign(Paint.Align.CENTER);
        tpred.setColor(context.getResources().getColor(R.color.red));
        tpblue = new Paint(Paint.ANTI_ALIAS_FLAG);
        tpblue.setTextSize(textSize);
        tpblue.setTextAlign(Paint.Align.CENTER);
        tpblue.setColor(context.getResources().getColor(R.color.blue));
        tpgray = new Paint(Paint.ANTI_ALIAS_FLAG);
        tpgray.setTextSize(textSize);
        tpgray.setTextAlign(Paint.Align.CENTER);
        tpgray.setColor(context.getResources().getColor(R.color.middlegray));
        ty = Dates.NOW.getDayOfWeekOfLastMonth()+Dates.NOW.dayOfMonth+1;
        tx = ty%7;
        if(tx==0){
            --ty;
            tx = 7;
        }
        rp = new Paint(Paint.ANTI_ALIAS_FLAG);
        rp.setColor(Color.parseColor(Common.MAIN_COLOR));
        rp.setAlpha(100);
        DOMP = new HashMap<>();
    }
    public MonthCaptureView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public MonthCaptureView(Context context, AttributeSet attrs, int defStyle)
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
        fetchMonthData();
    }
    public void fetchMonthData(){
        int year = Dates.NOW.year;
        int month = Dates.NOW.month;
        long month_startMillies = Dates.NOW.getDateMillis(year, month, 1, 8, 0);
        long month_endMillies = Dates.NOW.getDateMillis(year, month, Dates.NOW.dayNumOfMonth, 23, 0);
        for (MyTime mt :MyTimeRepo.getMonthTimes(AppController.getInstance(), month_startMillies, month_endMillies)){
            int dayCnt = mt.getDayofmonth() + Dates.NOW.dayOfWeek;
            int yth = dayCnt/7+1;
            int xth = Convert.wXthTomXth(mt.getDayofweek());
            DayOfMonthPos domp = DayOfMonthPos.valueOf(Convert.getxyMergeForMonth(xth, yth));
            if(DOMP.containsKey(domp.name())){
                DOMPData dData = DOMP.get(domp.name());
                dData.setTitleAndColor(mt.getName(), mt.getColor());
                dData.setEnrollCnt();
            }else{
                DOMPData dData = new DOMPData();
                dData.setTitleAndColor(mt.getName(), mt.getColor());
                dData.setEnrollCnt();
                dData.setXYth(xth,yth);
                DOMP.put(domp.name(),dData);
            }
        }
        for(DOMPData d : DOMP.values()){
            int xth = d.getXth();
            int yth = d.getYth();
            for(int i =0; i<4; i++) {
                rp.setColor(Color.parseColor(d.color[i]));
                canvas.drawRect(width * (xth - 1) / 7, height * ((10 * (yth - 1)) + 4 + 2 * i) / 64 + intervalSize,
                        width * (xth - 1) / 7 + intervalSize, height * ((10 * (yth - 1)) + 6 + 2 * i) / 64 + intervalSize, rp);
                canvas.drawText(d.title[i], width * (xth - 1) / 7 + intervalSize, height * ((10 * (yth - 1)) + 5 + 2 * i) / 64 + 2 * intervalSize, tp);
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
                // 가로선
                0, height / 32 + intervalSize, width, height / 32 + intervalSize , 0, height * 12 / 64 + intervalSize, width,
                height * 12 / 64 + intervalSize, 0, height * 22 / 64 + intervalSize, width, height * 22 / 64 + intervalSize, 0,
                height * 32 / 64 + intervalSize, width, height * 32 / 64 + intervalSize, 0, height * 42 / 64 + intervalSize, width,
                height * 42 / 64 + intervalSize, 0, height * 52 / 64 + intervalSize, width, height * 52 / 64 + intervalSize, 0,
                height * 62 / 64 + intervalSize, width, height * 62 / 64 + intervalSize};

        float[] vp = {
                // 세로 선
                2, height / 32 + intervalSize, 2, height * 62 / 64+ intervalSize, width / 7,
                height / 32 + intervalSize, width / 7, height * 62 / 64+ intervalSize, width * 2 / 7,
                height / 32 + intervalSize, width * 2 / 7, height * 62 / 64+ intervalSize, width * 3 / 7,
                height / 32 + intervalSize, width * 3 / 7, height * 62 / 64+ intervalSize, width * 4 / 7,
                height / 32 + intervalSize, width * 4 / 7, height * 62 / 64+ intervalSize, width * 5 / 7,
                height / 32 + intervalSize, width * 5 / 7, height * 62 / 64+ intervalSize, width * 6 / 7,
                height / 32 + intervalSize, width * 6 / 7, height * 62 / 64+ intervalSize, width-2,
                height / 32 + intervalSize, width-2, height * 62 / 64+ intervalSize};

        canvas.drawColor(Color.WHITE);
        canvas.drawLines(hp_hour, hp);
        canvas.drawLines(vp, hpvp);
        hp.setAlpha(40);
        if(Dates.NOW.isToday)canvas.drawRect(width * (tx - 1) / 7, height * ((ty / 7) * 10 + 2) / 64 + intervalSize,width * tx / 7, height * ((ty / 7 + 1) * 10 + 2) / 64 + intervalSize, hp);
        hp.setAlpha(100);
        for(int i = 0; i<Dates.NOW.dayOfWeek+1; i++){
            canvas.drawText(Dates.NOW.mData[i], width * (i % 7) / 7+2*intervalSize, height * ((10 * (i / 7)) + 4) / 64, tpgray);
        }
        for(int i = Dates.NOW.dayOfWeek+1; i<Dates.NOW.dayOfWeek+Dates.NOW.dayNumOfMonth+1; i++){
            int j = i%7;
            switch(j){
                case 0:
                    canvas.drawText(Dates.NOW.mData[i], 2+2*intervalSize, height* (( 10 * (i/7)) + 4) / 64, tpred);
                    break;
                case 6:
                    canvas.drawText(Dates.NOW.mData[i], width * 6 / 7+2*intervalSize, height * ((10 * (i/7)) + 4) / 64, tpblue);
                    break;
                default:
                    canvas.drawText(Dates.NOW.mData[i], width * j / 7+2*intervalSize, height * ((10 * (i/7)) + 4) / 64, tp);
                    break;
            }
        }
        for(int i = Dates.NOW.dayOfWeek + Dates.NOW.dayNumOfMonth + 1; i < 42; i++) {
            canvas.drawText(Dates.NOW.mData[i], width * (i % 7) / 7+2*intervalSize, height * ((10 * (i/7)) + 4) / 64, tpgray);
        }
        tp.setTextAlign(Paint.Align.CENTER);
        tpred.setTextAlign(Paint.Align.CENTER);
        tpblue.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(sun, width * 1 / 14, height * 2 / 62 - 1, tpred);
        canvas.drawText(mon, width * 3 / 14, height * 2 / 62 - 1, tp);
        canvas.drawText(tue, width * 5 / 14, height * 2 / 62 - 1, tp);
        canvas.drawText(wed, width * 7 / 14, height * 2 / 62 - 1, tp);
        canvas.drawText(thr, width * 9 / 14, height * 2 / 62 - 1, tp);
        canvas.drawText(fri, width * 11 / 14, height * 2 / 62 - 1, tp);
        canvas.drawText(sat, width * 13 / 14, height * 2 / 62 - 1, tpblue);
        tp.setTextAlign(Paint.Align.LEFT);
        tpred.setTextAlign(Paint.Align.LEFT);
        tpblue.setTextAlign(Paint.Align.LEFT);
    }
    class DOMPData{
        private int enrollCnt;
        private int xth,yth;
        private String[] title,color;
        public DOMPData() {
            enrollCnt=0;
            title = new String[4];
            color = new String[4];
            for(int i = 0; i<4; i++){
                title[i]="";
                color[i]= Common.TRANS_COLOR;
            }
        }
        public int getXth() {
            return xth;
        }

        public int getYth() {
            return yth;
        }
        public void setXYth(int xth, int yth) {
            this.xth=xth;
            this.yth=yth;
        }
        public int getEnrollCnt() {
            return enrollCnt;
        }
        public void setEnrollCnt() {
            ++this.enrollCnt;
        }
        public void setTitleAndColor(String title,String color) {
            if(title.length()>5) title = title.substring(0,5);
            if(enrollCnt<4) {
                this.color[enrollCnt] = color;
                this.title[enrollCnt] = title;
            }
        }
    }
}
