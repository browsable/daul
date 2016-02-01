package com.daemin.repository;

import android.content.Context;

import com.daemin.common.AppController;

import java.util.List;

import timedao.WidgetID;
import timedao.WidgetIDDao;

public class WidgetIDRepo {
    public static void insertOrUpdate(Context context, WidgetID widgetID) {
        getWidgetIDDao(context).insertOrReplace(widgetID);
    }
    public static void clearWidgetID(Context context) {
        getWidgetIDDao(context).deleteAll();
    }
    public static void deleteWithId(Context context, long id) {
        getWidgetIDDao(context).delete(getWidgetIDForId(context, id));
    }
    public static WidgetID getWidgetIDForId(Context context, long id) {
        return getWidgetIDDao(context).load(id);
    }
    public static List<WidgetID> getAllWidgetID(Context context) {
        return getWidgetIDDao(context).loadAll();
    }
    private static WidgetIDDao getWidgetIDDao(Context c) {
        return ((AppController) c.getApplicationContext()).getDaoSession().getWidgetIDDao();
    }
}
