package com.lm.android.gankapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.lm.android.gankapp.R;
import com.lm.android.gankapp.listener.MyBmobUploadListener;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by liumeng on 2015/12/15.
 */
public class Utils {
    public static String share_title_url = "http://gank.io";
    public static String base_category_data_url = "http://gank.avosapps.com/api/data/";
    public static String[] requestCategory = {"all", "Android", "iOS", "前端", "拓展资源", "休息视频", "福利"};
    public static int requestNum = 100;

    public static final int REQUEST_CODE_LOGIN = 20001;
    public static final int REQUEST_CODE_REGISTER = 20002;
    public static final int REQUEST_CODE_USERINFO = 20003;

    // Bmob对应各个key值
    public static final String Bmob_ID = "359548ddc36b912635b1a45c7ed39e1b";
    public static final String Bmob_ACCESS_KEY = "ded5326707fc425a52c1d23febe01472";
    // Bmob后端代码基础url，Secret Key需要替换为正式版本
    public static final String Bmob_BASE_URL = "http://cloud.bmob.cn/41f6b8733a573f37/";
    // 获取收藏数据
    public static final String get_favorite_url = Bmob_BASE_URL + "getFavoriteList";

    /**
     * @return app外部存储基准目录
     */
    public static String getAppBaseDir() {
        String path = Environment.getExternalStorageDirectory() + File.separator + "gankapp" + File.separator;
        if (!FileUtils.isFolderExist(path)) {
            FileUtils.makeFolders(path);
        }
        return path;
    }

    /**
     * @return GalleryFinal用的缓存文件夹
     */
    public static File getGalleryFinalCacheDir(Context context) {
        String path = context.getExternalCacheDir().getPath() + File.separator + "gfcache";
        return new File(path);
    }

    /**
     * @return 获取拍照图片存放目录
     */
    public static File getTakePictureDir(Context context) {
        String path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
        return new File(path);
    }

    /**
     * @return app外部图片保存目录
     */
    public static String getAppImageDir() {
        String path = getAppBaseDir() + "images" + File.separator;
        if (!FileUtils.isFolderExist(path)) {
            FileUtils.makeFolders(path);
        }
        return path;
    }

    public static Toast getToastShort(Context context, String msg) {
        return Toast.makeText(context, msg, Toast.LENGTH_SHORT);
    }

    public static Toast getToastShort(Context context, int msg) {
        return Toast.makeText(context, msg, Toast.LENGTH_SHORT);
    }

    public static Toast getToastLong(Context context, String msg) {
        return Toast.makeText(context, msg, Toast.LENGTH_LONG);
    }

    public static Toast getToastLong(Context context, int msg) {
        return Toast.makeText(context, msg, Toast.LENGTH_LONG);
    }

    public static void showToastShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToastShort(Context context, int msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showToastLong(Context context, int msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static AlertDialog getLoadingDialog(Context context, String message) {
        AlertDialog dialog = new AlertDialog.Builder(context, R.style.MyCustomAlertDialogTheme).create();
        dialog.setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null);
        final ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView text = (TextView) view.findViewById(R.id.message);
        if (!StringUtils.isEmpty(message)) {
            text.setText(message);
        }
        dialog.setView(view);
        final RotateAnimation rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.anim_rotate_loding);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                image.startAnimation(rotateAnimation);
            }
        });
        return dialog;
    }

    /**
     * Bmob上传单一文件操作
     *
     * @param context
     * @param path     文件路径
     * @param listener 上传文件回调监听
     * @return BTPFileResponse
     */
    public static BTPFileResponse uploadSingleFile(final Context context, String path, final MyBmobUploadListener listener) {
        BTPFileResponse response = BmobProFile.getInstance(context).upload(path, new UploadListener() {
            @Override
            public void onSuccess(String fileName, String url, BmobFile file) {
                String accessUrl = BmobProFile.getInstance(context).signURL(fileName, url, Utils.Bmob_ACCESS_KEY, 0, null);
                listener.onSuccess(fileName, url, file, accessUrl);
            }

            @Override
            public void onProgress(int progress) {
                listener.onProgress(progress);
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                listener.onError(statuscode, errormsg);
            }
        });
        return response;
    }
}
