package com.lm.android.gankapp.utils;

import com.google.gson.Gson;
import com.lm.android.gankapp.dao.FavoriteContent;
import com.lm.android.gankapp.dao.FavoriteContentDao;
import com.lm.android.gankapp.dao.PropertyContent;
import com.lm.android.gankapp.dao.PropertyContentDao;
import com.lm.android.gankapp.models.ContentItemFavorite;
import com.lm.android.gankapp.models.User;

import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * 用于对PropertyContent表进行操作的工具类，主要用来代替SharedPreference
 * Created by liumeng on 2015/12/30.
 */
public class PropertyUtils {
    public static final String name = "username";
    public static final String loginstatus = "loginstatus";
    public static final String pwd = "password";
    public static final String avatar = "avatar";
    public static final String nickname = "nickname";
    public static final String sex = "sex";
    public static final String province = "province";
    public static final String city = "city";
    public static final String fb_latest_dev_reply_time = "fb_latest_dev_reply_time";

    /**
     * 保存用户是否登录
     *
     * @param _status
     * @param dao
     */
    public static void setUserLoginStatus(String _status, PropertyContentDao dao) {
        PropertyContent entity = getPropertyItem(loginstatus, dao);
        if (entity == null) {
            entity = new PropertyContent();
            entity.setKey(loginstatus);
        }
        entity.setValue(_status);
        dao.insertOrReplace(entity);
    }

    /**
     * @param dao
     * @return 用户是否登录
     */
    public static boolean getUserLoginStatus(PropertyContentDao dao) {
        String login = getPropertyValue(PropertyUtils.loginstatus, dao);
        if (StringUtils.isEmpty(login)) {
            return false;
        }
        if (login.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

    /**
     * @param user
     * @return 用户名处所要显示的信息，显示顺序为昵称-用户名-用户id
     */
    public static String getUserDisplayName(User user) {
        if (StringUtils.isEmpty(user.getNickName())) {
            if (StringUtils.isEmpty(user.getUsername())) {
                return user.getObjectId();
            } else {
                return user.getUsername();
            }
        } else {
            return user.getNickName();
        }
    }

    /**
     * @param key
     * @param dao
     * @return 指定key值所对应的value值
     */
    public static String getPropertyValue(String key, PropertyContentDao dao) {
        Query query = dao.queryBuilder().where(PropertyContentDao.Properties.Key.eq(key)).build();
        if (query != null) {
            List<PropertyContent> list = query.list();
            if (!ListUtils.isEmpty(list)) {
                String value = list.get(0).getValue();
                if (!StringUtils.isEmpty(value)) {
                    return value;
                }
            }
        }
        return null;
    }

    /**
     * @param key
     * @param dao
     * @return 指定key值所对应的Property item
     */
    public static PropertyContent getPropertyItem(String key, PropertyContentDao dao) {
        Query query = dao.queryBuilder().where(PropertyContentDao.Properties.Key.eq(key)).build();
        if (query != null) {
            List<PropertyContent> list = query.list();
            if (!ListUtils.isEmpty(list)) {
                return list.get(0);
            }
        }
        return null;
    }

    /**
     * 将数据设置到本地数据库中
     */
    public static void setFavoriteToDB(ContentItemFavorite item, FavoriteContentDao dao) {
        FavoriteContent entity = null;
        Query query = dao.queryBuilder().where(FavoriteContentDao.Properties.ContentObjectId.eq(item.getContentObjectId())).build();
        if (query != null) {
            List<FavoriteContent> list = query.list();
            if (!ListUtils.isEmpty(list)) {
                entity = list.get(0);
            }
        }
        if (entity == null) {
            entity = new FavoriteContent();
            entity.setContentObjectId(item.getContentObjectId());
        }
        entity.setType(item.getType());
        entity.setDesc(item.getDesc());
        entity.setUrl(item.getUrl());
        entity.setFavoriteAt(item.getFavoriteAt());
        entity.setObjectId(item.getObjectId());
        entity.setShowFavorite(item.isShowFavorite());
        LogUtils.json(new Gson().toJson(entity));
        dao.insertOrReplace(entity);
    }

    /**
     * @param _time 用户意见反馈收到开发者最后一次回复的时间
     * @param dao
     */
    public static void setFbDevReplyTime(long _time, PropertyContentDao dao) {
        PropertyContent entity = getPropertyItem(fb_latest_dev_reply_time, dao);
        if (entity == null) {
            entity = new PropertyContent();
            entity.setKey(fb_latest_dev_reply_time);
        }
        entity.setValue(String.valueOf(_time));
        dao.insertOrReplace(entity);
    }

    /**
     * @param dao
     * @return 用户意见反馈收到开发者最后一次回复的时间
     */
    public static long getFbDevReplyTime(PropertyContentDao dao) {
        String time = getPropertyValue(PropertyUtils.fb_latest_dev_reply_time, dao);
        if (StringUtils.isEmpty(time)) {
            return 0;
        }
        return Long.valueOf(time);
    }
}
