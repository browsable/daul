package com.daemin.enumclass;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.daemin.common.Common;


/**
 * Created by hernia on 2015-06-27.
 */
public enum PosState {
    START("START"),
    INTERMEDIATE("INTERMEDIATE"),
    END("END"),
    ENROLL("ENROLL"),
    NO_PAINT("NO_PAINT") {
        @Override
        public void drawTimePos(Canvas canvas, int width, int height, int xth, int yth) {
        }
    },
    TEMPORARY("TEMPORARY"){ //대학에서 과목 임시선택
        @Override
        public void drawTimePos(Canvas canvas, int width, int height, int xth, int yth) {
            Common.checkTableStateIsNothing = false;
            canvas.drawRect(width * xth / 15, height * yth / 32,
                    width * (xth + 2) / 15, height * (yth + 1) / 32, rp);
        }
    },
    RECOMMEND("RECOMMEND"){
        @Override
        public void drawTimePos(Canvas canvas, int width, int height, int xth, int yth) {
            Common.checkTableStateIsNothing = false;
            canvas.drawRect(width * xth / 15, height * yth / 32,
                    width * (xth + 2) / 15, height * (yth + 1) / 32, rp);
        }
    };

    public Paint rp; // 사각형

    public String getStateName() {
        return stateName;
    }

    public String stateName;
    PosState(String stateName) {
        this.stateName = stateName;
        rp = new Paint(Paint.ANTI_ALIAS_FLAG);
        rp.setColor(Color.parseColor(Common.MAIN_COLOR));
        rp.setAlpha(100);
    }

    public void drawTimePos(Canvas canvas, int width, int height, int xth, int yth) {
        Common.checkTableStateIsNothing = false;
            canvas.drawRect(width * xth / 15, height * yth / 32,
                    width * (xth + 2) / 15, height * (yth + 2) / 32, rp);

    }
}