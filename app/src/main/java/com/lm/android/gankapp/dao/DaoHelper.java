package com.lm.android.gankapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by liumeng on 2015/12/22.
 */
public class DaoHelper {
    private static DaoSession daoSession;
    private static DaoMaster.DevOpenHelper helper;
    private static SQLiteDatabase db;
    private static DaoMaster daoMaster;

    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
            // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
            // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
            helper = new DaoMaster.DevOpenHelper(context, "gank_db", null);
            db = helper.getWritableDatabase();
            // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public static void closeDaoSession() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }

        if (db != null && db.isOpen()) {
            db.close();
        }

        if (daoMaster != null) {
            daoMaster = null;
        }

        if (helper != null) {
            helper.close();
            helper = null;
        }
    }
}
