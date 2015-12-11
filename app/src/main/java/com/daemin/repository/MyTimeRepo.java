package com.daemin.repository;

import android.content.Context;

import com.daemin.common.AppController;

import java.util.List;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
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
    public static void deleteWithTimeCode(Context context, String timeCode) {
        QueryBuilder qb = getMyTimeDao(context).queryBuilder();
        qb.where(MyTimeDao.Properties.Timecode.eq(timeCode));
        qb.buildDelete().executeDeleteWithoutDetachingEntities();
    }
    public static List<MyTime> getAllMyTime(Context context) {
        return getMyTimeDao(context).loadAll();
    }
    public static List<MyTime> getWeekTimes(Context context, long week_startMillies, long week_endMillies){
        QueryBuilder qb = getMyTimeDao(context).queryBuilder();
        qb.where(qb.or(MyTimeDao.Properties.Startmillis.between(week_startMillies, week_endMillies),
                MyTimeDao.Properties.Timetype.eq(1)));
        return qb.list();
    }
    public static List<MyTime> getHourTimes(Context context, long startmillis, long endmillis, int xth, int startHour){
        QueryBuilder qb = getMyTimeDao(context).queryBuilder();
            qb.where(qb.or(
                    qb.or(MyTimeDao.Properties.Startmillis.between(startmillis, endmillis),
                            qb.and(MyTimeDao.Properties.Startmillis.lt(startmillis),
                                    MyTimeDao.Properties.Endmillis.gt(endmillis)),
                            MyTimeDao.Properties.Endmillis.between(startmillis, endmillis)),
                    qb.and(MyTimeDao.Properties.Timetype.eq(1),
                            MyTimeDao.Properties.Dayofweek.eq(xth),
                            MyTimeDao.Properties.Starthour.le(startHour), //시간 중복되는거 해결해야함 13:00~15:00, 15:00~17:00같이나옴
                            MyTimeDao.Properties.Endhour.ge(startHour))));
        return qb.list();
    }
    public static MyTime getEnrollTime(Context context,long startmillis, int xth, int startHour){
        QueryBuilder qb = getMyTimeDao(context).queryBuilder();
        qb.where(
                qb.or(
                qb.and(MyTimeDao.Properties.Timetype.eq(0),
                        MyTimeDao.Properties.Startmillis.le(startmillis),
                        MyTimeDao.Properties.Endmillis.gt(startmillis)),
                qb.and(MyTimeDao.Properties.Timetype.eq(1),
                        MyTimeDao.Properties.Dayofweek.eq(xth),
                        MyTimeDao.Properties.Starthour.le(startHour),
                        MyTimeDao.Properties.Endhour.gt(startHour))
                       )
                );
        return (MyTime) qb.unique();
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
