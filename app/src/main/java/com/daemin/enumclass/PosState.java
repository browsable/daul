package com.daemin.enumclass;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.daemin.common.Common;


/**
 * Created by hernia on 2015-06-27.
 */
public enum PosState {
    INTERMEDIATE, END, ENROLL,SUBJECT,
    NO_PAINT {
        @Override
        public void drawTimePos(Canvas canvas, int width, int height, int xth, int yth) {
        }
    },
    START;

    public Paint rp; // 사각형

    PosState() {
        rp = new Paint(Paint.ANTI_ALIAS_FLAG);
        rp.setColor(Color.parseColor(Common.MAIN_COLOR));
        rp.setAlpha(100);
    }

    public void drawTimePos(Canvas canvas, int width, int height, int xth, int yth) {
        Common.checkTableStateIsNothing = false;
        canvas.drawRect(width * xth / 15, height * yth / 32,
                width * (xth + 2) / 15, height * (yth + 2) / 32, rp);
    }
    public void drawTimePosForSubject(Canvas canvas, int width, int height, int xth, int yth) {
        Common.checkTableStateIsNothing = false;
        canvas.drawRect(width * xth / 15, height * yth / 32,
                width * (xth + 2) / 15, height * (yth + 1) / 32, rp);
    }
}