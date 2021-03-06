package com.lm.android.gankapp.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;

import com.umeng.analytics.MobclickAgent;

/**
 * 带loading对话框的Activity
 * Created by liumeng on 2016/1/1.
 */
public abstract class BaseActivityWithLoadingDialog extends BaseActivity {
    protected AlertDialog loadingDialog;

    protected final int SHOW_LOGDING = 2001;
    protected final int DISMISS_LOADING = 2002;
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_LOGDING:
                    if (loadingDialog != null && !loadingDialog.isShowing()) {
                        loadingDialog.show();
                    }
                    break;
                case DISMISS_LOADING:
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    break;
            }
        }
    };

    /**
     * 初始化loadingDialog，继承自该类的子类必须实现
     */
    protected abstract void initLoadingDialog();

    /**
     * 显示loading对话框
     */
    protected void showLoadingDialog() {
        handler.sendEmptyMessage(SHOW_LOGDING);
    }

    /**
     * 隐藏loading对话框
     */
    protected void dismissLoadingDialog() {
        handler.sendEmptyMessage(DISMISS_LOADING);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLoadingDialog();
    }

    @Override
    public void onResume() {
        MobclickAgent.onPageStart(getClass().getSimpleName());
        super.onResume();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            loadingDialog = null;
        }
    }
}
