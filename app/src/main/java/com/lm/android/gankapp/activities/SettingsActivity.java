package com.lm.android.gankapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.lm.android.gankapp.R;
import com.lm.android.gankapp.models.User;
import com.lm.android.gankapp.utils.DataCleanManager;
import com.lm.android.gankapp.utils.Utils;

public class SettingsActivity extends BaseActivityWithLoadingDialog implements View.OnClickListener {
    private Button btnAbout;
    private Button btnExit;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initLoadingDialog() {
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAbout = (Button) findViewById(R.id.about);
        btnExit = (Button) findViewById(R.id.exit);

        btnAbout.setOnClickListener(this);
        btnExit.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about:
                AboutActivity.actionStart(context);
                break;
            case R.id.exit:
                final User user = User.getCurrentUser(context, User.class);
                if (user != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(getString(R.string.exit_tishi));
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            user.logOut(context);
                            DataCleanManager.cleanDatabaseByName(context, Utils.db_name);
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.create().show();
                } else {
                    Utils.showToastShort(context, getString(R.string.already_lagout));
                }
                break;
        }
    }
}
