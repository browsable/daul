package com.daemin.repository;

import android.content.Context;

import com.daemin.common.AppController;

import java.util.List;

import timedao.Normal;
import timedao.NormalDao;

/**
 * Created by hernia on 2015-06-13.
 */
public class normalRepository {
    public static void insertOrUpdate(Context context, Normal normal) {
        getNormalDao(context).insertOrReplace(normal);
    }

    public static void clearGroupListFromServer(Context context) {
        getNormalDao(context).deleteAll();
    }

    public static void deleteGroupListFromServerWithId(Context context, long id) {
        getNormalDao(context).delete(getNormalForId(context, id));
    }

    public static List<Normal> getAllGroupListFromServer(Context context) {
        return getNormalDao(context).loadAll();
    }

    public static Normal getNormalForId(Context context, long id) {
        return getNormalDao(context).load(id);
    }
   /* public static String getEngByKor(Context context, String key) {
        return getNormalDao(context)
                .queryBuilder()
                .where(normalDao.Properties)
                .list()
                .get(0)
                .getEngname();
    }*/
    private static NormalDao getNormalDao(Context c) {
        return ((AppController) c.getApplicationContext()).getDaoSession().getNormalDao();
    }
}
