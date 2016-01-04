package com.lm.android.gankapp.component.infiniteindicator.slideview;

import android.content.Context;
import android.view.View;

/**
 * When you want to make your own slider view, you must extends from this class.
 * BaseSliderView provides some useful methods.
 * if you want to show progressbar, you just need to set a progressbar id as @+id/loading_bar.
 * Thanks to :  https://github.com/daimajia/AndroidImageSlider
 */
public abstract class BaseSliderView {
    protected Context mContext;
    protected OnSliderClickListener mOnSliderClickListener;

    protected BaseSliderView(Context context) {
        mContext = context;
    }

    /**
     * set click listener on a slider image
     */
    public BaseSliderView setOnSliderClickListener(OnSliderClickListener l) {
        mOnSliderClickListener = l;
        return this;
    }

    /**
     * When you want to implement your own slider view, please call this method in the end in `getView()` method
     */
    protected void bindEventAndShow(View v) {
        final BaseSliderView me = this;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSliderClickListener != null) {
                    mOnSliderClickListener.onSliderClick(me);
                }
            }
        });
    }

    /**
     * the extended class have to implement getView(), which is called by the adapter,
     * every extended class response to render their own view.
     *
     * @return
     */
    public abstract View getView();
}
