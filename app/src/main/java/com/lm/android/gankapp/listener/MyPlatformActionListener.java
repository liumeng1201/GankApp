package com.lm.android.gankapp.listener;

import com.lm.android.gankapp.interfaces.ShareSDKOptCallback;
import com.lm.android.gankapp.models.ThirdPartyOptType;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by liumeng on 2015/12/28.
 */
public class MyPlatformActionListener implements PlatformActionListener {
    private ThirdPartyOptType optType;
    private ShareSDKOptCallback callback;

    /**
     * @param optType  第三方账号操作类型
     * @param callback 第三方账号登录操作结果回调
     */
    public MyPlatformActionListener(ThirdPartyOptType optType, ShareSDKOptCallback callback) {
        this.optType = optType;
        this.callback = callback;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        callback.onSuccess(platform, hashMap);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        callback.onFailed(throwable);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        callback.onCancel();
    }
}
