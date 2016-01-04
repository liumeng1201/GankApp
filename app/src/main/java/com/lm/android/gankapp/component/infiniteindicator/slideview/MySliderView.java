package com.lm.android.gankapp.component.infiniteindicator.slideview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lm.android.gankapp.R;

/**
 * Created by liumeng on 2016/1/4.
 */
public class MySliderView extends BaseSliderView {
    private String url;
    private String text;

    public MySliderView(Context context) {
        super(context);
    }

    public MySliderView setImageUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.my_slide_view_layout, null);
        ImageView target = (ImageView) v.findViewById(R.id.slider_image);
        Glide.with(mContext).load(url).crossFade().into(target);
        bindEventAndShow(v);
        return v;
    }
}
