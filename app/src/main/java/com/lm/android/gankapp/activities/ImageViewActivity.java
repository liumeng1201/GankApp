package com.lm.android.gankapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.lm.android.gankapp.R;
import com.lm.android.gankapp.component.PinchImageView;
import com.orhanobut.logger.Logger;

import icepick.State;

/**
 * Created by liumeng on 2015/12/18.
 */
public class ImageViewActivity extends BaseAppCompatActivity {
    @State
    String imageUri;

    private PinchImageView imageView;

    public static void actionStart(Context context, String imageUri) {
        Intent intent = new Intent(context, ImageViewActivity.class);
        intent.putExtra("imageUri", imageUri);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_imageview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (PinchImageView) findViewById(R.id.imageview);
        if (getIntent() != null) {
            imageUri = getIntent().getStringExtra("imageUri");
        }

        Logger.d(imageUri);
        Glide.with(this).load(imageUri).crossFade().into(imageView);
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
