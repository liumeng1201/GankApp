package com.lm.android.gankapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.lm.android.gankapp.R;

/**
 * Created by liumeng on 2015/12/18.
 */
public class TestActivity extends BaseAppCompatActivity {
    public static void actionStart(Context context, String url, String title) {
        Intent intent = new Intent(context, TestActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(30);
    }
}
