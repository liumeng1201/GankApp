package com.lm.android.gankapp;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.lm.android.gankapp.dao.DaoMaster;
import com.lm.android.gankapp.dao.DaoSession;
import com.lm.android.gankapp.dao.UpgradeHelper;
import com.lm.android.gankapp.utils.MyGlideImageLoader;
import com.lm.android.gankapp.utils.Utils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.okhttp.OkHttpClient;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import cn.bmob.v3.Bmob;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * Created by liumeng on 2015/12/14.
 */
public class GankApplication extends Application {
    private static GankApplication instance;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // 初始化LeakCanary
        LeakCanary.install(this);

        // 设置okhttp全局配置
        OkHttpClient client = OkHttpUtils.getInstance().getOkHttpClient();
        client.setConnectTimeout(10000, TimeUnit.MILLISECONDS);

        // 初始化bmob服务
        Bmob.initialize(instance, Utils.Bmob_ID);

        // 初始化GalleryFinal配置
        initGalleryFinalConfig();
    }

    public static GankApplication getInstance() {
        return instance;
    }

    public synchronized DaoSession getDaoSession() {
        if (daoSession == null) {
            initDaoSession();
        }
        return daoSession;
    }

    private void initDaoSession() {
        // 相当于得到数据库帮助对象，用于便捷获取db
        UpgradeHelper helper = new UpgradeHelper(this, Utils.db_name, null);
        // 得到可写的数据库操作对象
        SQLiteDatabase db = helper.getWritableDatabase();
        // 获得Master实例,相当于给database包装工具
        DaoMaster daoMaster = new DaoMaster(db);
        // 获取类似于缓存管理器,提供各表的DAO类
        daoSession = daoMaster.newSession();
    }

    private void initGalleryFinalConfig() {
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(getResources().getColor(R.color.colorPrimary))
                .setTitleBarTextColor(getResources().getColor(R.color.white))
                .setTitleBarIconColor(getResources().getColor(R.color.white))
                .setCropControlColor(getResources().getColor(R.color.colorAccent))
                .setIconCamera(R.mipmap.ic_camera_alt_white_36dp)
                .setIconCrop(R.mipmap.ic_crop_white_36dp)
                .setPreviewBg(new ColorDrawable(Color.WHITE)).build();
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableCrop(true)
                .setCropSquare(true).build();
        MyGlideImageLoader imageLoader = new MyGlideImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageLoader, theme)
                .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(functionConfig)
                .setEditPhotoCacheFolder(Utils.getGalleryFinalCacheDir(this))
                .setTakePhotoFolder(Utils.getTakePictureDir(this)).build();
        GalleryFinal.init(coreConfig);
    }

    private boolean mainRunning = false;
    public void setMainActivityRunning(boolean running) {
        this.mainRunning = running;
    }

    public boolean getMainActivityRunning() {
        return mainRunning;
    }
}
