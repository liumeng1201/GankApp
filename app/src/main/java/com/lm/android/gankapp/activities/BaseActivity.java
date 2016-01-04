package com.lm.android.gankapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lm.android.gankapp.GankApplication;
import com.lm.android.gankapp.R;
import com.lm.android.gankapp.dao.PropertyContentDao;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;

import icepick.Icepick;

/**
 * Created by liumeng on 2015/12/14.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected GankApplication gankApplication;
    protected PropertyContentDao propertyContentDao;

    protected Context context;
    protected Toolbar toolbar;
    protected abstract void setContentLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        gankApplication = GankApplication.getInstance();
        propertyContentDao = gankApplication.getDaoSession().getPropertyContentDao();
        Icepick.restoreInstanceState(context, savedInstanceState);
        setContentLayout();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
