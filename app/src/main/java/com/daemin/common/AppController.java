package com.daemin.common;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

import com.daemin.enumclass.User;
import com.google.firebase.analytics.FirebaseAnalytics;

import timedao.DaoMaster;
import timedao.DaoSession;

/**
 * Created by hernia on 2015-06-13.
 */
public class AppController extends Application {
    public timedao.DaoSession timedaoSession;
    public widget.DaoSession widgetSession;
    static AppController singleton;
    public static AppController getInstance() {
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        MyVolley.init(this);
        setupDatabase();
        getAppVer();
    }
    private void getAppVer(){
        PackageInfo pInfo;
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(
                    getApplicationContext().getPackageName(), 0);
            User.INFO.appVer = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "timedao", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        timedaoSession = daoMaster.newSession();

        widget.DaoMaster.DevOpenHelper helper2 = new widget.DaoMaster.DevOpenHelper(this, "widget", null);
        SQLiteDatabase db2 = helper2.getWritableDatabase();
        widget.DaoMaster daoMaster2 = new widget.DaoMaster(db2);
        widgetSession = daoMaster2.newSession();
    }

    public DaoSession getDaoSession() {
        return timedaoSession;
    }
    public widget.DaoSession getWidSession() {
        return widgetSession;
    }
}
