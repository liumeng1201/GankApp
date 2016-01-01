package com.lm.android.gankapp.listener;

import com.lm.android.gankapp.utils.LogUtils;

import org.json.JSONObject;

import cn.bmob.v3.listener.OtherLoginListener;

/**
 * Created by liumeng on 2016/1/1.
 */
public abstract class MyBmobOtherLoginListener extends OtherLoginListener {
    @Override
    public void onSuccess(JSONObject jsonObject) {
        LogUtils.json(jsonObject.toString());
        successOpt(jsonObject);
    }

    protected abstract void successOpt(JSONObject jsonObject);

    @Override
    public void onFailure(int i, String s) {
        failureOpt(i, s);
    }

    protected abstract void failureOpt(int i, String s);
}
