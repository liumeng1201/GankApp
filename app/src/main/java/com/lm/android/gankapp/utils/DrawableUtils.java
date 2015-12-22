package com.lm.android.gankapp.utils;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Created by liumeng on 2015/12/22.
 */
public class DrawableUtils {
    public static Drawable getDrawableStateListRes(Resources res, int drawableId, int colorTintList) {
        ColorStateList colorStateList;
        Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            colorStateList = res.getColorStateList(colorTintList, null);
            drawable = DrawableCompat.wrap(res.getDrawable(drawableId, null));
        } else {
            colorStateList = res.getColorStateList(colorTintList);
            drawable = DrawableCompat.wrap(res.getDrawable(drawableId));
        }
        DrawableCompat.setTintList(drawable, colorStateList);
        return drawable;
    }
}
