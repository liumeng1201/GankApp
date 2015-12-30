package com.lm.android.gankapp.listener;

import cn.bmob.v3.listener.SaveListener;

/**
 * 自定义bmob saveListener
 * Created by liumeng on 2015/12/30.
 */
public abstract class MyBmobSaveListener extends SaveListener {
    @Override
    public void onSuccess() {
        successOpt();
    }

    /**
     * 操作成功要执行的操作
     */
    protected abstract void successOpt();

    @Override
    public void onFailure(int i, String s) {
        // TODO 对出错信息进行统一处理
        failureOpt(i, s);
    }

    /**
     * 操作失败时要执行的操作
     *
     * @param i 错误号
     * @param s 错误描述
     */
    protected abstract void failureOpt(int i, String s);
}
