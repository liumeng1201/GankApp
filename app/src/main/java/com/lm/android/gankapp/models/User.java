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
    private Integer provinceId;
    private String provinceName;

    // 市/区
    private Integer cityId;
    private String cityName;

    // QQ
    private Boolean qqBinded;

    // 新浪微博
    private Boolean sinaWeiboBinded;

    // 微信
    private Boolean wechatBinded;

    // 个人主页
    private String homePage;

    // 个人签名
    private String signature;

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

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Boolean getQqBinded() {
        return qqBinded;
    }

    public void setQqBinded(Boolean qqBinded) {
        this.qqBinded = qqBinded;
    }

    public Boolean getSinaWeiboBinded() {
        return sinaWeiboBinded;
    }

    public void setSinaWeiboBinded(Boolean sinaWeiboBinded) {
        this.sinaWeiboBinded = sinaWeiboBinded;
    }

    public Boolean getWechatBinded() {
        return wechatBinded;
    }

    public void setWechatBinded(Boolean wechatBinded) {
        this.wechatBinded = wechatBinded;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
