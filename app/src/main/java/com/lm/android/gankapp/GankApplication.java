package com.lm.android.gankapp;

import android.app.Application;

import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by liumeng on 2015/12/14.
 */
public class GankApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化logger
        Logger.init(getString(R.string.app_name));

        // 初始化LeakCanary
        LeakCanary.install(this);
    }
}
