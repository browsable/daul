package com.daemin.repository;

import android.content.Context;

import com.daemin.common.AppController;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
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
    public static void deleteWithId(Context context, long id) {
        getMyTimeDao(context).delete(getMyTimeForId(context, id));
    }
    public static MyTime getMyTimeForId(Context context, long id) {
        return getMyTimeDao(context).load(id);
    }
    public static void deleteWithTimetype(Context context, int timetype) {
        QueryBuilder qb = getMyTimeDao(context).queryBuilder();
        qb.where(MyTimeDao.Properties.Timetype.eq(timetype));
        qb.buildDelete().executeDeleteWithoutDetachingEntities();
    }
    public static void deleteWithTimeCode(Context context, String timeCode) {
        QueryBuilder qb = getMyTimeDao(context).queryBuilder();
        qb.where(MyTimeDao.Properties.Timecode.eq(timeCode));
        qb.buildDelete().executeDeleteWithoutDetachingEntities();
    }
    public static List<MyTime> getAllMyTime(Context context) {
        return getMyTimeDao(context).loadAll();
    }
    public static List<MyTime> getMyTimeWithTimeCode(Context context, String timeCode){
        QueryBuilder qb = getMyTimeDao(context).queryBuilder();
        qb.where(MyTimeDao.Properties.Timecode.eq(timeCode));
        return qb.list();
    }
    public static List<MyTime> getWeekTimes(Context context, long week_startMillies, long week_endMillies){
        QueryBuilder qb = getMyTimeDao(context).queryBuilder();
        qb.where(qb.or(MyTimeDao.Properties.Startmillis.between(week_startMillies, week_endMillies),
                MyTimeDao.Properties.Timetype.eq(1)));
        return qb.list();
    }
    public static List<MyTime> getMonthTimes(Context context, long month_startMillies, long month_endMillies){
        QueryBuilder qb = getMyTimeDao(context).queryBuilder();
        qb.where(qb.and(MyTimeDao.Properties.Startmillis.between(month_startMillies, month_endMillies),
                        MyTimeDao.Properties.Timetype.eq(0)
                        ));
        return qb.orderAsc(MyTimeDao.Properties.Startmillis).list();
    }
    public static List<MyTime> getHourTimes(Context context, long startmillis, long endmillis, int xth, int startHour, int endMin){
        QueryBuilder qb = getMyTimeDao(context).queryBuilder();
            if(endMin==0) {
                qb.where(qb.or( qb.and(MyTimeDao.Properties.Timetype.eq(1),
                                MyTimeDao.Properties.Dayofweek.eq(xth),
                                MyTimeDao.Properties.Starthour.le(startHour),
                                MyTimeDao.Properties.Endhour.gt(startHour)),
                        qb.or(MyTimeDao.Properties.Startmillis.between(startmillis, endmillis),
                                qb.and(MyTimeDao.Properties.Startmillis.lt(startmillis),
                                        MyTimeDao.Properties.Endmillis.gt(endmillis)),
                                MyTimeDao.Properties.Endmillis.between(startmillis, endmillis))
                       ));
            }else{
                qb.where(qb.or(qb.and(MyTimeDao.Properties.Timetype.eq(1),
                                MyTimeDao.Properties.Dayofweek.eq(xth),
                                MyTimeDao.Properties.Starthour.le(startHour),
                                MyTimeDao.Properties.Endhour.ge(startHour)),
                        qb.or(MyTimeDao.Properties.Startmillis.between(startmillis, endmillis),
                                qb.and(MyTimeDao.Properties.Startmillis.lt(startmillis),
                                        MyTimeDao.Properties.Endmillis.gt(endmillis)),
                                MyTimeDao.Properties.Endmillis.between(startmillis, endmillis))
                        ));
            }
        return qb.list();
    }
    public static int overLapCheck(Context context,int xth, int startHour){
        int overlap = 0;
        QueryBuilder qb = getMyTimeDao(context).queryBuilder();
        qb.where(
                qb.and(MyTimeDao.Properties.Timetype.eq(1),
                        MyTimeDao.Properties.Dayofweek.eq(xth),
                        MyTimeDao.Properties.Starthour.le(startHour),
                        MyTimeDao.Properties.Endhour.ge(startHour))
        );
        if(qb.list()!=null) {
            overlap = 1;
            return overlap;
        }else return overlap;
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
