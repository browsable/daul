package com.daemin.enumclass;

import android.graphics.Canvas;

/**
 * Created by hernia on 2015-06-27.
 */
public enum TimePos {
    P0101(1, 1),P0103(1, 3),P0105(1, 5),P0107(1, 7),P0109(1, 9),P0111(1, 11),P0113(1, 13),P0115(1, 15),P0117(1, 17),P0119(1, 19),P0121(1, 21),P0123(1, 23),P0125(1, 25),P0127(1, 27),P0129(1, 29),
    P0301(3, 1),P0303(3, 3),P0305(3, 5),P0307(3, 7),P0309(3, 9),P0311(3, 11),P0313(3, 13),P0315(3, 15),P0317(3, 17),P0319(3, 19),P0321(3, 21),P0323(3, 23),P0325(3, 25),P0327(3, 27),P0329(3, 29),
    P0501(5, 1),P0503(5, 3),P0505(5, 5),P0507(5, 7),P0509(5, 9),P0511(5, 11),P0513(5, 13),P0515(5, 15),P0517(5, 17),P0519(5, 19),P0521(5, 21),P0523(5, 23),P0525(5, 25),P0527(5, 27),P0529(5, 29),
    P0701(7, 1),P0703(7, 3),P0705(7, 5),P0707(7, 7),P0709(7, 9),P0711(7, 11),P0713(7, 13),P0715(7, 15),P0717(7, 17),P0719(7, 19),P0721(7, 21),P0723(7, 23),P0725(7, 25),P0727(7, 27),P0729(7, 29),
    P0901(9, 1),P0903(9, 3),P0905(9, 5),P0907(9, 7),P0909(9, 9),P0911(9, 11),P0913(9, 13),P0915(9, 15),P0917(9, 17),P0919(9, 19),P0921(9, 21),P0923(9, 23),P0925(9, 25),P0927(9, 27),P0929(9, 29),
    P1101(11, 1),P1103(11, 3),P1105(11, 5),P1107(11, 7),P1109(11, 9),P1111(11, 11),P1113(11, 13),P1115(11, 15),P1117(11, 17),P1119(11, 19),P1121(11, 21),P1123(11, 23),P1125(11, 25),P1127(11, 27),P1129(11, 29),
    P1301(13, 1),P1303(13, 3),P1305(13, 5),P1307(13, 7),P1309(13, 9),P1311(13, 11),P1313(13, 13),P1315(13, 15),P1317(13, 17),P1319(13, 19),P1321(13, 21),P1323(13, 23),P1325(13, 25),P1327(13, 27),P1329(13, 29);

    private PosState posState;
    private int xth;
    public int yth,realStartYth;
    public int startMin,realStartMin;
    public int endMin;
    private int posIndex;
    private String title,second,place;
    TimePos() {
    }
    TimePos(int xth, int yth) {
        this.xth = xth;
        this.yth = yth;
        this.posState = PosState.NO_PAINT;
        startMin =0;
        endMin = 60;
        title ="";
        second="";
        place="";
        posIndex=0;
    }
    public int getXth() {
        return xth;
    }
    public int getYth() {
        return yth;
    }
    public int getStartMin() {
        return startMin;
    }
    public int getEndMin() {
        if(endMin==60) return 0;
        else return endMin;
    }

    public void setMin(int startMin,int endMin) {
        this.startMin = startMin;
        this.endMin = endMin;
    }
    public void setInitText(){
        this.title="";
        this.second="";
        this.place="";
        this.posIndex=0;
    }
    public void setRealStart(int realStartYth, int realStartMin){
        this.realStartYth = realStartYth;
        this.realStartMin = realStartMin;
    }
    public void setText(String title, String place) {
        if(this.title.equals("")){
            if(title.length()>5){
                this.title = title.substring(0,5);
                this.second = title.substring(5);
                if(this.second.length()>5){
                    this.second = second.substring(0,5)+"..";
                }
                ++posIndex;
            }else{
                this.title = title;
                this.second="";
            }
        }
        if(this.place.equals("")){
            ++posIndex;
            if(place.length()>5) this.place = place.substring(0,6);
            else this.place = place;
        }
    }
    public void setPosState(PosState posState) {
        this.posState = posState;
    }
    public PosState getPosState() {
        return posState;
    }
    public void drawTimePos(Canvas canvas, int width, int height) {
        posState.drawTimePos(canvas, width, height,title, second, place, realStartYth, realStartMin, posIndex, xth, yth, startMin,endMin);
    }
}