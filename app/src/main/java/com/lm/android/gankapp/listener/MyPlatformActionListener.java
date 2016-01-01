package com.lm.android.gankapp.listener;

import com.lm.android.gankapp.interfaces.ThirdPartyLoginCallback;
import com.lm.android.gankapp.interfaces.ThirdPartyShareCallback;
import com.lm.android.gankapp.models.ThirdPartyOptType;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by liumeng on 2015/12/28.
 */
public class MyPlatformActionListener implements PlatformActionListener {
    private ThirdPartyOptType optType;
    private ThirdPartyLoginCallback loginCallback;
    private ThirdPartyShareCallback shareCallback;

    /**
     * @param optType       第三方账号操作类型
     * @param loginCallback 第三方账号登录操作结果回调
     */
    public MyPlatformActionListener(ThirdPartyOptType optType, ThirdPartyLoginCallback loginCallback) {
        this.optType = optType;
        this.loginCallback = loginCallback;
    }

    /**
     * @param optType       第三方账号操作类型
     * @param shareCallback 第三方账号分享操作结果回调
     */
    public MyPlatformActionListener(ThirdPartyOptType optType, ThirdPartyShareCallback shareCallback) {
        this.optType = optType;
        this.shareCallback = shareCallback;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (optType == ThirdPartyOptType.LOGIN) {
            loginCallback.onSuccess(platform, hashMap);
        } else if (optType == ThirdPartyOptType.SHARE) {
            shareCallback.onSuccess(platform, hashMap);
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        if (optType == ThirdPartyOptType.LOGIN) {
            loginCallback.onFailed(throwable);
        } else if (optType == ThirdPartyOptType.SHARE) {
            shareCallback.onFailed(throwable);
        }
    }

    @Override
    public void onCancel(Platform platform, int i) {
        if (optType == ThirdPartyOptType.LOGIN) {
            loginCallback.onCancel();
        } else if (optType == ThirdPartyOptType.SHARE) {
            shareCallback.onCancel();
        }
    }
}
