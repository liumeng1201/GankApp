package com.lm.android.gankapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.lm.android.gankapp.GankApplication;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;

import icepick.Icepick;

/**
 * Created by liumeng on 2015/12/15.
 */
public class BaseFragment extends Fragment {
    protected GankApplication gankApplication;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        gankApplication = GankApplication.getInstance();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    @Override
    public void onStop() {
        super.onStop();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
