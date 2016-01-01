package com.lm.android.gankapp.models;

import com.lm.android.gankapp.dao.PropertyContent;
import com.lm.android.gankapp.dao.PropertyContentDao;
import com.lm.android.gankapp.utils.ListUtils;
import com.lm.android.gankapp.utils.StringUtils;

import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * 用于对PropertyContent表进行操作的工具类
 * Created by liumeng on 2015/12/30.
 */
public class PropertyUtils {
    public static final String name = "username";
    public static final String login = "login";
    public static final String pwd = "password";
    public static final String avatar = "avatar";
    public static final String nickname = "nickname";
    public static final String sex = "sex";
    public static final String province = "province";
    public static final String city = "city";


    /**
     * 保存用户密码
     *
     * @param _passwd
     * @param dao
     */
    public static void saveUserPassword(String _passwd, PropertyContentDao dao) {
        PropertyContent entity = new PropertyContent();
        entity.setKey(pwd);
        entity.setValue(_passwd);
        dao.insertOrReplace(entity);
    }

    /**
     * @param dao
     * @return 用户password
     */
    public static String getUserPwd(PropertyContentDao dao) {
        return getPropertyValue(PropertyUtils.pwd, dao);
    }

    /**
     * 保存用户是否登录
     *
     * @param _status
     * @param dao
     */
    public static void saveUserLoginStatus(String _status, PropertyContentDao dao) {
        PropertyContent entity = new PropertyContent();
        entity.setKey(login);
        entity.setValue(_status);
        dao.insertOrReplace(entity);
    }

    /**
     * @param dao
     * @return 用户是否登录
     */
    public static boolean getUserLoginStatus(PropertyContentDao dao) {
        String login = getPropertyValue(PropertyUtils.login, dao);
        if (StringUtils.isEmpty(login)) {
            return false;
        }
        if (login.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

    /**
     * 保存除密码之外用户的所有信息
     *
     * @param user
     * @param dao
     */
    public static void saveUserInfo(User user, PropertyContentDao dao) {
        saveUserName(user.getUsername(), dao);
        saveUserAvatar(user.getAvatar(), dao);
        saveUserNickName(user.getNickName(), dao);
        saveUserSex(Integer.toString(user.getSex()), dao);
        saveUserProvince(Integer.toString(user.getProvince()), dao);
        saveUserCity(Integer.toString(user.getCity()), dao);
    }

    /**
     * 保存用户名
     *
     * @param _name
     * @param dao
     */
    public static void saveUserName(String _name, PropertyContentDao dao) {
        PropertyContent entity = new PropertyContent();
        entity.setKey(name);
        entity.setValue(_name);
        dao.insertOrReplace(entity);
    }

    /**
     * @param dao
     * @return 用户username
     */
    public static String getUserName(PropertyContentDao dao) {
        return getPropertyValue(PropertyUtils.name, dao);
    }

    /**
     * 保存用户头像
     *
     * @param _avatar
     * @param dao
     */
    public static void saveUserAvatar(String _avatar, PropertyContentDao dao) {
        PropertyContent entity = new PropertyContent();
        entity.setKey(avatar);
        entity.setValue(_avatar);
        dao.insertOrReplace(entity);
    }

    /**
     * @param dao
     * @return 用户avatar
     */
    public static String getUserAvatar(PropertyContentDao dao) {
        return getPropertyValue(PropertyUtils.avatar, dao);
    }

    /**
     * 保存用户昵称
     *
     * @param _nickname
     * @param dao
     */
    public static void saveUserNickName(String _nickname, PropertyContentDao dao) {
        PropertyContent entity = new PropertyContent();
        entity.setKey(nickname);
        entity.setValue(_nickname);
        dao.insertOrReplace(entity);
    }

    /**
     * @param dao
     * @return 用户nickname
     */
    public static String getUserNickName(PropertyContentDao dao) {
        return getPropertyValue(PropertyUtils.nickname, dao);
    }

    /**
     * 保存用户性别
     *
     * @param _sex
     * @param dao
     */
    public static void saveUserSex(String _sex, PropertyContentDao dao) {
        PropertyContent entity = new PropertyContent();
        entity.setKey(sex);
        entity.setValue(_sex);
        dao.insertOrReplace(entity);
    }

    /**
     * @param dao
     * @return 用户sex
     */
    public static int getUserSex(PropertyContentDao dao) {
        String sex = getPropertyValue(PropertyUtils.sex, dao);
        if (StringUtils.isEmpty(sex)) {
            return 0;
        }
        return Integer.parseInt(sex);
    }

    /**
     * 保存用户省份信息
     *
     * @param _province
     * @param dao
     */
    public static void saveUserProvince(String _province, PropertyContentDao dao) {
        PropertyContent entity = new PropertyContent();
        entity.setKey(province);
        entity.setValue(_province);
        dao.insertOrReplace(entity);
    }

    /**
     * @param dao
     * @return 用户province对应的id
     */
    public static int getUserProvince(PropertyContentDao dao) {
        String province = getPropertyValue(PropertyUtils.province, dao);
        if (StringUtils.isEmpty(province)) {
            return 0;
        }
        return Integer.parseInt(province);
    }

    /**
     * 保存用户城市信息
     *
     * @param _city
     * @param dao
     */
    public static void saveUserCity(String _city, PropertyContentDao dao) {
        PropertyContent entity = new PropertyContent();
        entity.setKey(city);
        entity.setValue(_city);
        dao.insertOrReplace(entity);
    }

    /**
     * @param dao
     * @return 用户city对应的id
     */
    public static int getUserCity(PropertyContentDao dao) {
        String city = getPropertyValue(PropertyUtils.city, dao);
        if (StringUtils.isEmpty(city)) {
            return 0;
        }
        return Integer.parseInt(city);
    }

    /**
     * @param dao
     * @return 用户名处所要显示的信息，昵称不为空则显示昵称否则显示用户名
     */
    public static String getUserDisplayName(PropertyContentDao dao) {
        String nickname = getPropertyValue(PropertyUtils.nickname, dao);
        if (!StringUtils.isEmpty(nickname)) {
            return nickname;
        }
        return getPropertyValue(PropertyUtils.name, dao);
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
