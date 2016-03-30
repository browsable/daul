package com.daemin.enumclass;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.daemin.common.Common;


/**
 * Created by hernia on 2015-06-27.
 */
public enum PosState2 {
    PAINT(),
    OVERLAP(){
        @Override
        public void drawTimePos(Canvas canvas, int width, int height, String title, String second, String place, int realStartYth,int realStartMin, int posIndex, int xth, int yth, int startMin, int endMin) {
            rp.setColor(Color.parseColor("#FF4A4A"));
            canvas.drawRect(width * xth / 15, (height * yth / 32 + intervalSize)+(2*height/32)*startMin/60,
                    width * (xth + 2) / 15, (height * yth / 32 + intervalSize)+(2*height/32)*endMin/60, rp);
        }
    },
    NO_PAINT() {
        @Override
        public void drawTimePos(Canvas canvas, int width, int height, String title, String second, String place, int realStartYth,int realStartMin, int posIndex, int xth, int yth, int startMin, int endMin) {
        }
    },
    ENROLL(){
        @Override
        public void drawTimePos(Canvas canvas, int width, int height, String title, String second, String place, int realStartYth,int realStartMin,int posIndex, int xth, int yth, int startMin, int endMin) {
            canvas.drawText(title, width * (xth+1) / 15, (height * (realStartYth+1) / 32)+(2*height/32)*realStartMin/60-intervalSize, tp);
            if(posIndex==2)canvas.drawText(second, width * (xth+1) / 15, (height * (realStartYth+1) / 32) +(2*height/32)*realStartMin/60-intervalSize+(posIndex-1)*dateSize*9/10, tp);
            canvas.drawText(place, width * (xth + 1) / 15,(height * (realStartYth+1) / 32) +(2*height/32)*realStartMin/60-intervalSize+posIndex*dateSize*9/10,tp);

        }
    };
    public Paint rp; // 사각형
    public Paint tp;
    public int intervalSize,dateSize;
    PosState2() {
        intervalSize = User.INFO.intervalSize;
        dateSize = User.INFO.dateSize;
        rp = new Paint(Paint.ANTI_ALIAS_FLAG);
        rp.setColor(Color.parseColor(Common.MAIN_COLOR));
        rp.setAlpha(100);
        tp = new Paint(Paint.ANTI_ALIAS_FLAG);
        tp.setColor(Color.BLACK);
        tp.setTextAlign(Paint.Align.CENTER);
        tp.setTextSize(User.INFO.textSize);
        tp.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }
    public void drawTimePos(Canvas canvas, int width, int height, String title, String second, String place, int realStartYth,int realStartMin, int posIndex, int xth, int yth, int startMin, int endMin) {
        canvas.drawRect(width * xth / 15, (height * yth / 32 + intervalSize)+(2*height/32)*startMin/60,
                width * (xth + 2) / 15, (height * yth / 32 + intervalSize)+(2*height/32)*endMin/60, rp);
    }
}