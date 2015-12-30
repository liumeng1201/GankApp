package com.lm.android.gankapp.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by liumeng on 2015/12/15.
 */
public class Utils {
    public static String base_category_data_url = "http://gank.avosapps.com/api/data/";
    public static String[] requestCategory = {"all", "Android", "iOS", "前端", "拓展资源", "休息视频", "福利"};
    public static int requestNum = 100;

    public static int REQUEST_CODE_LOGIN = 20001;
    public static int REQUEST_CODE_REGISTER = 20002;

    public static Toast getToastShort(Context context, String msg) {
        return Toast.makeText(context, msg, Toast.LENGTH_SHORT);
    }

    public static Toast getToastShort(Context context, int msg) {
        return Toast.makeText(context, msg, Toast.LENGTH_SHORT);
    }

    public static Toast getToastLong(Context context, String msg) {
        return Toast.makeText(context, msg, Toast.LENGTH_LONG);
    }

    public static Toast getToastLong(Context context, int msg) {
        return Toast.makeText(context, msg, Toast.LENGTH_LONG);
    }
}
