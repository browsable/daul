package com.daemin.repository;

import android.content.Context;

import com.daemin.common.AppController;

import java.util.List;

import timedao.MyTime;
import timedao.MyTimeDao;

/**
 * Created by hernia on 2015-06-13.
 */
public class MyTimeRepo {
    public static void insertOrUpdate(Context context, MyTime myTime) {
        getMyTimeDao(context).insertOrReplace(myTime);
    }

    public static void clearMyTime(Context context) {
        getMyTimeDao(context).deleteAll();
    }

    public static void deleteMyTimeWithId(Context context, long id) {
        getMyTimeDao(context).delete(getMyTimeForId(context, id));
    }

    public static List<MyTime> getAllMyTime(Context context) {
        return getMyTimeDao(context).loadAll();
    }

    public static MyTime getMyTimeForId(Context context, long id) {
        return getMyTimeDao(context).load(id);
    }
   /* public static String getEngByKor(Context context, String key) {
        return getNormalDao(context)
                .queryBuilder()
                .where(normalDao.Properties)
                .list()
                .get(0)
                .getEngname();
    }*/
    private static MyTimeDao getMyTimeDao(Context c) {
        return ((AppController) c.getApplicationContext()).getDaoSession().getMyTimeDao();
    }
}
