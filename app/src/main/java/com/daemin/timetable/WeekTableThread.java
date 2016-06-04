package com.daemin.timetable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.daemin.common.Common;
import com.daemin.common.Convert;
import com.daemin.common.NotInException;
import com.daemin.dialog.DialEnroll;
import com.daemin.enumclass.Dates;
import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.enumclass.User;
import com.daemin.event.CreateDialEvent;
import com.daemin.event.ExcuteMethodEvent;

import org.greenrobot.eventbus.EventBus;

import timedao.MyTime;

@SuppressLint("DefaultLocale")
public class WeekTableThread extends InitThread {
    SurfaceHolder mholder;
    private boolean isLoop = true, downFlag = false, initFlag = true;
    private int width, height, dayOfWeek, intervalSize, startTime, startDay, endTime, endDay; //화면의 전체 너비, 높이
    Context context;
    private Paint hp; // 1시간 간격 수평선
    private Paint hpvp; // 30분 간격 수평선, 수직선
    private Paint tp; // 시간 텍스트
    private Paint rp;
    static int tempxth, tempyth, timeInterval, timeLength, dayInterval, dayLength,textSize;
    private static WeekTableThread singleton;
    Canvas canvas;
    public String[] day, wData;
    float[] hp_hour, vp;

    public WeekTableThread(SurfaceHolder holder, Context context) {
        singleton = this;
        this.mholder = holder;
        this.context = context;
        this.dayOfWeek = Dates.NOW.getDayOfWeek();
        startTime = User.INFO.getStartTime();
        endTime = User.INFO.getEndTime();
        startDay = User.INFO.getStartDay();
        endDay = User.INFO.getEndDay();
        textSize = User.INFO.getTextSize()*12/5;
        timeInterval = endTime-startTime;
        dayInterval = endDay-startDay+1;
        timeLength = (timeInterval+1)*4;
        dayLength = (dayInterval+1)*4;
        day = new String[dayInterval];
        wData = Dates.NOW.getWData();
        Common.fetchWeekData();
        int j=0;
        try {
            int y=1,z=1;
            for(int i = startDay; i<endDay+1; i++){
                int resId = context.getResources().getIdentifier("day"+i, "string", context.getPackageName());
                day[j] = context.getString(resId);
                for(int k = startTime; k<endTime; k++){
                    TimePos tp = TimePos.valueOf(Convert.getxyMerge(2*i+1, Convert.HourOfDayToYth(k)));
                    tp.setPos(y,z);
                    z++;
                }
                ++j;
                ++y;
                z=1;
            }
        }catch (IllegalArgumentException e){
        }
        catch (NotInException e) {
            e.printStackTrace();
        }

        tempxth = 0;
        tempyth = 0;
        intervalSize = User.INFO.intervalSize;
        hp = new Paint(Paint.ANTI_ALIAS_FLAG);
        hp.setColor(context.getResources().getColor(R.color.maincolor));
        hpvp = new Paint(Paint.ANTI_ALIAS_FLAG);
        hpvp.setAlpha(70);
        tp = new Paint(Paint.ANTI_ALIAS_FLAG);
        tp.setTextAlign(Paint.Align.CENTER);
        tp.setTextSize(textSize);
        tp.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        rp = new Paint(Paint.ANTI_ALIAS_FLAG);
        rp.setTextAlign(Paint.Align.CENTER);
        rp.setTextSize(User.INFO.dateSize);
    }

    public void setRunning(boolean isLoop) {
        this.isLoop = isLoop;
    }
    public void setDate(){
        wData = Dates.NOW.getWData();
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
                    for (TimePos ETP : TimePos.values()) {
                        ETP.drawTimePos(canvas, width, height,dayInterval,timeInterval);
                    }
                    fetchWeekData();
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
        makeTimePos2(xth, yth);
        downFlag = false;
        tempxth = xth;
        tempyth = yth;
    }

    public void getMoveXY(int xth, int yth) {
        if (tempxth != xth || tempyth != yth) {
            makeTimePos2(xth, yth);
            tempxth = xth;
            tempyth = yth;
        }
    }

    public void getActionUp() {
        EventBus.getDefault().post(new ExcuteMethodEvent("updateWeekList"));
        System.gc();
    }

    public void makeTimePos2(int xth, int yth) {
        try {
            int xIndex=2*(xth+startDay-1)+1;
            int yIndex=2*yth-1;
            TimePos ETP = TimePos.valueOf(Convert.getxyMerge(xIndex,yIndex));
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
                            i.putExtra("xIndex", xIndex);
                            i.putExtra("yIndex", yIndex);
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
                            i.putExtra("xIndex", xIndex);
                            i.putExtra("yIndex", yIndex);
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
            int sTime = mt.getStarthour();
            int eTime = mt.getEndhour();
            int sMin = mt.getStartmin();
            int eMin = mt.getEndmin();
            int dayOfWeek = mt.getDayofweek()/2;
            if(startTime>sTime){
                sTime = startTime;
                sMin = 0;
            }
            if(endTime<eTime){
                eTime = endTime;
                eMin = 0;
            }
            String title = mt.getName();
            String first,second;
            int posIndex=0;
            String place=mt.getPlace();
            if(title.length()>5){
                first = title.substring(0,5);
                second = title.substring(5);
                if(second.length()>5){
                    second = second.substring(0,5)+"..";
                }
                ++posIndex;
            }else{
                first = title;
                second="";
            }
            ++posIndex;
            if(place.length()>5) place = place.substring(0,6);
            try {
                if(dayOfWeek>=startDay) {
                    dayOfWeek = dayOfWeek-startDay;
                    int startHeight = (height * 15 / 16) / timeInterval * ((Convert.HourOfDayToYth(sTime) / 2 + 1) - 1) + height / 32 + intervalSize + ((height * 15 / 16) / timeInterval) * sMin / 60;
                    canvas.drawRect((width * 14 / 15) / dayInterval * dayOfWeek + width / 15,
                            startHeight,
                            (width * 14 / 15) / dayInterval * (dayOfWeek + 1) + width / 15,
                            (height * 15 / 16) / timeInterval * ((Convert.HourOfDayToYth(eTime) / 2 + 1) - 1) + height / 32 + intervalSize + ((height * 15 / 16) / timeInterval) * eMin / 60, rp);
                    dayOfWeek = 2*dayOfWeek+1;
                    if(sTime<endTime&&eTime>startTime) {
                        canvas.drawText(first, (width * 7 / 15) / dayInterval * dayOfWeek + width / 15,
                                startHeight + textSize, tp);
                        if (posIndex == 2)
                            canvas.drawText(second, (width * 7 / 15) / dayInterval * dayOfWeek + width / 15,
                                    startHeight + textSize + (posIndex - 1) * textSize*11/10, tp);
                        canvas.drawText(place, (width * 7 / 15) / dayInterval * dayOfWeek + width / 15,
                                startHeight + textSize + posIndex * textSize*11/10, tp);
                    }
                }
            }catch (IllegalArgumentException e){

            }
            catch (NotInException e){
            }
        }
    }

    public void drawScreen() {
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
                            hp_hour[i] = (height * 15 / 16) / timeInterval * (i / 4) + height / 32 + intervalSize;
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
                for (int i = 4; i < dayLength; i++) {
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
            }else{
                vp[4] =  width;
                vp[5] = height / 32 + intervalSize;
                vp[6] = width;
                vp[7] = height * 31 / 32 + intervalSize;
            }
            initFlag = false;
        }
        canvas.drawColor(Color.WHITE);
        canvas.drawLines(hp_hour, hp);
        canvas.drawLines(vp, hpvp);

        rp.setColor(context.getResources().getColor(android.R.color.black));
        for(int i = 0; i<timeInterval+1; i++){
            canvas.drawText(startTime+i+"", width / 40, hp_hour[4*i+1] + intervalSize, rp);
        }

        for(int i = 0; i<dayInterval+1; i++){
            if(day[i]==context.getString(R.string.day0)){
                rp.setColor(context.getResources().getColor(R.color.red));
            }else if(day[i]==context.getString(R.string.day6)){
                rp.setColor(context.getResources().getColor(R.color.blue));
            }else {
                rp.setColor(context.getResources().getColor(android.R.color.black));
            }
            canvas.drawText(wData[startDay+i], (vp[4*i]+vp[4*(i+1)])/2, (height / 32 + intervalSize) * 7 / 16, rp);
            canvas.drawText(day[i], (vp[4*i]+vp[4*(i+1)])/2, (height / 32 + intervalSize) * 15 / 16-1, rp);
            if(i==dayInterval-1)break;
        }
        hp.setAlpha(40);
        if(Dates.NOW.isToday&&dayOfWeek>=startDay&&dayOfWeek<=endDay)canvas.drawRect((width * 14 / 15) / dayInterval * (dayOfWeek-startDay) + width / 15, ((height * 2) - 10) / 64 + intervalSize,
                (width * 14 / 15) / dayInterval * (dayOfWeek+1-startDay) + width / 15, height * 62 / 64 + intervalSize, hp);
        hp.setAlpha(100);
    }
}
