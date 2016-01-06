package com.lm.android.gankapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lm.android.gankapp.R;
import com.lm.android.gankapp.component.PinchImageView;
import com.lm.android.gankapp.utils.FileUtils;
import com.lm.android.gankapp.utils.ImageUtils;
import com.lm.android.gankapp.utils.Utils;
import com.umeng.analytics.MobclickAgent;

import icepick.State;

/**
 * Created by liumeng on 2015/12/18.
 */
public class ImageViewActivity extends BaseActivity {
    @State
    String imageUri;
    @State
    String objectId;

    private PinchImageView imageView;

    public static void actionStart(Context context, String imageUri, String objectId) {
        Intent intent = new Intent(context, ImageViewActivity.class);
        intent.putExtra("imageUri", imageUri);
        intent.putExtra("objectId", objectId);
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
            objectId = getIntent().getStringExtra("objectId");
        }

        Glide.with(context).load(imageUri).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.imageviewactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_share:
                return true;
            case R.id.action_save:
                saveImage();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveImage() {
        String path = Utils.getAppImageDir() + objectId + ".jpg";
        if (FileUtils.isFileExist(path)) {
            Utils.showToastShort(context, getString(R.string.image_already_saved) + path);
            return;
        }
        imageView.setDrawingCacheEnabled(true);
        if (ImageUtils.saveBitmapToFile(imageView.getDrawingCache(), path)) {
            Utils.showToastShort(context, getString(R.string.image_success_saved) + path);
        }
        imageView.setDrawingCacheEnabled(false);
    }
}
