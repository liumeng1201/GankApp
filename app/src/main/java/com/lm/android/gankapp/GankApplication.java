package com.lm.android.gankapp;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.lm.android.gankapp.dao.DaoMaster;
import com.lm.android.gankapp.dao.DaoSession;
import com.lm.android.gankapp.dao.UpgradeHelper;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.okhttp.OkHttpClient;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import cn.bmob.v3.Bmob;

/**
 * Created by liumeng on 2015/12/14.
 */
public class GankApplication extends Application {
    private static GankApplication instance;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // 初始化logger
        Logger.init(getString(R.string.app_name));

        // 初始化LeakCanary
        LeakCanary.install(this);

        // 设置okhttp全局配置
        OkHttpClient client = OkHttpUtils.getInstance().getOkHttpClient();
        client.setConnectTimeout(10000, TimeUnit.MILLISECONDS);

        // 初始化bmob服务
        Bmob.initialize(instance, "1d12db91cc13949729e6db732046c69f");
    }

    public static GankApplication getInstance() {
        return instance;
    }

    public synchronized DaoSession getDaoSession() {
        if (daoSession == null) {
            initDaoSession();
        }
        return daoSession;
    }

    private void initDaoSession() {
        // 相当于得到数据库帮助对象，用于便捷获取db
        UpgradeHelper helper = new UpgradeHelper(this, "gank_db", null);
        // 得到可写的数据库操作对象
        SQLiteDatabase db = helper.getWritableDatabase();
        // 获得Master实例,相当于给database包装工具
        DaoMaster daoMaster = new DaoMaster(db);
        // 获取类似于缓存管理器,提供各表的DAO类
        daoSession = daoMaster.newSession();
    }
}
