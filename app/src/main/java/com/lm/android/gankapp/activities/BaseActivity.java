package com.lm.android.gankapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lm.android.gankapp.R;
import com.zhy.http.okhttp.OkHttpUtils;

import icepick.Icepick;
import icepick.State;

/**
 * Created by liumeng on 2015/12/14.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @State
    String username; // TODO This will be automatically saved and restored

    protected Context context;
    protected Toolbar toolbar;
    protected abstract void setContentLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Icepick.restoreInstanceState(context, savedInstanceState);
        setContentLayout();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
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
