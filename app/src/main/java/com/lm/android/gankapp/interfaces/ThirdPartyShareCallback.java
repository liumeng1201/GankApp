package com.lm.android.gankapp.interfaces;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;

/**
 * 社交分享结果回调
 * Created by liumeng on 2016/1/1.
 */
public interface ThirdPartyShareCallback {
    abstract void onSuccess(Platform platform, HashMap<String, Object> result);

    abstract void onFailed(Throwable throwable);

    abstract void onCancel();
}
