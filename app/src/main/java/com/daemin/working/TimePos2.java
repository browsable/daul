package com.daemin.working;

import android.graphics.Canvas;

/**
 * Created by hernia on 2015-06-27.
 */
public enum TimePos2 {
    P0101(),P0103(),P0105(),P0107(),P0109(),P0111(),P0113(),P0115(),P0117(),P0119(),P0121(),P0123(),P0125(),P0127(),P0129(),
    P0301(),P0303(),P0305(),P0307(),P0309(),P0311(),P0313(),P0315(),P0317(),P0319(),P0321(),P0323(),P0325(),P0327(),P0329(),
    P0501(),P0503(),P0505(),P0507(),P0509(),P0511(),P0513(),P0515(),P0517(),P0519(),P0521(),P0523(),P0525(),P0527(),P0529(),
    P0701(),P0703(),P0705(),P0707(),P0709(),P0711(),P0713(),P0715(),P0717(),P0719(),P0721(),P0723(),P0725(),P0727(),P0729(),
    P0901(),P0903(),P0905(),P0907(),P0909(),P0911(),P0913(),P0915(),P0917(),P0919(),P0921(),P0923(),P0925(),P0927(),P0929(),
    P1101(),P1103(),P1105(),P1107(),P1109(),P1111(),P1113(),P1115(),P1117(),P1119(),P1121(),P1123(),P1125(),P1127(),P1129(),
    P1301(),P1303(),P1305(),P1307(),P1309(),P1311(),P1313(),P1315(),P1317(),P1319(),P1321(),P1323(),P1325(),P1327(),P1329();

    private PosState2 posState;
    public int xth,yth;
    //public int realStartYth;
    public int startMin,realStartMin;
    public int endMin;
    //private int posIndex;
    //private String title,second,place;
    TimePos2() {
        this.posState = PosState2.NO_PAINT;
        startMin =0;
        endMin = 60;
        /*title ="";
        second="";
        place="";
        posIndex=0;*/
    }

    public int getStartMin() {
        return startMin;
    }
    public int getEndMin() {
        if(endMin==60) return 0;
        else return endMin;
    }
    public void setXY(int xth,int yth) {
        this.xth = xth;
        this.yth = yth;
    }
    public void setMin(int startMin,int endMin) {
        this.startMin = startMin;
        this.endMin = endMin;
    }
    /*
    public void setInitText(){
        this.title="";
        this.second="";
        this.place="";
        this.posIndex=0;A
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
    }*/
    public void setPosState(PosState2 posState) {
        this.posState = posState;
    }
    public PosState2 getPosState() {
        return posState;
    }
    public void drawTimePos(Canvas canvas, int width, int height, int dayInterval, int timeInterval) {
        posState.drawTimePos(canvas, width, height,dayInterval,timeInterval,xth, yth, startMin,endMin);
    }
}