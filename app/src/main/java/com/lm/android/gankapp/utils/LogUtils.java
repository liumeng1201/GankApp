package com.lm.android.gankapp.utils;

import com.lm.android.gankapp.BuildConfig;
import com.orhanobut.logger.Logger;

/**
 * Created by liumeng on 2015/12/18.
 */
public class LogUtils {
    public static void logd(String message, Object... args) {
        if (BuildConfig.DEBUG)
            Logger.d(message, args);
    }

    public static void logw(String message, Object... args) {
        if (BuildConfig.DEBUG)
            Logger.w(message, args);
    }

    public static void loge(String message, Object... args) {
        if (BuildConfig.DEBUG)
            Logger.e(message, args);
    }
}
