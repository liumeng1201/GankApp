package com.lm.android.gankapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
    public static final String ACTION_TYPE = "action_type";
    public static final String ACTION_DATA = "action_data";
    public static final int SYNC_FAV_DATA = 80001;
    public static final int UPDATE_FAV_DB = 80002;

    private Gson gson;

    private GankApplication gankApplication;
    private FavoriteContentDao favoriteDao;

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化logger
        Logger.init(getString(R.string.app_name));

        gankApplication = GankApplication.getInstance();
        favoriteDao = gankApplication.getDaoSession().getFavoriteContentDao();
        gson = new Gson();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int actionType = intent.getIntExtra(ACTION_TYPE, SYNC_FAV_DATA);
        User currentUser = BmobUser.getCurrentUser(this, User.class);
        if (currentUser != null) {
            String userId = currentUser.getObjectId();
            if (!StringUtils.isEmpty(userId)) {
                switch (actionType) {
                    case SYNC_FAV_DATA:
                        syncFavoriteData(userId);
                        break;
                    case UPDATE_FAV_DB:
                        String data = intent.getStringExtra(ACTION_DATA);
                        if (!StringUtils.isEmpty(data)) {
                            ArrayList<FavoriteModel> datas = gson.fromJson(data, new TypeToken<ArrayList<FavoriteModel>>() {}.getType());
                            if (!ListUtils.isEmpty(datas)) {
                                updateFavoriteDB(datas, userId);
                            }
                        }
                        break;
                }
            } else {
                stopSelf();
            }
        } else {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void syncFavoriteData(final String userId) {
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
                updateFavoriteDB(response, userId);
            }
        });
    }

    /**
     * 更新收藏数据表
     *
     * @param datas
     * @param userId
     */
    private void updateFavoriteDB(ArrayList<FavoriteModel> datas, String userId) {
        if (!ListUtils.isEmpty(datas)) {
            LogUtils.json(gson.toJson(datas));
            for (FavoriteModel data : datas) {
                ContentItemFavorite item = new ContentItemFavorite(data.getDesc(), data.getType(), data.getUrl(), data.getContentObjectId(), data.getFavoriteAt(), userId);
                item.setObjectId(data.getObjectId());
                PropertyUtils.setFavoriteToDB(item, favoriteDao);
            }
        }

        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
