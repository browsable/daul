package com.daemin.timetable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

import de.greenrobot.event.EventBus;
import timedao.MyTime;

@SuppressLint("DefaultLocale")
public class InitWeekThread extends InitThread {
    SurfaceHolder mholder;
    private boolean isLoop = true, downFlag=false;
    private int width, height,dayOfWeek,intervalSize,hourIntervalSize; //화면의 전체 너비, 높이
    Context context;
    private Paint hp; // 1시간 간격 수평선
    private Paint hpvp; // 30분 간격 수평선, 수직선
    private Paint tp, tpred, tpblue; // 시간 텍스트
    private Paint rp;
    static int tempxth, tempyth;
    Canvas canvas;
    public String sun,mon,tue,wed,thr,fri,sat;
    public InitWeekThread(SurfaceHolder holder, Context context) {
        this.mholder = holder;
        this.context = context;
        this.dayOfWeek = Dates.NOW.getDayOfWeek();
        Common.fetchWeekData();
        sun=context.getResources().getString(R.string.sun);
        mon=context.getResources().getString(R.string.mon);
        tue=context.getResources().getString(R.string.tue);
        wed=context.getResources().getString(R.string.wed);
        thr=context.getResources().getString(R.string.thr);
        fri=context.getResources().getString(R.string.fri);
        sat=context.getResources().getString(R.string.sat);
        tempxth = 0;
        tempyth = 0;
        intervalSize = User.INFO.intervalSize;
        hourIntervalSize = context.getResources().getDimensionPixelSize(R.dimen.margin_m);
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
        rp= new Paint(Paint.ANTI_ALIAS_FLAG);

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

    public void run() {
        while (isLoop) {
            canvas = null;
            try {
                canvas = mholder.lockCanvas();
                width = canvas.getWidth();
                height = canvas.getHeight();
                synchronized (mholder) {
                    initScreen();
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
        downFlag=true;
        makeTimePos(xth, yth);
        downFlag=false;
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
        int tmpYth, ryth = yth%2;
        if(ryth==0) tmpYth=yth-1;
        else tmpYth = yth;
        try{
            TimePos ETP = TimePos.valueOf(Convert.getxyMerge(xth, tmpYth));
            switch (DrawMode.CURRENT.getMode()) {
                case 0://일반
                    if (ETP.getPosState() == PosState.NO_PAINT) {
                        ETP.setPosState(PosState.PAINT);
                        if(!Common.getTempTimePos().contains(ETP.name()))
                        Common.getTempTimePos().add(ETP.name());
                    }else if(ETP.getPosState() == PosState.PAINT){
                        ETP.setMin(0,60);
                        ETP.setPosState(PosState.NO_PAINT);
                        Common.getTempTimePos().remove(ETP.name());
                    }else{
                        if(downFlag) {
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
                    if (Common.isTableEmpty()){
                        Toast.makeText(context, context.getResources().getString(R.string.univ_select), Toast.LENGTH_SHORT).show();
                    }
                    if (ETP.getPosState() == PosState.ENROLL) {
                        //등록된 다음주 시간표를 누를 때 위젯 업데이트로 날짜가 변동되는 현상을 막기위해 dialflag를 false로 해줌
                        if(downFlag) {
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
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return;
    }
    public void fetchWeekData(){
        for(MyTime mt :  User.INFO.weekData){
            rp.setColor(Color.parseColor(mt.getColor()));
            rp.setAlpha(130);
            canvas.drawRect(width * mt.getDayofweek() / 15, (height * Convert.HourOfDayToYth(mt.getStarthour()) / 32 + intervalSize) + (2 * height / 32) * mt.getStartmin() / 60,
                    width * (mt.getDayofweek() + 2) / 15, (height * Convert.HourOfDayToYth(mt.getEndhour()) / 32 + intervalSize) + (2 * height / 32) * mt.getEndmin() / 60, rp);
        }
    }
    public void initScreen() {
        float[] hp_hour = {
                // 가로선 : 1시간 간격
                width / 20, height / 32 + intervalSize, width, height / 32 + intervalSize, width / 20, height * 3 / 32 + intervalSize, width,
                height * 3 / 32 + intervalSize, width / 20, height * 5 / 32 + intervalSize, width, height * 5 / 32 + intervalSize, width / 20,
                height * 7 / 32 + intervalSize, width, height * 7 / 32 + intervalSize, width / 20, height * 9 / 32 + intervalSize, width,
                height * 9 / 32 + intervalSize, width / 20, height * 11 / 32 + intervalSize, width, height * 11 / 32 + intervalSize, width / 20,
                height * 13 / 32 + intervalSize, width, height * 13 / 32 + intervalSize, width / 20, height * 15 / 32 + intervalSize, width,
                height * 15 / 32 + intervalSize, width / 20, height * 17 / 32 + intervalSize, width, height * 17 / 32 + intervalSize, width / 20,
                height * 19 / 32 + intervalSize, width, height * 19 / 32 + intervalSize, width / 20, height * 21 / 32 + intervalSize, width,
                height * 21 / 32 + intervalSize, width / 20, height * 23 / 32 + intervalSize, width, height * 23 / 32 + intervalSize, width / 20,
                height * 25 / 32 + intervalSize, width, height * 25 / 32 + intervalSize, width / 20, height * 27 / 32 + intervalSize, width,
                height * 27 / 32 + intervalSize, width / 20, height * 29 / 32 + intervalSize, width, height * 29 / 32 + intervalSize, width / 20,
                height * 31 / 32 + intervalSize, width, height * 31 / 32 + intervalSize};
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
        hp.setAlpha(100);

    }
}
