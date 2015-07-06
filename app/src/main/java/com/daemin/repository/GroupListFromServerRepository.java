package com.daemin.repository;

import android.content.Context;

import com.daemin.common.AppController;

import java.util.List;

import timedao_group.GroupListFromServer;
import timedao_group.GroupListFromServerDao;

/**
 * Created by hernia on 2015-06-13.
 */
public class GroupListFromServerRepository {
    public static void insertOrUpdate(Context context, GroupListFromServer GroupListFromServer) {
        getGroupListFromServerDao(context).insertOrReplace(GroupListFromServer);
    }

    public static void clearGroupListFromServer(Context context) {
        getGroupListFromServerDao(context).deleteAll();
    }

    public static void deleteGroupListFromServerWithId(Context context, long id) {
        getGroupListFromServerDao(context).delete(getGroupListFromServerForId(context, id));
    }

    public static List<GroupListFromServer> getAllGroupListFromServer(Context context) {
        return getGroupListFromServerDao(context).loadAll();
    }

    public static GroupListFromServer getGroupListFromServerForId(Context context, long id) {
        return getGroupListFromServerDao(context).load(id);
    }
    public static String getEngByKor(Context context, String key) {
        return getGroupListFromServerDao(context)
                .queryBuilder()
                .where(GroupListFromServerDao.Properties.Korname.eq(key))
                .list()
                .get(0)
                .getEngname();
    }
    private static GroupListFromServerDao getGroupListFromServerDao(Context c) {
        return ((AppController) c.getApplicationContext()).getDaoSession().getGroupListFromServerDao();
    }
}
