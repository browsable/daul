package com.daemin.timetable;

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
import com.daemin.dialog.DialSchedule;
import com.daemin.enumclass.Dates;
import com.daemin.enumclass.DrawMode;
import com.daemin.enumclass.PosState;
import com.daemin.enumclass.TimePos;
import com.daemin.event.ExcuteMethodEvent;
import com.daemin.event.FinishDialogEvent;
import com.daemin.main.MainActivity;

import de.greenrobot.event.EventBus;

@SuppressLint("DefaultLocale")
public class InitWeekThread extends InitThread {
    SurfaceHolder mholder;
    private boolean isLoop = true;
    private int width, height,dayOfWeek; //화면의 전체 너비, 높이
    Context context;
    private Paint hp; // 1시간 간격 수평선
    private Paint hpvp; // 30분 간격 수평선, 수직선
    private Paint tp, tpred, tpblue; // 시간 텍스트
    static int tempxth, tempyth;
    Canvas canvas;
    public InitWeekThread(SurfaceHolder holder, Context context) {
        this.mholder = holder;
        this.context = context;
        Common.fetchWeekData();
        this.dayOfWeek = Dates.NOW.getDayOfWeek();
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
                    for (TimePos ETP : TimePos.values()) {
                        ETP.drawTimePos(canvas, width, height);
                    }
                }
            } catch (Exception e) {
            } finally {
                if (canvas != null)
                    mholder.unlockCanvasAndPost(canvas);
            }
            //System.gc();
        }
    }
    public void getDownXY(int xth, int yth) {
        makeTimePos(xth, yth);
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
                    EventBus.getDefault().post(new FinishDialogEvent());
                    if(ETP.getStartMin()==0&&ETP.getEndMin()==0) {
                        Intent i = new Intent(context, DialSchedule.class);
                        i.putExtra("enrollFlag", true);
                        i.putExtra("overlapEnrollFlag", false);
                        i.putExtra("xth", xth);
                        i.putExtra("yth", tmpYth);
                        i.putExtra("startMin", ETP.getStartMin());
                        context.startActivity(i);
                    }else{
                        DialEnroll de = new DialEnroll(context,xth,tmpYth,ETP.getStartMin());
                        de.show();
                    }
                }
                break;
            case 1: //대학
                //대학선택시에 그리는 것은 막고 선택한 과목은 함께 지워져야함
                if (Common.isTableEmpty()){
                    Toast.makeText(context, "과목을 선택하세요", Toast.LENGTH_SHORT).show();
                } else {
                    Common.stateFilter(0);
                }
                if (ETP.getPosState() == PosState.ENROLL) {
                    EventBus.getDefault().post(new FinishDialogEvent());
                    Intent i = new Intent(context, DialSchedule.class);
                    i.putExtra("enrollFlag",true);
                    i.putExtra("xth",xth);
                    i.putExtra("yth",tmpYth);
                    i.putExtra("startMin",ETP.getStartMin());
                    context.startActivity(i);
                }
                break;
        }
        return;
    }
    public void initScreen() {

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
        canvas.drawText(Dates.NOW.mdOfSun, width * 2 / 15, (height / 32 + 18) * 7 / 16, tpred);
        canvas.drawText(Dates.NOW.mdOfMon, width * 4 / 15, (height / 32 + 18) * 7 / 16, tp);
        canvas.drawText(Dates.NOW.mdOfTue, width * 6 / 15, (height / 32 + 18) * 7 / 16, tp);
        canvas.drawText(Dates.NOW.mdOfWed, width * 8 / 15, (height / 32 + 18) * 7 / 16, tp);
        canvas.drawText(Dates.NOW.mdOfThr, width * 10 / 15, (height / 32 + 18) * 7 / 16, tp);
        canvas.drawText(Dates.NOW.mdOfFri, width * 12 / 15, (height / 32 + 18) * 7 / 16, tp);
        canvas.drawText(Dates.NOW.mdOfSat, width * 14 / 15, (height / 32 + 18) * 7 / 16, tpblue);
        canvas.drawText("SUN", width * 2 / 15, (height / 32 + 18) * 15 / 16, tpred);
        canvas.drawText("MON", width * 4 / 15, (height / 32 + 18) * 15 / 16, tp);
        canvas.drawText("TUE", width * 6 / 15, (height / 32 + 18) * 15 / 16, tp);
        canvas.drawText("WED", width * 8 / 15, (height / 32 + 18) * 15 / 16, tp);
        canvas.drawText("THU", width * 10 / 15, (height / 32 + 18) * 15 / 16, tp);
        canvas.drawText("FRI", width * 12 / 15, (height / 32 + 18) * 15 / 16, tp);
        canvas.drawText("SAT", width * 14 / 15, (height / 32 + 18) * 15 / 16, tpblue);
        hp.setAlpha(40);
        if(Dates.NOW.isToday)canvas.drawRect(width * (2 * dayOfWeek + 1) / 15, ((height * 2) - 10) / 64 + 18, width * (2 * dayOfWeek + 3) / 15, height * 62 / 64 + 18, hp);
        hp.setAlpha(100);
    }
}
