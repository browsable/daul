package com.daemin.repository;

import android.content.Context;

import com.daemin.timetable.common.AppController;

import java.util.List;

import greendao.Box;
import greendao.BoxDao;

/**
 * Created by hernia on 2015-06-13.
 */
public class BoxRepository {
    public static void insertOrUpdate(Context context, Box box) {
        getBoxDao(context).insertOrReplace(box);
    }

    public static void clearBoxes(Context context) {
        getBoxDao(context).deleteAll();
    }

    public static void deleteBoxWithId(Context context, long id) {
        getBoxDao(context).delete(getBoxForId(context, id));
    }

    public static List<Box> getAllBoxes(Context context) {
        return getBoxDao(context).loadAll();
    }

    public static Box getBoxForId(Context context, long id) {
        return getBoxDao(context).load(id);
    }

    private static BoxDao getBoxDao(Context c) {
        return ((AppController) c.getApplicationContext()).getDaoSession().getBoxDao();
    }
}
