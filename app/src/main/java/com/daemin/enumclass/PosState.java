package com.daemin.enumclass;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import com.daemin.common.Common;


/**
 * Created by hernia on 2015-06-27.
 */
public enum PosState {
    PAINT(){
    },
    NO_PAINT() {
        @Override
        public void drawTimePos(Canvas canvas, int width, int height, String title, String second, int xth, int yth, int startMin, int endMin) {
        }
    },
    ENROLL(){
        @Override
        public void drawTimePos(Canvas canvas, int width, int height, String title, String second, int xth, int yth, int startMin, int endMin) {
            canvas.drawText(title, width * xth / 15, (height * (yth+1) / 32), tp);
            canvas.drawText(second, width * xth / 15, (height * (yth+2) / 32), tp);
        }
    };
    public Paint rp; // 사각형
    public Paint tp;

    PosState() {
        rp = new Paint(Paint.ANTI_ALIAS_FLAG);
        rp.setColor(Color.parseColor(Common.MAIN_COLOR));
        rp.setAlpha(100);
        rp.setTextAlign(Paint.Align.CENTER);
        tp = new Paint(Paint.ANTI_ALIAS_FLAG);
        tp.setColor(Color.BLACK);
        tp.setTextSize(30);
        tp.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }
    public void drawTimePos(Canvas canvas, int width, int height, String title, String second, int xth, int yth, int startMin, int endMin) {
        canvas.drawRect(width * xth / 15, (height * yth / 32 + 18)+(2*height/32)*startMin/60,
                width * (xth + 2) / 15, (height * yth / 32 + 18)+(2*height/32)*endMin/60, rp);
    }
}