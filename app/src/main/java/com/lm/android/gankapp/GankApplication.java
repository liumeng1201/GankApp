package com.lm.android.gankapp;

import android.app.Application;

import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.okhttp.OkHttpClient;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

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

        // 设置okhttp全局配置
        OkHttpClient client = OkHttpUtils.getInstance().getOkHttpClient();
        client.setConnectTimeout(10000, TimeUnit.MILLISECONDS);
    }
}
