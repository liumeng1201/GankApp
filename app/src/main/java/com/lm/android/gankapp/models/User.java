package com.lm.android.gankapp.models;

import cn.bmob.v3.BmobUser;

/**
 * Created by liumeng on 2015/12/27.
 */
public class User extends BmobUser {
    // 昵称
    private String nickName;

    // 性别，0：未知，1：男，2：女
    private Integer sex;

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
}
