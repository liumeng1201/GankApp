package com.lm.android.gankapp.listener;

import cn.bmob.v3.listener.DeleteListener;

/**
 * Created by liumeng on 2016/1/10.
 */
public abstract class MyBmobDeleteListener extends DeleteListener {
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
