package com.lm.android.gankapp.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.utils.Utils;

public class MeActivity extends BaseActivityWithLoadingDialog {

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.activity_me);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.title_activity_me);

        loadingDialog = Utils.getLoadingDialog(context, getString(R.string.saving_user_info));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
