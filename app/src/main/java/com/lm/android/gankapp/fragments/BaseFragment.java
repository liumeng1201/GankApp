package com.lm.android.gankapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zhy.http.okhttp.OkHttpUtils;

import icepick.Icepick;

/**
 * Created by liumeng on 2015/12/15.
 */
public class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
