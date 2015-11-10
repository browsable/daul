package com.daemin.lib;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "timedao");
        Entity normal = schema.addEntity("Normal");
        normal.addIdProperty();
        normal.addStringProperty("timecode");
        normal.addStringProperty("name");
        normal.addIntProperty("year");
        normal.addIntProperty("monthofyear");
        normal.addIntProperty("dayofmonth");
        normal.addIntProperty("dayofweek");
        normal.addIntProperty("starthour");
        normal.addIntProperty("startmin");
        normal.addIntProperty("endhour");
        normal.addIntProperty("endmin");
        normal.addIntProperty("startmillis");
        normal.addIntProperty("endmillis");
        normal.addStringProperty("memo");
        normal.addStringProperty("place");
        normal.addIntProperty("lat");
        normal.addIntProperty("lng");
        normal.addIntProperty("share");
        normal.addStringProperty("alarm");
        normal.addStringProperty("repeat");
        normal.addStringProperty("color");

        new DaoGenerator().generateAll(schema, args[0]);
    }
}