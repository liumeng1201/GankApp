package com.lm.android.gankapp.listener;

import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by liumeng on 2016/1/1.
 */
public abstract class MyBmobUpdateListener extends UpdateListener {
    @Override
    public void onSuccess() {
        successOpt();
    }

    protected abstract void successOpt();

    @Override
    public void onFailure(int i, String s) {
        failureOpt(i, s);
    }

    protected abstract void failureOpt(int i, String s);
}
