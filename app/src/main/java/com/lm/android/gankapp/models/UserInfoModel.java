package com.lm.android.gankapp.models;

/**
 * Created by liumeng on 2016/1/6.
 */
public class UserInfoModel {
    private int type;
    private String title;
    private String value;

    public UserInfoModel(int type, String title, String value) {
        this.type = type;
        this.title = title;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
