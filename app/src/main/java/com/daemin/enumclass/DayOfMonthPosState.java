package com.daemin.enumclass;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.daemin.common.Common;


/**
 * Created by hernia on 2015-06-27.
 */
public enum DayOfMonthPosState {
    PAINT(),
    NO_PAINT() {
        @Override
        public void drawTimePos(Canvas canvas, int width, int height, int xth, int yth) {
        }
    };
    public Paint rp; // 사각형
    DayOfMonthPosState() {
        rp = new Paint(Paint.ANTI_ALIAS_FLAG);
        rp.setColor(Color.parseColor(Common.MAIN_COLOR));
        rp.setAlpha(100);
    }

    public void drawTimePos(Canvas canvas, int width, int height, int xth, int yth) {
        Common.checkTableStateIsNothing = false;
            canvas.drawRect(width * (xth-1) / 7, height * ((yth-1)*10+2) / 64 + 6,
                    width * xth / 7, height * (yth*10+2) / 64 + 6, rp);

    }
}