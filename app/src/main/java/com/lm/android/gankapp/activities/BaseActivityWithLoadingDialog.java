package com.lm.android.gankapp.activities;

import android.support.v7.app.AlertDialog;

/**
 * 带loading对话框的Activity
 * Created by liumeng on 2016/1/1.
 */
public abstract class BaseActivityWithLoadingDialog extends BaseActivity {
    protected AlertDialog loadingDialog;

    /**
     * 显示loading对话框
     */
    protected void showLoadingDialog() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    /**
     * 隐藏loading对话框
     */
    protected void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
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
