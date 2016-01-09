package com.lm.android.gankapp.listener;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by liumeng on 2016/1/10.
 */
public interface MyBmobUploadListener {
    abstract void onSuccess(String fileName, String url, BmobFile file, String accessUrl);

    abstract void onProgress(int progress);

    abstract void onError(int statuscode, String errormsg);
}
