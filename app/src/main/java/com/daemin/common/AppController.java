package com.daemin.common;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.daemin.enumclass.EnumDialog;
import com.daemin.enumclass.MyPreferences;
import com.daemin.enumclass.User;

import timedao_group.DaoMaster;
import timedao_group.DaoSession;


/**
 * Created by hernia on 2015-06-13.
 */
public class AppController extends Application {
    public DaoSession daoSession;
    static AppController singleton;

    public static AppController getInstance() {
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        User.USER.setGroupListDownloadState(MyPreferences.USERINFO.getPref().getBoolean("GroupListDownloadState", false));
        User.USER.setSubjectDownloadState(MyPreferences.USERINFO.getPref().getBoolean("SubjectDownloadState", false));
        User.USER.setEngUnivName(MyPreferences.USERINFO.getPref().getString("EngUnivName",""));
        MyVolley.init(this);
        setupDatabase();
        /*TelephonyManager systemService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String phonenum = systemService.getLine1Number();
        if(phonenum!=null) {
            phonenum = phonenum.substring(phonenum.length() - 10, phonenum.length());
            phonenum = "0" + phonenum;
            phonenum = PhoneNumberUtils.formatNumber(phonenum);
            Log.i("phonenum", phonenum);ㅊ
            User.USER.setPhoneNum(phonenum);
        }*/
        if(User.USER.isSubjectDownloadState()) EnumDialog.BOTTOMDIAL.setDb(new DatabaseHandler(this));
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "timedao_group", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
