package com.daemin.timetable.common;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.daemin.enumclass.User;

import greendao.DaoMaster;
import greendao.DaoSession;


/**
 * Created by hernia on 2015-06-13.
 */
public class AppController extends Application {
    public DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        MyVolley.init(this);
        setupDatabase();
        TelephonyManager systemService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phonenum = systemService.getLine1Number();
        if(phonenum!=null) {
            phonenum = phonenum.substring(phonenum.length() - 10, phonenum.length());
            phonenum = "0" + phonenum;
            phonenum = PhoneNumberUtils.formatNumber(phonenum);
            Log.i("phonenum", phonenum);
            User.USER.setPhoneNum(phonenum);
        }
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "example-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
