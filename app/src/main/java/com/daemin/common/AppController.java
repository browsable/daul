package com.daemin.common;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import timedao.DaoMaster;
import timedao.DaoSession;

/**
 * Created by hernia on 2015-06-13.
 */
public class AppController extends Application {
    public DaoSession daoSession;
    static AppController singleton;
    String serverVer;
    String appVer;

    public static AppController getInstance() {
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        MyVolley.init(this);
        setupDatabase();
        MyRequest.getVersionFromServer(this);
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "timedao", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
