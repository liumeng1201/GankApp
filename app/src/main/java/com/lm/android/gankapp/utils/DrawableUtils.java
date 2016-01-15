package com.lm.android.gankapp.utils;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Created by liumeng on 2015/12/22.
 */
public class DrawableUtils {
    /**
     * 根据一个图片生成不同状态下要显示的图片
     *
     * @param res
     * @param drawableId    要显示的资源图片
     * @param colorTintList 不同状态下对应的tintColor
     * @return
     */
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

    /**
     * 获取一个图片染色之后的图片
     *
     * @param res
     * @param drawableId 要染色的图片
     * @param colorTint  要染的颜色，类型为Color
     * @return
     */
    public static Drawable getTintDrawable(Resources res, int drawableId, int colorTint) {
        Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            drawable = DrawableCompat.wrap(res.getDrawable(drawableId, null));
        } else {
            drawable = DrawableCompat.wrap(res.getDrawable(drawableId));
        }
        DrawableCompat.setTint(drawable, colorTint);
        return drawable;
    }

}
