package com.lm.android.gankapp.listener;

import android.content.Context;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.utils.Utils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by liumeng on 2015/12/28.
 */
public class MyPlatformActionListener implements PlatformActionListener {
    private Context context;

    public MyPlatformActionListener(Context context) {
        this.context = context;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Utils.showToastShort(context, context.getString(R.string.share_success));
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Utils.showToastShort(context, context.getString(R.string.share_failed));
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Utils.showToastShort(context, context.getString(R.string.share_cancel));
    }
}
