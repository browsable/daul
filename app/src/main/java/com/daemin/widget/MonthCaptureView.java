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

import com.daemin.common.Common;
import com.daemin.enumclass.Dates;
import com.daemin.timetable.R;

/**
 * Created by hernia on 2015-11-05.
 */
public class MonthCaptureView extends ImageView {
    private int width;
    private int height;
    private int ty;
    private int tx;//이전달의 마지막날 요일
    private Paint hp; // 1시간 간격 수평선
    private Paint hpvp; // 30분 간격 수평선, 수직선
    private Paint tp,tpred,tpblue,tpgray,rp; // 시간 텍스트
    static int tempxth;
    static int tempyth;
    private Canvas canvas;
    public MonthCaptureView(Context context)
    {
        super(context);
        tempxth = 0;
        tempyth = 0;
        int textSize = context.getResources().getDimensionPixelSize(R.dimen.textsize_l);
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
                0, height / 32 + 6, width, height / 32 + 6 , 0, height * 12 / 64 + 6, width,
                height * 12 / 64 + 6, 0, height * 22 / 64 + 6, width, height * 22 / 64 + 6, 0,
                height * 32 / 64 + 6, width, height * 32 / 64 + 6, 0, height * 42 / 64 + 6, width,
                height * 42 / 64 + 6, 0, height * 52 / 64 + 6, width, height * 52 / 64 + 6, 0,
                height * 62 / 64 + 6, width, height * 62 / 64 + 6};

        float[] vp = {
                // 세로 선
                2, height / 32 + 6, 2, height * 62 / 64+ 6, width / 7,
                height / 32 + 6, width / 7, height * 62 / 64+ 6, width * 2 / 7,
                height / 32 + 6, width * 2 / 7, height * 62 / 64+ 6, width * 3 / 7,
                height / 32 + 6, width * 3 / 7, height * 62 / 64+ 6, width * 4 / 7,
                height / 32 + 6, width * 4 / 7, height * 62 / 64+ 6, width * 5 / 7,
                height / 32 + 6, width * 5 / 7, height * 62 / 64+ 6, width * 6 / 7,
                height / 32 + 6, width * 6 / 7, height * 62 / 64+ 6, width-2,
                height / 32 + 6, width-2, height * 62 / 64+ 6};

        canvas.drawColor(Color.WHITE);
        canvas.drawLines(hp_hour, hp);
        canvas.drawLines(vp, hpvp);
        hp.setAlpha(40);
        if(Dates.NOW.isToday)canvas.drawRect(width * (tx - 1) / 7, height * ((ty / 7) * 10 + 2) / 64 + 6,width * tx / 7, height * ((ty / 7 + 1) * 10 + 2) / 64 + 6, hp);
        hp.setAlpha(100);
        canvas.drawText("SUN", width * 1 / 14, height * 2 / 62 - 1, tpred);
        canvas.drawText("MON", width * 3 / 14, height * 2/ 62 - 1, tp);
        canvas.drawText("TUE", width * 5 / 14, height * 2/ 62 - 1, tp);
        canvas.drawText("WED", width * 7 / 14, height * 2/ 62 - 1, tp);
        canvas.drawText("THU", width * 9 / 14, height * 2 / 62 - 1, tp);
        canvas.drawText("FRI", width * 11 / 14, height * 2 / 62 - 1, tp);
        canvas.drawText("SAT", width * 13 / 14, height * 2 / 62 - 1, tpblue);

        for(int i = 0; i<Dates.NOW.dayOfWeek+1; i++){
            canvas.drawText(Dates.NOW.mData[i], width * (4 * (i % 7) + 1) / 28 - 6, height * ((10 * (i / 7)) + 4) / 64, tpgray);
        }
        for(int i = Dates.NOW.dayOfWeek+1; i<Dates.NOW.dayOfWeek+Dates.NOW.dayNumOfMonth+1; i++){
            int j = i%7;
            switch(j){
                case 0:
                    canvas.drawText(Dates.NOW.mData[i], width * 1 / 28-6, height* (( 10 * (i/7)) + 4) / 64, tpred);
                    break;
                case 6:
                    canvas.drawText(Dates.NOW.mData[i], width * 25 / 28-6, height * ((10 * (i/7)) + 4) / 64, tpblue);
                    break;
                default:
                    canvas.drawText(Dates.NOW.mData[i], width * (4*j+1) / 28-6, height * ((10 * (i/7)) + 4) / 64, tp);
                    break;
            }
        }
        for(int i = Dates.NOW.dayOfWeek + Dates.NOW.dayNumOfMonth + 1; i < 42; i++) {
            canvas.drawText(Dates.NOW.mData[i], width * (4 * (i%7)+1) / 28-6, height * ((10 * (i/7)) + 4) / 64, tpgray);
        }

    }
}
