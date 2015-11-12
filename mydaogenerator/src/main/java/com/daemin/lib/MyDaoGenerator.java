package com.daemin.lib;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "timedao");
        Entity MyTime = schema.addEntity("MyTime");
        MyTime.addIdProperty().autoincrement();
        MyTime.addStringProperty("timecode");
        MyTime.addStringProperty("name");
        MyTime.addIntProperty("year");
        MyTime.addIntProperty("monthofyear");
        MyTime.addIntProperty("dayofmonth");
        MyTime.addIntProperty("dayofweek");
        MyTime.addIntProperty("starthour");
        MyTime.addIntProperty("startmin");
        MyTime.addIntProperty("endhour");
        MyTime.addIntProperty("endmin");
        MyTime.addIntProperty("startmillis");
        MyTime.addIntProperty("endmillis");
        MyTime.addStringProperty("memo");
        MyTime.addStringProperty("place");
        MyTime.addDoubleProperty("lat");
        MyTime.addDoubleProperty("lng");
        MyTime.addIntProperty("share");
        MyTime.addStringProperty("alarm");
        MyTime.addStringProperty("repeat");
        MyTime.addStringProperty("color");

        new DaoGenerator().generateAll(schema, args[0]);
    }
}