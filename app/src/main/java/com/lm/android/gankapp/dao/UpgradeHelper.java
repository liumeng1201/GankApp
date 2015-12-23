package com.lm.android.gankapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lm.android.gankapp.utils.LogUtils;

/**
 * Created by liumeng on 2015/12/23.
 */
public class UpgradeHelper extends DaoMaster.OpenHelper {
    private final boolean UPDATE_DB = false;

    public UpgradeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * Here is where the calls to upgrade are executed
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (UPDATE_DB) {
        /* i represent the version where the user is now and the class named with this number implies that is upgrading from i to i++ schema */
            for (int i = oldVersion; i < newVersion; i++) {
                LogUtils.logi("Upgrading schema from version " + oldVersion + " to " + newVersion + " by migrating all tables data");
                // TODO 需要upgrade的表
                MigrationHelper.getInstance().migrate(db, FavoriteContentDao.class);
            }
        }
    }
}