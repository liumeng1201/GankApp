package com.lm.android.gankapp.utils;

import com.lm.android.gankapp.dao.PropertyContent;
import com.lm.android.gankapp.dao.PropertyContentDao;
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

    /**
     * 保存用户是否登录
     *
     * @param _status
     * @param dao
     */
    public static void setUserLoginStatus(String _status, PropertyContentDao dao) {
        PropertyContent entity = new PropertyContent();
        entity.setKey(loginstatus);
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
        Query nameQuery = dao.queryBuilder().where(PropertyContentDao.Properties.Key.eq(key)).build();
        List<PropertyContent> list = nameQuery.list();
        if (!ListUtils.isEmpty(list)) {
            String value = list.get(0).getValue();
            if (!StringUtils.isEmpty(value)) {
                return value;
            }
        }
        return null;
    }
}
