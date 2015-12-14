package com.lm.android.gankapp.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import icepick.Icepick;
import icepick.State;

/**
 * Created by liumeng on 2015/12/14.
 */
public class BaseAppCompatActivity extends AppCompatActivity {
    @State
    String username; // TODO This will be automatically saved and restored

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

}
