package com.daemin.enumclass;

import android.graphics.Canvas;

import com.daemin.common.Common;

/**
 * Created by hernia on 2015-06-27.
 */
public enum DayOfMonthPos {
    M0101(1, 1),M0201(2, 1),M0301(3, 1),M0401(4, 1),M0501(5, 1),M0601(6, 1),M0701(7, 1),
    M0102(1, 2),M0202(2, 2),M0302(3, 2),M0402(4, 2),M0502(5, 2),M0602(6, 2),M0702(7, 2),
    M0103(1, 3),M0203(2, 3),M0303(3, 3),M0403(4, 3),M0503(5, 3),M0603(6, 3),M0703(7, 3),
    M0104(1, 4),M0204(2, 4),M0304(3, 4),M0404(4, 4),M0504(5, 4),M0604(6, 4),M0704(7, 4),
    M0105(1, 5),M0205(2, 5),M0305(3, 5),M0405(4, 5),M0505(5, 5),M0605(6, 5),M0705(7, 5),
    M0106(1, 6),M0206(2, 6),M0306(3, 6),M0406(4, 6),M0506(5, 6),M0606(6, 6),M0706(7, 6);
    private DayOfMonthPosState posState;
    private int xth;
    private int yth;
    private int enrollCnt;
    private String[] title,color;
    DayOfMonthPos() {
    }
    DayOfMonthPos(int xth, int yth) {
        this.xth = xth;
        this.yth = yth;
        this.posState = DayOfMonthPosState.NO_PAINT;
        enrollCnt=0;
        title = new String[4];
        color = new String[4];
        for(int i = 0; i<4; i++){
            title[i]="";
            color[i]= Common.TRANS_COLOR;
        }
    }
    public int getXth() {
        return xth;
    }
    public int getYth() {
        return yth;
    }
    public int getEnrollCnt() {
        return enrollCnt;
    }
    public void setEnrollCnt() {
        ++this.enrollCnt;
    }
    public void setPosState(DayOfMonthPosState posState) {
        this.posState = posState;
    }
    public void setTitleAndColor(String title,String color) {
        if(title.length()>5) title = title.substring(0,5);
        if(enrollCnt<4) {
            this.color[enrollCnt] = color;
            this.title[enrollCnt] = title;
        }
    }
    public void setInitTitle(){
        for(int i = 0; i<4; i++){
            title[i]="";
            color[i]=Common.TRANS_COLOR;
        }
        enrollCnt=0;
    }
    public DayOfMonthPosState getPosState() {
        return posState;
    }

    public void drawTimePos(Canvas canvas, int width, int height) {
        posState.drawTimePos(canvas, width, height,title,color, xth, yth);
    }
}