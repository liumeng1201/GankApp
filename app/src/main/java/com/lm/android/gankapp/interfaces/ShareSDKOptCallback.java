package com.lm.android.gankapp.interfaces;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;

/**
 * ShareSDK操作结果回调
 * Created by liumeng on 2016/1/1.
 */
public interface ShareSDKOptCallback {
    abstract void onSuccess(Platform platform, HashMap<String, Object> result);

    abstract void onFailed(Throwable throwable);

    abstract void onCancel();
}
