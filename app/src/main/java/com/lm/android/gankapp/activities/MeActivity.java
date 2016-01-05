package com.lm.android.gankapp.activities;

import android.os.Bundle;
import android.view.Menu;

import com.lm.android.gankapp.R;

public class MeActivity extends BaseActivityWithLoadingDialog {

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.activity_me);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_me, menu);
        return true;
    }
}
