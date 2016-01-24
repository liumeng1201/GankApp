package com.lm.android.gankapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.lm.android.gankapp.GankApplication;
import com.lm.android.gankapp.R;
import com.lm.android.gankapp.dao.FavoriteContentDao;
import com.lm.android.gankapp.interfaces.FavoriteDatasCallback;
import com.lm.android.gankapp.models.ContentItemFavorite;
import com.lm.android.gankapp.models.FavoriteModel;
import com.lm.android.gankapp.models.User;
import com.lm.android.gankapp.utils.ListUtils;
import com.lm.android.gankapp.utils.LogUtils;
import com.lm.android.gankapp.utils.PropertyUtils;
import com.lm.android.gankapp.utils.StringUtils;
import com.lm.android.gankapp.utils.Utils;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.BmobUser;

/**
 * Created by liumeng on 2016/1/10.
 */
public class SyncDataService extends Service {
    private GankApplication gankApplication;
    private FavoriteContentDao favoriteDao;

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化logger
        Logger.init(getString(R.string.app_name));
        LogUtils.logi("create service");

        gankApplication = GankApplication.getInstance();
        favoriteDao = gankApplication.getDaoSession().getFavoriteContentDao();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.logi("start service");
        User currentUser = BmobUser.getCurrentUser(this, User.class);
        if (currentUser != null) {
            String userId = currentUser.getObjectId();
            if (!StringUtils.isEmpty(userId)) {
                syncFavoriteData(userId);
            } else {
                stopSelf();
            }
        } else {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void syncFavoriteData(final String userId) {
        LogUtils.logi("sync favorite data");
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        OkHttpUtils.post().url(Utils.get_favorite_url).params(params).build().execute(new FavoriteDatasCallback() {
            @Override
            public void onError(Request request, Exception e) {
                // Error
                stopSelf();
            }

            @Override
            public void onResponse(ArrayList<FavoriteModel> response) {
                if (response == null) {
                    return;
                }

                ArrayList<FavoriteModel> datas = response;
                if (!ListUtils.isEmpty(datas)) {
                    LogUtils.json(new Gson().toJson(datas));
                    for (FavoriteModel data : datas) {
                        ContentItemFavorite item = new ContentItemFavorite(data.getDesc(), data.getType(), data.getUrl(), data.getContentObjectId(), data.getFavoriteAt(), userId);
                        item.setObjectId(data.getObjectId());
                        PropertyUtils.setFavoriteToDB(item, favoriteDao);
                    }
                }
                stopSelf();
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
