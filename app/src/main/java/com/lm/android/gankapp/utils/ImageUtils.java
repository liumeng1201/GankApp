package com.lm.android.gankapp.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by liumeng on 2016/1/6.
 */
public class ImageUtils {
    /**
     * 将bitmap保存至指定路径
     *
     * @param bitmap 要保存的图片的drawable
     * @param path     要保存到的路径
     * @return 保存是否成功
     */
    public static boolean saveBitmapToFile(Bitmap bitmap, String path) {
        boolean result = false;
        File file = new File(path);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] data = bos.toByteArray();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap getBitmapFromRes(Resources res, int resId) {
        return BitmapFactory.decodeResource(res, resId);
    }
}
