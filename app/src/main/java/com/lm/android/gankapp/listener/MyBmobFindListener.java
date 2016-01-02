package com.lm.android.gankapp.listener;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * 自定义bmob查看监听
 * Created by liumeng on 2016/1/2.
 */
public abstract class MyBmobFindListener<T> extends FindListener<T> {
    @Override
    public void onSuccess(List<T> list) {
        successOpt(list);
    }

    protected abstract void successOpt(List<T> list);

    @Override
    public void onError(int i, String s) {
        failureOpt(i, s);
    }

    protected abstract void failureOpt(int i, String s);
}
