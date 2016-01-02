package com.lm.android.gankapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.lm.android.gankapp.R;
import com.lm.android.gankapp.component.PinchImageView;
import com.lm.android.gankapp.utils.LogUtils;
import com.umeng.analytics.MobclickAgent;

import icepick.State;

/**
 * Created by liumeng on 2015/12/18.
 */
public class ImageViewActivity extends BaseActivity {
    @State
    String imageUri;

    private PinchImageView imageView;

    public static void actionStart(Context context, String imageUri) {
        Intent intent = new Intent(context, ImageViewActivity.class);
        intent.putExtra("imageUri", imageUri);
        context.startActivity(intent);
    }

    @Override
    protected void setContentLayout() {
        setContentView(R.layout.activity_big_imageview);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (PinchImageView) findViewById(R.id.imageview);
        if (getIntent() != null) {
            imageUri = getIntent().getStringExtra("imageUri");
        }

        LogUtils.logd(imageUri);
        Glide.with(context).load(imageUri).crossFade().into(imageView);
    }

    @Override
    public void onResume() {
        MobclickAgent.onPageStart(getClass().getSimpleName());
        super.onResume();
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        super.onPause();
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
