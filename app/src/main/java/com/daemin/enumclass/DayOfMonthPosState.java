package com.daemin.enumclass;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.daemin.common.Common;


/**
 * Created by hernia on 2015-06-27.
 */
public enum DayOfMonthPosState {
    PAINT(),
    ENROLL(){
        @Override
        public void drawTimePos(Canvas canvas, int width, int height,String[] title,String[] color, int xth, int yth) {
            for(int i =0; i<4; i++) {
                rp.setColor(Color.parseColor(color[i]));
                canvas.drawRect(width * (xth - 1) / 7, height * ((10 * (yth - 1)) + 4 + 2*i) / 64+intervalSize,
                        width * (xth - 1) / 7+User.INFO.intervalSize, height * ((10 * (yth - 1)) + 6 + 2*i) / 64+intervalSize, rp);
                canvas.drawText(title[i], width * (xth - 1) / 7 + User.INFO.intervalSize, height * ((10 * (yth - 1)) + 5 + 2*i) / 64+User.INFO.intervalSize, tp);
            }
        }
    },
    NO_PAINT() {
        @Override
        public void drawTimePos(Canvas canvas, int width, int height,String[] title,String[] color, int xth, int yth) {
        }
    };
    public Paint rp; // 사각형
    public Paint tp;
    public int intervalSize,dateSize;
    DayOfMonthPosState() {
        intervalSize = User.INFO.intervalSize;
        dateSize = User.INFO.dateSize;
        rp = new Paint(Paint.ANTI_ALIAS_FLAG);
        rp.setColor(Color.parseColor(Common.MAIN_COLOR));
        rp.setAlpha(100);
        tp = new Paint(Paint.ANTI_ALIAS_FLAG);
        tp.setColor(Color.BLACK);
        tp.setTextAlign(Paint.Align.LEFT);
        tp.setTextSize(dateSize);
        tp.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    }

    public void drawTimePos(Canvas canvas, int width, int height,String[] title, String[] color, int xth, int yth) {
            canvas.drawRect(width * (xth-1) / 7, height * ((yth-1)*10+2) / 64 + intervalSize,
                    width * xth / 7, height * (yth*10+2) / 64 + intervalSize, rp);
    }
}