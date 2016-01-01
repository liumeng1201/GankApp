package com.lm.android.gankapp.models;

import cn.bmob.v3.BmobUser;

/**
 * Created by liumeng on 2015/12/27.
 */
public class User extends BmobUser {
    // 昵称
    private String nickName;

    // 头像
    private String avatar;

    // 性别，0：未知，1：男，2：女
    private Integer sex;

    // 省/直辖市
    private Integer province;

    // 市/区
    private Integer city;

    // QQ
    private String qq;

    // 新浪微博
    private String sinaWeibo;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getSinaWeibo() {
        return sinaWeibo;
    }

    public void setSinaWeibo(String sinaWeibo) {
        this.sinaWeibo = sinaWeibo;
    }
}
