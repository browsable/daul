package com.daemin.lib;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    // Your first database schema version
    private static final int timedaoSchemaVer = 1;
    // Your second database schema version
    private static final int widgetSchemaVer = 1;
    // Your first database package
    private static final String timedaoPackage = "timedao";
    // Your second database package
    private static final String widgetPackage = "widget";
    // Path to save generated files
    private static final String _path = "../app/src/main/java-gen";


    public static void main(String args[]) throws Exception {

        // Database1 schema
        Schema timedaoSchema = new Schema(timedaoSchemaVer, timedaoPackage);
       // timedaoSchema.enableKeepSectionsByDefault();

        // Database2 schema
        Schema widgetSchema = new Schema(widgetSchemaVer, widgetPackage);
        widgetSchema.enableKeepSectionsByDefault();

        //Add methods where you define your databases
        addTimeDAO(timedaoSchema);
        addWidget(widgetSchema);

        // Generate your databases
        try {
            DaoGenerator gen = new DaoGenerator();

            // Generate database1
            gen.generateAll(timedaoSchema, _path);
            // Generate database2
            gen.generateAll(widgetSchema, _path);

            System.out.println("Successfully generated all files to: " + _path);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception error: " + e.getMessage());
        }
    }
    private static void addTimeDAO(Schema schema) {
        Entity MyTime = schema.addEntity("MyTime");
        MyTime.addIdProperty().autoincrement();
        MyTime.addStringProperty("timecode").notNull();
        MyTime.addIntProperty("timetype").notNull();
        MyTime.addStringProperty("name");
        MyTime.addIntProperty("year");
        MyTime.addIntProperty("monthofyear");
        MyTime.addIntProperty("dayofmonth");
        MyTime.addIntProperty("dayofweek").notNull();
        MyTime.addIntProperty("starthour").notNull();
        MyTime.addIntProperty("startmin").notNull();
        MyTime.addIntProperty("endhour").notNull();
        MyTime.addIntProperty("endmin").notNull();
        MyTime.addLongProperty("startmillis");
        MyTime.addLongProperty("endmillis");
        MyTime.addStringProperty("memo");
        MyTime.addStringProperty("place");
        MyTime.addDoubleProperty("lat");
        MyTime.addDoubleProperty("lng");
        MyTime.addIntProperty("share");
        MyTime.addLongProperty("alarm");
        MyTime.addStringProperty("repeat");
        MyTime.addStringProperty("color");
    }
    private static void addWidget(Schema schema) {
        Entity WidgetID = schema.addEntity("WidgetID");
        WidgetID.addIdProperty().autoincrement();
        WidgetID.addIntProperty("tvID").notNull();

    }
}