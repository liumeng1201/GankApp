package com.lm.android.gankapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.lm.android.gankapp.R;
import com.orhanobut.logger.Logger;

import icepick.State;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by liumeng on 2015/12/18.
 */
public class ImageViewActivity extends BaseAppCompatActivity {
    @State
    String imageUri;

    private PhotoView imageView;

    public static void actionStart(Context context, String imageUri) {
        Intent intent = new Intent(context, ImageViewActivity.class);
        intent.putExtra("imageUri", imageUri);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_imageview);

        imageView = (PhotoView) findViewById(R.id.imageview);
        if (getIntent() != null) {
            imageUri = getIntent().getStringExtra("imageUri");
        }

        Logger.d(imageUri);
        Glide.with(this).load(imageUri).crossFade().into(imageView);
    }
}
