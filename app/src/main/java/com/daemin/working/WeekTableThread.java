package com.daemin.working;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.dialog.DialEnroll;
import com.daemin.enumclass.Dates;
import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.enumclass.User;
import com.daemin.event.CreateDialEvent;
import com.daemin.event.ExcuteMethodEvent;
import com.daemin.timetable.InitThread;
import com.daemin.timetable.R;

import de.greenrobot.event.EventBus;
import timedao.MyTime;

@SuppressLint("DefaultLocale")
public class WeekTableThread extends InitThread {
    SurfaceHolder mholder;
    private boolean isLoop = true, downFlag = false, initFlag = true;
    private int width, height, dayOfWeek, intervalSize, startTime, endTime; //화면의 전체 너비, 높이
    Context context;
    private Paint hp; // 1시간 간격 수평선
    private Paint hpvp; // 30분 간격 수평선, 수직선
    private Paint tp, tpred, tpblue; // 시간 텍스트
    private Paint rp;
    static int tempxth, tempyth, timeInterval, timeLength, dayInterval, dayLength;
    private static WeekTableThread singleton;
    Canvas canvas;
    public String sun, mon, tue, wed, thr, fri, sat;
    float[] hp_hour, vp;

    public WeekTableThread(SurfaceHolder holder, Context context) {
        singleton = this;
        this.mholder = holder;
        this.context = context;
        this.dayOfWeek = Dates.NOW.getDayOfWeek();
        startTime = User.INFO.getStartTime();
        endTime = User.INFO.getEndTime();
        timeInterval = endTime - startTime;
        timeLength = (timeInterval + 1) * 4;
        dayInterval = User.INFO.getEndDay()-User.INFO.getStartDay()+1;
        dayLength = (dayInterval+1)*4;
        //Common.fetchWeekData();
        sun = context.getResources().getString(R.string.day0);
        mon = context.getResources().getString(R.string.day1);
        tue = context.getResources().getString(R.string.day2);
        wed = context.getResources().getString(R.string.day3);
        thr = context.getResources().getString(R.string.day4);
        fri = context.getResources().getString(R.string.day5);
        sat = context.getResources().getString(R.string.day6);
        tempxth = 0;
        tempyth = 0;
        intervalSize = User.INFO.intervalSize;
        hp = new Paint(Paint.ANTI_ALIAS_FLAG);
        hp.setColor(context.getResources().getColor(R.color.maincolor));
        hpvp = new Paint(Paint.ANTI_ALIAS_FLAG);
        hpvp.setAlpha(70);
        tp = new Paint(Paint.ANTI_ALIAS_FLAG);
        tp.setTextSize(User.INFO.dateSize);
        tp.setTextAlign(Paint.Align.CENTER);
        tpred = new Paint(Paint.ANTI_ALIAS_FLAG);
        tpred.setTextSize(User.INFO.dateSize);
        tpred.setTextAlign(Paint.Align.CENTER);
        tpred.setColor(context.getResources().getColor(R.color.red));
        tpblue = new Paint(Paint.ANTI_ALIAS_FLAG);
        tpblue.setTextSize(User.INFO.dateSize);
        tpblue.setTextAlign(Paint.Align.CENTER);
        tpblue.setColor(context.getResources().getColor(R.color.blue));
        rp = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    public void setRunning(boolean isLoop) {
        this.isLoop = isLoop;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static WeekTableThread getInstance() {
        return singleton;
    }

    public void run() {
        while (isLoop) {
            canvas = null;
            try {
                canvas = mholder.lockCanvas();
                width = canvas.getWidth();
                height = canvas.getHeight();
                synchronized (mholder) {
                    drawScreen();
                    fetchWeekData();
                    for (TimePos ETP : TimePos.values()) {
                        ETP.drawTimePos(canvas, width, height);
                    }
                }
            } catch (Exception e) {
            } finally {
                if (canvas != null)
                    mholder.unlockCanvasAndPost(canvas);
            }
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void getDownXY(int xth, int yth) {
        downFlag = true;
        makeTimePos(xth, yth);
        downFlag = false;
        tempxth = xth;
        tempyth = yth;
    }

    public void getMoveXY(int xth, int yth) {
        if (tempxth != xth || tempyth != yth) {
            makeTimePos(xth, yth);
            tempxth = xth;
            tempyth = yth;
        }
    }

    public void getActionUp() {
        EventBus.getDefault().post(new ExcuteMethodEvent("updateWeekList"));
        System.gc();
    }

    public void makeTimePos(int xth, int yth) {
        int tmpYth, ryth = yth % 2;
        if (ryth == 0) tmpYth = yth - 1;
        else tmpYth = yth;
        try {
            TimePos ETP = TimePos.valueOf(Convert.getxyMerge(xth, tmpYth));
            switch (DrawMode.CURRENT.getMode()) {
                case 0://일반
                    if (ETP.getPosState() == PosState.NO_PAINT) {
                        ETP.setPosState(PosState.PAINT);
                        if (!Common.getTempTimePos().contains(ETP.name()))
                            Common.getTempTimePos().add(ETP.name());
                    } else if (ETP.getPosState() == PosState.PAINT) {
                        ETP.setMin(0, 60);
                        ETP.setPosState(PosState.NO_PAINT);
                        Common.getTempTimePos().remove(ETP.name());
                    } else {
                        if (downFlag) {
                            //등록된 다음주 시간표를 누를 때 위젯 업데이트로 날짜가 변동되는 현상을 막기위해 dialflag를 false로 해줌
                            EventBus.getDefault().post(new CreateDialEvent(false));
                            Intent i = new Intent(context, DialEnroll.class);
                            i.putExtra("xth", xth);
                            i.putExtra("yth", tmpYth);
                            i.putExtra("startMin", ETP.getStartMin());
                            context.startActivity(i);
                        }

                    }
                    break;
                case 1: //대학
                    //대학선택시에 그리는 것은 막고 선택한 과목은 함께 지워져야함
                    if (Common.isTableEmpty()) {
                        Toast.makeText(context, context.getResources().getString(R.string.univ_select), Toast.LENGTH_SHORT).show();
                    }
                    if (ETP.getPosState() == PosState.ENROLL) {
                        //등록된 다음주 시간표를 누를 때 위젯 업데이트로 날짜가 변동되는 현상을 막기위해 dialflag를 false로 해줌
                        if (downFlag) {
                            EventBus.getDefault().post(new CreateDialEvent(false));
                            Intent i = new Intent(context, DialEnroll.class);
                            i.putExtra("xth", xth);
                            i.putExtra("yth", tmpYth);
                            i.putExtra("startMin", ETP.getStartMin());
                            i.putExtra("weekFlag", true);
                            context.startActivity(i);
                        }
                    }
                    break;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return;
    }

    public void fetchWeekData() {
        for (MyTime mt : User.INFO.weekData) {
            rp.setColor(Color.parseColor(mt.getColor()));
            rp.setAlpha(130);
            canvas.drawRect(width * mt.getDayofweek() / 15, (height * Convert.HourOfDayToYth(mt.getStarthour()) / 32 + intervalSize) + (2 * height / 32) * mt.getStartmin() / 60,
                    width * (mt.getDayofweek() + 2) / 15, (height * Convert.HourOfDayToYth(mt.getEndhour()) / 32 + intervalSize) + (2 * height / 32) * mt.getEndmin() / 60, rp);
        }
    }

    public void drawScreen() {
        //Log.i("test",tableLength+"");
        if (initFlag) {
            hp_hour = new float[timeLength];
            hp_hour[0] = width / 20;
            hp_hour[1] = height / 32 + intervalSize;
            hp_hour[2] = width;
            hp_hour[3] = height / 32 + intervalSize;
            hp_hour[timeLength - 4] = width / 20;
            hp_hour[timeLength - 3] = height * 31 / 32 + intervalSize;
            hp_hour[timeLength - 2] = width;
            hp_hour[timeLength - 1] = height * 31 / 32 + intervalSize;

            if (timeInterval != 1) {
                for (int i = 4; i < timeLength - 4; i++) {
                    switch (i % 4) {
                        case 0:
                            hp_hour[i] = width / 20;
                            break;
                        case 1:
                            hp_hour[i] = (height * 15 / 16) / timeInterval * (i / 4) + height / 32 + intervalSize; // height * (30*(i/4))/ 32*(tableLength/4-1) + intervalSize;
                            break;
                        case 2:
                            hp_hour[i] = width;
                            break;
                        case 3:
                            hp_hour[i] = (height * 15 / 16) / timeInterval * (i / 4) + height / 32 + intervalSize;
                            break;
                    }
                }
            }
            vp = new float[dayLength];
            vp[0] =  width / 15;
            vp[1] = height / 32 + intervalSize;
            vp[2] = width / 15;
            vp[3] = height * 31 / 32 + intervalSize;
            if (dayInterval != 1) {
                for (int i = 4; i < dayLength - 4; i++) {
                    switch (i % 4) {
                        case 0:
                            vp[i] = (width*14 / 15)/dayInterval * (i / 4)+ width / 15;
                            break;
                        case 1:
                            vp[i] = height / 32 + intervalSize;
                            break;
                        case 2:
                            vp[i] = (width*14 / 15)/dayInterval * (i / 4)+ width / 15;
                            break;
                        case 3:
                            vp[i] = height * 31 / 32 + intervalSize;
                            break;
                    }
                }
            }

            initFlag = false;
        }
        canvas.drawColor(Color.WHITE);
        canvas.drawLines(hp_hour, hp);
        canvas.drawLines(vp, hpvp);
        for(int i = 0; i<timeInterval+1; i++){
            canvas.drawText(startTime+i+"", width / 40, hp_hour[4*i+1] + intervalSize, tp);
        }
       /*
        canvas.drawText("8", (width / 20) * 5 / 8, height * 1 / 32 + hourIntervalSize, tp);
        canvas.drawText("9", (width / 20) * 5 / 8, height * 3 / 32 + hourIntervalSize, tp);
        for (int i = 2; i < 16; i++) {
            canvas.drawText(String.valueOf(i + 8), width / 40,
                    ((2 * i + 1) * height / 32) + hourIntervalSize, tp);
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
        hp.setAlpha(100);*/

    }
}
