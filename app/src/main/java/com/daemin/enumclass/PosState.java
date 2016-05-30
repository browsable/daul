package com.daemin.enumclass;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.daemin.common.Common;


/**
 * Created by hernia on 2015-06-27.
 */
public enum PosState {
    PAINT(),
    OVERLAP(){
        @Override
        public void drawTimePos(Canvas canvas, int width, int height, int dayInterval, int timeInterval, int xth, int yth, int startMin, int endMin) {
            rp.setColor(Color.parseColor("#FF4A4A"));
            canvas.drawRect((width * 14 / 15) / dayInterval * (xth - 1) + width / 15,
                    (height * 15 / 16) / timeInterval * (yth - 1) + height / 32 + intervalSize + ((height * 15 / 16) / timeInterval) * startMin / 60,
                    (width * 14 / 15) / dayInterval * xth + width / 15,
                    (height * 15 / 16) / timeInterval * (yth - 1) + height / 32 + intervalSize + ((height * 15 / 16) / timeInterval) * endMin / 60, rp);
        }
    },
    NO_PAINT() {
        @Override
        public void drawTimePos(Canvas canvas, int width, int height, int dayInterval, int timeInterval, int xth, int yth, int startMin, int endMin) {
        }
    },
    ENROLL(){
        @Override
        public void drawTimePos(Canvas canvas, int width, int height, int dayInterval, int timeInterval, int xth, int yth, int startMin, int endMin) {
            /*canvas.drawText(title, (width * 7 / 15) /dayInterval * (2*xth-1)+ width / 15,
                    (height * 15 / 16) / timeInterval * (yth - 1) + height / 32 + intervalSize + ((height * 15 / 16) / timeInterval) * startMin / 60+2*intervalSize, tp);
            if(posIndex==2)canvas.drawText(second, (width * 7 / 15) /dayInterval * (2*xth-1)+ width / 15,
                    (height * 15 / 16) / timeInterval * (yth - 1) + height / 32 + intervalSize + ((height * 15 / 16) / timeInterval) * startMin / 60+2*intervalSize+(posIndex-1)*dateSize*9/10, tp);
            canvas.drawText(place, (width * 7 / 15) /dayInterval * (2*xth-1)+ width / 15,
                    (height * 15 / 16) / timeInterval * (yth - 1) + height / 32 + intervalSize + ((height * 15 / 16) / timeInterval) * startMin / 60+2*intervalSize+posIndex*dateSize*9/10,tp);*/
        }
    };
    public Paint rp; // 사각형
    public Paint tp;
    public int intervalSize;
    PosState() {
        intervalSize = User.INFO.intervalSize;
        rp = new Paint(Paint.ANTI_ALIAS_FLAG);
        rp.setColor(Color.parseColor(Common.MAIN_COLOR));
        rp.setAlpha(100);
    }
    public void drawTimePos(Canvas canvas, int width, int height, int dayInterval, int timeInterval, int xth, int yth, int startMin, int endMin) {
        if(xth>0) {
            canvas.drawRect((width * 14 / 15) / dayInterval * (xth - 1) + width / 15,
                    (height * 15 / 16) / timeInterval * (yth - 1) + height / 32 + intervalSize + ((height * 15 / 16) / timeInterval) * startMin / 60,
                    (width * 14 / 15) / dayInterval * xth + width / 15,
                    (height * 15 / 16) / timeInterval * (yth - 1) + height / 32 + intervalSize + ((height * 15 / 16) / timeInterval) * endMin / 60, rp);
        }
    }
}